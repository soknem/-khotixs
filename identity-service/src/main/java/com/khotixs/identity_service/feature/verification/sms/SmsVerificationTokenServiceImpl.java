package com.khotixs.identity_service.feature.verification.sms;

import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.VerificationToken;
import com.khotixs.identity_service.feature.user.UserRepository;
import com.khotixs.identity_service.feature.verification.VerificationTokenRepository;
import com.khotixs.identity_service.feature.verification.sms.dto.SmsVerifyRequest;
import com.khotixs.identity_service.util.RandomUtil;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsVerificationTokenServiceImpl implements SmsVerificationTokenService {

    private final VerificationTokenRepository smsVerificationTokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Override
    public void verify(SmsVerifyRequest smsVerifyRequest) {

        // check if user attempts to verify exists or not
        User foundUser = userRepository.findByPhoneNumberAndIsEnabledTrue(smsVerifyRequest.phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with corresponding verification token"));

        VerificationToken foundToken = smsVerificationTokenRepository.getByToken(smsVerifyRequest.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Verification token is invalid"));

        if (this.isUsersToken(foundToken, foundUser)) {
            if (this.isExpired(foundToken)) {
                foundUser.setEmailVerified(true);
                userRepository.save(foundUser);
                smsVerificationTokenRepository.deleteByUser(foundUser);
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token has expired");
    }

    @Override
    public void resend(String phoneNumber) {

        // check if user attempts to verify exists or not
        User foundUser = userRepository.findByPhoneNumberAndIsEnabledTrue(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unsuccessfully creation of confirmation sms!"));

        smsVerificationTokenRepository.deleteByUser(foundUser);

        generate(foundUser);
    }

    @Override
    public boolean isUsersToken(VerificationToken token, User user) {
        return Objects.equals(user.getId(), token.getUser().getId());
    }

    @Override
    public boolean isTokenInDb(VerificationToken token, String tokenToVerify) {
        return token.getToken().equals(tokenToVerify);
    }

    @Override
    public void generate(User user) {

        LocalTime expiration = LocalTime.now().plusMinutes(1);

        VerificationToken smsVerificationToken = new VerificationToken();

        smsVerificationToken.setToken(RandomUtil.generate6Digits());
        smsVerificationToken.setExpiration(expiration);
        smsVerificationToken.setUser(user);

        smsVerificationTokenRepository.save(smsVerificationToken);

        try {
            Message.creator(
                    new PhoneNumber(user.getPhoneNumber()),               // To
                    new PhoneNumber(twilioPhoneNumber),
                    smsVerificationToken.getToken()
            ).create();
            log.info( "SMS sent successfully to: {}", user.getPhoneNumber());
        } catch (Exception e) {
            log.info( "SMS sent successfully to: {}", user.getPhoneNumber());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        }
    }

    @Override
    public boolean isExpired(VerificationToken token) {
        return !token.getExpiration().isBefore(LocalTime.now());
    }

}