package com.icampagne.seabattle;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class RestRequests {

    private static WebSocketServer webSocketServer = new WebSocketServer();

	@GET
	@Path("/version")
    public String getMsg()
    {
         return "Seabattle 0.0.4";
    }

	@GET
    @Path("/shoot/{userId}")
    public Response shoot(@PathParam("userId") String userId, @QueryParam("seaX") int seaX, @QueryParam("seaY") int seaY) {
    	System.out.println(String.format("User '%s' shoots at %d,%d", userId, seaX, seaY));
    	int[][] sea = Player.getSeaOfPlayer(userId);
    	Player player = Player.getPlayer(userId);
    	int status = Player.getSeaOfPlayer(player.getEnemyId())[seaX][seaY];
    	System.out.println(String.format("User '%s' shoots at %d,%d; status = %d", userId, seaX, seaY, status));
    	webSocketServer.shoot(userId, seaX, seaY);
    	String result = "{\"status\":\"" + status + "\"}";
		return Response.status(201).entity(result ).build();
    }

	@POST
	@Consumes("application/json")
	@Path("/inGame/{userId}")
    public Response inGame(@PathParam("userId") String userId, String mySea) {
    	System.out.println(mySea);
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			Sea[] sea = mapper.readValue(mySea, Sea[].class);
			int seaSpot[][] = new int[4][4];
			for (Sea s : sea) {
				seaSpot[s.getH()][s.getV()] = s.getStatus();
			}
			Player.addPlayer(userId, seaSpot);
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
    	webSocketServer.inGame(userId);
        String result = "OK";
		return Response.status(201).entity(result ).build();
    }
}
