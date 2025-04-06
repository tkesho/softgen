package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.test.sotfgen.dto.UserEntityDto;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
@SequenceGenerator(name = "user_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
public class UserEntity {

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
    private boolean active;


    public UserEntity(UserEntityDto user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.active = true;
    }
}
