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


    public static String getLeftOrRightPaddingData(CardPaymentDataPadding cardPaymentDataLength, String data) {
        String reqPayType = cardPaymentDataLength.getPadType();
        if (StringUtils.contains(reqPayType, "leftPad")) {
            return StringUtils.leftPad(data, cardPaymentDataLength.getLength(), cardPaymentDataLength.padStr);
        }

        if (StringUtils.contains(reqPayType, "rightPad")) {
            return StringUtils.rightPad(data, cardPaymentDataLength.getLength(), cardPaymentDataLength.padStr);
        }
        throw new RuntimeException("포함되지 않은 데이터 입니다.");
    }



    public static Integer getDataBodyInLastIndex(CardPaymentDataPadding cardPaymentDataPadding){
        int result = getDataBodyInStartIndex(cardPaymentDataPadding);
        for(CardPaymentDataPadding value  : values()){
            result += value.getLength();
            if(value.name().equals(cardPaymentDataPadding.name())){
                break;
            }
        }
        return  result;
    }
    public static Integer getDataBodyInStartIndex(CardPaymentDataPadding cardPaymentDataPadding) {
        Integer result = 0;
        for(CardPaymentDataPadding value  : values()){
            if(value.name().equals(cardPaymentDataPadding.name())){
                break;
            }
            result += value.getLength();
        }
        return result;
    }

    public static String getPaymentValueInDataBody(CardPaymentDataPadding cardPaymentDataPadding, String dataBody){
        return dataBody.substring(getDataBodyInStartIndex(cardPaymentDataPadding), getDataBodyInLastIndex(cardPaymentDataPadding));
    }
}
