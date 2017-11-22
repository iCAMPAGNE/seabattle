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
//    static Set<Session> players = Collections.synchronizedSet(new HashSet<Session>());
//    static Session playerSession[] = new Session[2];
//    static Player player[] = new Player[2];
    
    public void inGame(String id) {
    	
		Player player = Player.getPlayerByUserId(id);
		Player enemy = Player.getPlayerBySessionId(player.getEnemySession().getId());
		
		try {
			if (enemy.getPlayerState().equals(PlayerState.IN_GAME)) {
				player.setPlayerState(PlayerState.SHOOTED);
				System.out.println("Send SHOOTED to " + player.getUserId());
				player.getPlayerSession().getBasicRemote().sendText("{\"command\":\"status\", \"state\":\"SHOOTED\"}");
				enemy.setPlayerState(PlayerState.SHOOTING);
				player.getEnemySession().getBasicRemote().sendText("{\"command\":\"status\", \"state\":\"SHOOTING\"}");
				System.out.println("Send SHOOTING to " + enemy.getUserId());
			} else {
				player.setPlayerState(PlayerState.IN_GAME);
				enemy.getPlayerSession().getBasicRemote().sendText("{\"command\":\"enemyStatus\", \"state\":\"IN_GAME\"}");
				System.out.println("Send IN_GAME to " + enemy.getUserId());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public void shoot(String id, int x, int y) {
    	
		Player player = Player.getPlayerByUserId(id);
		System.out.println("shoot from " + player.getPlayerSession().getId() + " to " + player.getEnemySession().getId());
    	try {
			player.getEnemySession().getBasicRemote().sendText("{\"command\":\"shoot\", \"shot\":{\"x\":\"" + x + "\", \"y\":\"" + y + "\", \"status\":\"1\"}}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@OnOpen
	public void open(Session session) {
		Player.addPlayer(session);
		System.out.println("Websocket OnOpen for player " + session.getId());
    }

    @OnClose
        public void close(Session session) {
		System.out.println("Websocket OnClose session " + session.getId());
		if (Player.getPlayerBySessionId(session.getId()) != null && Player.getPlayerBySessionId(session.getId()).getEnemySession() != null) {
			Player.removePlayer(Player.getPlayerBySessionId(session.getId()).getEnemySession());
		}
		Player.removePlayer(session);
    }

    @OnError
        public void onError(Throwable error) {
		System.out.println("Websocket OnError: " + error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
		System.out.println("Websocket OnMessage, userId will be " + message + " for session nr " + session.getId());
		Player player = Player.getPlayerBySessionId(session.getId());
		player.setUserId(message);
   }
}
