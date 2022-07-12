package com.example.junittesting.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query("select s from Student s where s.indexNumber=?1")
    public Optional<Student> selectStudentByIndexNumber(String indexNumber);

}
