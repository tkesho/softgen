package org.test.sotfgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.test.sotfgen.dto.PersonEntityDto;
import org.test.sotfgen.model.base.Auditable;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "persons", schema = "people")
@NoArgsConstructor
@SequenceGenerator(name = "persons_id_gen", sequenceName = "persons_id_seq", allocationSize = 1, schema = "people")
public class PersonEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persons_id_gen")
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

    @NotNull
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

    public PersonEntity(PersonEntityDto dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.birthDate = dto.getBirthDate();
        this.gender = dto.getGender();
        this.phoneNumber = dto.getPhoneNumber();
        this.address = dto.getAddress();
        this.nationalId = dto.getNationalId();
    }
}