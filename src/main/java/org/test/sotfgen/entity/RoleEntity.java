package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "roles", schema = "auth")
@SequenceGenerator(name = "role_id_gen", sequenceName = "roles_id_seq", allocationSize = 1)
public class RoleEntity {

    @Id
    @GeneratedValue(generator = "role_id_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role")
    private String role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_authorities", schema = "auth",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<AuthorityEntity> authorities;
}
