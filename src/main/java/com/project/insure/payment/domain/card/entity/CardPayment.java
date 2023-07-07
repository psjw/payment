package com.project.insure.payment.domain.card.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CardPayment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(length = 20, nullable = false, unique = true)
    private String paymentId;

    @Column(length = 450, nullable = false)
    private String dataBody;

}
