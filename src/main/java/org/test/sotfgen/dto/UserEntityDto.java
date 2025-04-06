package org.test.sotfgen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserEntityDto {

    public UserEntityDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @NotNull
    @NotBlank
    @NotEmpty
    private String username;
    @NotNull
    @NotBlank
    @NotEmpty
    private String password;
    @NotNull
    @NotBlank
    @NotEmpty
    private String email;
}
