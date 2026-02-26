package com.taller.patrones.infrastructure.combat.strategy;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public interface DamageStrategy {
    int calculate(Character attacker, Character defender, Attack attack);
}
