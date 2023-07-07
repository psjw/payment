package com.project.insure.payment.application.usecase;

import com.project.insure.exception.payment.DuplicatePaymentException;
import com.project.insure.exception.payment.ImpossibleCancelPaymentException;
import com.project.insure.payment.domain.card.code.CardPaymentDataPadding;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.code.PrefixDataType;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentWriteServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentReadServiceImpl;
import com.project.insure.util.ManagementIdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.insure.payment.domain.card.code.CardPaymentDataPadding.*;


@Service
@RequiredArgsConstructor
public class HanaCardCancelPaymentUsecase implements CardCancelPaymentUsecase {
    private final HanaCardCancelPaymentReadServiceImpl hanaCardPaymentWriteService;
    private final HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private final HanaCardCancelPaymentWriteServiceImpl hanaCardCancelPaymentWriteService;

    private final PaymentDuplicateService paymentDuplicateService;


    @Override
    public CardCancelPaymentResponseDto cancelPayment(CardCancelPaymentRequestDto requestDto, String paymentId, String dataType) {
        if(!paymentDuplicateService.isDuplicatePaymentId(paymentId)){
            throw new DuplicatePaymentException(String.format("이미 결제 취소 진행중입니다. 결제번호 : %s",paymentId));
        }

        //1. 결제정보 API 조회
        CardPaymentResponseDto paymentInfo = hanaCardPaymentReadService.findPaymentInfoByPaymentId(paymentId);

        //2. 취소정보 API 조회
        hanaCardPaymentWriteService.findCancelPaymentInfoByPaymentId(paymentId);

        //3. 데이터 취소 API 호출
        Long paymentAmount = Long.parseLong(getPaymentValueInCancelDataBody(결제금액, paymentInfo.getDataBody()).replaceAll(" ",""));
        if(paymentAmount < requestDto.getAmount()){
            throw new ImpossibleCancelPaymentException(String.format("[결제번호 : [%s]] 결제금액보다 취소금액이 많습니다.",paymentId));
        }
        String cancelPaymentId = ManagementIdGeneratorUtil.getPaymentId(PrefixDataType.취소);
        String cancelDataBody = convertDataBodyToCancelDataBodyByPaymentInfo(paymentInfo, cancelPaymentId);
        CardCancelPaymentResponseDto cardCancelPaymentResponseDto = hanaCardCancelPaymentWriteService.cancelPayment(requestDto, paymentId, cancelPaymentId, cancelDataBody);
        paymentDuplicateService.destroyPaymentId(paymentId);
        return cardCancelPaymentResponseDto;
    }

    @Override
    public PaymentCompany getPaymentCompany() {
        return PaymentCompany.Hana;
    }

    private String convertDataBodyToCancelDataBodyByPaymentInfo(CardPaymentResponseDto paymentInfo, String cancelPaymentId){
        StringBuffer replaceBuffer = new StringBuffer(paymentInfo.getDataBody());
        int paymentPosition = paymentInfo.getDataBody().indexOf(paymentInfo.getPaymentId());
        replaceBuffer.replace(paymentPosition, paymentPosition + paymentInfo.getPaymentId().length(), cancelPaymentId);
        replaceBuffer.replace(getCancelDataBodyInStartIndex(결제관리번호), getCacnelDataBodyInLastIndex(결제관리번호), paymentInfo.getPaymentId());
        return replaceBuffer.toString();
    }

    private Integer getCacnelDataBodyInLastIndex(CardPaymentDataPadding cardPaymentDataPadding) {
        int result = getCancelDataBodyInStartIndex(cardPaymentDataPadding)
                + CardPaymentDataPadding.valueOf(cardPaymentDataPadding.name()).getLength();
        return result;
    }

    private Integer getCancelDataBodyInStartIndex(CardPaymentDataPadding cardPaymentDataPadding) {
        Integer result = 0;
        for (CardPaymentDataPadding value : CardPaymentDataPadding.values()) {
            if (value.name().equals(cardPaymentDataPadding.name())) {
                break;
            }
            result += value.getLength();
        }
        return result;
    }

    private String getPaymentValueInCancelDataBody(CardPaymentDataPadding cardPaymentDataPadding, String dataBody) {
        return dataBody.substring(getCancelDataBodyInStartIndex(cardPaymentDataPadding), getCacnelDataBodyInLastIndex(cardPaymentDataPadding));
    }


}
