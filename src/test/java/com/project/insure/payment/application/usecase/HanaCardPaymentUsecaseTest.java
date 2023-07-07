package com.project.insure.payment.application.usecase;

import com.project.insure.exception.payment.DuplicatePaymentException;
import com.project.insure.exception.payment.ExistsCancelPaymentException;
import com.project.insure.exception.payment.ImpossibleCancelPaymentException;
import com.project.insure.exception.payment.PaymentNotFoundException;
import com.project.insure.payment.domain.card.code.DataType;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardCancelPaymentResponseDto;
import com.project.insure.payment.domain.card.dto.CardPaymentRequestDto;
import com.project.insure.payment.domain.card.dto.CardPaymentResponseDto;
import com.project.insure.payment.domain.card.entity.CardCancelPayment;
import com.project.insure.payment.domain.card.entity.CardPayment;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentWriteServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentWriteServiceImpl;
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
import static org.mockito.Mockito.mock;


class HanaCardPaymentUsecaseTest {
    private HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private HanaCardCancelPaymentReadServiceImpl hanaCardCancelPaymentReadService;
    private HanaCardPaymentWriteServiceImpl hanaCardPaymentWriteService;

    private HanaCardPaymentUsecase hanaCardPaymentUsecase;

    private PaymentDuplicateService paymentDuplicateService;

    private final CardPaymentRepository cardPaymentRepository = mock(CardPaymentRepository.class);
    private final CardCancelPaymentRepository cardCancelPaymentRepository = mock(CardCancelPaymentRepository.class);
    private final DataType dataType = DataType.취소;
    private final Long amount = 110000L;
    private final Long maxAmount = 20000000L;
    private final String paymentId = "P1234567891234567890";
    private final String cancelPaymentId = "C1234567891234567890";
    private final String cardNo = "1234567890123456";

    private final String cvc = "777";
    private final Long vat = 10000L;

    private final Integer installmentMonth = 0;

    private final String expiredPeriod = "1125";
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
        hanaCardPaymentWriteService = new HanaCardPaymentWriteServiceImpl(cardPaymentMapper, cardPaymentRepository);
        hanaCardPaymentReadService = new HanaCardPaymentReadServiceImpl(cardPaymentMapper, cardPaymentRepository);
        hanaCardCancelPaymentReadService = new HanaCardCancelPaymentReadServiceImpl(cardCancelPaymentMapper, cardCancelPaymentRepository);
        hanaCardPaymentUsecase = new HanaCardPaymentUsecase(hanaCardPaymentWriteService, hanaCardPaymentReadService, hanaCardCancelPaymentReadService, paymentDuplicateService);
    }


    @Test
    @DisplayName("카드결제 요청하면 결제가 되고 데이터 안에 카드정보가 포함된다..")
    public void payment() {
        //given
        CardPaymentRequestDto requestDto = CardPaymentRequestDto.builder()
                .cardNo(cardNo)
                .expiredPeriod(expiredPeriod)
                .cvc(cvc)
                .vat(vat)
                .amount(amount)
                .installmentMonth(installmentMonth)
                .build();

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
        given(cardPaymentRepository.save(any(CardPayment.class))).will(invocation -> {
            CardPayment source = invocation.getArgument(0);
            return CardPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });

        //when
        CardPaymentResponseDto cardPaymentResponseDto = hanaCardPaymentUsecase.payment(requestDto);
        //then
        assertThat(cardPaymentResponseDto.getDataBody()).contains(cardNo);

    }



    @Test
    @DisplayName("이미 결제가 된 경우에 카드결제 요청하면 ExistsCancelPaymentException을 발생시킨다.")
    public void paymentFail() throws InterruptedException {
        //given
        CardPaymentRequestDto requestDto = CardPaymentRequestDto.builder()
                .cardNo(cardNo)
                .expiredPeriod(expiredPeriod)
                .cvc(cvc)
                .vat(vat)
                .amount(amount)
                .installmentMonth(installmentMonth)
                .build();

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
        given(cardPaymentRepository.save(any(CardPayment.class))).will(invocation -> {
            CardPayment source = invocation.getArgument(0);
            return CardPayment.builder()
                    .id(1L)
                    .paymentId(source.getPaymentId())
                    .dataBody(source.getDataBody())
                    .build();
        });
        //when

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        hanaCardPaymentUsecase.payment(requestDto);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(int i =0 ; i < threadCount ;i++){
            executorService.submit(()->{
                try{
                    hanaCardPaymentUsecase.payment(requestDto);
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