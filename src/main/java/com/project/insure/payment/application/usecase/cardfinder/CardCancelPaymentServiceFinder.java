package com.project.insure.payment.application.usecase.cardfinder;

import com.project.insure.exception.payment.DuplicatePaymentException;
import com.project.insure.exception.payment.NotSupportedCardCompanyException;
import com.project.insure.payment.application.usecase.CardCancelPaymentUsecase;
import com.project.insure.payment.application.usecase.PaymentDuplicateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CardCancelPaymentServiceFinder {
    private final List<CardCancelPaymentUsecase> cancelPaymentUsecases;

    public CardCancelPaymentUsecase findPaymentUsecaseByCardCompany(String paymentCompany) throws NotSupportedCardCompanyException {

        return cancelPaymentUsecases.stream()
                .filter(payment -> payment.getPaymentCompany().name().equals(paymentCompany))
                .findFirst().orElseThrow(() -> new NotSupportedCardCompanyException("지원하지 않는 카드회사 입니다."));
    }

}
