package com.example.junittesting.payment;

import com.example.junittesting.student.Student;
import com.example.junittesting.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    private PaymentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new PaymentService(paymentRepository,studentRepository,cardPaymentCharger);
    }


    @Test
    void itShouldChargeCardSuccessfully() {
        //Given

        // ... Student will be returned
        UUID studentId = UUID.randomUUID();
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
}