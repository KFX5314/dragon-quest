package com.taller.patrones.interfaces.rest.adapter;

import java.util.Map;

public class LegacyFighterPayloadAdapter implements ExternalBattlePayloadAdapter {

    @Override
    public boolean supports(Map<String, Object> payload) {
        return payload != null && payload.containsKey("fighter1_hp");
    }

    @Override
    public ExternalStartBattleRequest adapt(Map<String, Object> payload) {
        String fighter1Name = (String) payload.getOrDefault("fighter1_name", "Héroe");
        int fighter1Hp = ((Number) payload.getOrDefault("fighter1_hp", 150)).intValue();
        int fighter1Attack = ((Number) payload.getOrDefault("fighter1_atk", 25)).intValue();

        String fighter2Name = (String) payload.getOrDefault("fighter2_name", "Dragón");
        int fighter2Hp = ((Number) payload.getOrDefault("fighter2_hp", 120)).intValue();
        int fighter2Attack = ((Number) payload.getOrDefault("fighter2_atk", 30)).intValue();

        return new ExternalStartBattleRequest(
                fighter1Name, fighter1Hp, fighter1Attack,
                fighter2Name, fighter2Hp, fighter2Attack
        );
    }
}
