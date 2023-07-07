package com.project.insure.payment.domain.card.mapper;

import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T18:19:32+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.18 (Amazon.com Inc.)"
)
@Component
public class CardCancelPaymentMapperImpl implements CardCancelPaymentMapper {

    @Override
    public CardCancelPaymentResponseDto toDto(CardCancelPayment arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CardCancelPaymentResponseDto.CardCancelPaymentResponseDtoBuilder cardCancelPaymentResponseDto = CardCancelPaymentResponseDto.builder();

        cardCancelPaymentResponseDto.paymentId( arg0.getPaymentId() );
        cardCancelPaymentResponseDto.dataBody( arg0.getDataBody() );

        return cardCancelPaymentResponseDto.build();
    }

    @Override
    public CardCancelPayment toEntity(CardCancelPaymentResponseDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CardCancelPayment.CardCancelPaymentBuilder cardCancelPayment = CardCancelPayment.builder();

        cardCancelPayment.paymentId( arg0.getPaymentId() );
        cardCancelPayment.dataBody( arg0.getDataBody() );

        return cardCancelPayment.build();
    }

    @Override
    public void updateFromDto(CardCancelPaymentResponseDto arg0, CardCancelPayment arg1) {
        if ( arg0 == null ) {
            return;
        }
    }
}
