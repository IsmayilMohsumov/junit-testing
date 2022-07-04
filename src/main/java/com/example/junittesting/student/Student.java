package com.example.junittesting.student;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    private UUID Id;

    private String name;

    private String surname;

    @NotNull
    @Column(nullable = false, unique = true)
    private String indexNumber;
}
