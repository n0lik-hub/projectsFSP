package com.stoloto.bonusarena.controller;

import com.stoloto.bonusarena.dto.*;
import com.stoloto.bonusarena.model.GameParticipant;
import com.stoloto.bonusarena.model.GameRoom;
import com.stoloto.bonusarena.model.RoundHistory;
import com.stoloto.bonusarena.model.User;
import com.stoloto.bonusarena.service.ConfigurationService;
import com.stoloto.bonusarena.service.GameService;
import com.stoloto.bonusarena.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GameController {
    
    private final GameService gameService;
    private final ConfigurationService configService;
    private final UserService userService;
    
    // ==================== Пользователи ====================
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        Boolean isAdmin = (Boolean) request.get("isAdmin");
        User user = userService.createUser(username, isAdmin);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    // ==================== Конфигурации комнат ====================
    
    @GetMapping("/configurations")
    public ResponseEntity<List<RoomConfigurationDto>> getConfigurations() {
        return ResponseEntity.ok(configService.getAllConfigurations());
    }
    
    @PostMapping("/configurations")
    public ResponseEntity<RoomConfigurationDto> createConfiguration(
            @RequestBody CreateRoomConfigRequest request) {
        RoomConfigurationDto dto = configService.createConfiguration(request);
        return ResponseEntity.ok(dto);
    }
    
    @PutMapping("/configurations/{id}")
    public ResponseEntity<RoomConfigurationDto> updateConfiguration(
            @PathVariable Long id,
            @RequestBody CreateRoomConfigRequest request) {
        RoomConfigurationDto dto = configService.updateConfiguration(id, request);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/configurations/{id}")
    public ResponseEntity<Void> deactivateConfiguration(@PathVariable Long id) {
        configService.deactivateConfiguration(id);
        return ResponseEntity.ok().build();
    }
    
    // ==================== Игровые комнаты ====================
    
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        return ResponseEntity.ok(gameService.getAllRooms());
    }
    
    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getRoomById(id));
    }
    
    @PostMapping("/rooms")
    public ResponseEntity<GameRoom> createRoom(@RequestBody Map<String, Long> request) {
        Long configurationId = request.get("configurationId");
        GameRoom room = gameService.createRoom(configurationId);
        return ResponseEntity.ok(room);
    }
    
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<?> joinRoom(
            @PathVariable Long roomId,
            @RequestBody JoinRoomRequest request) {
        try {
            GameParticipant participant = gameService.joinRoom(
                    roomId, 
                    request.getUserId(), 
                    request.getBuyBoost() != null && request.getBuyBoost()
            );
            return ResponseEntity.ok(participant);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/rooms/{roomId}/boost")
    public ResponseEntity<?> buyBoost(
            @PathVariable Long roomId,
            @RequestBody Map<String, Long> request) {
        try {
            GameParticipant participant = gameService.buyBoost(
                    roomId, 
                    request.get("userId")
            );
            return ResponseEntity.ok(participant);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/rooms/{roomId}/start")
    public ResponseEntity<GameRoom> startRound(@PathVariable Long roomId) {
        GameRoom room = gameService.startRound(roomId);
        return ResponseEntity.ok(room);
    }
    
    @PostMapping("/rooms/{roomId}/finish")
    public ResponseEntity<GameRoom> finishRound(@PathVariable Long roomId) {
        GameRoom room = gameService.getRoomById(roomId);
        
        // Находим комнату с участниками
        GameRoom fullRoom = gameService.startRound(roomId);
        
        // Определяем победителя
        GameParticipant winner = gameService.determineWinner(fullRoom);
        
        // Завершаем раунд
        GameRoom finishedRoom = gameService.finishRound(roomId, winner);
        
        return ResponseEntity.ok(finishedRoom);
    }
    
    // ==================== История раундов ====================
    
    @GetMapping("/history")
    public ResponseEntity<List<RoundHistory>> getRoundHistory() {
        return ResponseEntity.ok(gameService.getAllRooms().stream()
                .filter(r -> "FINISHED".equals(r.getStatus()))
                .map(r -> {
                    // Упрощённое получение истории
                    return null;
                })
                .filter(h -> h != null)
                .toList());
    }
}
