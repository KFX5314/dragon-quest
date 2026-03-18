package com.taller.patrones.application.composite;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.infrastructure.combat.CombatEngine;

import java.util.ArrayList;
import java.util.List;

public class ComboAttackUnit implements AttackUnit {

    private final String comboName;
    private final List<AttackUnit> children;

    public ComboAttackUnit(String comboName) {
        this.comboName = comboName;
        this.children = new ArrayList<>();
    }

    public ComboAttackUnit add(AttackUnit child) {
        children.add(child);
        return this;
    }

    @Override
    public String getName() {
        return comboName;
    }

    @Override
    public List<Attack> expand(CombatEngine combatEngine) {
        List<Attack> expanded = new ArrayList<>();
        for (AttackUnit child : children) {
            expanded.addAll(child.expand(combatEngine));
        }
        return expanded;
    }
}
