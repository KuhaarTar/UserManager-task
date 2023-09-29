package com.kuhar.usermanager.services;

import com.kuhar.usermanager.exceptions.ExceptionResponse;
import com.kuhar.usermanager.exceptions.InvalidInputException;
import com.kuhar.usermanager.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(InstantiationException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .timestamp(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(UserNotFoundException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .timestamp(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
