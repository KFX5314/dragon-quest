package com.taller.patrones.infrastructure.combat.strategy;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

import java.util.Random;

public class CriticalDamageStrategy implements DamageStrategy {
    private static final Random RNG = new Random();

    @Override
    public int calculate(Character attacker, Character defender, Attack attack) {
        // base damage same que normal
        int raw = attacker.getAttack() * attack.getBasePower() / 100;
        int effective = Math.max(1, raw - defender.getDefense());
        // 20% de probabilidad de crítico que multiplica por 1.5
        if (RNG.nextDouble() < 0.20) {
            return (int) Math.ceil(effective * 1.5);
        }
        return effective;
    }
}
