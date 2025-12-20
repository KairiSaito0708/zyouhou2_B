package com.example.application.ApplicationServer;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class RoomManager {
    private Map<String, Room> activeRooms = new ConcurrentHashMap<>();

    public Room getRoom(String roomId) {
        return activeRooms.get(roomId);
    }

    public Room createRoom() {
        Room newRoom = new Room();
        activeRooms.put(newRoom.getRoomId(), newRoom);
        return newRoom;
    }

    public Room findAvailableRoom() {
        return activeRooms.values().stream()
                .filter(room -> room.getPlayers().size() < 4)
                .findFirst()
                .orElse(null);
    }
}