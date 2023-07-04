package com.project.insure.payment.application.usecase;

import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.service.HanaCardPaymentWriteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@RequiredArgsConstructor
public class HanaCardPaymentUsecase implements CardPaymentUsecase {
    private final HanaCardPaymentWriteServiceImpl hanaCardPaymentWriteService;

    @Override
    public void payment(CardPaymentRequestDto requestDto) {
        hanaCardPaymentWriteService.payment(requestDto);
    }

    @Override
    public PaymentCompany getPaymentCompany() {
        return PaymentCompany.Hana;
    }
}
