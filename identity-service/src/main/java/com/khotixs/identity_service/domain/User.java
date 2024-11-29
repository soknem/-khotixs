package com.khotixs.identity_service.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.khotixs.identity_service.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String uuid;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private Integer status;

    @Column(unique = true, length = 64)
    private String facebookId;

    @Column(unique = true, length = 64)
    private String googleId;

    @Column(unique = true, length = 64)
    private String telegramId;

    @Column(nullable = false, columnDefinition = "Text")
    private String familyName;

    @Column(nullable = false, columnDefinition = "Text")
    private String givenName;

    @Column(unique = true)
    private String phoneNumber;

    private String gender;

    private LocalDate dob;

    @Column(length = 256)
    private String profileImage;

    @Column(length = 256)
    private String coverImage;

    @Column(length = 256)
    private String ipAddress;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean accountNonExpired;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean accountNonLocked;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean credentialsNonExpired;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isEnabled;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean emailVerified;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<UserRole> userRoles;

}
