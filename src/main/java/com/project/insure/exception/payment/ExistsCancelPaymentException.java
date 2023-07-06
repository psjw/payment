package com.project.insure.exception.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistsCancelPaymentException extends RuntimeException {
    public ExistsCancelPaymentException(String message) {
        super(message);
    }
}
