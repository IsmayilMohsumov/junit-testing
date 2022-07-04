package com.example.junittesting.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.assertj.core.api.Assertions.assertThat;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new StudentService(studentRepository);
    }

    @Test
    void itShouldSaveNewStudent() {
        //Given
        String indexNumber = "36467";
        Student student = new Student(UUID.randomUUID(), "Ismayil", "Mohsumov", indexNumber);

        StudentRegistrationRequest request = new StudentRegistrationRequest(student);

        //given() - force to not return value
        given(studentRepository.selectStudentByIndexNumber(indexNumber)).willReturn(Optional.empty());

        //When
        underTest.registerNewStudent(request);

        //Then
        then(studentRepository).should().save(studentArgumentCaptor.capture());
        Student studentArgumentCaptorValue = studentArgumentCaptor.getValue();
        assertThat(studentArgumentCaptorValue).isEqualTo(student);
    }
}