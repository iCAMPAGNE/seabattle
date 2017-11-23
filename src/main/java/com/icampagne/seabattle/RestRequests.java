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
	
	private static Play play = new Play();

	@GET
	@Path("/version")
    public String getMsg()
    {
         return "Seabattle 0.0.4";
    }

	@GET
    @Path("/shoot/{userId}")
    public Response shoot(@PathParam("userId") String userId, @QueryParam("seaX") int seaX, @QueryParam("seaY") int seaY) {
    	int status = play.shoot(userId, seaX, seaY);
    	String result = "{\"status\":\"" + status + "\"}";
		return Response.status(201).entity(result ).build();
    }

	@POST
	@Consumes("application/json")
	@Path("/inGame/{userId}")
    public Response inGame(@PathParam("userId") String userId, String mySea) {
    	play.inGame(userId, mySea);
        String result = "OK";
		return Response.status(201).entity(result ).build();
    }
}
