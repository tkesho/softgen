package org.test.sotfgen.Exceptions.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String message) {
        super(message);
    }
}
