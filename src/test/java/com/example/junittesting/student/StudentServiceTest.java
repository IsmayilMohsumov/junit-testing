package com.example.junittesting.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

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

    @Test
    void itShouldNotSaveStudentWhenIndexNumberTaken() {
        //Given
        String indexNumber = "36467";
        UUID id = UUID.randomUUID();

        Student student = new Student(id, "Ismayil", "Mohsumov", indexNumber);

        StudentRegistrationRequest request = new StudentRegistrationRequest(student);

        //given() - force to not return value
        given(studentRepository.selectStudentByIndexNumber(indexNumber)).willReturn(Optional.of(student));

        //When
        underTest.registerNewStudent(request);

        //Then
        then(studentRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowAnExceptionWhenIndexNumberTaken() {
        //Given
        String indexNumber = "36467";
        UUID id = UUID.randomUUID();

        Student student = new Student(id, "Ismayil", "Mohsumov", indexNumber);
        Student student2 = new Student(id, "Sue", "Mathilde", indexNumber);

        StudentRegistrationRequest request = new StudentRegistrationRequest(student);

        //given() - force to not return value
        given(studentRepository.selectStudentByIndexNumber(indexNumber)).willReturn(Optional.of(student2));

        //When
        //Then
        assertThatThrownBy(() -> underTest.registerNewStudent(request))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining(String.format("This index number [%s] is taken", indexNumber));

        //Finally
        then(studentRepository).should(never()).save(any(Student.class));
    }
}