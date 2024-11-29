package com.khotixs.identity_service.feature.auth2.dto;

public record ChangeForgotPasswordRequest(

        String username,
        String token,
        String password,
        String confirmPassword
) {
}
