package com.project.insure.payment.application.controller;

import com.project.insure.payment.application.usecase.cardfinder.CardPaymentServiceFinder;
import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.code.PrefixDataType;
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
@RequestMapping("/payment")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    private final CardPaymentServiceFinder paymentServiceFinder;

    @GetMapping("/getPaymentId")
    public PaymentIdResponseDto getPaymentId(){
        String paymentId = ManagementIdGeneratorUtil.getPaymentId(PrefixDataType.결제);
        return PaymentIdResponseDto.builder().paymentId(paymentId).build();
    }
    //TODO : ArgumentException 처리
    @PostMapping("/card")
    public CardPaymentResponseDto cardPayment(@RequestBody CardPaymentRequestDto requestDto
            , @RequestHeader(value = "Data-Type", required = true) String dataType){
        CardPaymentUsecase paymentUsecase = paymentServiceFinder.findPaymentUsecaseByCardCompany(PaymentCompany.Hana.name());

        paymentUsecase.payment(requestDto);
        return null;
    }

    @GetMapping("")
    public void aa(@Valid @RequestBody CardPaymentRequestDto requestDto){
        System.out.println("a");
    }
}
