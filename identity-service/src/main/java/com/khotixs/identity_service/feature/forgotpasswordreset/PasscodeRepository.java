package com.khotixs.identity_service.feature.forgotpasswordreset;//package com.khotixs.identity_service.feature.forgotpasswordreset;
//
//import com.khotixs.identity_service.domain.Passcode;
//import com.khotixs.identity_service.domain.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//public interface PasscodeRepository extends JpaRepository<Passcode,Long> {
//
//    Optional<Passcode> findByToken(String token);
//
//    Passcode findByUser(User user);
//
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM VerificationToken e where e.user=:user")
//    void deleteByUser(@Param("user") User user);
//}
