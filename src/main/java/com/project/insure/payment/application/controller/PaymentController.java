package com.project.insure.payment.application.controller;

import com.project.insure.payment.application.usecase.CardPaymentServiceFinder;
import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.code.PaymentMethod;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.dto.PaymentIdResponseDto;
import com.project.insure.util.ManagementIdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.ByteBuffer;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final CardPaymentServiceFinder paymentServiceFinder;

    @GetMapping("/getPaymentId")
    public PaymentIdResponseDto getPaymentId(){
        String paymentId = ManagementIdGeneratorUtil.getPaymentId(PaymentMethod.결제);
        return PaymentIdResponseDto.builder().paymentId(paymentId).build();
    }

    @PostMapping("/card")
    public CardPaymentResponseDto cardPayment(@RequestBody CardPaymentRequestDto requestDto){
        CardPaymentUsecase paymentUsecase = paymentServiceFinder.findPaymentUsecaseByCardCompany(PaymentCompany.Hana.name());

        paymentUsecase.payment(requestDto);
        return null;
    }

    @GetMapping("")
    public void aa(@Valid @RequestBody CardPaymentRequestDto requestDto){
        System.out.println("a");
    }
}
