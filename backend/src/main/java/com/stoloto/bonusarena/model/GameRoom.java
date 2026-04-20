package com.stoloto.bonusarena.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "configuration_id", nullable = false)
    private RoomConfiguration configuration;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoomStatus status = RoomStatus.WAITING;
    
    @OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GameParticipant> participants = new ArrayList<>();
    
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime startedAt;
    
    @Column
    private LocalDateTime finishedAt;
    
    @Column
    private Long winnerId;
    
    @Column
    private Integer totalPrizePool;
    
    public enum RoomStatus {
        WAITING,    // Ожидание игроков
        STARTING,   // Таймер истёк, идёт заполнение ботами
        RUNNING,    // Раунд в процессе
        FINISHED    // Раунд завершён
    }
    
    public int getOccupiedSeats() {
        return (int) participants.stream()
                .filter(p -> !p.isBot() || p.getUser() != null)
                .count();
    }
    
    public boolean hasFreeSeats() {
        return getOccupiedSeats() < configuration.getMaxPlayers();
    }
}
