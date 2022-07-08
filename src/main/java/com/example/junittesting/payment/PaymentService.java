package com.example.junittesting.payment;

import com.example.junittesting.student.Student;
import com.example.junittesting.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD, Currency.GPB);

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    void chargeCard(UUID studentId, PaymentRequest paymentRequest){
        boolean isStudentFound = studentRepository.findById(studentId).isPresent();
        if (!isStudentFound){
            throw new IllegalStateException(String.format("Student with id [%s] not found", studentId));
        }

        // 2. Do we support the currency if not throw
        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(paymentRequest.getPayment().getCurrency());

        if (!isCurrencySupported) {
            String message = String.format("Currency [%s] not supported",
                    paymentRequest.getPayment().getCurrency());
            throw new IllegalStateException(message);
        }

        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );

        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format("Card not debited for student %s", studentId));
        }

        // 5. Insert payment
        paymentRequest.getPayment().setStudentId(studentId);

        paymentRepository.save(paymentRequest.getPayment());

        // 6. TODO: send sms


    }
}
