package com.example.experiments.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.experiments.R;
import com.example.experiments.database.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    EditText e1, e2;
    Button b1;
    CheckBox mCheckBox;
    TextView t1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.pass);
        b1 = findViewById(R.id.login);
        mCheckBox = findViewById(R.id.checkbox);
        t1 = findViewById(R.id.textView);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        checkSharedPreferences();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                Boolean chkemailpass = db.emailpassword(email, password);
                if(chkemailpass) {
                    Snackbar.make(v, "Успешные данные!", Snackbar.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Snackbar.make(v, "Неверный логин или пароль!", Snackbar.LENGTH_SHORT).show();
                }
                if(mCheckBox.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox), "True");
                    mEditor.apply();

                    String name = e1.getText().toString();
                    mEditor.putString(getString(R.string.names), name);
                    mEditor.commit();

                    String password1 = e2.getText().toString();
                    mEditor.putString(getString(R.string.password), password1);
                    mEditor.commit();

                } else {
                    mEditor.putString(getString(R.string.checkbox), "False");
                    mEditor.commit();

                    mEditor.putString(getString(R.string.names), "");
                    mEditor.commit();

                    mEditor.putString(getString(R.string.password), "");
                    mEditor.commit();
                }
            }
        });
    }

    private void checkSharedPreferences() {
        String checkbox = mPreferences.getString(getString(R.string.checkbox), "False");
        String name = mPreferences.getString(getString(R.string.names), "");
        String password = mPreferences.getString(getString(R.string.password), "");

        e1.setText(name);
        e2.setText(password);

        if(checkbox.equals("True")) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }
}