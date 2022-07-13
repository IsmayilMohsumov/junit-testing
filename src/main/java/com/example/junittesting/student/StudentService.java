package com.example.junittesting.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    public void  registerNewStudent(StudentRegistrationRequest request){
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

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(()-> new IllegalStateException(String.format("There is no student with this id [%s]" , studentId)));
    }

    public Student getStudentByIndexNumber(String indexNumber) {
        return studentRepository.selectStudentByIndexNumber(indexNumber)
                .orElseThrow(()-> new IllegalStateException(String.format("There is no student with this indexNumber [%s]" , indexNumber)));
    }
}
