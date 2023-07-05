package com.project.insure.payment.domain.card.mapper;

import com.project.insure.common.CommonMapper;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardPaymentMapper extends CommonMapper<CardPaymentResponseDto, CardPayment> {
}
