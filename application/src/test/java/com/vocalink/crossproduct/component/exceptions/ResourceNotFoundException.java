package com.vocalink.crossproduct.component.exceptions;

public class ResourceNotFoundException extends Exception {

    private static final String MESSAGE = "Resource you are trying to access in World container has not been found";

    public ResourceNotFoundException() {
        super(MESSAGE);
    }

}
