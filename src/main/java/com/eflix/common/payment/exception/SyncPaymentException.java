package com.eflix.common.payment.exception;

public class SyncPaymentException extends RuntimeException {
    public SyncPaymentException() {
        super();
    }

    public SyncPaymentException(String message) {
        super(message);
    }

    public SyncPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}