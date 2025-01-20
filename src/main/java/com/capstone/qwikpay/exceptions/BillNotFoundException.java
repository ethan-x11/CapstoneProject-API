package com.capstone.qwikpay.exceptions;

public class BillNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public BillNotFoundException(String message) {
        super(message);
    }

    public BillNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}