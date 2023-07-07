package com.project.insure.payment.domain.card.code;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum DataType {
    결제("PAYMENT"), 취소("CANCEL");
    private String code;

}
