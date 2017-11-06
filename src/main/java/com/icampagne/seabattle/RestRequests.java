package com.icampagne.seabattle;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
    	System.out.println(String.format("User '%s' shoots at %d,%d", userId, seaX, seaY));
    	webSocketServer.shoot(userId, seaX, seaY);
        return String.format("User '%s' shoots at %d,%d", userId, seaX, seaY);
    }

	@POST
	@Consumes("application/json")
	@Path("/startGame/{userId}")
    public Response startGame(String mySea) {
    	System.out.println(mySea);
        String result = "OK";
		return Response.status(201).entity(result ).build();
    }
}
