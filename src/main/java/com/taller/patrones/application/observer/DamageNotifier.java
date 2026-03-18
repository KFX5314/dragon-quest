package com.taller.patrones.application.observer;

import java.util.ArrayList;
import java.util.List;

public class DamageNotifier {

    private final List<DamageObserver> observers = new ArrayList<>();

    public void subscribe(DamageObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void notifyDamage(DamageEvent event) {
        for (DamageObserver observer : observers) {
            observer.onDamage(event);
        }
    }
}
