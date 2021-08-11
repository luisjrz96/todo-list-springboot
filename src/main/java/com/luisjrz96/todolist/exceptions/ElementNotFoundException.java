package com.luisjrz96.todolist.exceptions;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(String id, Class objectClass) {
        super(String.format("Object of type %s with id %s, was not found", objectClass.getName(), id));
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
