package com.example.junittesting;

import com.example.junittesting.student.Student;
import com.example.junittesting.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class JunitTestingApplication implements CommandLineRunner {

    private final StudentRepository studentRepository;
    public static void main(String[] args) {
        SpringApplication.run(JunitTestingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        studentRepository.save(new Student(UUID.randomUUID(), "Ismayil","Moshumov","36467"));
    }
}
