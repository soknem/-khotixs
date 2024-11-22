package com.khotixs.identity_service.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String roleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "role")
    @JsonBackReference
    private Set<UserRole> userRoles;

    @ManyToMany
    @JoinTable(
            name = "roles_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
//    @JsonManagedReference
    private Set<Authority> authorities;
}
