package com.example.application.ApplicationServer;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {
    private final RoomManager roomManager;

    public MatchingController(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @PostMapping("/auto-join")
    public ResponseEntity<Map<String, Object>> autoJoin(@RequestParam String playerName) {
        Room room = roomManager.findAvailableRoom();
        if (room == null) {
            room = roomManager.createRoom();
        }

        Player me = new Player(playerName);
        room.addPlayer(me);

        Map<String, Object> response = new HashMap<>();
        response.put("room", room);
        response.put("me", me); // 自分のIDを返す

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Room> getStatus(@RequestParam String roomId) {
        Room room = roomManager.getRoom(roomId);
        return (room != null) ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }
}