package com.khotixs.identity_service.feature.verification.sms;

import com.khotixs.identity_service.base.BasedMessage;
import com.khotixs.identity_service.feature.verification.sms.dto.SmsResendTokenRequest;
import com.khotixs.identity_service.feature.verification.sms.dto.SmsVerifyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/sms-verification")
@RequiredArgsConstructor
public class SmsVerificationTokenController {

    private final SmsVerificationTokenService smsVerificationTokenService;

    @PostMapping
    BasedMessage verify(@Valid @RequestBody SmsVerifyRequest smsVerifyRequest) {
        smsVerificationTokenService.verify(smsVerifyRequest);
        return new BasedMessage("Phone number has been verified successfully");
    }


    @PostMapping("/token")
    BasedMessage resendToken(@Valid @RequestBody SmsResendTokenRequest smsResendTokenRequest) {
        smsVerificationTokenService.resend(smsResendTokenRequest.phoneNumber());
        return new BasedMessage("New confirmation sms has been sent, check your message");
    }

}