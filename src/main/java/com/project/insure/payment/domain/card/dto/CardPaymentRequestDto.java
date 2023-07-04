package com.project.insure.payment.domain.card.dto;

import lombok.*;

import javax.validation.constraints.*;


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

}
