package com.project.insure.payment.domain.card.entity;

import com.project.insure.payment.domain.card.code.PaymentCompany;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CardPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(length = 20, nullable = false, unique = true)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentCompany paymentType;

    @Column(length = 100, nullable = false)
    private String cardNo; //카드번호

    @Column(length = 3, nullable = false)
    private String cvc; //cvc

    @Column(length = 4, nullable = false)
    private String expiredPeriod;

    @Column(length = 4, nullable = false)
    private String installmentMonth; //할부개월 수

    @Column(length = 20, nullable = true)
    private String cancelPaymentId;

    private Long amount;

    @Column(length = 450, nullable = false)
    private String encPaymentInfo;

    private Long vat;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

}
