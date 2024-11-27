package com.khotixs.identity_service.feature.auth2;

import com.khotixs.identity_service.domain.*;
import com.khotixs.identity_service.feature.auth2.dto.*;
import com.khotixs.identity_service.feature.forgotpasswordreset.ForgotPasswordResetRepository;
import com.khotixs.identity_service.feature.user.dto.CustomerUserRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.CustomerUserWithPhoneNumberRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.UserResponse;
import com.khotixs.identity_service.feature.verification.email.EmailVerificationTokenService;
import com.khotixs.identity_service.feature.role.RoleRepository;
import com.khotixs.identity_service.feature.user.UserRepository;
import com.khotixs.identity_service.feature.user.UserRoleRepository;
import com.khotixs.identity_service.feature.verification.sms.SmsVerificationTokenService;
import com.khotixs.identity_service.mapper.UserMapper;
import com.khotixs.identity_service.util.RandomTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final SmsVerificationTokenService smsVerificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordResetRepository forgotPasswordResetRepository;
    private final

    @Override
    public void registerCustomerUserWithEmail(CustomerUserRegisterRequest registerRequest) {

        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("confirm password must be match with password"));
        }


        User user = userMapper.fromUserRequest(registerRequest);

        user.setUuid(UUID.randomUUID().toString());

        user.setStatus(1);

        UserRole userRole = new UserRole();

        Role role = roleRepository.findByRoleName("USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("role = %s has been not found", "USER")));

        userRole.setRole(role);
        userRole.setUser(user);

        user.setEmailVerified(false);

        user.setEmail(user.getUsername());

        user.setIsEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        userRoleRepository.save(userRole);

        log.info("user register: {}", user);

        emailVerificationTokenService.generate(user);

    }

    @Override
    public void registerCustomerUserWithPhoneNumber(CustomerUserWithPhoneNumberRegisterRequest registerRequest) {
        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("confirm password must be match with password"));
        }


        User user = userMapper.fromUserWithPhoneRequest(registerRequest);

        user.setUuid(UUID.randomUUID().toString());

        user.setStatus(1);

        UserRole userRole = new UserRole();

        Role role = roleRepository.findByRoleName("USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("role = %s has been not found", "USER")));

        userRole.setRole(role);
        userRole.setUser(user);

        user.setEmailVerified(false);

        user.setUsername(user.getPhoneNumber());

        user.setIsEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        userRoleRepository.save(userRole);

        log.info("user register: {}", user);

        smsVerificationTokenService.generate(user);
    }

    @Override
    public UserResponse findMe(Authentication authentication) {

        String username = authentication.getName();

        return UserResponse.builder()
                .username(username)
                .build();
    }

    @Override
    public void changePassword(Authentication authentication, ChangePasswordRequest changePasswordRequest) {

        isNotAuthenticated(authentication);

        checkForPasswords(changePasswordRequest.password(), changePasswordRequest.confirmedPassword());

        checkForOldPassword(authentication.getName(), changePasswordRequest.oldPassword());

        // retrieve user by username from db
        User user = userRepository.findByUsernameAndIsEnabledTrue(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found"));

        user.setPassword(passwordEncoder.encode(changePasswordRequest.password()));
        userRepository.save(user);

    }

    @Override
    public ResetPasswordResponse resetPassword(Authentication authentication, ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository
                .findByUsername(resetPasswordRequest.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found"));

        String newPassword = RandomTokenGenerator.generate(8);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new ResetPasswordResponse(newPassword);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {

        User user  = userRepository.findByUsernameAndIsEnabledTrue(forgotPasswordRequest.username()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("user not found")));

        emailVerificationTokenService.generate(user);

    }

    public void validatePasscode(PasscodeRequest passcodeRequest){
        // check if user attempts to verify exists or not
        User foundUser = userRepository.findByUsernameAndIsEnabledTrue(passcodeRequest.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with corresponding verification token"));

        ForgotPasswordReset foundToken = forgotPasswordResetRepository.findByToken(passcodeRequest.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification token is invalid"));

        if (this.isUsersToken(foundToken, foundUser)) {
            if (this.isExpired(foundToken)) {
                foundUser.setEmailVerified(true);
                userRepository.save(foundUser);
                emailVerificationTokenRepository.deleteByUser(foundUser);
                return;
            }
        }
    }

    public void changeForgottenPassword(){

    }

    @Override
    public void isNotAuthenticated(Authentication authentication) {

    }

    public void checkTermsAndConditions(String value) {
        if (!value.equals("true") && !value.equals("false")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal value!");
        } else if (value.equals("false")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Terms and Conditions must be accepted in order to register!");
        }
    }


    private void checkForPasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password doesn't match!");
        }
    }

    private void checkForOldPassword(String username, String oldPassword) {

        // retrieve user by username from db
        User user = userRepository.findByUsernameAndIsEnabledTrue(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong old password");
        }
    }

    public boolean isUsersToken(ForgotPasswordReset token, User user) {
        return Objects.equals(user.getId(), token.getUser().getId());
    }

    public boolean isTokenInDb(VerificationToken token, String tokenToVerify) {
        return token.getToken().equals(tokenToVerify);
    }


    public boolean isExpired(ForgotPasswordReset token) {
        return !token.getExpiryDateTime().isBefore(LocalDateTime.now());
    }

}
