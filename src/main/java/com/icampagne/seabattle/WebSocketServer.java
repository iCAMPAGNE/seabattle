package com.icampagne.seabattle;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.icampagne.seabattle.Player.PlayerState;

@ServerEndpoint("/socket")
public class WebSocketServer {

	// All open WebSocket sessions of players
    static Set<Session> players = Collections.synchronizedSet(new HashSet<Session>());
    static Session playerSession[] = new Session[2];
    static Player player[] = new Player[2];
    
    public void inGame(String id) {
    	
		int i = id.equals(player[0].getUserId()) ? 0 : 1;
		
		try {
			if (player[1 - i].getPlayerState().equals(PlayerState.IN_GAME)) {
				player[i].setPlayerState(PlayerState.SHOOTED);
				System.out.println("Send SHOOTED to " + i);
				playerSession[i].getBasicRemote().sendText("{\"command\":\"status\", \"state\":\"SHOOTED\"}");
				player[1-i].setPlayerState(PlayerState.SHOOTING);
				playerSession[1-i].getBasicRemote().sendText("{\"command\":\"status\", \"state\":\"SHOOTING\"}");
				System.out.println("Send SHOOTING to " + (1-i));
			} else {
				player[i].setPlayerState(PlayerState.IN_GAME);
				playerSession[1-i].getBasicRemote().sendText("{\"command\":\"enemyStatus\", \"state\":\"IN_GAME\"}");
				System.out.println("Send IN_GAME to " + (1-i));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public void shoot(String id, int x, int y) {
    	
		int i = id.equals(player[0].getUserId()) ? 0 : 1;
    	try {
			playerSession[1-i].getBasicRemote().sendText("{\"command\":\"shoot\", \"shot\":{\"x\":\"" + x + "\", \"y\":\"" + y + "\", \"status\":\"1\"}}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@OnOpen
	public void open(Session session) {
		int i = playerSession[0] == null || playerSession[1] != null ? 0 : 1;
		playerSession[i] = session;
		System.out.println("Websocket OnOpen for player " + session.getId());
   }

   @OnClose
       public void close(Session session) {
		System.out.println("Websocket OnClose session " + session.getId());
		for (int i = 0; i < playerSession.length; i++) {
			System.out.println("On close playerSession[" + i + "] = " + playerSession[i].getId());
			if (session.getId().equals(playerSession[i].getId())) {
				playerSession[i] = null;
			}
		}
   }

   @OnError
       public void onError(Throwable error) {
		System.out.println("Websocket OnError");
   }

   @OnMessage
       public void handleMessage(String message, Session session) {
		System.out.println("Websocket OnMessage: " + message);
		int i = session.getId().equals(playerSession[0].getId()) ? 0 : 1;
		player[i] = new Player(message);
   }
}
