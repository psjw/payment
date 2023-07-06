package com.project.insure.payment.domain.card.mapper;

import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T00:59:58+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.18 (Amazon.com Inc.)"
)
@Component
public class CardCancelPaymentMapperImpl implements CardCancelPaymentMapper {

    @Override
    public CardCancelPaymentResponseDto toDto(CardCancelPayment e) {
        if ( e == null ) {
            return null;
        }

        CardCancelPaymentResponseDto.CardCancelPaymentResponseDtoBuilder cardCancelPaymentResponseDto = CardCancelPaymentResponseDto.builder();

        return cardCancelPaymentResponseDto.build();
    }

    @Override
    public CardCancelPayment toEntity(CardCancelPaymentResponseDto d) {
        if ( d == null ) {
            return null;
        }

        CardCancelPayment cardCancelPayment = new CardCancelPayment();

        return cardCancelPayment;
    }

    @Override
    public void updateFromDto(CardCancelPaymentResponseDto dto, CardCancelPayment entity) {
        if ( dto == null ) {
            return;
        }
    }
}
