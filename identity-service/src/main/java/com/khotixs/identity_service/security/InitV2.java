//package com.khotixs.identity_service.security;
//
//import com.khotixs.identity_service.domain.Authority;
//import com.khotixs.identity_service.domain.Role;
//import com.khotixs.identity_service.domain.User;
//import com.khotixs.identity_service.domain.UserRole;
//import com.khotixs.identity_service.feature.authority.AuthorityRepository;
//import com.khotixs.identity_service.feature.role.RoleRepository;
//import com.khotixs.identity_service.feature.user.UserRepository;
//import com.khotixs.identity_service.feature.user.UserRoleRepository;
//import com.khotixs.identity_service.security.repository.JpaRegisteredClientRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class InitV2 {
//
//    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthorityRepository authorityRepository;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final UserRoleRepository userRoleRepository;
//
//    @PostConstruct
//    void initUserDetails() {
//        if (userRepository.count() < 1) {
//
//            // Create authorities
//            Authority userAuthority = saveAuthority("USER", "Regular user with basic privileges.");
//            Authority adminAuthority = saveAuthority("file:read", "Permission to read files.");
//
//            // Create roles and assign authorities
//            Role userRole = saveRole("USER", "Standard user role", Set.of(userAuthority));
//            Role adminRole = saveRole("ADMIN", "Admin role", Set.of(adminAuthority, userAuthority));
//
//            // Create admin user
//            User adminUser = User.builder()
//                    .uuid(UUID.randomUUID().toString())
//                    .username("admin")
//                    .email("admin@example.com")
//                    .password(passwordEncoder.encode("admin123"))
//                    .familyName("Admin")
//                    .givenName("User")
//                    .profileImage("default_admin_avatar.png")
//                    .coverImage("default_admin_cover.png")
//                    .dob(LocalDate.of(1990, 1, 1))
//                    .gender("Male")
//                    .phoneNumber("1234567890")
//                    .status(1) // Assuming 1 indicates active/enabled
//                    .emailVerified(true)
//                    .isEnabled(true)
//                    .accountNonExpired(true)
//                    .accountNonLocked(true)
//                    .credentialsNonExpired(true)
//                    .createdDate(LocalDateTime.now())
//                    .createdBy("system")
//                    .roles(Set.of(adminRole)) // Direct assignment of roles
//                    .build();
//
//            // Save admin user
//            userRepository.save(adminUser);
//
//            log.info("Admin user, roles, and authorities initialized successfully.");
//        }
//    }
//
//    private Authority saveAuthority(String name, String description) {
//        Authority authority = Authority.builder()
//                .authorityName(name)
//                .description(description)
//                .build();
//        return authorityRepository.save(authority);
//    }
//
//    private Role saveRole(String name, String description, Set<Authority> authorities) {
//        Role role = Role.builder()
//                .roleName(name)
//                .description(description)
//                .authorities(authorities)
//                .build();
//        return roleRepository.save(role);
//    }
//
//    @PostConstruct
//    void initOAuth2() {
//
//        TokenSettings tokenSettings = TokenSettings.builder()
//                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
//                .accessTokenTimeToLive(Duration.ofMinutes(1))
//                .build();
//
//        ClientSettings clientSettings = ClientSettings.builder()
//                .requireProofKey(true)
//                .requireAuthorizationConsent(false)
//                .build();
//
//        var web = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("nextjs")
//                .clientSecret(passwordEncoder.encode("nextjs123")) // store in secret manager
//                .scopes(scopes -> {
//                    scopes.add(OidcScopes.OPENID);
//                    scopes.add(OidcScopes.PROFILE);
//                    scopes.add(OidcScopes.EMAIL);
//                })
//                .redirectUris(uris -> {
//                    uris.add("http://localhost:8085/login/oauth2/code/nextjs");
////                    uris.add("http://localhost:8168/login/oauth2/code/nextjs");
//                })
//                .postLogoutRedirectUris(uris -> {
//                    uris.add("http://localhost:8085");
//                })
//                .clientAuthenticationMethods(method -> {
//                    method.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
//                }) //TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
//                .authorizationGrantTypes(grantTypes -> {
//                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//                })
//                .clientSettings(clientSettings)
//                .tokenSettings(tokenSettings)
//                .build();
//
//        RegisteredClient registeredClient = jpaRegisteredClientRepository.findByClientId("yelp");
//
//        if (registeredClient == null) {
//            jpaRegisteredClientRepository.save(web);
//        }
//
//    }
//
//}