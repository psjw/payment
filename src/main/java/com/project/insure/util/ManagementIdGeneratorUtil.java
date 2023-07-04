package com.project.insure.util;

import com.project.insure.payment.domain.card.code.PaymentMethod;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.UUID;

@Component
public class ManagementIdGeneratorUtil {
    private static Integer PAYMENT_ID_LENGTH = 18;
    public static String getPaymentId(PaymentMethod paymentMethod){
        StringBuilder uuid = new StringBuilder();
        long l = ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong();
        uuid.append(paymentMethod.getCode());
        uuid.append(Long.toString(l,PAYMENT_ID_LENGTH));
        return uuid.toString();
    }
}
