package com.project.insure.payment.application.usecase;

import com.project.insure.exception.payment.DuplicatePaymentException;
import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.code.CardPaymentDataPadding;
import com.project.insure.payment.domain.card.code.DataType;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.*;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentWriteServiceImpl;
import com.project.insure.util.MaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.project.insure.payment.domain.card.code.CardPaymentDataPadding.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class HanaCardPaymentUsecase implements CardPaymentUsecase {
    private final HanaCardPaymentWriteServiceImpl hanaCardPaymentWriteService;
    private final HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private final HanaCardCancelPaymentReadServiceImpl hanaCardCancelPaymentReadService;
    private final PaymentDuplicateService paymentDuplicateService;

    @Override
    public CardPaymentResponseDto payment(CardPaymentRequestDto requestDto) {
        if(!paymentDuplicateService.isDuplicateCardNo(requestDto.getCardNo())){
            throw new DuplicatePaymentException(String.format("이미 결제 진행중입니다. 카드번호 : %s",requestDto.getCardNo()));
        }

        CardPaymentResponseDto payment = hanaCardPaymentWriteService.payment(requestDto);
        paymentDuplicateService.destroyCardNo(requestDto.getCardNo());
        return payment;
    }

    @Override
    public CardPaymentInfoResponseDto findPaymentAndCancelInfoByPaymentId(CardPaymentInfoRequestDto requestDto, String paymentId) {

        //1. 결제정보
        CardPaymentResponseDto cardPaymentResponseDto = hanaCardPaymentReadService.findPaymentInfoByPaymentId(paymentId);
        //2. 취소정보
        CardCancelPaymentResponseDto cardCancelPaymentResponseDto = new CardCancelPaymentResponseDto();
        try {
            //2. 취소정보
            cardCancelPaymentResponseDto = hanaCardCancelPaymentReadService.findCancelPaymentInfoByPaymentId(paymentId);
        } catch (PaymentNotFoundException paymentNotFoundException) {
            log.info("[HanaCardPaymentUsecase.findPaymentAndCancelInfoByPaymentId] 취소 결제 정보가 없습니다. 결제번호 : {}", paymentId);
        }
        //3. 결제정보 조합
        return convertDataBodyToCancelDataBodyByPaymentInfo(cardPaymentResponseDto, cardCancelPaymentResponseDto, paymentId);

    }


    private CardPaymentInfoResponseDto convertDataBodyToCancelDataBodyByPaymentInfo(CardPaymentResponseDto paymentInfo, CardCancelPaymentResponseDto cancelInfo, String paymentId) {
        String dataBody = paymentInfo.getDataBody();
        String dataType = Objects.nonNull(cancelInfo.getPaymentId()) ? DataType.취소.getCode() : DataType.결제.getCode();
        return CardPaymentInfoResponseDto.builder()
                .cardNo(MaskingUtil.cardMasking(getPaymentValueInDataBody(카드번호, dataBody)))
                .paymentType(dataType)
                .cvc(getPaymentValueInDataBody(CVC, dataBody))
                .installmentMonth(Integer.parseInt(getPaymentValueInDataBody(할부개월수, dataBody)))
                .vat(Long.parseLong(getPaymentValueInDataBody(부가가치세, dataBody)))
                .amount(Long.parseLong(getPaymentValueInDataBody(결제금액, dataBody)))
                .build();
    }

    @Override
    public PaymentCompany getPaymentCompany() {
        return PaymentCompany.Hana;
    }


    private Integer getDataBodyInLastIndex(CardPaymentDataPadding cardPaymentDataPadding) {
        int result = getDataBodyInStartIndex(cardPaymentDataPadding);
        for (CardPaymentDataPadding value : CardPaymentDataPadding.values()) {
            result += value.getLength();
            if (value.name().equals(cardPaymentDataPadding.name())) {
                break;
            }
        }
        return result;
    }

    private Integer getDataBodyInStartIndex(CardPaymentDataPadding cardPaymentDataPadding) {
        Integer result = 0;
        for (CardPaymentDataPadding value : CardPaymentDataPadding.values()) {
            if (value.name().equals(cardPaymentDataPadding.name())) {
                break;
            }
            result += value.getLength();
        }
        return result;
    }

    private String getPaymentValueInDataBody(CardPaymentDataPadding cardPaymentDataPadding, String dataBody) {
        return dataBody.substring(getDataBodyInStartIndex(cardPaymentDataPadding), getDataBodyInLastIndex(cardPaymentDataPadding));
    }
}
