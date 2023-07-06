package com.project.insure.payment.domain.card.mapper;

import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T00:59:57+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.18 (Amazon.com Inc.)"
)
@Component
public class CardPaymentMapperImpl implements CardPaymentMapper {

    @Override
    public CardPaymentResponseDto toDto(CardPayment e) {
        if ( e == null ) {
            return null;
        }

        CardPaymentResponseDto.CardPaymentResponseDtoBuilder cardPaymentResponseDto = CardPaymentResponseDto.builder();

        return cardPaymentResponseDto.build();
    }

    @Override
    public CardPayment toEntity(CardPaymentResponseDto d) {
        if ( d == null ) {
            return null;
        }

        CardPayment.CardPaymentBuilder cardPayment = CardPayment.builder();

        cardPayment.paymentId( d.getPaymentId() );
        cardPayment.dataBody( d.getDataBody() );

        return cardPayment.build();
    }

    @Override
    public void updateFromDto(CardPaymentResponseDto dto, CardPayment entity) {
        if ( dto == null ) {
            return;
        }
    }
}
