package com.project.insure.payment.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class PaymentDuplicateService {
    private Set<String> cardNoSet = new HashSet<>();
    private Set<String> paymentIdSet = new HashSet<>();

    public synchronized boolean isDuplicateCardNo(String cardNo) {

        if (cardNoSet.contains(cardNo)) {
            log.info("fail cardNo : {} / cardNoSet : {}",cardNo, cardNoSet);
            return false;
        }
        log.info("success cardNo : {} / cardNoSet : {}",cardNo, cardNoSet);
        cardNoSet.add(cardNo);
        return true;
    }


    public void destroyCardNo(String cardNo) {
        cardNoSet.remove(cardNo);
    }

    public synchronized boolean isDuplicatePaymentId(String paymentId) {

        if (paymentIdSet.contains(paymentId)) {
            log.info("fail paymentId : {} / paymentIdSet : {}",paymentId, paymentIdSet);

            return false;
        }
        log.info("success paymentId : {} / paymentIdSet : {}",paymentId, paymentIdSet);
        paymentIdSet.add(paymentId);
        return true;
    }

    public void destroyPaymentId(String paymentId) {
        paymentIdSet.remove(paymentId);
    }
}
