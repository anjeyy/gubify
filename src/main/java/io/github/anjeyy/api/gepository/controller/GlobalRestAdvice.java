package io.github.anjeyy.api.gepository.controller;

import io.github.anjeyy.api.gepository.dto.ErrorResponse;
import io.github.anjeyy.api.gepository.dto.ErrorResponse.ErrorResponseBuilder;
import io.github.anjeyy.infrastructure.exception.GithubRestException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalRestAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GithubRestException.class)
    public ResponseEntity<Object> githubRestTemplateException(GithubRestException exception) {
        HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
        ErrorResponse restErrorResponse = new ErrorResponseBuilder()
            .httpStatus(httpStatus)
            .message(exception.getMessage())
            .hint("Please wait for another 60s to refresh your requests.")
            .build();
        return ResponseEntity.status(httpStatus).body(restErrorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintException(ConstraintViolationException exception) {
        String errorMsg = exception.getConstraintViolations()
                                   .stream()
                                   .map(GlobalRestAdvice::extractConstraintParameter)
                                   .collect(Collectors.joining("; "));

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse restErrorResponse = new ErrorResponseBuilder()
            .httpStatus(httpStatus)
            .message(errorMsg)
            .build();
        return ResponseEntity.status(httpStatus).body(restErrorResponse);
    }

    private static String extractConstraintParameter(ConstraintViolation<?> constraintViolation) {
        String constraintPath = constraintViolation.getPropertyPath().toString();
        String[] splitConstraint = constraintPath.split("\\.");
        String actualRequestParam = splitConstraint[splitConstraint.length - 1];
        return actualRequestParam + " " + constraintViolation.getMessage();
    }
}
