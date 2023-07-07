package com.project.insure.payment.domain.card.mapper;

import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T21:16:46+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.18 (Amazon.com Inc.)"
)
@Component
public class CardPaymentMapperImpl implements CardPaymentMapper {

    @Override
    public CardPaymentResponseDto toDto(CardPayment arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CardPaymentResponseDto.CardPaymentResponseDtoBuilder cardPaymentResponseDto = CardPaymentResponseDto.builder();

        cardPaymentResponseDto.paymentId( arg0.getPaymentId() );
        cardPaymentResponseDto.dataBody( arg0.getDataBody() );

        return cardPaymentResponseDto.build();
    }

    @Override
    public CardPayment toEntity(CardPaymentResponseDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CardPayment.CardPaymentBuilder cardPayment = CardPayment.builder();

        cardPayment.paymentId( arg0.getPaymentId() );
        cardPayment.dataBody( arg0.getDataBody() );

        return cardPayment.build();
    }

    @Override
    public void updateFromDto(CardPaymentResponseDto arg0, CardPayment arg1) {
        if ( arg0 == null ) {
            return;
        }
    }
}
