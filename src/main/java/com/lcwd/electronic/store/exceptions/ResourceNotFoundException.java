package com.lcwd.electronic.store.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceNotFoundException extends RuntimeException{


    public ResourceNotFoundException() {
        super("Resource not found !!");
    }

    //String mes;
    public ResourceNotFoundException(String message) {
        super(message);
        //this.mes = message;
    }
}
