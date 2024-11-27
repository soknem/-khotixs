package com.khotixs.identity_service.feature.verification.email;

import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.VerificationToken;
import com.khotixs.identity_service.feature.verification.email.dto.EmailVerifyRequest;

public interface EmailVerificationTokenService {

    void verify(EmailVerifyRequest emailVerifyRequest);

    boolean isUsersToken(VerificationToken token, User user);

    boolean isTokenInDb(VerificationToken token, String tokenToVerify);

    void generate(User user);

    boolean isExpired(VerificationToken token);

    void resend(String username);

}