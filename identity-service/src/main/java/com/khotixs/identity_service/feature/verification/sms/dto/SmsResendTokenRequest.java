package com.khotixs.identity_service.feature.verification.sms.dto;

public record SmsResendTokenRequest(
        String phoneNumber
) {
}
