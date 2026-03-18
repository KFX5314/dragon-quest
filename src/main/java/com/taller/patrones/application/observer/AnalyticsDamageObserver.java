package com.taller.patrones.application.observer;

import java.util.logging.Logger;

public class AnalyticsDamageObserver implements DamageObserver {

    private static final Logger LOGGER = Logger.getLogger(AnalyticsDamageObserver.class.getName());

    @Override
    public void onDamage(DamageEvent event) {
        LOGGER.info("[analytics] attack=" + event.attackName() + " damage=" + event.damage());
    }
}
