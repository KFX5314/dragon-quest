package com.taller.patrones.infrastructure.combat.strategy;

import com.taller.patrones.domain.Attack;

import java.util.HashMap;
import java.util.Map;

public class DamageStrategyRegistry {

    private final Map<Attack.AttackType, DamageStrategy> strategies = new HashMap<>();

    public DamageStrategyRegistry() {
        register(Attack.AttackType.NORMAL, new NormalDamageStrategy());
        register(Attack.AttackType.SPECIAL, new SpecialDamageStrategy());
        register(Attack.AttackType.STATUS, new StatusDamageStrategy());
        register(Attack.AttackType.CRITICAL, new CriticalDamageStrategy());
    }

    public void register(Attack.AttackType attackType, DamageStrategy strategy) {
        if (attackType == null || strategy == null) {
            return;
        }
        strategies.put(attackType, strategy);
    }

    public DamageStrategy resolve(Attack.AttackType attackType) {
        return strategies.get(attackType);
    }
}
