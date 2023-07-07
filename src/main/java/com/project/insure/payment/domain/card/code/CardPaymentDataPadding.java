package com.project.insure.payment.domain.card.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum CardPaymentDataPadding {
    데이터길이(4, " ", "leftPad"),
    데이터구분(10, " ", "rightPad"),
    결제관리번호(20, " ", "rightPad"),
    카드번호(20, " ", "rightPad"),
    할부개월수(2, "0", "leftPad"),
    카드유효기간(4, " ", "rightPad"),
    CVC(3, " ", "rightPad"),
    결제금액(10, " ", "rightPad"),
    부가가치세(10, "0", "leftPad"),
    원거래관리번호(20, " ", "rightPad"),
    암호화된카드정보(300, " ", "rightPad"),
    예비필드(47, " ", "rightPad");
    private Integer length;
    private String padStr;
    private String padType;



}
