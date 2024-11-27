package com.khotixs.identity_service.feature.forgotpasswordreset;

import com.khotixs.identity_service.domain.ForgotPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordResetRepository extends JpaRepository<ForgotPasswordReset,Long> {

    Optional<ForgotPasswordReset> findByToken(String token);
}
