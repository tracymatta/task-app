package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    TextView message;
    Button login;
    CheckBox remember;
    TaskDbHelper dbHelper;
    SharedPreferences sharedPref;
    String sharedPrefFile = "com.example.android.login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        message = (TextView) findViewById(R.id.message);
        login = (Button) findViewById(R.id.login);
        remember = (CheckBox) findViewById(R.id.remember);
        message.setText("");

        dbHelper = new TaskDbHelper(MainActivity.this);

        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        email.setText(sharedPref.getString("email", ""));
        password.setText(sharedPref.getString("password", ""));
    }

    public void register(View v) {
        Intent registerPage = new Intent(MainActivity.this, register.class);
        startActivity(registerPage);
    }

    public void login(View v) {
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        if(emailInput.equals("") || passwordInput.equals("")) {
            message.setText("Please fill out both inputs!");
            return;
        }

        if(!searchInDatabase(emailInput, passwordInput)) {
            message.setText("Incorrect email or password!");
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        if(remember.isChecked()) {
            editor.putString("email", emailInput);
            editor.putString("password", passwordInput);
            editor.apply();
        }
        else {
            editor.clear();
            editor.apply();
        }

        showLists(emailInput);
    }

    private boolean searchInDatabase(String email, String password) {
        return dbHelper.checkUser(email, password);
    }

    private void showLists(String email) {
        Intent todoListPage = new Intent(MainActivity.this, list.class);
        todoListPage.putExtra("email", email);
        startActivity(todoListPage);
    }
}

