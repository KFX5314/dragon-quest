package com.taller.patrones.interfaces.rest.adapter;

public record ExternalStartBattleRequest(
        String playerName,
        int playerHp,
        int playerAttack,
        String enemyName,
        int enemyHp,
        int enemyAttack
) {}
