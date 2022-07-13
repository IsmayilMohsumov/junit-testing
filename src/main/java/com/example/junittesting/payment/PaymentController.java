package com.example.junittesting.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public void makePayment(@RequestBody PaymentRequest paymentRequest){
        paymentService.chargeCard(paymentRequest.getPayment().getStudentId(), paymentRequest);
    }
}
