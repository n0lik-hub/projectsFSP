package com.stoloto.bonusarena.repository;

import com.stoloto.bonusarena.model.RoundHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoundHistoryRepository extends JpaRepository<RoundHistory, Long> {
    
    List<RoundHistory> findAllByOrderByStartTimeDesc();
    
    List<RoundHistory> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<RoundHistory> findByGameRoomId(Long gameRoomId);
}
