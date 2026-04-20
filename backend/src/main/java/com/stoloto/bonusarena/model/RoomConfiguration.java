package com.stoloto.bonusarena.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomConfiguration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer maxPlayers = 10;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer entryCost = 100;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer prizePoolPercent = 90;
    
    @Column(nullable = false)
    @Builder.Default
    private Double boostMultiplier = 2.0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer boostCost = 50;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer waitTimeSeconds = 60;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Расчётный показатель: ожидаемый возврат игроку (RTP)
    @Transient
    public Double getExpectedRTP() {
        return (prizePoolPercent / 100.0) * 100.0;
    }
    
    // Расчётный показатель: маржа системы
    @Transient
    public Double getSystemMargin() {
        return 100.0 - prizePoolPercent;
    }
}
