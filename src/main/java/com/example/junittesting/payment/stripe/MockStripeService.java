package com.example.junittesting.payment.stripe;


import com.example.junittesting.payment.CardPaymentCharge;
import com.example.junittesting.payment.CardPaymentCharger;
import com.example.junittesting.payment.Currency;
import com.stripe.exception.StripeException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(value = "stripe.enabled",havingValue = "false")
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        return new CardPaymentCharge(true); //HAPPY PATH
    }
}
