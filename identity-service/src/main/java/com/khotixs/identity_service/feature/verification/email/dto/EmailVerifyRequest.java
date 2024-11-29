package com.khotixs.identity_service.feature.verification.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EmailVerifyRequest(

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Token is required")
        String token

) {
}