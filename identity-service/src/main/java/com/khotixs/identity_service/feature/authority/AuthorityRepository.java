package com.khotixs.identity_service.feature.authority;

import com.khotixs.identity_service.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
