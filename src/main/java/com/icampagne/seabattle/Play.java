package com.icampagne.seabattle;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icampagne.seabattle.Player.PlayerState;

public class Play {

    private static WebSocketServer webSocketServer = new WebSocketServer();
    
    private int shotNr = 0;

    public void inGame(String userId, String mySea) {
		Player player = Player.getPlayerByUserId(userId);
		Player enemy = Player.getPlayerBySessionId(player.getEnemySession().getId());
    	System.out.println(mySea);
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			Sea[] sea = mapper.readValue(mySea, Sea[].class);
			int seaSpot[][] = new int[4][4];
			for (Sea s : sea) {
				seaSpot[s.getH()][s.getV()] = s.getStatus();
			}
			System.out.println("inGame userId = " + userId + ", player = " + Player.getPlayerByUserId(userId));
			Player.getPlayerByUserId(userId).setSea(seaSpot);

			if (enemy.getPlayerState().equals(PlayerState.IN_GAME)) {
				player.setPlayerState(PlayerState.SHOOTED);
				System.out.println("Send SHOOTED to " + player.getUserId());
				webSocketServer.shooted(player.getPlayerSession());
				enemy.setPlayerState(PlayerState.SHOOTING);
				webSocketServer.shooting(player.getEnemySession());
				System.out.println("Send SHOOTING to " + enemy.getUserId());
				shotNr = 1;
			} else {
				player.setPlayerState(PlayerState.IN_GAME);
				System.out.println("Send IN_GAME to " + enemy.getUserId());
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public int shoot(String userId, int seaX, int seaY) {
    	System.out.println(String.format("User '%s' shoots at %d,%d", userId, seaX, seaY));
    	Player player = Player.getPlayerByUserId(userId);
    	int status = ++Player.getPlayerBySessionId(player.getEnemySession().getId()).getSea()[seaX][seaY];
    	System.out.println(String.format("User '%s' shoots at %d,%d; status = %d", userId, seaX, seaY, status));

		System.out.println("shoot from " + player.getPlayerSession().getId() + " to " + player.getEnemySession().getId());
		webSocketServer.sendMessage(player.getEnemySession(), "{'command':'shoot', 'shot':{'x':'" + seaX + "', 'y':'" + seaY + "', 'status':'" + status + "'}}");
		if (checkSea(Player.getPlayerBySessionId(player.getEnemySession().getId()))) {
			webSocketServer.sendStatus(player.getPlayerSession(), PlayerState.WON.name());
			webSocketServer.sendStatus(player.getEnemySession(), PlayerState.LOST.name());
		} else {
			shotNr++;
			if (shotNr >= 4) {
				webSocketServer.sendStatus(player.getPlayerSession(), PlayerState.SHOOTED.name());
				webSocketServer.sendStatus(player.getEnemySession(), PlayerState.SHOOTING.name());
				shotNr = 1;
			}
		}
		return status;
    }
    
    private boolean checkSea(Player player) {
    	boolean won = true;
    	int[][] sea = player.getSea();
    	for (int[] seaX : sea) {
    		for (int seaY : seaX) {
    			if (seaX[seaY] == 2) {
    				won = false;
    			}
    		}
    	}
		return won;
    }

}
