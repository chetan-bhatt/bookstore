package com.example.bookstore.advice;

import com.example.bookstore.exception.BookStoreException;
import com.example.bookstore.rest.ServiceError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.example.bookstore.exception.ErrorCode.VALIDATION_ERROR;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookStoreException.class)
    public ResponseEntity<ServiceError> handleBookStoreException(BookStoreException exception) {
        log.error("BookStore Exception", exception);
        ServiceError serviceError = constructError(exception);
        switch (exception.getErrorCode()) {
            case BOOK_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(serviceError);
            case BOOK_ALREADY_EXIST:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(serviceError);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serviceError);
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception occurred", ex);
        ServiceError error = new ServiceError().errorCode(VALIDATION_ERROR.name())
                .errorMessage("Validation Failed")
                .details(ex.getBindingResult().getAllErrors());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception occurred", ex);
        ServiceError error = new ServiceError().errorCode(VALIDATION_ERROR.name())
                .errorMessage("Validation Failed")
                .details(ex.getCause().getMessage());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception occurred", ex);
        ServiceError error = new ServiceError().errorCode(VALIDATION_ERROR.name())
                .errorMessage("Validation Failed")
                .details(ex.getMessage());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceError> handleUnexpected(Exception exception) {
        log.error("Unexpected exception occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceError().errorMessage(exception.getMessage()));
    }

    private ServiceError constructError(BookStoreException ex){
        ServiceError error = new ServiceError();
        error.setErrorCode(ex.getErrorCode().name());
        error.setErrorMessage(ex.getMessage());
        error.setDetails(ex.getDetails());
        return error;
    }
}