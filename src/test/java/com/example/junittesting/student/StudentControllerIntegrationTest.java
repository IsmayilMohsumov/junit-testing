package com.example.junittesting.student;

import com.example.junittesting.payment.Payment;
import com.example.junittesting.payment.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void itShouldGetString() throws Exception {
        //Given

        //When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/student-controller/test")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")));

        //Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void itShouldSaveStudent() throws Exception {
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

        //Then
        studentRegResultActions.andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/student-controller/index/{indexNumber}",indexNumber)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.indexNumber").value(indexNumber));

    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}