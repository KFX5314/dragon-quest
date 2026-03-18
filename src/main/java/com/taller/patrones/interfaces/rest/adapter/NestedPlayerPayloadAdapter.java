package com.taller.patrones.interfaces.rest.adapter;

import java.util.Map;

public class NestedPlayerPayloadAdapter implements ExternalBattlePayloadAdapter {

    @Override
    public boolean supports(Map<String, Object> payload) {
        return payload != null && payload.containsKey("player") && payload.containsKey("enemy");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExternalStartBattleRequest adapt(Map<String, Object> payload) {
        Map<String, Object> player = (Map<String, Object>) payload.get("player");
        Map<String, Object> enemy = (Map<String, Object>) payload.get("enemy");

        String playerName = (String) player.getOrDefault("name", "Héroe");
        int playerHp = ((Number) player.getOrDefault("health", 150)).intValue();
        int playerAttack = ((Number) player.getOrDefault("attack", 25)).intValue();

        String enemyName = (String) enemy.getOrDefault("name", "Dragón");
        int enemyHp = ((Number) enemy.getOrDefault("health", 120)).intValue();
        int enemyAttack = ((Number) enemy.getOrDefault("attack", 30)).intValue();

        return new ExternalStartBattleRequest(
                playerName, playerHp, playerAttack,
                enemyName, enemyHp, enemyAttack
        );
    }
}
