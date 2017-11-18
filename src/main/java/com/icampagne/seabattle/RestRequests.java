package com.icampagne.seabattle;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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
    
    private static Map<String, int[][]> playerSeaMap = new TreeMap<String, int[][]>();

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
    	int[][] sea = playerSeaMap.get(userId);
    	System.out.println(String.format("User '%s' shoots at %d,%d; status = %d", userId, seaX, seaY, sea[seaX][seaY]));
    	webSocketServer.shoot(userId, seaX, seaY);
        return String.format("User '%s' shoots at %d,%d", userId, seaX, seaY);
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
			playerSeaMap.put(userId, seaSpot);
			System.out.println(userId + " playerSeas: " + playerSeaMap.size());
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
