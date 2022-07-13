package com.example.junittesting.payment;

import com.example.junittesting.student.Student;
import com.example.junittesting.student.StudentRepository;
import com.example.junittesting.twilio.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


class PaymentServiceTest {
    @Mock
    private  PaymentRepository paymentRepository;
    @Mock
    private  StudentRepository studentRepository;
    @Mock
    private  CardPaymentCharger cardPaymentCharger;

    @Mock
    private SmsService smsService;

    private PaymentService underTest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new PaymentService(paymentRepository,studentRepository,cardPaymentCharger,smsService);
    }

    @Test
    void itShouldChargeCardSuccessfully() {
        //Given
        UUID studentId = UUID.randomUUID();

        // ... Student will be returned
        given(studentRepository.findById(studentId)).willReturn(Optional.of(mock(Student.class)));

        // ... Currency will be returned
        Currency currency = Currency.USD;
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                null,
                null,
                new BigDecimal("100.00"),
                currency,
                "VISA",
                "Donation"
                ));

        // ... Card is charged
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(true));


        //When
        underTest.chargeCard(studentId, paymentRequest);

        //Then
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();

        assertThat(paymentArgumentCaptorValue).isEqualTo(paymentRequest.getPayment());
        assertThat(paymentArgumentCaptorValue.getStudentId()).isEqualTo(studentId);


    }

    @Test
    void itShouldThrowWhenCardIsNotCharged() {
        //Given
        UUID studentId = UUID.randomUUID();

        // ... Student will be returned
        given(studentRepository.findById(studentId)).willReturn(Optional.of(mock(Student.class)));

        // ... Currency is USD which is supported
        Currency currency = Currency.USD;

        // ... Payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        currency,
                        "VISA",
                        "Donation"
                ));

        // ... Card is not charged successfully
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(false));

        //When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(studentId,paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Card not debited for student "+ studentId);

        // ... No interaction with paymentRepository
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeCardAndThrowWhenCurrencyNotSupported() {
        // Given
        UUID studentId = UUID.randomUUID();

        // ... Customer exists
        given(studentRepository.findById(studentId)).willReturn(Optional.of(mock(Student.class)));

        // ... Not supported currency which is AZN
        Currency currency = Currency.AZN;

        // ... Payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        currency,
                        "card123xx",
                        "Donation"
                )
        );

        //When

        assertThatThrownBy(() -> underTest.chargeCard(studentId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Currency [%s] not supported",
                        paymentRequest.getPayment().getCurrency()));

        // Then

        // ... No interaction with cardPaymentCharger
        then(cardPaymentCharger).shouldHaveNoInteractions();

        // ... No interaction with paymentRepository
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeAndThrowWhenCustomerNotFound() {
        // Given
        UUID studentId = UUID.randomUUID();

        // ... Customer not found in Database
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(studentId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Student with id [%s] not found", studentId));

        // ... No interaction with cardPaymentCharger
        then(cardPaymentCharger).shouldHaveNoInteractions();

        // ... No interaction with paymentRepository
        then(paymentRepository).shouldHaveNoInteractions();
    }


}