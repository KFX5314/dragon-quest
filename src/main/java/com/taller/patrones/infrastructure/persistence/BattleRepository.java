package com.taller.patrones.infrastructure.persistence;

import com.taller.patrones.domain.Battle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Almacena las batallas activas en memoria.
 * <p>
 * Se expone como singleton para compartir estado entre todos los servicios.
 */
public class BattleRepository {

    private static final BattleRepository INSTANCE = new BattleRepository();

    private final Map<String, Battle> battles = new ConcurrentHashMap<>();

    private BattleRepository() {}

    public static BattleRepository getInstance() {
        return INSTANCE;
    }

    public void save(String id, Battle battle) {
        battles.put(id, battle);
    }

    public Battle findById(String id) {
        return battles.get(id);
    }

    public void remove(String id) {
        battles.remove(id);
    }
}
