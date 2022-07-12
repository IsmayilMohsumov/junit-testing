package com.example.junittesting.student;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/student-controller")
@RequiredArgsConstructor
public class StudentController {


    private final StudentService studentService;
    @PutMapping
    public void registerNewStudent(@RequestBody StudentRegistrationRequest request) {
        studentService.registerNewStudent(request);
    }

    @GetMapping
    public List<Student> getAll(){
        return studentService.getAll();
    }

    @GetMapping("/test")
    public String getString(){
        return "Hello";
    }
}
