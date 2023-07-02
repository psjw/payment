package com.project.insure.payment.application.controller;

import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;
import java.util.UUID;

@RestController
public class PaymentController {
    public static void main(String[] args) {
        long l = ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong();
        System.out.println(Long.toString(l, 20));
    }
}
