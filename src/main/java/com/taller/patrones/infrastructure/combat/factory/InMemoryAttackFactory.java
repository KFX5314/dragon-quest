package com.taller.patrones.infrastructure.combat.factory;

import com.taller.patrones.domain.Attack;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAttackFactory implements AttackFactory {

    private static final String DEFAULT_ATTACK = "GOLPE";
    private final Map<String, Attack> attackCatalog = new HashMap<>();

    public InMemoryAttackFactory() {
        register("TACKLE", new Attack("Tackle", 40, Attack.AttackType.NORMAL));
        register("SLASH", new Attack("Slash", 55, Attack.AttackType.NORMAL));
        register("FIREBALL", new Attack("Fireball", 80, Attack.AttackType.SPECIAL));
        register("ICE_BEAM", new Attack("Ice Beam", 70, Attack.AttackType.SPECIAL));
        register("POISON_STING", new Attack("Poison Sting", 20, Attack.AttackType.STATUS));
        register("THUNDER", new Attack("Thunder", 90, Attack.AttackType.SPECIAL));
        register("METEORO", new Attack("Meteoro", 120, Attack.AttackType.SPECIAL));
        register(DEFAULT_ATTACK, new Attack("Golpe", 30, Attack.AttackType.NORMAL));
    }

    @Override
    public Attack create(String attackName) {
        if (attackName == null) {
            return attackCatalog.get(DEFAULT_ATTACK);
        }

        Attack attack = attackCatalog.get(attackName.toUpperCase());
        return attack != null ? attack : attackCatalog.get(DEFAULT_ATTACK);
    }

    @Override
    public void register(String attackKey, Attack attack) {
        if (attackKey == null || attack == null) {
            return;
        }
        attackCatalog.put(attackKey.toUpperCase(), attack);
    }
}
