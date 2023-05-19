package com.example.bookstore.exception;

/**
 * Custom Exception class for BookStore application.
 * @author chetanbhatt
 */
public class BookStoreException extends RuntimeException{

    private final ErrorCode errorCode;

    private final Object details;

    public BookStoreException(ErrorCode errorCode, Object details, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getDetails() {
        return details;
    }

}
