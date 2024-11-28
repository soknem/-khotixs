package com.khotixs.identity_service.feature.forgotpasswordreset.dto;

import jakarta.validation.constraints.NotBlank;

public record PasscodeVerifyRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Token is required")
        String token
) {
}
