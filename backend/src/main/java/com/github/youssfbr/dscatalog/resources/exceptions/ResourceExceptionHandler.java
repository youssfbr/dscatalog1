package com.github.youssfbr.dscatalog.resources.exceptions;

import com.github.youssfbr.dscatalog.services.exceptions.DatabaseException;
import com.github.youssfbr.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        final StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(NOT_FOUND.value());
        err.setError("Resource not found");
        err.setMessage(ex.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(NOT_FOUND).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException ex, HttpServletRequest request) {
        final StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(BAD_REQUEST.value());
        err.setError("Resource not found");
        err.setMessage(ex.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(BAD_REQUEST).body(err);
    }
}
