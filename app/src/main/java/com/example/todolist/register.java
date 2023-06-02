package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class register extends AppCompatActivity {

    EditText username, email, password, repassword;
    TextView message;
    Button register;
    TaskDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        message = (TextView) findViewById(R.id.message);
        register = (Button) findViewById(R.id.register);

        message.setText("");

        dbHelper = new TaskDbHelper(register.this);
    }

    public void login(View v) {
        Intent registerPage = new Intent(register.this, MainActivity.class);
        startActivity(registerPage);
    }

    public void register(View v) {
        String usernameInput = username.getText().toString();
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        String repasswordInput = repassword.getText().toString();

        if(usernameInput.equals("") || emailInput.equals("") || passwordInput.equals("") || repasswordInput.equals("")) {
            message.setText("Please fill out all inputs!");
            return;
        }

        if(!passwordInput.equals(repasswordInput)) {
            message.setText("Error occured, please retry!");
            return;
        }

        if(searchInDatabase(emailInput)) {
            message.setText("Account already exists! Try using a different name.");
            return;
        }
        if(!addToDatabase(usernameInput, emailInput, passwordInput)) {
            message.setText("Error occured, please retry!");
        }
        else {
            showLists(emailInput);
        }
    }

    private boolean searchInDatabase(String email) {
        return dbHelper.getUser(email);
    }

    private boolean addToDatabase(String username, String email, String password) {
        return dbHelper.addUser(username, email, password);
    }

    private void showLists(String email) {
        Intent todoListPage = new Intent(register.this, list.class);
        todoListPage.putExtra("email", email);
        startActivity(todoListPage);
    }
}