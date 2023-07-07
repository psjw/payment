package com.project.insure.payment.application.usecase;

import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.code.DataType;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.*;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentWriteServiceImpl;
import com.project.insure.util.MaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public CardPaymentResponseDto payment(CardPaymentRequestDto requestDto) {
        return hanaCardPaymentWriteService.payment(requestDto);
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

}
