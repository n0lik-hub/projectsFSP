package com.stoloto.bonusarena.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    
    private Long id;
    private String username;
    private Integer bonusBalance;
    private Boolean isAdmin;
}
