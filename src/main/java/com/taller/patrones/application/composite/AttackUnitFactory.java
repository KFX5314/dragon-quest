package com.taller.patrones.application.composite;

public class AttackUnitFactory {

    public AttackUnit fromKey(String attackKey) {
        String key = attackKey == null ? "TACKLE" : attackKey.toUpperCase();

        if ("COMBO_TRIPLE".equals(key)) {
            return new ComboAttackUnit("COMBO_TRIPLE")
                    .add(new SingleAttackUnit("TACKLE"))
                    .add(new SingleAttackUnit("SLASH"))
                    .add(new SingleAttackUnit("FIREBALL"));
        }

        return new SingleAttackUnit(key);
    }
}
