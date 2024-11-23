package com.khotixs.identity_service.feature.authority;

import com.khotixs.identity_service.domain.Authority;
import com.khotixs.identity_service.domain.Role;
import com.sun.source.doctree.SeeTree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {

    Set<Authority> findByRolesIn(Set<Role> roles);

    Set<Authority> findByAuthorityNameIn(Set<String> authorityName);
}
