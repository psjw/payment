package com.project.insure.payment.domain.card.code;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrefixDataType {
    결제("P"), 취소("C");
    private String code;

}
