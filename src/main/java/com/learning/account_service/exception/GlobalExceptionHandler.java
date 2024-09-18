package com.learning.account_service.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String METHOD_ARGUMENT_NOT_VALID_CODE = "Method.argument.not.valid";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception) {
        ExceptionResponse exceptionResponse = getExceptionResponse(exception, exception.getCode());
        exceptionResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException exception) {
        ExceptionResponse exceptionResponse = getExceptionResponse(exception, exception.getCode());
        exceptionResponse.setStatusCode(HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(MethodArgumentNotValidException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        String errorMessage = getBadRequestErrorMessage(exception);

        exceptionResponse.setMessage(errorMessage);
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setCode(METHOD_ARGUMENT_NOT_VALID_CODE);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Format custom error response into JSON format.
     *
     * @param exception Exception type which contains its message.
     * @return Formatted REST custom response in JSON format.
     */
    private ExceptionResponse getExceptionResponse(
            Exception exception,
            String code) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(code);
        exceptionResponse.setMessage(exception.getMessage());

        return exceptionResponse;
    }

    /**
     * Retrieve error message from invalid field of DTOs.
     *
     * @param exception MethodArgumentNotValidException.class.
     * @return Error message associated with the field.
     */
    private static String getBadRequestErrorMessage(MethodArgumentNotValidException exception) {
        return exception.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }
}
