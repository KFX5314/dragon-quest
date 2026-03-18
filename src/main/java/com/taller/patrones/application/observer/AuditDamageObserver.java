package com.taller.patrones.application.observer;

import java.util.logging.Logger;

public class AuditDamageObserver implements DamageObserver {

    private static final Logger LOGGER = Logger.getLogger(AuditDamageObserver.class.getName());

    @Override
    public void onDamage(DamageEvent event) {
        LOGGER.info("[audit] " + event.attackerName() + " -> " + event.defenderName() + " with " + event.attackName());
    }
}
