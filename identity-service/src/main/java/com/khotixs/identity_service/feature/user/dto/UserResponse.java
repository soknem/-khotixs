package com.khotixs.identity_service.feature.user.dto;

import lombok.Builder;

@Builder
public record UserResponse(

    String username,
    String email,
    String profileImage,
    String familyName,
    String givenName

) {
    
}