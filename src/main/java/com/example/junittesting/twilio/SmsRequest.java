package com.example.junittesting.twilio;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class SmsRequest {

    private final String phoneNumber; // destination

    private final String message;

}
