package com.app.userrestapi.controller.exceptionhandler;

import com.app.userrestapi.exception.ApiException;
import com.app.userrestapi.exception.ResourceNotFoundException;
import com.app.userrestapi.model.dto.ExceptionMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(OrderValue.LOWEST_PRIORITY)
@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleApiException(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionMessageDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleApiException(IllegalStateException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionMessageDto(ex.getMessage()), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionMessageDto(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
