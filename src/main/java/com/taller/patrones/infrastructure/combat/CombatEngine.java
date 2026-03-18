package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;
import com.taller.patrones.infrastructure.combat.factory.AttackFactory;
import com.taller.patrones.infrastructure.combat.factory.InMemoryAttackFactory;
import com.taller.patrones.infrastructure.combat.strategy.DamageStrategy;
import com.taller.patrones.infrastructure.combat.strategy.DamageStrategyRegistry;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {

    private final AttackFactory attackFactory;
    private final DamageStrategyRegistry strategyRegistry;

    public CombatEngine() {
        this(new InMemoryAttackFactory(), new DamageStrategyRegistry());
    }

    public CombatEngine(AttackFactory attackFactory) {
        this(attackFactory, new DamageStrategyRegistry());
    }

    public CombatEngine(AttackFactory attackFactory, DamageStrategyRegistry strategyRegistry) {
        this.attackFactory = attackFactory;
        this.strategyRegistry = strategyRegistry;
    }

    public Attack createAttack(String name) {
        return attackFactory.create(name);
    }

    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        DamageStrategy strategy = strategyRegistry.resolve(attack.getType());
        if (strategy == null) return 0;
        return strategy.calculate(attacker, defender, attack);
    }

    public void registerAttack(String name, Attack attack) {
        attackFactory.register(name, attack);
    }

    // Para registrar nuevas estrategias de daño
    public void registerDamageStrategy(Attack.AttackType type, DamageStrategy strategy) {
        strategyRegistry.register(type, strategy);
    }
}
