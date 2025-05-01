package org.test.sotfgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.test.sotfgen.audit.BaseAuditTable;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "person", schema = "social")
@NoArgsConstructor
public class PersonEntity extends BaseAuditTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 64)
    @Column(name = "first_name", length = 64)
    private String firstName;

    @Size(max = 128)
    @Column(name = "last_name", length = 128)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::gender")
    @Column(name = "gender", length = 16)
    private Gender gender;

    @Size(max = 11)
    @Column(name = "national_id", length = 11)
    private String nationalId;

    @Size(max = 256)
    @Column(name = "address", length = 256)
    private String address;

    @Size(max = 10)
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "person_group", schema = "social",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<GroupEntity> groups;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}