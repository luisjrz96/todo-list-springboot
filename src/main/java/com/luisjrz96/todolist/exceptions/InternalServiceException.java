package com.luisjrz96.todolist.exceptions;

public class InternalServiceException extends RuntimeException{

    public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
