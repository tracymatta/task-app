package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

public class list extends AppCompatActivity {

    ListView tasks;
    EditText newTask;
    Button addTask, submitTask;
    String email;
    List<Triple<Integer, Integer, String>> userTasks;
    List<String> contentTasks;
    TaskDbHelper dbHelper;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        tasks = (ListView) findViewById(R.id.tasks);
        newTask = (EditText) findViewById(R.id.newTask);
        addTask = (Button) findViewById(R.id.add);
        submitTask = (Button) findViewById(R.id.submit);

        newTask.setVisibility(View.GONE);
        submitTask.setVisibility(View.GONE);

        dbHelper = new TaskDbHelper(list.this);
        userTasks = dbHelper.getTasks(email);
        contentTasks = new ArrayList<>();
        for(Triple<Integer, Integer, String> currentTask : userTasks) {
            contentTasks.add(currentTask.getThird());
        }
        adapter = new ArrayAdapter<String>(list.this, android.R.layout.simple_list_item_1, contentTasks);
        tasks.setAdapter(adapter);
        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(list.this, taskOperations.class);
                intent.putExtra("email", email);
                intent.putExtra("ID", userTasks.get(position).getFirst());
                intent.putExtra("Flag", userTasks.get(position).getSecond());
                intent.putExtra("content", userTasks.get(position).getThird());
                startActivity(intent);
            }
        });
    }

    public void addNewTask(View v) {
        tasks.setVisibility(View.GONE);
        addTask.setVisibility(View.GONE);
        newTask.setVisibility(View.VISIBLE);
        submitTask.setVisibility(View.VISIBLE);
    }

    public void submitNewTask(View v) {
        newTask.setVisibility(View.GONE);
        submitTask.setVisibility(View.GONE);
        String content = newTask.getText().toString();
        newTask.setText("");
        if(!content.equals("")) {
            dbHelper.addTask(email, content);
        }

        userTasks.clear();
        userTasks.addAll(dbHelper.getTasks(email));
        contentTasks.clear();
        for(Triple<Integer, Integer, String> currentTask : userTasks) {
            contentTasks.add(currentTask.getThird());
        }
        adapter.notifyDataSetChanged();

        tasks.setVisibility(View.VISIBLE);
        addTask.setVisibility(View.VISIBLE);
    }
}