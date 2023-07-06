package com.project.insure.payment.domain.card.dto;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CardPaymentResponseDto {
    private String paymentId;
    private String dataBody;


}
