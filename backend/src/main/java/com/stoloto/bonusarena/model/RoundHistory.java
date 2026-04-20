package com.stoloto.bonusarena.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "round_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_room_id", nullable = false)
    private GameRoom gameRoom;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column(nullable = false)
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private Integer entryCost;
    
    @Column(nullable = false)
    private Integer prizePool;
    
    @Column(nullable = false)
    private Double boostMultiplier;
    
    @Column
    private String participantsInfo; // JSON-представление участников
    
    @Column
    private Long winnerId;
    
    @Column
    private String winnerName;
    
    @Column
    private Boolean winnerIsBot;
    
    @Column
    private Integer systemCommission;
    
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
