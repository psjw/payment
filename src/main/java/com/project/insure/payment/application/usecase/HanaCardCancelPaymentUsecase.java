package com.project.insure.payment.application.usecase;

import com.project.insure.exception.payment.ExistsCancelPaymentException;
import com.project.insure.exception.payment.NotCancelPaymentException;
import com.project.insure.exception.payment.PaymentNotFoundException;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.project.insure.payment.domain.card.code.CardPaymentDataPadding.*;


@Service
@RequiredArgsConstructor
public class HanaCardCancelPaymentUsecase implements CardCancelPaymentUsecase {
    private final HanaCardCancelPaymentReadServiceImpl hanaCardPaymentWriteService;
    private final HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private final HanaCardCancelPaymentWriteServiceImpl hanaCardCancelPaymentWriteService;
    @Override
    public CardCancelPaymentResponseDto cancelPayment(CardCancelPaymentRequestDto requestDto, String paymentId, String dataType) {
        //1. 결제정보 API 조회
        CardPaymentResponseDto paymentInfo = hanaCardPaymentReadService.findPaymentInfoByPaymentId(paymentId);

        if(Objects.isNull(paymentInfo)){
            throw new PaymentNotFoundException(String.format("[결제번호 : [%s]] 결제 이력이 없습니다. ", paymentId));
        }

        //2. 취소정보 API 조회
        CardCancelPaymentResponseDto cancelPaymentInfo = hanaCardPaymentWriteService.findCancelPaymentInfoByPaymentId(paymentId);
        if(Objects.nonNull(cancelPaymentInfo)){
            throw new ExistsCancelPaymentException(String.format("[결제번호 : [%s]] 이미 취소되었습니다.",paymentId));
        }
        //3. 데이터 취소 API 호출
        Long paymentAmount = Long.parseLong(getPaymentValueInDataBody(결제금액, paymentInfo.getDataBody()));
        if(paymentAmount < requestDto.getAmount()){
            throw new NotCancelPaymentException(String.format("[결제번호 : [%s]] 결제금액보다 취소금액이 많습니다.",paymentId));
        }
        String cancelPaymentId = ManagementIdGeneratorUtil.getPaymentId(PrefixDataType.취소);
        String cancelDataBody = convertDataBodyToCancelDataBodyByPaymentInfo(paymentInfo, cancelPaymentId);
        CardCancelPaymentResponseDto cardCancelPaymentResponseDto = hanaCardCancelPaymentWriteService.cancelPayment(requestDto, paymentId, cancelPaymentId, cancelDataBody);

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
        replaceBuffer.replace(getPaymentDataBodyStartIndex(결제관리번호),getPaymentDataBodyLastIndex(결제관리번호), paymentInfo.getPaymentId());
        return replaceBuffer.toString();
    }

    private String getPaymentValueInDataBody(CardPaymentDataPadding cardPaymentDataPadding, String dataBody){
        return dataBody.substring(getPaymentDataBodyStartIndex(cardPaymentDataPadding), getPaymentDataBodyLastIndex(cardPaymentDataPadding));
    }

    private Integer getPaymentDataBodyLastIndex(CardPaymentDataPadding cardPaymentDataPadding){
        int result = getPaymentDataBodyStartIndex(cardPaymentDataPadding);
        for(CardPaymentDataPadding value  : values()){
            result += value.getLength();
            if(value.name().equals(cardPaymentDataPadding.name())){
                break;
            }
        }
        return  result;
    }
    private Integer getPaymentDataBodyStartIndex(CardPaymentDataPadding cardPaymentDataPadding) {
        Integer result = 0;
        for(CardPaymentDataPadding value  : values()){
            if(value.name().equals(cardPaymentDataPadding.name())){
                break;
            }
            result += value.getLength();
        }
        return result;
    }

}
