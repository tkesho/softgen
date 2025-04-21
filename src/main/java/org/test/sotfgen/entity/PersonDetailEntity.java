package org.test.sotfgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.test.sotfgen.audit.BaseAuditTable;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "person_detail", schema = "social")
@NoArgsConstructor
public class PersonDetailEntity extends BaseAuditTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 64)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Size(max = 128)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 128)
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::gender")
    @Column(name = "gender", nullable = false, length = 16)
    private Gender gender;

    @Size(max = 11)
    @NotNull
    @Column(name = "national_id", nullable = false, length = 11)
    private String nationalId;

    @Size(max = 256)
    @Column(name = "address", nullable = false, length = 256)
    private String address;

    @Size(max = 10)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public enum Gender {
        MALE, FEMALE, OTHER
    }
}