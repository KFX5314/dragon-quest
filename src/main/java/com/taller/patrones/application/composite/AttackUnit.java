package com.taller.patrones.application.composite;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.infrastructure.combat.CombatEngine;

import java.util.List;

public interface AttackUnit {
    String getName();

    List<Attack> expand(CombatEngine combatEngine);
}
