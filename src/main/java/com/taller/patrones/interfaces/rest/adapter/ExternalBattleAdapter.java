package com.taller.patrones.interfaces.rest.adapter;

import java.util.List;
import java.util.Map;

public class ExternalBattleAdapter {

    private final List<ExternalBattlePayloadAdapter> adapters;

    public ExternalBattleAdapter() {
        this.adapters = List.of(
                new LegacyFighterPayloadAdapter(),
                new NestedPlayerPayloadAdapter()
        );
    }

    public ExternalStartBattleRequest adapt(Map<String, Object> payload) {
        for (ExternalBattlePayloadAdapter adapter : adapters) {
            if (adapter.supports(payload)) {
                return adapter.adapt(payload);
            }
        }

        return new ExternalStartBattleRequest("Héroe", 150, 25, "Dragón", 120, 30);
    }
}
