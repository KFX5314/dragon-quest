package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;
import com.taller.patrones.application.command.AttackBattleCommand;
import com.taller.patrones.application.command.BattleCommand;
import com.taller.patrones.application.composite.AttackUnit;
import com.taller.patrones.application.composite.AttackUnitFactory;
import com.taller.patrones.application.observer.AnalyticsDamageObserver;
import com.taller.patrones.application.observer.AuditDamageObserver;
import com.taller.patrones.application.observer.DamageEvent;
import com.taller.patrones.application.observer.DamageNotifier;
import com.taller.patrones.application.observer.RealtimeStatsObserver;
import com.taller.patrones.infrastructure.combat.CombatEngine;
import com.taller.patrones.infrastructure.persistence.BattleRepository;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Caso de uso: gestionar batallas.
 * <p>
 * Nota: Se reutiliza una sola instancia de BattleRepository (singleton).
 */
public class BattleService {

    private final CombatEngine combatEngine = new CombatEngine();
    private final BattleRepository battleRepository = BattleRepository.getInstance();
    private final DamageNotifier damageNotifier = new DamageNotifier();
    private final Map<String, Deque<BattleCommand>> commandHistory = new HashMap<>();
    private final AttackUnitFactory attackUnitFactory = new AttackUnitFactory();

    public BattleService() {
        damageNotifier.subscribe(new AnalyticsDamageObserver());
        damageNotifier.subscribe(new AuditDamageObserver());
        damageNotifier.subscribe(new RealtimeStatsObserver());
    }

    public static final List<String> PLAYER_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL", "ICE_BEAM", "POISON_STING", "THUNDER", "METEORO", "COMBO_TRIPLE");
    public static final List<String> ENEMY_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL");

    public BattleStartResult startBattle(String playerName, String enemyName) {
        Character player = Character.builder(playerName != null ? playerName : "Héroe")
                .maxHp(150)
                .attack(25)
                .defense(15)
                .speed(20)
                // se podrían añadir .charClass("Guerrero").equipment("Espada").buffs("Fuerza") etc.
                .build();

        Character enemy = Character.builder(enemyName != null ? enemyName : "Dragón")
                .maxHp(120)
                .attack(30)
                .defense(10)
                .speed(15)
                .build();

        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);

        return new BattleStartResult(battleId, battle);
    }

    public Battle getBattle(String battleId) {
        return battleRepository.findById(battleId);
    }

    public void undoLastAttack(String battleId) {
        Deque<BattleCommand> history = commandHistory.get(battleId);
        if (history == null || history.isEmpty()) {
            return;
        }
        BattleCommand command = history.pop();
        command.undo();
    }

    public void executePlayerAttack(String battleId, String attackName) {
        Battle battle = battleRepository.findById(battleId);
        if (battle == null || battle.isFinished() || !battle.isPlayerTurn()) return;

        Attack attack = buildAttackFromUnit(attackName, battle.getPlayer(), battle.getEnemy());
        int damage = calculateDamageFromUnit(attackName, battle.getPlayer(), battle.getEnemy());
        executeAttackCommand(battleId, battle, battle.getPlayer(), battle.getEnemy(), damage, attack);
    }

    public void executeEnemyAttack(String battleId, String attackName) {
        Battle battle = battleRepository.findById(battleId);
        if (battle == null || battle.isFinished() || battle.isPlayerTurn()) return;

        String selectedAttack = attackName != null ? attackName : "TACKLE";
        Attack attack = buildAttackFromUnit(selectedAttack, battle.getEnemy(), battle.getPlayer());
        int damage = calculateDamageFromUnit(selectedAttack, battle.getEnemy(), battle.getPlayer());
        executeAttackCommand(battleId, battle, battle.getEnemy(), battle.getPlayer(), damage, attack);
    }

    private Attack buildAttackFromUnit(String attackName, Character attacker, Character defender) {
        AttackUnit unit = attackUnitFactory.fromKey(attackName);
        java.util.List<Attack> expanded = unit.expand(combatEngine);
        if (expanded.size() == 1) {
            return expanded.get(0);
        }

        // Para ataques compuestos usamos un contenedor con nombre propio de combo.
        return new Attack(unit.getName(), 0, Attack.AttackType.NORMAL);
    }

    private int calculateDamageFromUnit(String attackName, Character attacker, Character defender) {
        AttackUnit unit = attackUnitFactory.fromKey(attackName);
        java.util.List<Attack> expanded = unit.expand(combatEngine);

        int totalDamage = 0;
        for (Attack attack : expanded) {
            totalDamage += combatEngine.calculateDamage(attacker, defender, attack);
        }
        return totalDamage;
    }

    private void executeAttackCommand(String battleId, Battle battle, Character attacker, Character defender, int damage, Attack attack) {
        BattleCommand command = new AttackBattleCommand(battle, attacker, defender, attack, damage);
        command.execute();

        commandHistory.computeIfAbsent(battleId, id -> new ArrayDeque<>()).push(command);

        String target = defender == battle.getPlayer() ? "player" : "enemy";
        damageNotifier.notifyDamage(new DamageEvent(
                attacker.getName(),
                defender.getName(),
                attack.getName(),
                damage,
                target
        ));
    }

    public BattleStartResult startBattleFromExternal(String fighter1Name, int fighter1Hp, int fighter1Atk,
                                                     String fighter2Name, int fighter2Hp, int fighter2Atk) {
        Character player = Character.builder(fighter1Name)
                .maxHp(fighter1Hp)
                .attack(fighter1Atk)
                .defense(10)
                .speed(10)
                .build();
        Character enemy = Character.builder(fighter2Name)
                .maxHp(fighter2Hp)
                .attack(fighter2Atk)
                .defense(10)
                .speed(10)
                .build();
        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);
        return new BattleStartResult(battleId, battle);
    }

    public record BattleStartResult(String battleId, Battle battle) {}
}
