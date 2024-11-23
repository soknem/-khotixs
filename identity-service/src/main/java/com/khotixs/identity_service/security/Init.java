package com.khotixs.identity_service.security;

import com.khotixs.identity_service.domain.Authority;
import com.khotixs.identity_service.domain.Role;
import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.UserRole;
import com.khotixs.identity_service.feature.authority.AuthorityRepository;
import com.khotixs.identity_service.feature.role.RoleRepository;
import com.khotixs.identity_service.feature.user.UserRepository;
import com.khotixs.identity_service.feature.user.UserRoleRepository;
import com.khotixs.identity_service.security.repository.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class Init {

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @PostConstruct
    void init(){
        if (userRepository.count() < 1) {
            initAuthorities();
            initUser();
            initAdmin();
        }

    }


    void initUser() {
        if (userRepository.count() < 2) {

            Set<String> authorityNames= Set.of(
                    "file:read",
                    "file:write"
            );

            Set<Authority> authorities = authorityRepository.findByAuthorityNameIn(authorityNames);

            // Create roles and assign authorities
            Role userRole = saveRole("user", "user role", authorities);

            // Create admin user
            User user = User.builder()
                    .uuid(UUID.randomUUID().toString())
                    .username("user")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("user"))
                    .familyName("customer")
                    .givenName("user")
                    .profileImage("avatar.png")
                    .coverImage("cover.png")
                    .dob(LocalDate.now())
                    .gender("Male")
                    .phoneNumber("9876543210")
                    .status(1)
                    .emailVerified(true)
                    .isEnabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .createdDate(LocalDateTime.now())
                    .createdBy("system")
                    .build();

            UserRole defaultUsrRole = new UserRole();

            defaultUsrRole.setUser(user);
            defaultUsrRole.setRole(userRole);

//            adminUser.setUserRoles(Set.of(defaultUsrRole));

            // Save admin user
            userRepository.save(user);
            userRoleRepository.save(defaultUsrRole);


            log.info("Admin user, roles, and authorities initialized successfully.");
        }
    }

    void initAdmin() {
        if (userRepository.count() < 2) {

            Set<String> authorityNames= Set.of(
                    "file:read",
                    "file:write",
                    "user:read",
                    "user:write",
                    "user:update",
                    "user:delete"
            );
            Set<Authority> authorities = authorityRepository.findByAuthorityNameIn(authorityNames);

            // Create roles and assign
            Role adminRole = saveRole("ADMIN", "Admin role",authorities);

            // Create admin user
            User adminUser = User.builder()
                    .uuid(UUID.randomUUID().toString())
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin"))
                    .familyName("admin")
                    .givenName("user")
                    .profileImage("avatar.png")
                    .coverImage("cover.png")
                    .dob(LocalDate.now())
                    .gender("Male")
                    .phoneNumber("0123456789")
                    .status(1)
                    .emailVerified(true)
                    .isEnabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .createdDate(LocalDateTime.now())
                    .createdBy("system")
                    .build();

            UserRole adminUserRole = new UserRole();

            adminUserRole.setUser(adminUser);
            adminUserRole.setRole(adminRole);

//            adminUser.setUserRoles(Set.of(defaultUsrRole));

            // Save admin user
            userRepository.save(adminUser);
            userRoleRepository.save(adminUserRole);


            log.info("Admin user, roles, and authorities initialized successfully.");
        }
    }

    void initAuthorities() {
        // Auto generate role (USER, CUSTOMER, STAFF, ADMIN)
        if (authorityRepository.count() < 12) {
            List<String> authorityNames = List.of(
                    "payment:read",
                    "payment:write",
                    "payment:update",
                    "payment:delete",
                    "user:read",
                    "user:write",
                    "user:update",
                    "user:delete",
                    "file:read",
                    "file:write",
                    "file:update",
                    "file:delete"
            );

            List<Authority> authorities = authorityNames.stream()
                    .map(this::createAuthority)
                    .toList();

            authorityRepository.saveAll(authorities);
        }
    }

    private Authority createAuthority(String authorityName) {
        Authority authority = new Authority();
        authority.setAuthorityName(authorityName);
        return authority;
    }


    private Authority saveAuthority(String name, String description) {
        Authority authority = Authority.builder()
                .authorityName(name)
                .description(description)
                .build();
        return authorityRepository.save(authority);
    }

    private Role saveRole(String name, String description, Set<Authority> authorities) {
        Role role = Role.builder()
                .roleName(name)
                .description(description)
                .authorities(authorities)
                .build();
        return roleRepository.save(role);
    }

    @PostConstruct
    void initOAuth2() {

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(1))
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(true)
                .requireAuthorizationConsent(false)
                .build();

        var web = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("nextjs")
                .clientSecret(passwordEncoder.encode("nextjs123")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID);
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    uris.add("http://localhost:8085/login/oauth2/code/nextjs");
//                    uris.add("http://localhost:8168/login/oauth2/code/nextjs");
                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:8085");
                })
                .clientAuthenticationMethods(method -> {
                    method.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                }) //TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        RegisteredClient registeredClient = jpaRegisteredClientRepository.findByClientId("yelp");

        if (registeredClient == null) {
            jpaRegisteredClientRepository.save(web);
        }

    }

}