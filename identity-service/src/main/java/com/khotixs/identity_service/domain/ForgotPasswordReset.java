package com.khotixs.identity_service.domain;

import com.khotixs.identity_service.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "password_resets")
public class ForgotPasswordReset  extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(128)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(256)")
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;

    @OneToOne
    private User user;

}