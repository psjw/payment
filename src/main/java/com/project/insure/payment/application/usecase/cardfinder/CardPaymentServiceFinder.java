package com.project.insure.payment.application.usecase.cardfinder;

import com.project.insure.exception.payment.NotSupportedCardCompanyException;
import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CardPaymentServiceFinder {
    private final List<CardPaymentUsecase> paymentUsecases;

    public CardPaymentUsecase findPaymentUsecaseByCardCompany(String paymentCompany){
        return paymentUsecases.stream()
                .filter(payment -> payment.getPaymentCompany().name().equals(paymentCompany))
                .findFirst().orElseThrow(() -> new NotSupportedCardCompanyException("지원하지 않는 카드회사 입니다."));
    }
}
