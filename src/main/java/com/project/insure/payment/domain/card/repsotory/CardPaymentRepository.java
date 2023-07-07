package com.project.insure.payment.domain.card.repsotory;

import com.project.insure.payment.domain.card.entity.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {

    Optional<CardPayment> findCardPaymentByPaymentId(String paymentId);
}
