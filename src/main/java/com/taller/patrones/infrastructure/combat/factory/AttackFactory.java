package com.taller.patrones.infrastructure.combat.factory;

import com.taller.patrones.domain.Attack;

public interface AttackFactory {
    Attack create(String attackName);

    void register(String attackKey, Attack attack);
}
