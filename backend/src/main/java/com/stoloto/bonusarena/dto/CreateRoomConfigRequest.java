package com.stoloto.bonusarena.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomConfigRequest {
    
    private String name;
    private Integer maxPlayers;
    private Integer entryCost;
    private Integer prizePoolPercent;
    private Double boostMultiplier;
    private Integer boostCost;
    private Integer waitTimeSeconds;
}
