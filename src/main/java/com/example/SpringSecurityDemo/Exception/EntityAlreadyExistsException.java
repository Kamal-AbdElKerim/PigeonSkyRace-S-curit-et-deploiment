package com.example.SpringSecurityDemo.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityAlreadyExistsException extends RuntimeException {
    private String field;

    public EntityAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
    }

}

