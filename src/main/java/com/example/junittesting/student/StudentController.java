package com.example.junittesting.student;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/student-controller")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @PostMapping
    public void registerNewStudent(@RequestBody StudentRegistrationRequest request) {
        studentService.registerNewStudent(request);
    }

    @GetMapping
    public List<Student> getAll(){
        return studentService.getAll();
    }

    @GetMapping("{id}")
    public Student getStudentById(@PathVariable("id")UUID studentId){
        return studentService.getStudentById(studentId);
    }

    @GetMapping("/index/{indexNumber}")
    public Student getStudentByIndexNumber(@PathVariable("indexNumber")String indexNumber){
        return studentService.getStudentByIndexNumber(indexNumber);
    }

    @GetMapping("/test")
    public String getString(){
        return "Hello";
    }
}
