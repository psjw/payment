package com.project.insure.payment.domain.card.service;

import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HanaCardCancelPaymentWriteServiceImplTest {
    private HanaCardCancelPaymentWriteServiceImpl hanaCardCancelPaymentWriteService;

    private final CardCancelPaymentRepository cardCancelPaymentRepository = mock(CardCancelPaymentRepository.class);
    private final String paymentId = "P1234567891234567890";
    private final String cancelPaymentId = "C1234567891234567890";
    private final String notFoundPaymentId = "P9876543219876543210";

    private final String resultCancelDataBody = "_446CANCEL____ZZZZZZZZZZZZZZZZZZZZ1234567890123456____001125777____11000000\n" +
            "00010000XXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
            "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY________________________\n" +
            "____________________________________________________________________________\n" +
            "____________________________________________________________________________\n" +
            "_______________________________________________________________________";

    private final String resultDataBody = "_446PAYMENT___XXXXXXXXXXXXXXXXXXXX1234567890123456____001125777____11000000\n" +
            "00010000____________________YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
            "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY________________________\n" +
            "____________________________________________________________________________\n" +
            "____________________________________________________________________________\n" +
            "_______________________________________________________________________";

    @BeforeEach
    void setUp() {
        CardCancelPaymentMapper cardPaymentMapper = Mappers.getMapper(CardCancelPaymentMapper.class);
        hanaCardCancelPaymentWriteService = new HanaCardCancelPaymentWriteServiceImpl(cardPaymentMapper, cardCancelPaymentRepository);
    }

    @Test
    @DisplayName("결제 정보저장하면 동일한 Payment Id를 반환한다.")
    public void payment() {
        //given
        given(cardCancelPaymentRepository.save(any(CardCancelPayment.class))).will(invocation -> {
            CardCancelPayment source = invocation.getArgument(0);
            return CardCancelPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .cancelPaymentId(source.getCancelPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });
        //when
        CardCancelPayment savedCardCancelPayment = cardCancelPaymentRepository.save(CardCancelPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .cancelPaymentId(cancelPaymentId)
                .dataBody(resultDataBody)
                .build());
        //then
        assertThat(savedCardCancelPayment.getPaymentId()).isEqualTo(paymentId);
    }
}