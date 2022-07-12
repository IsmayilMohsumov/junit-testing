package com.example.junittesting.payment;

import lombok.*;

@Data
@RequiredArgsConstructor
@ToString
public class CardPaymentCharge {
    private final boolean isCardDebited;
}
