package com.project.insure.payment.domain.card.service;

import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HanaCardCancelPaymentReadServiceImpl {
    private final CardCancelPaymentMapper cardPaymentMapper;
    private final CardCancelPaymentRepository cardCancelPaymentRepository;
    public CardCancelPaymentResponseDto findCancelPaymentInfoByPaymentId(String paymentId) {
        return cardPaymentMapper.toDto(cardCancelPaymentRepository
                .findCardCancelPaymentByPaymentId(paymentId)
                .orElseThrow(() ->  new PaymentNotFoundException(String.format("[결제번호 : [%s]] 결제취소 이력이 없습니다. ", paymentId))));
    }
}