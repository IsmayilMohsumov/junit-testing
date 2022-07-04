package com.example.junittesting.student;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties ="spring.jpa.properties.javax.persistence.validation.mode=none")
class StudentRepositoryTest {
    @Autowired
    private StudentRepository underTest;

    @Test
    void itShouldSelectStudentByIndexNumber() {
        //Given
        UUID id = UUID.randomUUID();
        String indexNumber = "36467";
        Student studentIsmayil = new Student(id,"Ismayil","Mohsumov",indexNumber);
        //When

        underTest.save(studentIsmayil);
        //Then

        Optional<Student> optionalStudent = underTest.selectStudentByIndexNumber(indexNumber);
        assertThat(optionalStudent)
                .isPresent()
                .hasValueSatisfying(s ->{
                   assertThat(s).isEqualToComparingFieldByField(studentIsmayil);
                });

    }

    @Test
    void itNotShouldSelectStudentByIndexNumber() {
        //Given
        String indexNumber = "36460";
        //When

        Optional<Student> optionalStudent = underTest.selectStudentByIndexNumber(indexNumber);
        //Then

        assertThat(optionalStudent).isNotPresent();
    }
}