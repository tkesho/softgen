package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role", schema = "security")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}