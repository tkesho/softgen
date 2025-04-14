package org.test.sotfgen.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EmailDto {

    @NotNull
    private String fromEmail;
    @NotNull
    private String toEmail;
    @NotNull
    private String subject;
    @NotNull
    private String body;
}
