package com.stoloto.bonusarena.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomConfigurationDto {
    
    private Long id;
    private String name;
    private Integer maxPlayers;
    private Integer entryCost;
    private Integer prizePoolPercent;
    private Double boostMultiplier;
    private Integer boostCost;
    private Integer waitTimeSeconds;
    private Boolean active;
    private Double expectedRTP;
    private Double systemMargin;
    private List<String> warnings;
}
