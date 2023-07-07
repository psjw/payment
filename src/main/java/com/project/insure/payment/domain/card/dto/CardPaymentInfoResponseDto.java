package com.project.insure.payment.domain.card.dto;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CardPaymentInfoResponseDto {
    private String paymentId;
    private String cardNo;
    private String expiredPeriod;
    private String paymentType; //결제/취소

    private Integer installmentMonth;

    private String cvc;

    private Long amount;

    private Long vat;

    private String cancelPaymentId;

    private Long cancelAmount;

}
