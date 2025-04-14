package org.test.sotfgen.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.test.sotfgen.entity.PersonDetailEntity;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class PersonDetailDto {
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String phoneNumber;
    @NotNull
    @NotBlank
    private PersonDetailEntity.Gender gender;
    @NotNull
    @NotBlank
    private String nationalId;
}
