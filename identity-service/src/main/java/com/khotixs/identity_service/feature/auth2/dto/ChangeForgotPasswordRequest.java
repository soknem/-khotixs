package com.khotixs.identity_service.feature.auth2.dto;

public record ChangeForgotPasswordRequest(

        String email,
        String password,
        String confirmPassword
) {
}
