package org.example.vega.exception;

public class NotFoundStockException extends RuntimeException {

    public NotFoundStockException(String message) {
        super(message);
    }
}
