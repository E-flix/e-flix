package com.eflix.common.exception;

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