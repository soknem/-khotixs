    package com.khotixs.identity_service.security;

    import com.khotixs.identity_service.domain.Authority;
    import com.khotixs.identity_service.domain.Role;
    import com.khotixs.identity_service.domain.User;
    import com.khotixs.identity_service.domain.UserRole;
    import com.khotixs.identity_service.feature.authority.AuthorityRepository;
    import com.khotixs.identity_service.feature.role.RoleRepository;
    import com.khotixs.identity_service.feature.user.UserRepository;
    import com.khotixs.identity_service.feature.user.UserRoleRepository;
    import com.khotixs.identity_service.security.custom.CustomUserDetails;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import java.util.Collections;
    import java.util.HashSet;
    import java.util.Set;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class UserDetailsServiceImpl implements UserDetailsService {

        private final UserRepository userRepository;

        private final UserRoleRepository userRoleRepository;

        private  final RoleRepository roleRepository;

        private final AuthorityRepository authorityRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            User user = userRepository.findByUsernameAndIsEnabledTrue(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));

            log.info("loadUserByUsername: {}", user);
            log.info("FETCH_USER_ROLES: {}", user.getUserRoles());

            Set<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
            log.info("FETCH_USER_ROLES: {}", userRoles);

            Set<Role> rolesSet = userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());

            Set<String> authorities=  authorityRepository.findByRolesIn(rolesSet.stream().toList()).stream().map(Authority::getAuthorityName).collect(Collectors.toSet());
            log.info("Authorities: {}",authorities);


            return new CustomUserDetails(user,authorities);
        }

    }