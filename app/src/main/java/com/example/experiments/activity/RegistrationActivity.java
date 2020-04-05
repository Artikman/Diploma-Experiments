package com.example.experiments.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.experiments.R;
import com.example.experiments.database.DatabaseHelper;
import com.example.experiments.validation.EmailValidator;
import com.example.experiments.validation.ValidatorsComposer;
import com.google.android.material.snackbar.Snackbar;

public class RegistrationActivity extends AppCompatActivity {

    DatabaseHelper db;

    EditText e1, e2, e3;
    Button b1;
    TextView t1;

    final ValidatorsComposer<String> emailValidatorsComposer = new ValidatorsComposer<>(new EmailValidator());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = new DatabaseHelper(this);
        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.pass);
        e3 = findViewById(R.id.cpass);
        b1 = findViewById(R.id.register);
        t1 = findViewById(R.id.textView);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if(s1.equals("")|| s2.equals("") || s3.equals("")) {
                    Snackbar.make(v, "Поля являются пустыми!", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if(e2.getText().toString().trim().length() > 6)
                            Snackbar.make(v, "Пароль должен иметь > 6 символов!", Snackbar.LENGTH_SHORT).show();
                        if (s2.equals(s3)) {
                            if (emailValidatorsComposer.isValid(e1.getText().toString())) {
                                Boolean chkemail = db.chkemail(s1);
                                if (chkemail) {
                                    boolean insert = db.insert(s1, s2);
                                    if (insert) {
                                        Snackbar.make(v, "Успешная регистрация!", Snackbar.LENGTH_SHORT).show();
                                        e1.setText("");
                                        e2.setText("");
                                        e3.setText("");
                                    }
                                } else {
                                    Snackbar.make(v, "Адрес электронной почты уже существует!", Snackbar.LENGTH_SHORT).show();
                                    e1.setText("");
                                }
                            } else {
                                Snackbar.make(v, emailValidatorsComposer.getDescription(), Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(v, "Пароли не совпадают!", Snackbar.LENGTH_SHORT).show();
                            e2.setText("");
                            e3.setText("");
                        }
                }
            }
        });
    }
}