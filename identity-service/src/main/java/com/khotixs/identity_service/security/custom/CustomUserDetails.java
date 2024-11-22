package com.khotixs.identity_service.security.custom;

import com.khotixs.identity_service.domain.Role;
import com.khotixs.identity_service.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class CustomUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getUserRoles().stream().flatMap(userRole -> userRole.getRole().getAuthorities().stream())
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName())).collect(Collectors.toSet());

//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//        user
//                .getUserRoles()
//                .forEach(userRole -> {
//                    authorities.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
//                });
//
//        return authorities;
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