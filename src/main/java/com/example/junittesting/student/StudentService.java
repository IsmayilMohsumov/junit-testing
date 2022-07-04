package com.example.junittesting.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    public void registerNewStudent(StudentRegistrationRequest request){
        String indexNumber = request.getStudent().getIndexNumber();
        Optional<Student> optionalStudent = studentRepository.selectStudentByIndexNumber(indexNumber);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getName().equals(request.getStudent().getName())) {
                return;
            }
            throw new IllegalStateException(String.format("This index number [%s] is taken", indexNumber));
        }

        if(request.getStudent().getId() == null) {
            request.getStudent().setId(UUID.randomUUID());
        }

        studentRepository.save(request.getStudent());
    }
}
