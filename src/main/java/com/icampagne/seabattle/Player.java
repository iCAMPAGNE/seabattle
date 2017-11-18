package com.icampagne.seabattle;

public class Player {
    
    public enum PlayerState { PREPARING, IN_GAME, SHOOTING, SHOOTED };

	private String userId;
	private PlayerState playerState;

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

	public PlayerState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
    
    
}
