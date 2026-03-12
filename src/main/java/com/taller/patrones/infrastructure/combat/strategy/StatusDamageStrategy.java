package com.taller.patrones.infrastructure.combat.strategy;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class StatusDamageStrategy implements DamageStrategy {
    @Override
    public int calculate(Character attacker, Character defender, Attack attack) {
        // Ataques de estado (veneno, parálisis) no hacen daño directo.
        // Podría implementarse un efecto diferido, pero por ahora siempre 0.
        return 0;
    }
}
