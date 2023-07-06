package com.project.insure.payment.application.usecase;

import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;

public interface CardCancelPaymentUsecase {
    CardCancelPaymentResponseDto cancelPayment(CardCancelPaymentRequestDto requestDto, String paymentId, String dataType); //결제취소

    PaymentCompany getPaymentCompany(); //카드회사

}
