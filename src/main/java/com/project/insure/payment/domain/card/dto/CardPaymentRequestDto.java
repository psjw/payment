package com.project.insure.payment.domain.card.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.insure.payment.domain.card.code.CardPaymentDataPadding;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.util.EncryptOrDecryptDataGeneratorUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;

import java.util.Objects;

import static com.project.insure.payment.domain.card.code.CardPaymentDataPadding.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class CardPaymentRequestDto {

    @NotBlank(message = "카드번호를 입력해 주세요.")
    @Size(min =10, max = 16)
    @Pattern(regexp = "[0-9]", message = "카드번호는 숫자형식만 지원합니다.")
    private String cardNo; //카드번호(10 ~ 16자리 숫자)

    @NotBlank(message = "카드번호를 입력해 주세요.")
    @Size(min = 4, max = 4)
    @Pattern(regexp = "[0-9]", message = "유효기간은 숫자형식만 지원합니다.")
    private String expiredPeriod; //유효기간(4자리 숫자, mmyy)

    @NotBlank(message = "cvc번호를 입력해 주세요.")
    @Size(min = 3, max = 3)
    @Pattern(regexp = "[0-9]", message = "cvc는 숫자형식만 지원합니다.")
    private String cvc; //cvc(3자리 숫자)

    @NotBlank(message = "할부개월 수를 입력해 주세요.")
    @Positive
    @Max(value=12)
    @Pattern(regexp = "[0-9]", message = "결제액는 숫자형식만 지원합니다.")
    private Integer installmentMonth; //할부개월 수 0(일시불), 1 ~ 12

    @NotBlank(message = "결제액를 입력해 주세요.")
    @Min(value = 100, message = "결제액은 최소 100원 이상 입니다.")
    @Max(value = 1000000000, message = "결재액은 최대 10억원 입니다.")
    @Pattern(regexp = "[0-9]", message = "결제액는 숫자형식만 지원합니다.")
    private Long amount; //결제액(100원 이상, 10억원 이하, 숫자)
    private Long vat; //부가가치세 (option)

    @JsonIgnore
    private final String originPaymentId = "";

    public Long getVat(){
        if(Objects.isNull(vat)){
            this.vat =  Math.round((double) this.amount / (double) 11);
        }
        if(vat > amount ){
            throw new RuntimeException("부가세(VAT)는 결재금액보다 클 수 없습니다.");
        }
        return vat;
    }

    public CardPayment dtoToCardPaymentEntity(String paymentId, String dataType){
        StringBuffer resultStringBuffer = new StringBuffer();
        int totalLength = 450;
        resultStringBuffer.append(getLeftOrRightPaddingData(데이터구분, dataType))
                .append(getLeftOrRightPaddingData(결제관리번호, paymentId))
                .append(getLeftOrRightPaddingData(카드번호, this.cardNo))
                .append(getLeftOrRightPaddingData(할부개월수, Integer.toString(this.installmentMonth)))
                .append(getLeftOrRightPaddingData(카드유효기간, this.expiredPeriod))
                .append(getLeftOrRightPaddingData(CVC, this.cvc))
                .append(getLeftOrRightPaddingData(결제금액, Long.toString(this.amount)))
                .append(getLeftOrRightPaddingData(부가가치세, Long.toString(getVat())))
                .append(getLeftOrRightPaddingData(원거래관리번호, originPaymentId));


        String encryptDataBody = EncryptOrDecryptDataGeneratorUtil
                .getEncryptData(this.cardNo, Integer.toString(this.installmentMonth), this.cvc);
        resultStringBuffer.append(getLeftOrRightPaddingData(암호화된카드정보, encryptDataBody));
        resultStringBuffer.append(getLeftOrRightPaddingData(예비필드, originPaymentId));
        resultStringBuffer.insert(0,  getLeftOrRightPaddingData(데이터길이,Integer.toString(resultStringBuffer.length())));

        return CardPayment.builder()
                .paymentId(paymentId)
                .dataBody(resultStringBuffer.toString())
                .build();
    }


    private String getLeftOrRightPaddingData(CardPaymentDataPadding cardPaymentDataLength, String data) {
        String reqPayType = cardPaymentDataLength.getPadType();
        if (StringUtils.contains(reqPayType, "leftPad")) {
            return StringUtils.leftPad(data, cardPaymentDataLength.getLength(), cardPaymentDataLength.getPadStr());
        }

        if (StringUtils.contains(reqPayType, "rightPad")) {
            return StringUtils.rightPad(data, cardPaymentDataLength.getLength(), cardPaymentDataLength.getPadStr());
        }
        throw new RuntimeException("포함되지 않은 데이터 입니다.");
    }

}
