package com.project.insure.payment.domain.card.service;

import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import static org.assertj.core.api.Assertions.assertThat;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HanaCardCancelPaymentReadServiceImplTest {
    private HanaCardCancelPaymentReadServiceImpl hanaCardCancelPaymentReadService;

    private final CardCancelPaymentRepository cardCancelPaymentRepository = mock(CardCancelPaymentRepository.class);
    private final String paymentId = "P1234567891234567890";
    private final String notFoundPaymentId = "P9876543219876543210";

    private final String resultDataBody = "_446CANCEL____ZZZZZZZZZZZZZZZZZZZZ1234567890123456____001125777____11000000\n" +
            "00010000XXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
            "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY________________________\n" +
            "____________________________________________________________________________\n" +
            "____________________________________________________________________________\n" +
            "_______________________________________________________________________";

    @BeforeEach
    void setUp(){
        CardCancelPaymentMapper cardPaymentMapper = Mappers.getMapper(CardCancelPaymentMapper.class);
        hanaCardCancelPaymentReadService = new HanaCardCancelPaymentReadServiceImpl(cardPaymentMapper, cardCancelPaymentRepository);
    }

    @Test
    @DisplayName("결제번호가 존재한다면 카드취소 정보를 반환합니다.")
    void findCardCancelPaymentByPaymentIdTest(){

        //given
        CardCancelPayment resultCardCancelPayment = CardCancelPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build();
        given(cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardCancelPayment));
        //when
        Optional<CardCancelPayment> cardCancelPayment = cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(paymentId);
        //then
        assertThat(cardCancelPayment.get().getPaymentId()).isEqualTo(paymentId);
    }


    @Test
    @DisplayName("결제번호가 존재하지 않으면 빈 Optional 값을 반환합니다.")
    void findNotFoundCardCancelPaymentByPaymentIdTest(){
        //given
        given(cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(notFoundPaymentId)).willReturn(Optional.empty());
        //when
        Optional<CardCancelPayment> cardCancelPayment = cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(notFoundPaymentId);
        //then
        assertThat(cardCancelPayment).isEmpty();

    }
}