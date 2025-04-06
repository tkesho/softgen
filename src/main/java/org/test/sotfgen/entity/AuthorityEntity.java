package org.test.sotfgen.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authorities", schema = "auth")
@SequenceGenerator(name = "authority_id_gen", sequenceName = "authorities_id_seq", allocationSize = 1)
public class AuthorityEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "authority_id_gen", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "authority")
    private String authority;
}
