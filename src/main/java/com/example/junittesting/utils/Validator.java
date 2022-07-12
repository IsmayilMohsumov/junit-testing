package com.example.junittesting.utils;

import java.util.function.Predicate;

public interface Validator extends Predicate<String> {

    @Override
    boolean test(String s);
}
