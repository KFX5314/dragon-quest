package com.taller.patrones.interfaces.rest;

import com.taller.patrones.application.BattleService;
import com.taller.patrones.application.facade.CombatFacade;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;
import com.taller.patrones.interfaces.rest.adapter.ExternalBattleAdapter;
import com.taller.patrones.interfaces.rest.adapter.ExternalStartBattleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/battle")
@CrossOrigin(origins = "*")
public class BattleController {

    private final CombatFacade combatFacade = new CombatFacade();
    private final ExternalBattleAdapter externalBattleAdapter = new ExternalBattleAdapter();

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startBattle(@RequestBody(required = false) Map<String, String> body) {
        String playerName = body != null && body.containsKey("playerName") ? body.get("playerName") : null;
        String enemyName = body != null && body.containsKey("enemyName") ? body.get("enemyName") : null;

        var result = combatFacade.startBattle(playerName, enemyName);
        Battle battle = result.battle();

        return ResponseEntity.ok(Map.of(
                "battleId", result.battleId(),
                "player", toCharacterDto(battle.getPlayer()),
                "enemy", toCharacterDto(battle.getEnemy()),
                "currentTurn", battle.getCurrentTurn(),
                "battleLog", battle.getBattleLog(),
                "finished", battle.isFinished(),
                "playerAttacks", BattleService.PLAYER_ATTACKS,
                "enemyAttacks", BattleService.ENEMY_ATTACKS,
                "lastDamage", 0,
                "lastDamageTarget", ""
        ));
    }

    /**
     * Endpoint alternativo: recibe datos de combate en formato "externo".
     * Los campos vienen con nombres distintos (fighter1_hp, fighter1_atk...).
     * La conversión se hace aquí, manualmente, en el controller.
     */
    @PostMapping("/start/external")
    public ResponseEntity<Map<String, Object>> startBattleFromExternal(@RequestBody Map<String, Object> body) {
        ExternalStartBattleRequest request = externalBattleAdapter.adapt(body);

        var result = combatFacade.startBattleFromExternal(request);

        Battle battle = result.battle();

        return ResponseEntity.ok(Map.of(
                "battleId", result.battleId(),
                "player", toCharacterDto(battle.getPlayer()),
                "enemy", toCharacterDto(battle.getEnemy()),
                "currentTurn", battle.getCurrentTurn(),
                "battleLog", battle.getBattleLog(),
                "finished", battle.isFinished(),
                "playerAttacks", BattleService.PLAYER_ATTACKS,
                "lastDamage", 0,
                "lastDamageTarget", ""
        ));
    }

    @GetMapping("/{battleId}")
    public ResponseEntity<Map<String, Object>> getBattle(@PathVariable String battleId) {
        Battle battle = combatFacade.getBattle(battleId);
        if (battle == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toBattleDto(battle));
    }

    @PostMapping("/{battleId}/attack")
    public ResponseEntity<Map<String, Object>> attack(@PathVariable String battleId,
                                                       @RequestBody Map<String, String> body) {
        Battle battle = combatFacade.getBattle(battleId);
        if (battle == null) return ResponseEntity.notFound().build();

        String attackName = body != null && body.get("attack") != null ? body.get("attack") : "TACKLE";
        return ResponseEntity.ok(toBattleDto(combatFacade.executeAttack(battleId, attackName)));
    }

    @PostMapping("/{battleId}/enemy-turn")
    public ResponseEntity<Map<String, Object>> enemyTurn(@PathVariable String battleId) {
        Battle battle = combatFacade.getBattle(battleId);
        if (battle == null) return ResponseEntity.notFound().build();
        if (battle.isPlayerTurn() || battle.isFinished()) {
            return ResponseEntity.ok(toBattleDto(battle));
        }
        return ResponseEntity.ok(toBattleDto(combatFacade.executeEnemyTurn(battleId)));
    }

    @PostMapping("/{battleId}/undo")
    public ResponseEntity<Map<String, Object>> undoLastAttack(@PathVariable String battleId) {
        Battle battle = combatFacade.getBattle(battleId);
        if (battle == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toBattleDto(combatFacade.undoLastAttack(battleId)));
    }

    private Map<String, Object> toBattleDto(Battle battle) {
        return Map.of(
                "player", toCharacterDto(battle.getPlayer()),
                "enemy", toCharacterDto(battle.getEnemy()),
                "currentTurn", battle.getCurrentTurn(),
                "battleLog", battle.getBattleLog(),
                "finished", battle.isFinished(),
                "playerAttacks", BattleService.PLAYER_ATTACKS,
                "lastDamage", battle.getLastDamage(),
                "lastDamageTarget", battle.getLastDamageTarget() != null ? battle.getLastDamageTarget() : ""
        );
    }

    private Map<String, Object> toCharacterDto(Character c) {
        return Map.of(
                "name", c.getName(),
                "currentHp", c.getCurrentHp(),
                "maxHp", c.getMaxHp(),
                "hpPercentage", c.getHpPercentage(),
                "attack", c.getAttack(),
                "defense", c.getDefense(),
                "speed", c.getSpeed(),
                "alive", c.isAlive()
        );
    }
}
