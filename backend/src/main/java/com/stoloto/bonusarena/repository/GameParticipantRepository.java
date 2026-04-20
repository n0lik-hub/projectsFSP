package com.stoloto.bonusarena.repository;

import com.stoloto.bonusarena.model.GameParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {
    
    List<GameParticipant> findByGameRoomId(Long gameId);
    
    List<GameParticipant> findByUserId(Long userId);
}
