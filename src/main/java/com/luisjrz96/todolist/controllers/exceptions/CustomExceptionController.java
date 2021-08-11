package com.luisjrz96.todolist.controllers.exceptions;

import com.luisjrz96.todolist.exceptions.ElementNotFoundException;
import com.luisjrz96.todolist.exceptions.InternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Map<String, Object>> handleElementNotFoundException(ElementNotFoundException exception, WebRequest webRequest) {
        return new ResponseEntity<>(generateResponseMap(exception, webRequest, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InternalServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Map<String, Object>> handleInternalServiceException(InternalServiceException exception, WebRequest webRequest) {
        return new ResponseEntity<>(generateResponseMap(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
        return new ResponseEntity<>(generateResponseMap(ex, webRequest, HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    protected Map<String, Object> generateResponseMap(Exception exception, WebRequest webRequest, HttpStatus status) {
        Map<String, Object>  response = new HashMap<>();
        response.put("status", status);
        response.put("code", status.value());
        response.put("message", exception.getMessage());
        response.put("endpoint", ((ServletWebRequest) webRequest).getRequest().getRequestURL().toString());
        response.put("method", ((ServletWebRequest) webRequest).getRequest().getMethod());
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

}
