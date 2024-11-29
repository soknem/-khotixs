package com.khotixs.identity_service.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.khotixs.identity_service.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;


}
