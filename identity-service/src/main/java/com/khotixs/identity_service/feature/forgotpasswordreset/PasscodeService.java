//package com.khotixs.identity_service.feature.forgotpasswordreset;
//
//import com.khotixs.identity_service.domain.Passcode;
//import com.khotixs.identity_service.domain.User;
//import com.khotixs.identity_service.feature.auth2.dto.ForgotPasswordRequest;
//import com.khotixs.identity_service.feature.forgotpasswordreset.dto.PasscodeVerifyRequest;
//import com.khotixs.identity_service.feature.forgotpasswordreset.dto.PasscodeVerifyResendRequest;
//
//import java.time.LocalDateTime;
//import java.util.Objects;
//
//public interface PasscodeService {
//
//    void verify(PasscodeVerifyRequest passcodeVerifyRequest);
//    void resend(PasscodeVerifyResendRequest passcodeVerifyResendRequest);
//
//    void generate(User user);
//
//    boolean isUsersToken(Passcode token, User user) ;
//
//
//    boolean isExpired(Passcode token) ;
//}
