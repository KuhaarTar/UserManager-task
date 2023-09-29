package com.kuhar.usermanager.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;
}
