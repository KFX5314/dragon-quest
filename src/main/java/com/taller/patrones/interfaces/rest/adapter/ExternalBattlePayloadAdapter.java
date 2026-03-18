package com.taller.patrones.interfaces.rest.adapter;

import java.util.Map;

public interface ExternalBattlePayloadAdapter {
    boolean supports(Map<String, Object> payload);

    ExternalStartBattleRequest adapt(Map<String, Object> payload);
}
