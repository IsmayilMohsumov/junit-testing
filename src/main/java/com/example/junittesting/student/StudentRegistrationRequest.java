package com.example.junittesting.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentRegistrationRequest {

    private final Student student;


    public StudentRegistrationRequest(@JsonProperty("student") Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public String toString() {
        return "StudentRegistrationRequest{" +
                "student=" + student +
                '}';
    }
}
