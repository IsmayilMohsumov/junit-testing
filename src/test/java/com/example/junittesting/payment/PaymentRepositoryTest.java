package com.example.junittesting.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties ="spring.jpa.properties.javax.persistence.validation.mode=none")
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository underTest;

    @Test
    void itShouldInsertPayment() {
        //Given
        Long id = 1L;
        UUID userId= UUID.randomUUID();
        Payment payment = new Payment(id,userId,new BigDecimal("10.00"), Currency.USD, "VISA","Donation");

        //When
        underTest.save(payment);

        //Then
        Optional<Payment> optionalPayment = underTest.findById(id);
        assertThat(optionalPayment).isEqualTo(Optional.of(payment));
    }
}