package com.icampagne.seabattle;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/socket")
public class WebSocketServer {

    public boolean inGame(Session session) {
    	return sendStatus(session, "IN_GAME");
    }

    public boolean shooting(Session session) {
    	return sendStatus(session, "SHOOTING");
    }
    
    public boolean shooted(Session session) {
    	return sendStatus(session, "SHOOTED");
    }
    
    public boolean sendStatus(Session session, String status) {
    	System.out.println("Sending status " + status + " to player " + session.getId());
    	return sendMessage(session, String.format("{'command':'status', 'state':'%s'}", status));
    }
    
    public boolean sendMessage(Session session, String message) {
    	if (message == null) {
    		return false;
    	}
    	String text = message.replace('\'', '"');
    	try {
			session.getBasicRemote().sendText(text);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
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
