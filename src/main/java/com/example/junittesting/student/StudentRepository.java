package com.example.junittesting.student;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends CrudRepository<Student, UUID> {

    @Query("select s from Student s where s.indexNumber=?1")
    public Optional<Student> selectStudentByIndexNumber(String indexNumber);

}
