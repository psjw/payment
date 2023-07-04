package com.project.insure.payment.domain.card.repsotory;

import com.project.insure.payment.domain.card.entity.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {
}
