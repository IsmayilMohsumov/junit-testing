package com.example.junittesting.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Payment {
    @Id
    @GeneratedValue
    private Long paymentId;

    private UUID studentId;

    private BigDecimal amount;

    private Currency currency;

    private String source;

    private String description;
}
