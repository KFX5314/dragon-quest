package com.taller.patrones.application.composite;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.infrastructure.combat.CombatEngine;

import java.util.List;

public class SingleAttackUnit implements AttackUnit {

    private final String attackKey;

    public SingleAttackUnit(String attackKey) {
        this.attackKey = attackKey;
    }

    @Override
    public String getName() {
        return attackKey;
    }

    @Override
    public List<Attack> expand(CombatEngine combatEngine) {
        return List.of(combatEngine.createAttack(attackKey));
    }
}
