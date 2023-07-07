package com.project.insure.payment.application.usecase.cardfinder;

import com.project.insure.exception.payment.NotSupportedCardCompanyException;
import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import com.project.insure.payment.application.usecase.HanaCardPaymentUsecase;
import com.project.insure.payment.domain.card.code.PaymentCompany;
import com.project.insure.payment.domain.card.mapper.CardCancelPaymentMapper;
import com.project.insure.payment.domain.card.mapper.CardPaymentMapper;
import com.project.insure.payment.domain.card.repsotory.CardCancelPaymentRepository;
import com.project.insure.payment.domain.card.repsotory.CardPaymentRepository;
import com.project.insure.payment.domain.card.service.HanaCardCancelPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentReadServiceImpl;
import com.project.insure.payment.domain.card.service.HanaCardPaymentWriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CardPaymentServiceFinderTest {

    private CardPaymentServiceFinder cardPaymentServiceFinder;

    private HanaCardPaymentWriteServiceImpl hanaCardPaymentWriteService;
    private HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private HanaCardCancelPaymentReadServiceImpl hanaCardCancelPaymentReadService;

    private HanaCardPaymentUsecase hanaCardPaymentUsecase;

    private final CardPaymentRepository cardPaymentRepository = mock(CardPaymentRepository.class);
    private final CardCancelPaymentRepository cardCancelPaymentRepository = mock(CardCancelPaymentRepository.class);
    private List<CardPaymentUsecase> paymentUsecases = new ArrayList<>();

    private PaymentCompany companyName = PaymentCompany.Hana;
    private PaymentCompany notFoundCompanyName = PaymentCompany.Samsung;

    @BeforeEach
    void setUp() {
        CardPaymentMapper cardPaymentMapper = Mappers.getMapper(CardPaymentMapper.class);
        CardCancelPaymentMapper cardCancelPaymentMapper = Mappers.getMapper(CardCancelPaymentMapper.class);
        hanaCardPaymentWriteService = new HanaCardPaymentWriteServiceImpl(cardPaymentMapper, cardPaymentRepository);
        hanaCardPaymentReadService = new HanaCardPaymentReadServiceImpl(cardPaymentMapper, cardPaymentRepository);
        hanaCardCancelPaymentReadService = new HanaCardCancelPaymentReadServiceImpl(cardCancelPaymentMapper, cardCancelPaymentRepository);
        hanaCardPaymentUsecase = new HanaCardPaymentUsecase(hanaCardPaymentWriteService, hanaCardPaymentReadService, hanaCardCancelPaymentReadService);
        paymentUsecases.add(hanaCardPaymentUsecase);
        cardPaymentServiceFinder = new CardPaymentServiceFinder(paymentUsecases);
    }


    @Test
    @DisplayName("요청한 카드 정보가 있다면 요청한 카드서비스 클래스를 반환한다.")
    void findCardPaymentService() {
        //given
        //when
        CardPaymentUsecase paymentUsecaseByCardCompany = cardPaymentServiceFinder.findPaymentUsecaseByCardCompany(companyName.name());
        //then
        assertThat(paymentUsecaseByCardCompany.getPaymentCompany().name()).isEqualTo(companyName.name());
    }

    @Test
    @DisplayName("요청한 카드 정보가 없다면 NotSupportedCardCompanyException을 반환한다.")
    void findNotFoundCardPaymentService() {
        //given
        //when
        //then
        assertThatThrownBy(() -> cardPaymentServiceFinder.findPaymentUsecaseByCardCompany(notFoundCompanyName.name()))
                .isInstanceOf(NotSupportedCardCompanyException.class);

    }
}