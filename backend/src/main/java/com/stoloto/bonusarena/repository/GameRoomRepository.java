package com.stoloto.bonusarena.repository;

import com.stoloto.bonusarena.model.GameRoom;
import com.stoloto.bonusarena.model.GameRoom.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
    
    List<GameRoom> findByStatus(RoomStatus status);
    
    List<GameRoom> findAllByOrderByCreatedAtDesc();
}
