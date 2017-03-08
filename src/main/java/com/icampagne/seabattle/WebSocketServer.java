package com.icampagne.seabattle;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.inject.Inject;

@ServerEndpoint("/socket")
public class WebSocketServer {
	   
    @Inject
    private DeviceSessionHandler sessionHandler;

	@OnOpen
       public void open(Session session) {
		System.out.println("Websocket OnOpen");
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
