package com.example.junittesting.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class IndexNumberValidatorTest {

    private IndexNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new IndexNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "36467,true",
            "000000, false",
            "abcsde, false"
    })
    void itShouldValidateIndexNumber(String indexNumber, boolean expected) {
        //Given
//        String indexNumber = "36467";

        //When
        boolean isValid = underTest.test(indexNumber);

        //Then
        assertThat(isValid).isEqualTo(expected);
    }
}
