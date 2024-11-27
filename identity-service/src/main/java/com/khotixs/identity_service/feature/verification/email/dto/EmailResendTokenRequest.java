package com.khotixs.identity_service.feature.verification.email.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailResendTokenRequest(
        @NotBlank(message = "Username is required")
        String username
) {
}