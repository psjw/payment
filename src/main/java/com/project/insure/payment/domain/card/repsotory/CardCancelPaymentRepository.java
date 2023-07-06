package com.project.insure.payment.domain.card.repsotory;

import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import com.project.insure.payment.domain.card.entity.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardCancelPaymentRepository extends JpaRepository<CardCancelPayment, Long> {
    Optional<CardCancelPayment> findCardCancelPaymentByPaymentId(String paymentId);
}
