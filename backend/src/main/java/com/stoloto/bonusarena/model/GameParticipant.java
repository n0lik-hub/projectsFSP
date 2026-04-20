package com.stoloto.bonusarena.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameParticipant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_room_id", nullable = false)
    private GameRoom gameRoom;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isBot = false;
    
    @Column
    private String botName;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean hasBoost = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer weight = 1;
    
    @Column
    private Integer position; // Позиция в гонке (для визуализации)
    
    @Column
    private LocalDateTime joinedAt;
    
    @Column
    private Boolean boughtBoost = false;
    
    // Вычисление веса участника с учётом буста
    public void calculateWeight(Double boostMultiplier) {
        if (hasBoost) {
            this.weight = (int) Math.round(boostMultiplier);
        } else {
            this.weight = 1;
        }
    }
    
    public String getDisplayName() {
        if (isBot) {
            return botName != null ? botName : "Бот";
        }
        return user != null ? user.getUsername() : "Неизвестный";
    }
}
