package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e){

        return new ResponseEntity<>(new ApiResponse(e.mes, false), HttpStatus.BAD_REQUEST);
    }
}
