package com.project.insure.payment.domain.card.entity;

import com.project.insure.payment.domain.card.code.PaymentCompany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardPayment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(length = 20, nullable = false, unique = true)
    private String paymentId;


    @Column(length = 450, nullable = false)
    private String dataBody;


}
