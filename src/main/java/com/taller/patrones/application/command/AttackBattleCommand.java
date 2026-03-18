package com.taller.patrones.application.command;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

public class AttackBattleCommand implements BattleCommand {

    private final Battle battle;
    private final Character attacker;
    private final Character defender;
    private final Attack attack;
    private final int damage;

    private int previousHp;
    private int previousLastDamage;
    private String previousLastDamageTarget;
    private boolean wasFinished;
    private int addedLogEntries;

    public AttackBattleCommand(Battle battle, Character attacker, Character defender, Attack attack, int damage) {
        this.battle = battle;
        this.attacker = attacker;
        this.defender = defender;
        this.attack = attack;
        this.damage = damage;
    }

    @Override
    public void execute() {
        previousHp = defender.getCurrentHp();
        previousLastDamage = battle.getLastDamage();
        previousLastDamageTarget = battle.getLastDamageTarget();
        wasFinished = battle.isFinished();
        addedLogEntries = 0;

        defender.takeDamage(damage);
        String target = defender == battle.getPlayer() ? "player" : "enemy";
        battle.setLastDamage(damage, target);

        battle.log(attacker.getName() + " usa " + attack.getName() + " y hace " + damage + " de daño a " + defender.getName());
        addedLogEntries++;

        battle.switchTurn();
        if (!defender.isAlive()) {
            battle.finish(attacker.getName());
            addedLogEntries++;
        }
    }

    @Override
    public void undo() {
        defender.restoreHp(previousHp);
        battle.switchTurn();

        for (int i = 0; i < addedLogEntries; i++) {
            battle.removeLastLog();
        }

        battle.setLastDamage(previousLastDamage, previousLastDamageTarget);
        if (!wasFinished) {
            battle.reopen();
        }
    }
}
