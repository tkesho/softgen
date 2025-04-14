package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authority", schema = "security")
public class AuthorityEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
}
