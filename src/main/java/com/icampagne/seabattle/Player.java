package com.icampagne.seabattle;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import javax.websocket.Session;


public class Player {
    
    public enum PlayerState { PREPARING, IN_GAME, SHOOTING, SHOOTED };
    
    private static Set<Player> playersSet = new HashSet<Player>();

	private String userId;
	private Session playerSession;
	private Session enemySession;
	private PlayerState playerState;
	private int[][] sea;

    public Player() {
    	playerState = PlayerState.PREPARING;
    }

    public Player(Session playerSession) {
    	playerState = PlayerState.PREPARING;
    	this.playerSession = playerSession;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Session getEnemySession() {
		return enemySession;
	}

	public void setEnemySession(Session enemySession) {
		this.enemySession = enemySession;
	}

	public Session getPlayerSession() {
		return playerSession;
	}

	public void setPlayerSession(Session playerSession) {
		this.playerSession = playerSession;
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
		return getPlayerByUserId(userId).getSea();
	}

	public static void addPlayer(Session session) {
		Player player = new Player(session);
		if (!playersSet.isEmpty()) {
			if (playersSet.size() >= 2) {
				playersSet.clear();
			} else {
				Optional<Player> enemyPlayerOptional = playersSet.stream().findFirst();
				Player enemyPlayer = enemyPlayerOptional.get();
				enemyPlayer.setEnemySession(session);
				player.setEnemySession(enemyPlayer.getPlayerSession());
			}
		}
		playersSet.add(player);
	}

	public static Player getPlayerBySessionId(String sessionId) {
		Optional<Player> optionalPlayer = playersSet.stream().filter(player -> sessionId.equals(player.playerSession.getId())).findFirst();
		return optionalPlayer.isPresent() ? optionalPlayer.get() : null;
	}

	public static Player getPlayerByUserId(String userId) {
		Optional<Player> optionalPlayer = playersSet.stream().filter(player -> userId.equals(player.getUserId())).findFirst();
		return optionalPlayer.isPresent() ? optionalPlayer.get() : null;
	}
	
	public static boolean removePlayer(Session session) {
		System.out.println(session.getId() + "  " + Player.getPlayerBySessionId(session.getId()));
		Player player = Player.getPlayerBySessionId(session.getId());
		return player != null && playersSet.contains(player) ? playersSet.remove(player) : false;
	}
}
