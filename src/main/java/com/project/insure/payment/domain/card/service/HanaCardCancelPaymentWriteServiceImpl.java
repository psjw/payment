package com.project.insure.payment.domain.card.service;

import com.project.insure.payment.domain.card.dto.CardCancelPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HanaCardCancelPaymentWriteServiceImpl {
    private final CardCancelPaymentMapper cardCancelPaymentMapper;
    private final CardCancelPaymentRepository cardCancelPaymentRepository;
    public CardCancelPaymentResponseDto cancelPayment(CardCancelPaymentRequestDto requestDto, String paymentId, String cancelPaymentId, String cancelDataBody) {
        CardCancelPayment cardCancelPayment = CardCancelPayment.builder()
                .cancelPaymentId(cancelPaymentId)
                .paymentId(paymentId)
                .dataBody(cancelDataBody)
                .build();


        CardCancelPayment savedCardCancelPayment = cardCancelPaymentRepository.save(cardCancelPayment);
        return cardCancelPaymentMapper.toDto(savedCardCancelPayment);
    }

}
