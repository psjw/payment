package com.project.insure.payment.application.usecase;

import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;

public interface CardPaymentUsecase {
    void payment(CardPaymentRequestDto requestDto); //결제
    PaymentCompany getPaymentCompany(); //카드회사
}
