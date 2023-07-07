package com.project.insure.payment.domain.card.service;

import com.project.insure.payment.domain.card.code.CardPaymentDataPadding;
import com.project.insure.payment.domain.card.code.DataType;
import com.project.insure.payment.domain.card.code.PrefixDataType;
import com.project.insure.payment.domain.card.dto.*;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import com.project.insure.util.ManagementIdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HanaCardPaymentWriteServiceImpl {
    private final CardPaymentMapper cardPaymentMapper;
    private final CardPaymentRepository cardPaymentRepository;
    public CardPaymentResponseDto payment(CardPaymentRequestDto requestDto) {
        CardPayment reqeustCardPayment = requestDto.dtoToCardPaymentEntity(ManagementIdGeneratorUtil.getPaymentId(PrefixDataType.결제), DataType.결제.getCode());
        CardPayment savedCardPayment = cardPaymentRepository.save(reqeustCardPayment);
        return cardPaymentMapper.toDto(savedCardPayment);

    }
}
