package com.example.junittesting.student;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student-controller")
public class StudentController {

    @PutMapping
    public void registerNewStudent(
            @RequestBody StudentRegistrationRequest request) {
    }
}
