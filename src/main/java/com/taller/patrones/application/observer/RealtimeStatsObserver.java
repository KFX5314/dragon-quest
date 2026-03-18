package com.taller.patrones.application.observer;

import java.util.logging.Logger;

public class RealtimeStatsObserver implements DamageObserver {

    private static final Logger LOGGER = Logger.getLogger(RealtimeStatsObserver.class.getName());

    @Override
    public void onDamage(DamageEvent event) {
        LOGGER.info("[realtime] target=" + event.targetSide() + " damage=" + event.damage());
    }
}
