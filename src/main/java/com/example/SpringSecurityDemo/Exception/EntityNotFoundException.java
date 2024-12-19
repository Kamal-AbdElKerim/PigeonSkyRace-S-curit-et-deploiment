package com.example.SpringSecurityDemo.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityNotFoundException extends RuntimeException {
    private String field;

    public EntityNotFoundException(String field, String message) {
        super(message);
        this.field = field;
    }
}
