package com.project.insure.payment.application.usecase.cardfinder;

import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CardPaymentServiceFinder {
    private final List<CardPaymentUsecase> paymentUsecases;

    public CardPaymentUsecase findPaymentUsecaseByCardCompany(String paymentCompany){
        return paymentUsecases.stream()
                .filter(payment -> payment.getPaymentCompany().name().equals(paymentCompany))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
