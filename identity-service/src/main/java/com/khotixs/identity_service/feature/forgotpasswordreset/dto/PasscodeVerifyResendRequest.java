package com.khotixs.identity_service.feature.forgotpasswordreset.dto;

import jakarta.validation.constraints.NotBlank;

public record PasscodeVerifyResendRequest(
        @NotBlank(message = "Username is required")
        String username
) {
}
