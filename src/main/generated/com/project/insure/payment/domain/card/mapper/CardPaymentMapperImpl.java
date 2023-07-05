package com.project.insure.payment.domain.card.mapper;

import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-05T23:35:54+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.18 (Amazon.com Inc.)"
)
@Component
public class CardPaymentMapperImpl implements CardPaymentMapper {

    @Override
    public CardPaymentResponseDto toDto(CardPayment arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CardPaymentResponseDto cardPaymentResponseDto = new CardPaymentResponseDto();

        return cardPaymentResponseDto;
    }

    @Override
    public CardPayment toEntity(CardPaymentResponseDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CardPayment.CardPaymentBuilder cardPayment = CardPayment.builder();

        return cardPayment.build();
    }

    @Override
    public void updateFromDot(CardPaymentResponseDto arg0, CardPayment arg1) {
        if ( arg0 == null ) {
            return;
        }
    }
}
