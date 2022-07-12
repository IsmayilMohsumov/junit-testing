package com.example.junittesting.utils;

public class IndexNumberValidator implements Validator{
    @Override
    public boolean test(String indexNumber) {
        return indexNumber.length() == 5 && indexNumber.matches("\\d*");
    }
}
