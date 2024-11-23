package com.khotixs.identity_service.security.custom;

import com.khotixs.identity_service.domain.Authority;
import com.khotixs.identity_service.domain.Role;
import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.UserRole;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
@Slf4j
public class CustomUserDetails implements UserDetails {

    private User user;

    private Collection<? extends GrantedAuthority> authorities;

//     Constructor to initialize user and authorities
    public CustomUserDetails(User user, Set<String> authorities) {
        this.user = user;
        this.authorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getIsEnabled();
    }

}