package com.icampagne.seabattle;

import java.util.Map;
import java.util.TreeMap;

public class Player {
    
    public enum PlayerState { PREPARING, IN_GAME, SHOOTING, SHOOTED };
    
    private static Map<String, Player> playerMap = new TreeMap<String, Player>();

	private String userId;
	private String enemyId;
	private PlayerState playerState;
	private int[][] sea;

    public Player() {
    	playerState = PlayerState.PREPARING;
    }

    public Player(String userId) {
    	playerState = PlayerState.PREPARING;
    	this.userId = userId;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEnemyId() {
		return enemyId;
	}

	public void setEnemyId(String enemyId) {
		this.enemyId = enemyId;
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	
	public int[][] getSea() {
		return sea;
	}

	public void setSea(int[][] sea) {
		this.sea = sea;
	}

	public static int[][] getSeaOfPlayer(String userId) {
		return playerMap.get(userId).getSea();
	}

	public static void addPlayer(String userId, int[][] sea) {
		Player player = new Player(userId);
		player.setSea(sea);
		if (!playerMap.isEmpty()) {
			if (playerMap.size() >= 2) {
				playerMap.clear();
			} else {
				String enemyId = (String) playerMap.keySet().toArray()[0];
				Player enemy = playerMap.get(enemyId);
				enemy.setEnemyId(userId);
				player.setEnemyId(enemyId);
			}
		}
		playerMap.put(userId, player);
	}
	
	public static Player getPlayer(String userId) {
		return playerMap.get(userId);
	}
}
