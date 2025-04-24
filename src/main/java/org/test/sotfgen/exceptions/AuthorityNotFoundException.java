package org.test.sotfgen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException(String authorityNotFound) {
        super(authorityNotFound);
    }
}
