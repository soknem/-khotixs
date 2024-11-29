package com.khotixs.identity_service.feature.auth2.dto;

public record PasscodeRequest(
        String username,
        String token
) {
}
