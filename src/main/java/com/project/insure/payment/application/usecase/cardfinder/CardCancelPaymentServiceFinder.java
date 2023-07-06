package com.project.insure.payment.application.usecase.cardfinder;

import com.project.insure.payment.application.usecase.CardCancelPaymentUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CardCancelPaymentServiceFinder {
    private final List<CardCancelPaymentUsecase> cancelPaymentUsecases;

    public CardCancelPaymentUsecase findPaymentUsecaseByCardCompany(String paymentCompany){
        return cancelPaymentUsecases.stream()
                .filter(payment -> payment.getPaymentCompany().name().equals(paymentCompany))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
