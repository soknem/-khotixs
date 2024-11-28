package com.khotixs.identity_service.feature.auth2;

import com.khotixs.identity_service.feature.auth2.dto.*;

import com.khotixs.identity_service.feature.user.dto.CustomerUserRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.CustomerUserWithPhoneNumberRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.UserResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    void registerCustomerUserWithEmail(CustomerUserRegisterRequest registerRequest);

    void registerCustomerUserWithPhoneNumber(CustomerUserWithPhoneNumberRegisterRequest customerUserWithPhoneNumberRegisterRequest);
    UserResponse findMe(Authentication authentication);


    void changePassword(Authentication authentication, ChangePasswordRequest changePasswordRequest);

    ResetPasswordResponse resetPassword(Authentication authentication, ResetPasswordRequest resetPasswordRequest);

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    void changeForgotPassword(ChangeForgotPasswordRequest changeForgotPasswordRequest);

    void isNotAuthenticated(Authentication authentication);

}