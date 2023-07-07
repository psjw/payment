package com.project.insure.payment.domain.card.service;

import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HanaCardPaymentWriteServiceImplTest {
    private HanaCardPaymentWriteServiceImpl hanaCardPaymentWriteService;

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
    void setUp() {
        CardPaymentMapper cardPaymentMapper = Mappers.getMapper(CardPaymentMapper.class);
        hanaCardPaymentWriteService = new HanaCardPaymentWriteServiceImpl(cardPaymentMapper, cardPaymentRepository);
    }

    @Test
    @DisplayName("결제 정보저장하면 동일한 Payment Id를 반환한다.")
    public void payment() {
        //given
        given(cardPaymentRepository.save(any(CardPayment.class))).will(invocation -> {
            CardPayment source = invocation.getArgument(0);
            return CardPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });

        //when
        CardPayment savedCardPayment = cardPaymentRepository.save(CardPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build());
        //then
        assertThat(savedCardPayment.getPaymentId()).isEqualTo(paymentId);
    }

}