package org.test.sotfgen.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.test.sotfgen.audit.BaseAuditTable;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "user", schema = "security")
public class UserEntity extends BaseAuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private Boolean active;

    @JsonBackReference
    @ManyToMany()
    @JoinTable(
            name = "user_role", schema = "security",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @JsonBackReference
    @ManyToMany()
    @JoinTable(
            name = "user_authority", schema = "security",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<AuthorityEntity> authorities;

    @PrePersist
    private void prePersist() {
        if (this.active == null) {
            this.active = true;
        }
    }
}
