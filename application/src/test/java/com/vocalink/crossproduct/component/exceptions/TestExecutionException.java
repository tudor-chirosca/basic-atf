package com.vocalink.crossproduct.component.exceptions;

public class TestExecutionException extends Exception {

    private static final String MESSAGE = "Test flow/logic exception";

    public TestExecutionException() {
        super(MESSAGE);
    }

    public TestExecutionException(String msg) {
        super(msg);
    }

}
