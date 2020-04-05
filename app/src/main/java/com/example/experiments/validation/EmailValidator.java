package com.example.experiments.validation;

import android.util.Patterns;

public class EmailValidator implements Validator<String> {

    @Override
    public boolean isValid(String value) {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    @Override
    public String getDescription() {
        return "Email должен быть в \'a@a.com\' формате!";
    }
}