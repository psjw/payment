package com.project.insure.payment.domain.card.service;

import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HanaCardPaymentWriteServiceImpl {
    private final CardPaymentMapper cardPaymentMapper;
    private final CardPaymentRepository cardPaymentRepository;
    public CardPayment payment(CardPaymentRequestDto requestDto) {

//        requestDto.
//        requestDto.set
        CardPayment cardPayment = cardPaymentMapper.toEntity(requestDto);

        CardPayment save = cardPaymentRepository.save(cardPayment);
        return null;
    }
}
