package com.project.insure.util;

import com.project.insure.payment.domain.card.code.PrefixDataType;
import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;
import java.util.UUID;

@UtilityClass
public class ManagementIdGeneratorUtil {
    private static Integer PAYMENT_ID_LENGTH = 18;
    public static String getPaymentId(PrefixDataType prefixDataType){
        StringBuilder uuid = new StringBuilder();
        long l = ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong();
        uuid.append(prefixDataType.getCode());
        uuid.append(Long.toString(l,PAYMENT_ID_LENGTH));
        return uuid.toString();
    }
}
