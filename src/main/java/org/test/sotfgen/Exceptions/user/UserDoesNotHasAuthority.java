package org.test.sotfgen.Exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserDoesNotHasAuthority extends RuntimeException {
    public UserDoesNotHasAuthority(String message) {
        super(message);
    }
}
