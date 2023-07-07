package com.project.insure.payment.domain.card.service;

import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HanaCardPaymentReadServiceImplTest {
    private HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;

    private final CardPaymentRepository cardPaymentRepository = mock(CardPaymentRepository.class);
    private final String paymentId = "P1234567891234567890";
    private final String notFoundPaymentId = "P9876543219876543210";

    private final String resultDataBody = "_446PAYMENT___XXXXXXXXXXXXXXXXXXXX1234567890123456____001125777____11000000\n" +
            "00010000____________________YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
            "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY________________________\n" +
            "____________________________________________________________________________\n" +
            "____________________________________________________________________________\n" +
            "_______________________________________________________________________";

    @BeforeEach
    void setUp(){
        CardPaymentMapper cardPaymentMapper = Mappers.getMapper(CardPaymentMapper.class);
        hanaCardPaymentReadService = new HanaCardPaymentReadServiceImpl(cardPaymentMapper, cardPaymentRepository);
    }

    @Test
    @DisplayName("결제번호가 존재한다면 카드결제 정보를 반환합니다.")
    void findCardCancelPaymentByPaymentIdTest(){

        //given
        CardPayment resultCardPayment = CardPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build();
        given(cardPaymentRepository.findCardPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardPayment));
        //when
        Optional<CardPayment> cardPayment = cardPaymentRepository.findCardPaymentByPaymentId(paymentId);
        //then
        assertThat(cardPayment.get().getPaymentId()).isEqualTo(paymentId);
    }


    @Test
    @DisplayName("결제번호가 존재하지 않으면 빈 Optional 값을 반환합니다.")
    void findNotFoundCardCancelPaymentByPaymentIdTest(){
        //given
        given(cardPaymentRepository.findCardPaymentByPaymentId(notFoundPaymentId)).willReturn(Optional.empty());
        //when
        Optional<CardPayment> cardCancelPayment = cardPaymentRepository.findCardPaymentByPaymentId(notFoundPaymentId);
        //then
        assertThat(cardCancelPayment).isEmpty();

    }
}