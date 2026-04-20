package com.stoloto.bonusarena.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRoomRequest {
    
    private Long roomId;
    private Boolean buyBoost;
}
