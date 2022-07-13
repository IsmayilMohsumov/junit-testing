package com.example.junittesting.payment;

import com.example.junittesting.student.Student;
import com.example.junittesting.student.StudentRegistrationRequest;
import com.example.junittesting.student.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

//    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        //Given
        UUID studentId = UUID.randomUUID();
        String indexNumber = "36467";
        Student student = new Student(studentId,"Ismayil","Mohsumova",indexNumber);

        StudentRegistrationRequest studentRegistrationRequest = new StudentRegistrationRequest(student);

        //When
        String API_PATH = "/api/v1/student-controller";
        ResultActions studentRegResultActions = mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(studentRegistrationRequest)))
        );

        // ... Payment
        long paymentId = 1L;

        Payment payment = new Payment(
                paymentId,
                studentId,
                new BigDecimal("100.00"),
                Currency.GPB,
                "x0x0x0x0",
                "Zakat"
        );

        // ... Payment request
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        // ... When payment is sent
        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));

        // Then both customer registration and payment requests are 200 status code
        studentRegResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(status().isOk());

        // Payment is stored in db
        // TODO: Do not use paymentRepository instead create an endpoint to retrieve payments for customers
        assertThat(paymentRepository.findById(paymentId))
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p).isEqualToComparingFieldByField(payment));

        // TODO: Ensure sms is delivered

    }

//    @Test
//    void itShouldInsertValueStudentAPI() throws Exception {
//        //Given
//        UUID studentId = UUID.randomUUID();
//        String indexNumber = "36467";
//        Student student = new Student(studentId,"Ismayil","Mohsumova",indexNumber);
//
//        //When
////        StudentRegistrationRequest studentRegistrationRequest = new StudentRegistrationRequest(student);
////
////        // ... Send request to controller
////        ResultActions studentRegResultActions = mockMvc.perform(put(API_PATH)
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(Objects.requireNonNull(objectToJson(studentRegistrationRequest)))
////        );
//
//        studentRepository.save(student);
//
//        //Then
////        studentRegResultActions.andExpect(status().isOk());
//        assertThat(studentRepository.selectStudentByIndexNumber(indexNumber))
//                .isPresent()
//                .hasValueSatisfying(p -> assertThat(p).isEqualToComparingFieldByField(student));
//    }
//
//    @Test
//    void itShouldInsertValuePaymentAPI() throws Exception {
//        //Given
//
//        UUID studentId = UUID.randomUUID();
//        String indexNumber = "36467";
//        Student student = new Student(studentId,"Ismayil","Mohsumova",indexNumber);
//
//        //When
//        StudentRegistrationRequest studentRegistrationRequest = new StudentRegistrationRequest(student);
//
//        // ... Send request to controller
//        ResultActions studentRegResultActions = mockMvc.perform(put(API_PATH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Objects.requireNonNull(objectToJson(studentRegistrationRequest)))
//        );
//        //When
//
//        long paymentId = 1L;
//
//        Payment payment = new Payment(
//                paymentId,
//                studentId,
//                new BigDecimal("100.00"),
//                Currency.GPB,
//                "x0x0x0x0",
//                "Zakat"
//        );
//
//        //Then
//
//
//        // ... Payment request
//        PaymentRequest paymentRequest = new PaymentRequest(payment);
//
//        // ... When payment is sent
//        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Objects.requireNonNull(objectToJson(paymentRequest))));
//
//        // Then both customer registration and payment requests are 200 status code
//        paymentResultActions.andExpect(status().isOk());
//
//        // Payment is stored in db
//        // TODO: Do not use paymentRepository instead create an endpoint to retrieve payments for customers
//        assertThat(paymentRepository.findById(paymentId))
//                .isPresent()
//                .hasValueSatisfying(p -> assertThat(p).isEqualToComparingFieldByField(payment));
//    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
