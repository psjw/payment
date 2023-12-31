package com.project.insure.payment.domain.card.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PaymentIdResponseDto {
    private String paymentId;
}
