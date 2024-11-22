package com.khotixs.identity_service.feature.role;

import com.khotixs.identity_service.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
