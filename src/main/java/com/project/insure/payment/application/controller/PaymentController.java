package com.project.insure.payment.application.controller;

import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.ByteBuffer;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    public static void main(String[] args) {
        long l = ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong();
        System.out.println(Long.toString(l, 20));
    }

    @PostMapping("/card")
    public CardPaymentResponseDto cardPayment(@Valid @RequestBody CardPaymentRequestDto requestDto){


        return null;
    }
}
