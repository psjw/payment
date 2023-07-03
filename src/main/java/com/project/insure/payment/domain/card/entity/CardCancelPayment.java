package com.project.insure.payment.domain.card.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CardCancelPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    private String cardNum; //카드번호


    private String cvc; //cvc

//    private String //유효기간
    private String installmentMonth; //할부개월 수


}
