package com.project.insure.payment.domain.card.repsotory;

import com.project.insure.payment.domain.card.entity.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {
    CardPayment findCardPaymentByPaymentId(String paymentId);
}
