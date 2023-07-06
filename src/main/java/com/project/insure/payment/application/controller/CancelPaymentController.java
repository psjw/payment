package com.project.insure.payment.application.controller;

import com.project.insure.payment.application.usecase.cardfinder.CardCancelPaymentServiceFinder;
import com.project.insure.payment.application.usecase.CardCancelPaymentUsecase;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.code.PrefixDataType;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.dto.PaymentIdResponseDto;
import com.project.insure.util.ManagementIdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/payment/cancel")
@RequiredArgsConstructor
@Validated
public class CancelPaymentController {
    private final CardCancelPaymentServiceFinder cardCancelPaymentServiceFinder;

    @GetMapping("/getPaymentId")
    public PaymentIdResponseDto getPaymentId(){
        String paymentId = ManagementIdGeneratorUtil.getPaymentId(PrefixDataType.취소);
        return PaymentIdResponseDto.builder().paymentId(paymentId).build();
    }

    //TODO : ArgumentException 처리
    @PostMapping("/card")
    public CardPaymentResponseDto cardCancelPayment(@RequestBody CardCancelPaymentRequestDto requestDto
            , @RequestHeader(value = "Payment-Id", required = true) @Size(min = 20, max = 20) String paymentId
            , @RequestHeader(value = "Data-Type", required = true) String dataType){
        CardCancelPaymentUsecase paymentUsecaseByCardCompany = cardCancelPaymentServiceFinder.findPaymentUsecaseByCardCompany(PaymentCompany.Hana.name());

        paymentUsecaseByCardCompany.cancelPayment(requestDto, paymentId, dataType);
        return null;
    }

    @GetMapping("")
    public void aa(@Valid @RequestBody CardPaymentRequestDto requestDto){
        System.out.println("a");
    }
}
