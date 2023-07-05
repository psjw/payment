package com.project.insure.payment.application.usecase;

import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.service.HanaCardPaymentWriteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class HanaCardPaymentUsecase implements CardPaymentUsecase {
    private final HanaCardPaymentWriteServiceImpl hanaCardPaymentWriteService;
    @Override
    public CardPaymentResponseDto payment(CardPaymentRequestDto requestDto) {
        return hanaCardPaymentWriteService.payment(requestDto);
    }

    @Override
    public PaymentCompany getPaymentCompany() {
        return PaymentCompany.Hana;
    }
}
