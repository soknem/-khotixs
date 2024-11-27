package com.khotixs.identity_service.feature.verification.sms;

import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.VerificationToken;
import com.khotixs.identity_service.feature.verification.sms.dto.SmsVerifyRequest;

public interface SmsVerificationTokenService {

    void verify(SmsVerifyRequest smsVerifyRequest);

    boolean isUsersToken(VerificationToken token, User user);

    boolean isTokenInDb(VerificationToken token, String tokenToVerify);

    void generate(User user);

    boolean isExpired(VerificationToken token);

    void resend(String phoneNumber);

}