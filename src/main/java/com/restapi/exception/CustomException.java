package com.restapi.exception;

public class CustomException extends Exception {

    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public CustomException(String errMsg) {
        super(errMsg);
        this.errorMessage = errMsg;
        System.out.println(".........................Custom Exception....................");
    }

    public CustomException() {
        super();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
