package com.project.insure.payment.application.usecase.cardfinder;

import com.project.insure.exception.payment.NotSupportedCardCompanyException;
import com.project.insure.payment.application.usecase.CardCancelPaymentUsecase;
import com.project.insure.payment.application.usecase.CardPaymentUsecase;
import com.project.insure.payment.application.usecase.HanaCardCancelPaymentUsecase;
import com.project.insure.payment.application.usecase.HanaCardPaymentUsecase;
import com.project.insure.payment.domain.card.code.PaymentCompany;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class CardCancelPaymentServiceFinderTest {
    private CardCancelPaymentServiceFinder cardCancelPaymentServiceFinder;

    private HanaCardPaymentReadServiceImpl hanaCardPaymentReadService;
    private HanaCardCancelPaymentReadServiceImpl hanaCardCancelPaymentReadService;
    private HanaCardCancelPaymentWriteServiceImpl hanaCardCancelPaymentWriteService;

    private HanaCardCancelPaymentUsecase hanaCardCancelPaymentUsecase;

    private final CardPaymentRepository cardPaymentRepository = mock(CardPaymentRepository.class);
    private final CardCancelPaymentRepository cardCancelPaymentRepository = mock(CardCancelPaymentRepository.class);
    private List<CardCancelPaymentUsecase> cancelPaymentUsecases = new ArrayList<>();

    private PaymentCompany companyName = PaymentCompany.Hana;
    private PaymentCompany notFoundCompanyName = PaymentCompany.Samsung;

    @BeforeEach
    void setUp() {
        CardPaymentMapper cardPaymentMapper = Mappers.getMapper(CardPaymentMapper.class);
        CardCancelPaymentMapper cardCancelPaymentMapper = Mappers.getMapper(CardCancelPaymentMapper.class);
        hanaCardCancelPaymentWriteService = new HanaCardCancelPaymentWriteServiceImpl(cardCancelPaymentMapper, cardCancelPaymentRepository);
        hanaCardPaymentReadService = new HanaCardPaymentReadServiceImpl(cardPaymentMapper, cardPaymentRepository);
        hanaCardCancelPaymentReadService = new HanaCardCancelPaymentReadServiceImpl(cardCancelPaymentMapper, cardCancelPaymentRepository);
        hanaCardCancelPaymentUsecase = new HanaCardCancelPaymentUsecase(hanaCardCancelPaymentReadService, hanaCardPaymentReadService, hanaCardCancelPaymentWriteService);
        cancelPaymentUsecases.add(hanaCardCancelPaymentUsecase);
        cardCancelPaymentServiceFinder = new CardCancelPaymentServiceFinder(cancelPaymentUsecases);
    }


    @Test
    @DisplayName("요청한 카드 정보가 있다면 요청한 카드서비스 클래스를 반환한다.")
    void findCardPaymentService() {
        //given
        //when
        CardCancelPaymentUsecase cardCancelPaymentUsecase = cardCancelPaymentServiceFinder.findPaymentUsecaseByCardCompany(companyName.name());
        //then
        assertThat(cardCancelPaymentUsecase.getPaymentCompany().name()).isEqualTo(companyName.name());
    }

    @Test
    @DisplayName("요청한 카드 정보가 없다면 NotSupportedCardCompanyException을 반환한다.")
    void findNotFoundCardPaymentService() {
        //given
        //when
        //then
        assertThatThrownBy(() -> cardCancelPaymentServiceFinder.findPaymentUsecaseByCardCompany(notFoundCompanyName.name()))
                .isInstanceOf(NotSupportedCardCompanyException.class);

    }
}