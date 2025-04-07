package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.entity.PersonEntity;

@Setter
@Getter
public class PersonSearchParams {
    private String firstName;
    private String lastName;
    private String Address;
    private String phoneNumber;
    private PersonEntity.Gender gender;
    private String nationalId;
}
