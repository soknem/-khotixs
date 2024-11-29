package com.khotixs.identity_service.feature.verification.sms.dto;

public record SmsVerifyRequest(
        String phoneNumber,
        String token


) {
}
