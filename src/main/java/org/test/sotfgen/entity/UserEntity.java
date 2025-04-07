package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.test.sotfgen.dto.UserEntityDto;
import org.test.sotfgen.model.base.Auditable;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users", schema = "auth")
@SequenceGenerator(name = "user_id_gen", sequenceName = "users_id_seq", allocationSize = 1, schema = "auth")
public class UserEntity extends Auditable {

    @Id
    @GeneratedValue(generator = "user_id_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "username", nullable = false,  unique = true)
    private String username;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles", schema = "auth",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;


    @ManyToMany(mappedBy = "usersGroups")
    private Set<GroupEntity> groups;

    public UserEntity(UserEntityDto user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.active = true;
    }
}
