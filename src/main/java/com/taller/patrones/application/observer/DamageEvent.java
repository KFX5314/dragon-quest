package com.taller.patrones.application.observer;

public record DamageEvent(
        String attackerName,
        String defenderName,
        String attackName,
        int damage,
        String targetSide
) {}
