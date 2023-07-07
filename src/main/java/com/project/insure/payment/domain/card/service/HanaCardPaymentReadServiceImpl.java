package com.project.insure.payment.domain.card.service;

import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.dto.CardPaymentInfoRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentInfoResponseDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HanaCardPaymentReadServiceImpl {
    private final CardPaymentMapper cardPaymentMapper;
    private final CardPaymentRepository cardpaymentrepository;
    public CardPaymentResponseDto findPaymentInfoByPaymentId(String paymentId) {
        return cardPaymentMapper.toDto(cardpaymentrepository.findCardPaymentByPaymentId(paymentId)
                .orElseThrow(() ->  new PaymentNotFoundException(String.format("[결제번호 : [%s]] 결제 이력이 없습니다. ", paymentId))));
    }

}