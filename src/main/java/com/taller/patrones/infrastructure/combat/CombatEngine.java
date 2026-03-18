package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;
import com.taller.patrones.infrastructure.combat.factory.AttackFactory;
import com.taller.patrones.infrastructure.combat.factory.InMemoryAttackFactory;
import com.taller.patrones.infrastructure.combat.strategy.CriticalDamageStrategy;
import com.taller.patrones.infrastructure.combat.strategy.DamageStrategy;
import com.taller.patrones.infrastructure.combat.strategy.NormalDamageStrategy;
import com.taller.patrones.infrastructure.combat.strategy.SpecialDamageStrategy;
import com.taller.patrones.infrastructure.combat.strategy.StatusDamageStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {

    private final AttackFactory attackFactory;

    public CombatEngine() {
        this(new InMemoryAttackFactory());
    }

    public CombatEngine(AttackFactory attackFactory) {
        this.attackFactory = attackFactory;
    }

    public Attack createAttack(String name) {
        return attackFactory.create(name);
    }

    // Estrategias de daño
    private static final Map<Attack.AttackType, DamageStrategy> DAMAGE_STRATEGIES = new HashMap<>();
    static {
        DAMAGE_STRATEGIES.put(Attack.AttackType.NORMAL, new NormalDamageStrategy());
        DAMAGE_STRATEGIES.put(Attack.AttackType.SPECIAL, new SpecialDamageStrategy());
        DAMAGE_STRATEGIES.put(Attack.AttackType.STATUS, new StatusDamageStrategy());
        DAMAGE_STRATEGIES.put(Attack.AttackType.CRITICAL, new CriticalDamageStrategy());
    }

    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        DamageStrategy strategy = DAMAGE_STRATEGIES.get(attack.getType());
        if (strategy == null) return 0;
        return strategy.calculate(attacker, defender, attack);
    }

    public void registerAttack(String name, Attack attack) {
        attackFactory.register(name, attack);
    }

    // Para registrar nuevas estrategias de daño
    public static void registerDamageStrategy(Attack.AttackType type, DamageStrategy strategy) {
        DAMAGE_STRATEGIES.put(type, strategy);
    }
}
