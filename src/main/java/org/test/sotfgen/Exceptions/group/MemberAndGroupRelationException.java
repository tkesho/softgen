package org.test.sotfgen.Exceptions.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class MemberAndGroupRelationException extends RuntimeException {
    public MemberAndGroupRelationException(String message) {
        super(message);
    }
}
