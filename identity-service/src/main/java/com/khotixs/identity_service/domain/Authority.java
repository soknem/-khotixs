package com.khotixs.identity_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String authorityName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles = new HashSet<>();
}