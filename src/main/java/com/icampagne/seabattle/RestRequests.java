package com.icampagne.seabattle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/")
public class RestRequests {

    private WebSocketServer webSocketServer = new WebSocketServer();

	@GET
	@Path("/version")
    public String getMsg()
    {
         return "Seabattle 0.0.3";
    }

	@GET
    @Path("/shoot/{userId}")
    public String shoot(@PathParam("userId") String userId, @QueryParam("seaX") int seaX, @QueryParam("seaY") int seaY) {
    	System.out.println(String.format("User '%s' shoots to %d,%d", userId, seaX, seaY));
    	webSocketServer.shoot(seaX, seaY);
        return String.format("User '%s' shoots to %d,%d", userId, seaX, seaY);
    }
}
