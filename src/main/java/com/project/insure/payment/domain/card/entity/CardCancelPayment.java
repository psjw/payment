package com.project.insure.payment.domain.card.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CardCancelPayment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(length = 20, nullable = false, unique = true)
    private String cancelPaymentId;

    @Column(length = 20, nullable = false, unique = true)
    private String paymentId;

    @Column(length = 450, nullable = false)
    private String dataBody;


}
