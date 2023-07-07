package com.project.insure.payment.application.usecase;

import com.project.insure.exception.payment.DuplicatePaymentException;
import com.project.insure.exception.payment.ImpossibleCancelPaymentException;
import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.code.DataType;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentWriteServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentReadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mapstruct.factory.Mappers;


import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class HanaCardCancelPaymentUsecaseTest {
    private HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private HanaCardCancelPaymentReadServiceImpl hanaCardCancelPaymentReadService;
    private HanaCardCancelPaymentWriteServiceImpl hanaCardCancelPaymentWriteService;

    private HanaCardCancelPaymentUsecase hanaCardCancelPaymentUsecase;

    private PaymentDuplicateService paymentDuplicateService;


    private final CardPaymentRepository cardPaymentRepository = mock(CardPaymentRepository.class);
    private final CardCancelPaymentRepository cardCancelPaymentRepository = mock(CardCancelPaymentRepository.class);
    private final DataType dataType = DataType.취소;
    private final Long amount = 110000L;
    private final Long minAmount = 20000000L;
    private final String paymentId = "P1234567891234567890";
    private final String cancelPaymentId = "C1234567891234567890";
    private final String notFoundPaymentId = "P9876543219876543210";

    private final String resultCancelDataBody = " 446CANCEL    C12345678912345678901234567890123456    001125777    11000000\n" +
            "00010000P1234567891234567890YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
            "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY                        \n" +
            "                                                                            \n" +
            "                                                                            \n" +
            "                                                                       ";

    private final String resultDataBody = " 446PAYMENT   P12345678912345678901234567890123456    001125777    11000000\n" +
            "00010000                    YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\n" +
            "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY                        \n" +
            "                                                                            \n" +
            "                                                                            \n" +
            "                                                                       ";
    @BeforeEach
    void setUp() {
        CardPaymentMapper cardPaymentMapper = Mappers.getMapper(CardPaymentMapper.class);
        CardCancelPaymentMapper cardCancelPaymentMapper = Mappers.getMapper(CardCancelPaymentMapper.class);
        paymentDuplicateService = new PaymentDuplicateService();
        hanaCardCancelPaymentWriteService = new HanaCardCancelPaymentWriteServiceImpl(cardCancelPaymentMapper, cardCancelPaymentRepository);
        hanaCardPaymentReadService = new HanaCardPaymentReadServiceImpl(cardPaymentMapper, cardPaymentRepository);
        hanaCardCancelPaymentReadService = new HanaCardCancelPaymentReadServiceImpl(cardCancelPaymentMapper, cardCancelPaymentRepository);
        hanaCardCancelPaymentUsecase = new HanaCardCancelPaymentUsecase(hanaCardCancelPaymentReadService, hanaCardPaymentReadService, hanaCardCancelPaymentWriteService, paymentDuplicateService);
    }


    @Test
    @DisplayName("카드결제 취소를 요청하면 취소된다.")
    public void cancelPayment() {
        //given
        CardCancelPaymentRequestDto requestDto = CardCancelPaymentRequestDto.builder().amount(amount).build();
        CardPayment resultCardPayment = CardPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build();

        CardCancelPayment resultCardCancelPayment = CardCancelPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultCancelDataBody)
                .build();

        given(cardPaymentRepository.findCardPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardPayment));
        given(cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardCancelPayment));
        given(cardCancelPaymentRepository.save(any(CardCancelPayment.class))).will(invocation -> {
            CardCancelPayment source = invocation.getArgument(0);
            return CardCancelPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });

        //when
        CardCancelPaymentResponseDto cardCancelPaymentResponseDto = hanaCardCancelPaymentUsecase.cancelPayment(requestDto, paymentId, dataType.getCode());
        //then
        assertThat(cardCancelPaymentResponseDto.getPaymentId()).isEqualTo(paymentId);

    }



    @Test
    @DisplayName("이미 결제가 취소된 경우에 카드결제 취소를 요청하면 PaymentNotFoundException을 발생시킨다.")
    public void cancelPaymentFail() {
        //given
        CardCancelPaymentRequestDto requestDto = CardCancelPaymentRequestDto.builder().amount(amount).build();
        CardPayment resultCardPayment = CardPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build();

        CardCancelPayment resultCardCancelPayment = CardCancelPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultCancelDataBody)
                .build();

        given(cardPaymentRepository.findCardPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardPayment));
        given(cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardCancelPayment));
        given(cardCancelPaymentRepository.save(any(CardCancelPayment.class))).will(invocation -> {
            CardCancelPayment source = invocation.getArgument(0);
            return CardCancelPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });

        //when
        //then
        assertThatThrownBy(() -> {
            hanaCardCancelPaymentUsecase.cancelPayment(requestDto, notFoundPaymentId, dataType.getCode());
        }).isInstanceOf(PaymentNotFoundException.class);
    }



    @Test
    @DisplayName("결제액이 결제 취소액보다 작으면 ImpossibleCancelPaymentException을 발생시킨다.")
    public void cancelPaymentImpossible() {
        //given
        CardCancelPaymentRequestDto requestDto = CardCancelPaymentRequestDto.builder().amount(minAmount).build();
        CardPayment resultCardPayment = CardPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build();

        CardCancelPayment resultCardCancelPayment = CardCancelPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultCancelDataBody)
                .build();

        given(cardPaymentRepository.findCardPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardPayment));
        given(cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardCancelPayment));
        given(cardCancelPaymentRepository.save(any(CardCancelPayment.class))).will(invocation -> {
            CardCancelPayment source = invocation.getArgument(0);
            return CardCancelPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });

        //when
        //then
        assertThatThrownBy(() -> {
            hanaCardCancelPaymentUsecase.cancelPayment(requestDto, paymentId, dataType.getCode());
        }).isInstanceOf(ImpossibleCancelPaymentException.class);
    }




    @Test
    @DisplayName("이미 결제취소가 된 경우에 카드결제 요청하면 DuplicatePaymentException을 발생시킨다.")
    public void paymentFail() throws InterruptedException {
        //given
        CardCancelPaymentRequestDto requestDto = CardCancelPaymentRequestDto.builder().amount(amount).build();
        CardPayment resultCardPayment = CardPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultDataBody)
                .build();

        CardCancelPayment resultCardCancelPayment = CardCancelPayment.builder()
                .id(1L)
                .paymentId(paymentId)
                .dataBody(resultCancelDataBody)
                .build();

        given(cardPaymentRepository.findCardPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardPayment));
        given(cardCancelPaymentRepository.findCardCancelPaymentByPaymentId(paymentId)).willReturn(Optional.of(resultCardCancelPayment));
        given(cardCancelPaymentRepository.save(any(CardCancelPayment.class))).will(invocation -> {
            CardCancelPayment source = invocation.getArgument(0);
            return CardCancelPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });
        //when

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        hanaCardCancelPaymentUsecase.cancelPayment(requestDto, paymentId, dataType.getCode());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(int i =0 ; i < threadCount ;i++){
            executorService.submit(()->{
                try{
                    hanaCardCancelPaymentUsecase.cancelPayment(requestDto, paymentId, dataType.getCode());
                }catch (DuplicatePaymentException e){
                    System.out.println("DuplicatePaymentException 발생");
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }
}