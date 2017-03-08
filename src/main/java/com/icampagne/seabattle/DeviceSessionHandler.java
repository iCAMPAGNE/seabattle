package com.icampagne.seabattle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.websocket.Session;

import org.json.JSONObject;


public class DeviceSessionHandler {
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Device> devices = new HashSet<>();
    
 public void addSession(Session session) {
     sessions.add(session);
 }

 public void removeSession(Session session) {
     sessions.remove(session);
 }

 public List<Device> getDevices() {
     return new ArrayList<>(devices);
 }

 public void addDevice(Device device) {
 }

 public void removeDevice(int id) {
 }

 public void toggleDevice(int id) {
 }

 private Device getDeviceById(int id) {
     return null;
 }

 private JSONObject createAddMessage(Device device) {
     return null;
 }

 private void sendToAllConnectedSessions(JSONObject message) {
 }

 private void sendToSession(Session session, JSONObject message) {
 }
}