package com.project.insure.exception.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImpossibleCancelPaymentException extends RuntimeException {
    public ImpossibleCancelPaymentException(String message) {
        super(message);
    }
}
