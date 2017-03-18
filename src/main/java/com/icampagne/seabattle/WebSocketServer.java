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

@ServerEndpoint("/socket")
public class WebSocketServer {

	// All open WebSocket sessions of players
    static Set<Session> players = Collections.synchronizedSet(new HashSet<Session>());
    static Session playerSession;
    
    public void shoot(int x, int y) {
    	
    	try {
			playerSession.getBasicRemote().sendText("{\"x\":\"" + x + "\", \"y\":\"" + y + "\", \"status\":\"1\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@OnOpen
	public void open(Session session) {
		players.add(session);
		playerSession = session;
		System.out.println("Websocket OnOpen for player " + session.getId());
   }

   @OnClose
       public void close(Session session) {
		System.out.println("Websocket OnClose");
   }

   @OnError
       public void onError(Throwable error) {
		System.out.println("Websocket OnError");
   }

   @OnMessage
       public void handleMessage(String message, Session session) {
		System.out.println("Websocket OnMessage");
   }
}
