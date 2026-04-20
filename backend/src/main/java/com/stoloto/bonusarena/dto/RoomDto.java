package com.stoloto.bonusarena.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    
    private Long id;
    private String configurationName;
    private Integer maxPlayers;
    private Integer entryCost;
    private Integer prizePoolPercent;
    private Double boostMultiplier;
    private Integer boostCost;
    private Integer waitTimeSeconds;
    private Integer occupiedSeats;
    private String status;
    private LocalDateTime createdAt;
    private List<ParticipantDto> participants;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParticipantDto {
        private Long id;
        private String displayName;
        private Boolean isBot;
        private Boolean hasBoost;
        private Integer weight;
    }
}
