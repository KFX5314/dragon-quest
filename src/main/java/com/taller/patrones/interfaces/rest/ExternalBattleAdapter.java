package com.taller.patrones.interfaces.rest;

import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

import java.util.Map;

/**
 * Convierte estructuras JSON externas a nuestro dominio.
 * <p>
 * Permite introducir nuevos proveedores con formatos distintos sin tocar
 * el controller (patrón Adapter).
 */
public class ExternalBattleAdapter {

    public static Battle toBattle(Map<String, Object> body) {
        // detectar esquema conocido y mapear apropiadamente
        // ejemplo: proveedor original usa fighter1_hp etc.
        if (body.containsKey("fighter1_hp")) {
            String f1n = (String) body.getOrDefault("fighter1_name", "Héroe");
            int f1hp = ((Number) body.getOrDefault("fighter1_hp", 150)).intValue();
            int f1atk = ((Number) body.getOrDefault("fighter1_atk", 25)).intValue();
            String f2n = (String) body.getOrDefault("fighter2_name", "Dragón");
            int f2hp = ((Number) body.getOrDefault("fighter2_hp", 120)).intValue();
            int f2atk = ((Number) body.getOrDefault("fighter2_atk", 30)).intValue();
            Character p = Character.builder(f1n).maxHp(f1hp).attack(f1atk).defense(10).speed(10).build();
            Character e = Character.builder(f2n).maxHp(f2hp).attack(f2atk).defense(10).speed(10).build();
            return new Battle(p, e);
        }
        // nuevo proveedor con player.health / player.attack etc.
        if (body.containsKey("player")) {
            Map<String, Object> player = (Map<String, Object>) body.get("player");
            Map<String, Object> enemy = (Map<String, Object>) body.get("enemy");
            String pName = (String) player.getOrDefault("name", "Héroe");
            int pHp = ((Number) player.getOrDefault("health", 150)).intValue();
            int pAtk = ((Number) player.getOrDefault("attack", 25)).intValue();
            String eName = (String) enemy.getOrDefault("name", "Dragón");
            int eHp = ((Number) enemy.getOrDefault("health", 120)).intValue();
            int eAtk = ((Number) enemy.getOrDefault("attack", 30)).intValue();
            Character p = Character.builder(pName).maxHp(pHp).attack(pAtk).defense(10).speed(10).build();
            Character e = Character.builder(eName).maxHp(eHp).attack(eAtk).defense(10).speed(10).build();
            return new Battle(p, e);
        }
        // formato desconocido -> batalla vacía
        return new Battle(
            Character.builder("Héroe").build(),
            Character.builder("Dragón").build()
        );
    }
}
