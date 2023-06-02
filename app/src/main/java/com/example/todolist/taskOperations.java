package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class taskOperations extends AppCompatActivity {

    EditText task;
    Button delete, save, markAsDone;
    int id, flag;
    String email, content;
    TaskDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_operations);

        task = (EditText) findViewById(R.id.task);
        delete = (Button) findViewById(R.id.delete);
        save = (Button) findViewById(R.id.save);
        markAsDone = (Button) findViewById(R.id.done);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        id = intent.getIntExtra("ID", -1);
        flag = intent.getIntExtra("Flag", 0);
        content = intent.getStringExtra("content");

        task.setText(content);

        if(flag == 1) {
            task.setEnabled(false);
            save.setClickable(false);
            markAsDone.setClickable(false);
        }

        dbHelper = new TaskDbHelper(taskOperations.this);
    }

    public void delete(View v) {
        dbHelper.deleteTask(id);
        Intent intent = new Intent(taskOperations.this, list.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void update(View v) {
        if(!task.getText().toString().equals("")) {
            dbHelper.updateTask(id, task.getText().toString());
        }
        Intent intent = new Intent(taskOperations.this, list.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void markAsDone(View v) {
        dbHelper.markAsDone(id);
        Intent intent = new Intent(taskOperations.this, list.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}