package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {

    // Catálogo de ataques
    private static final java.util.Map<String, Attack> ATTACK_CATALOG = new java.util.HashMap<>();
    static {
        ATTACK_CATALOG.put("TACKLE", new Attack("Tackle", 40, Attack.AttackType.NORMAL));
        ATTACK_CATALOG.put("SLASH", new Attack("Slash", 55, Attack.AttackType.NORMAL));
        ATTACK_CATALOG.put("FIREBALL", new Attack("Fireball", 80, Attack.AttackType.SPECIAL));
        ATTACK_CATALOG.put("ICE_BEAM", new Attack("Ice Beam", 70, Attack.AttackType.SPECIAL));
        ATTACK_CATALOG.put("POISON_STING", new Attack("Poison Sting", 20, Attack.AttackType.STATUS));
        ATTACK_CATALOG.put("THUNDER", new Attack("Thunder", 90, Attack.AttackType.SPECIAL));
        ATTACK_CATALOG.put("METEORO", new Attack("Meteoro", 120, Attack.AttackType.SPECIAL));
        ATTACK_CATALOG.put("GOLPE", new Attack("Golpe", 30, Attack.AttackType.NORMAL));
    }

    public Attack createAttack(String name) {
        if (name == null) return ATTACK_CATALOG.get("GOLPE");
        Attack attack = ATTACK_CATALOG.get(name.toUpperCase());
        return attack != null ? attack : ATTACK_CATALOG.get("GOLPE");
    }

    // Estrategias de daño
    private static final java.util.Map<Attack.AttackType, com.taller.patrones.infrastructure.combat.strategy.DamageStrategy> DAMAGE_STRATEGIES = new java.util.HashMap<>();
    static {
        DAMAGE_STRATEGIES.put(Attack.AttackType.NORMAL, new com.taller.patrones.infrastructure.combat.strategy.NormalDamageStrategy());
        DAMAGE_STRATEGIES.put(Attack.AttackType.SPECIAL, new com.taller.patrones.infrastructure.combat.strategy.SpecialDamageStrategy());
        DAMAGE_STRATEGIES.put(Attack.AttackType.STATUS, new com.taller.patrones.infrastructure.combat.strategy.StatusDamageStrategy());
        // nueva estrategia CRÍTICO
        DAMAGE_STRATEGIES.put(Attack.AttackType.CRITICAL, new com.taller.patrones.infrastructure.combat.strategy.CriticalDamageStrategy());
    }

    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        com.taller.patrones.infrastructure.combat.strategy.DamageStrategy strategy = DAMAGE_STRATEGIES.get(attack.getType());
        if (strategy == null) return 0;
        return strategy.calculate(attacker, defender, attack);
    }
    // Para registrar nuevos ataques desde fuera (opcional)
    public static void registerAttack(String name, Attack attack) {
        ATTACK_CATALOG.put(name.toUpperCase(), attack);
    }

    // Para registrar nuevas estrategias de daño
    public static void registerDamageStrategy(Attack.AttackType type, com.taller.patrones.infrastructure.combat.strategy.DamageStrategy strategy) {
        DAMAGE_STRATEGIES.put(type, strategy);
    }
}
