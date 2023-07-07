package com.project.insure.payment.application.usecase;

import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.CardPaymentInfoRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentInfoResponseDto;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;

public interface CardPaymentUsecase {
    CardPaymentResponseDto payment(CardPaymentRequestDto requestDto); //결제
    CardPaymentInfoResponseDto findPaymentAndCancelInfoByPaymentId(CardPaymentInfoRequestDto requestDto, String paymentId); //결제


    PaymentCompany getPaymentCompany(); //카드회사
}
