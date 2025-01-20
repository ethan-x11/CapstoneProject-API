package com.capstone.qwikpay.exceptions;

public class PaymentFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}