package com.app.userrestapi.controller.exceptionhandler;

import com.app.userrestapi.model.dto.ExceptionMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Order(OrderValue.HIGH_PRIORITY)
@ControllerAdvice
@Slf4j
public class PreValidationExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        log.error("Invalid API request: " + ex.getMessage());
        return new ResponseEntity<>(new ExceptionMessageDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String wrappedError = getError(ex);
        log.error("Invalid API request: \n" + wrappedError);
        return new ResponseEntity<>(generateResponse(wrappedError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> allErrors = getErrors(ex);
        String errorMessage = generateErrorMessage(allErrors);
        log.error("Invalid API request: \n" + errorMessage);
        return new ResponseEntity<>(generateResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }


    private String getError(HttpMessageNotReadableException ex) {
        String error = ex.getCause().getMessage().substring(0, ex.getCause().getMessage().indexOf("\n"));
        String noQuotes = error.replace("\"", "'");
        return StringUtils.wrap(noQuotes, "\"");
    }

    private List<FieldError> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors()
                .stream()
                .map(this::createFieldError)
                .collect(Collectors.toList());
    }

    private FieldError createFieldError(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            return (FieldError) objectError;
        } else {
            return new FieldError(
                    objectError.getObjectName(),
                    "location",
                    objectError.getDefaultMessage() != null ? objectError.getDefaultMessage() : "Unknown error"
            );
        }
    }

    private String generateResponse(String errorMessage) {
        return "{" + System.lineSeparator()
                + "\"errors\": " + errorMessage
                + "}" + System.lineSeparator();
    }

    private String generateErrorMessage(List<FieldError> allErrors) {
        return "{" + System.lineSeparator()
                + allErrors.stream().map(this::generateFieldErrorMessage).collect(Collectors.joining("," + System.lineSeparator()))
                + "}" + System.lineSeparator();
    }

    private String generateFieldErrorMessage(FieldError error) {
        return String.format("\t\t\"%s\": \"%s\"" + System.lineSeparator(), error.getField(), error.getDefaultMessage());
    }
}
