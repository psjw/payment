package com.project.insure.payment.domain.card.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import lombok.*;

import javax.validation.constraints.*;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class CardCancelPaymentRequestDto {


    @NotBlank(message = "취소금액를 입력해 주세요.")
    @Min(value = 100, message = "취소금액은 최소 100원 이상 입니다.")
    @Max(value = 1000000000, message = "취소금액은 최대 10억원 입니다.")
    @Pattern(regexp = "[0-9]", message = "취소금액는 숫자형식만 지원합니다.")
    private Long amount; //취소액(100원 이상, 10억원 이하, 숫자)
    private Long vat; //부가가치세 (option)


    @JsonIgnore
//    private final String originPaymentId = "";

    public Long getVat(){
        if(Objects.isNull(vat)){
            this.vat =  Math.round((double) this.amount / (double) 11);
        }
        if(vat > amount ){
            throw new RuntimeException("부가세(VAT)는 취소금액보다 클 수 없습니다.");
        }
        return vat;
    }

}
