package com.lcwd.electronic.store.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceNotFoundException extends RuntimeException{

    String mes;

    public ResourceNotFoundException(String message) {
        super();
        this.mes = message;
    }
}
