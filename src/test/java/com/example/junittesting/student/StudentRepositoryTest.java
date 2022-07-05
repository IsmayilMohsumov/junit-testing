package com.example.junittesting.student;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

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
    void itNotShouldSelectStudentByIndexNumberWhenIndexNumberDoesNotExists() {
        //Given
        String indexNumber = "36460";
        //When

        Optional<Student> optionalStudent = underTest.selectStudentByIndexNumber(indexNumber);
        //Then

        assertThat(optionalStudent).isNotPresent();
    }

    @Test
    void itShouldNotSaveStudentWhenNameIsNull() {
        //Given
        String indexNumber = "36467";
        UUID id = UUID.randomUUID();
        Student student = new Student(id, null, "Mohsumov", indexNumber);

        //When
        //Then
        assertThatThrownBy(() -> underTest.save(student))
                .hasMessageContaining("not-null property references a null or transient value : com.example.junittesting.student.Student.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test
    void itShouldNotSaveStudentWhenIndexNumberIsNull() {
        //Given
        String indexNumber = "36467";
        Student student = new Student(UUID.randomUUID(), "Isi", "Mohsumov", null);

        //When
        //Then
        assertThatThrownBy(() -> underTest.save(student))
                .hasMessageContaining("not-null property references a null or transient value : com.example.junittesting.student.Student.indexNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldSaveStudentWhenSurnameIsNull() {
        //Given
        String indexNumber = "36467";
        UUID id = UUID.randomUUID();
        Student student = new Student(id, "Isi", null, indexNumber);

        //When
        underTest.save(student);

        //Then
        assertThat(underTest.findById(id)).isPresent()
                .isEqualTo(Optional.of(student));
    }
    @Test
    void itShouldSaveStudent() {
        //Given
        String indexNumber = "36467";
        Student student = new Student(UUID.randomUUID(), "Ismayil", "Mohsumov", indexNumber);

        //When
        underTest.save(student);

        //Then
        Optional<Student> optionalStudent = underTest.selectStudentByIndexNumber(indexNumber);
        assertThat(optionalStudent).isPresent();


    }
}