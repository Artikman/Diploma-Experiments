package com.example.experiments.validation;

public interface Validator<T> {

    boolean isValid(T value);

    String getDescription();

}