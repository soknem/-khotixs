package com.khotixs.identity_service.feature.auth2;

import com.khotixs.identity_service.feature.auth2.dto.ChangeForgotPasswordRequest;
import com.khotixs.identity_service.feature.auth2.dto.ForgotPasswordRequest;
import com.khotixs.identity_service.feature.user.dto.CustomerUserRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.CustomerUserWithPhoneNumberRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/oauth2")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthService authService;

    @PostMapping("/register/email")
    public void registerUserWithEmail(@RequestBody CustomerUserRegisterRequest registerRequest) {

        log.info("registerRequest: {}",registerRequest);
        authService.registerCustomerUserWithEmail(registerRequest);

    }

    @PostMapping("/register/phone-number")
    @ResponseBody
    public void registerUserWithPhoneNumber(@RequestBody CustomerUserWithPhoneNumberRegisterRequest registerRequest) {

        log.info("registerRequest: {}",registerRequest);
        authService.registerCustomerUserWithPhoneNumber(registerRequest);

    }

    @GetMapping("/me")
    public UserResponse getMe(Authentication authentication){
        return authService.findMe(authentication);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        authService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("/change-forgot-password")
    public void changeForgotPassword(@RequestBody ChangeForgotPasswordRequest changeForgotPasswordRequest){
        authService.changeForgotPassword(changeForgotPasswordRequest);
    }
}
