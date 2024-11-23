package com.khotixs.identity_service.feature.user;

import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Set<UserRole> findByUserId(Long id);
}