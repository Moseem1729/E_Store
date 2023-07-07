package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.payload.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e){

        logger.info("ResourceNotFoundException Handler Invoked !!");
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), HttpStatus.NOT_FOUND, false), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, ApiResponse>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){

        logger.info("MethodArgumentNotValidException Handler Invoked !!");
        Map<String, String> resp = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName= ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });

        Map<String, ApiResponse> response = new HashMap<>();

        String message = null;
        String fieldName = null;
        for (ObjectError error : e.getAllErrors()) {
            message = error.getDefaultMessage();
            fieldName = ((FieldError)error).getField();
            ApiResponse apiResponse = ApiResponse.builder().message(message).httpStatus(HttpStatus.NOT_ACCEPTABLE).success(false).build();
            response.put(fieldName, apiResponse);
        }


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }





}
