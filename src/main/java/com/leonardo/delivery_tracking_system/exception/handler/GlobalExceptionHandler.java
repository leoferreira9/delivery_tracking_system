package com.leonardo.delivery_tracking_system.exception.handler;

import com.leonardo.delivery_tracking_system.dto.error.ErrorResponse;
import com.leonardo.delivery_tracking_system.exception.EntityAlreadyRegisteredException;
import com.leonardo.delivery_tracking_system.exception.EntityNotFoundException;
import com.leonardo.delivery_tracking_system.exception.FailedToAssignDelivererException;
import com.leonardo.delivery_tracking_system.exception.FailedToUpdateDeliveryStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(int status, String error, String message, WebRequest request){
        return new ErrorResponse(
                status,
                error,
                message,
                Instant.now(),
                request.getDescription(false).replace("uri=", "")
        );
    }

    @ExceptionHandler(EntityAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyRegistered(EntityAlreadyRegisteredException ex, WebRequest request){
        log.warn(ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(409, "Already registered", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest request){
        log.warn(ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(404, "Not found", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedToAssignDelivererException.class)
    public ResponseEntity<ErrorResponse> handleFailedToAssignDeliverer(FailedToAssignDelivererException ex, WebRequest request){
        log.warn(ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(409, "Conflict", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FailedToUpdateDeliveryStatusException.class)
    public ResponseEntity<ErrorResponse> handleFailedToUpdateDeliveryStatus(FailedToUpdateDeliveryStatusException ex, WebRequest request){
        log.warn(ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(409, "Conflict", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request){
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String message = String.join(", ", errors);

        log.error("message", ex);
        ErrorResponse errorResponse = buildErrorResponse(400, "Bad request", message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request){
        log.error("Unexpected error", ex);
        ErrorResponse errorResponse = buildErrorResponse(409, "Conflict", "Data integrity violation: duplicate or invalid data", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request){
        log.error("message", ex);
        ErrorResponse errorResponse = buildErrorResponse(500, "Internal Server Error", "An internal server error occurred", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
