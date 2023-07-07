package com.project.insure.payment.application.controller;

import com.project.insure.exception.payment.DuplicatePaymentException;
import com.project.insure.payment.application.usecase.PaymentDuplicateService;
import com.project.insure.payment.application.usecase.cardfinder.CardPaymentServiceFinder;
import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.dto.*;
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

    @PostMapping("/card")
    public CardPaymentResponseDto cardPayment(@RequestBody CardPaymentRequestDto requestDto,
                                              @RequestHeader(value = "Data-Type", required = true) String dataType){
        CardPaymentUsecase paymentUsecase = paymentServiceFinder.findPaymentUsecaseByCardCompany(PaymentCompany.Hana.name());
        CardPaymentResponseDto cardPaymentResponseDto = paymentUsecase.payment(requestDto);
        return cardPaymentResponseDto;
    }

    @GetMapping("/payment/info")
    public CardPaymentInfoResponseDto getCardPaymentInfo(@Valid @RequestBody CardPaymentInfoRequestDto requestDto,
                                                         @RequestHeader(value = "Payment-Id", required = true) @Size(min = 20, max = 20) String paymentId,
                                                         @RequestHeader(value = "Data-Type", required = true) String dataType){
        CardPaymentUsecase paymentUsecase = paymentServiceFinder.findPaymentUsecaseByCardCompany(PaymentCompany.Hana.name());
        CardPaymentInfoResponseDto cardPaymentInfoResponseDto = paymentUsecase.findPaymentAndCancelInfoByPaymentId(requestDto, paymentId);
        return cardPaymentInfoResponseDto;
    }
}
