package com.taller.patrones.application.facade;

import com.taller.patrones.application.BattleService;
import com.taller.patrones.interfaces.rest.adapter.ExternalStartBattleRequest;
import com.taller.patrones.domain.Battle;

public class CombatFacade {

    private final BattleService battleService;

    public CombatFacade() {
        this.battleService = new BattleService();
    }

    public BattleService.BattleStartResult startBattle(String playerName, String enemyName) {
        return battleService.startBattle(playerName, enemyName);
    }

    public BattleService.BattleStartResult startBattleFromExternal(ExternalStartBattleRequest request) {
        return battleService.startBattleFromExternal(
                request.playerName(), request.playerHp(), request.playerAttack(),
                request.enemyName(), request.enemyHp(), request.enemyAttack()
        );
    }

    public Battle getBattle(String battleId) {
        return battleService.getBattle(battleId);
    }

    public Battle executeAttack(String battleId, String attackName) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null || battle.isFinished()) {
            return battle;
        }

        if (battle.isPlayerTurn()) {
            battleService.executePlayerAttack(battleId, attackName);
        } else {
            battleService.executeEnemyAttack(battleId, attackName);
        }

        return battleService.getBattle(battleId);
    }

    public Battle executeEnemyTurn(String battleId) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null || battle.isFinished() || battle.isPlayerTurn()) {
            return battle;
        }

        String attack = BattleService.ENEMY_ATTACKS.get((int) (Math.random() * BattleService.ENEMY_ATTACKS.size()));
        battleService.executeEnemyAttack(battleId, attack);
        return battleService.getBattle(battleId);
    }

    public Battle undoLastAttack(String battleId) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null) {
            return null;
        }

        battleService.undoLastAttack(battleId);
        return battleService.getBattle(battleId);
    }
}
