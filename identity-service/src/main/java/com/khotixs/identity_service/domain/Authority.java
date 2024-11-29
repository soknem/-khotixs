package com.khotixs.identity_service.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.khotixs.identity_service.config.jpa.Auditable;
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
public class Authority extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String authorityName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "authorities",fetch = FetchType.EAGER)
//    @JsonBackReference
    private Set<Role> roles;

}

