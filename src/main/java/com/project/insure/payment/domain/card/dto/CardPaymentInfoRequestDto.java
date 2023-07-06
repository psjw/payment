package com.project.insure.payment.domain.card.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.insure.payment.domain.card.entity.CardPayment;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class CardPaymentInfoRequestDto {
    @JsonIgnore
    private String paymentId;


    public CardPayment dtoToCardPaymentEntity(String paymentId, String dataType){
        return null;
    }

}
