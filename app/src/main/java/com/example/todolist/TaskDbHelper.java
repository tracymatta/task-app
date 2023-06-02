package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

public class TaskDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tasks.db";
    public static final String USER_TABLE = "User";
    public static final String COLUMN_USER_NAME = "Username";
    public static final String COLUMN_USER_EMAIL = "Email";
    public static final String COLUMN_USER_PASSWORD = "Password";
    public static final String TASK_TABLE = "Task";
    public static final String COLUMN_TASK_ID = "ID";
    public static final String COLUMN_TASK_CONTENT = "Task";
    public static final String COLUMN_TASK_FLAG = "Flag";

    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (" +
                COLUMN_USER_EMAIL + " TEXT PRIMARY KEY," +
                COLUMN_USER_NAME+ " TEXT," +
                COLUMN_USER_PASSWORD + " TEXT)";
    private static final String SQL_CREATE_TASKS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TASK_TABLE + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_TASK_CONTENT + " TEXT, " +
                COLUMN_TASK_FLAG + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_USER_EMAIL + ") REFERENCES " + USER_TABLE + "(" + COLUMN_USER_EMAIL +"))";

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_NAME, username);
        cv.put(COLUMN_USER_EMAIL, email);
        cv.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(USER_TABLE, null, cv);
        if(result == -1) return false;
        return true;
    }

    public boolean addTask(String email, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_EMAIL, email);
        cv.put(COLUMN_TASK_CONTENT, content);
        cv.put(COLUMN_TASK_FLAG, 0);

        long result = db.insert(TASK_TABLE, null, cv);
        if(result == -1) return false;
        return true;
    }

    public boolean getUser(String email) {
        SQLiteDatabase db = getReadableDatabase();

        String SQLQuery = "SELECT " + COLUMN_USER_EMAIL + " FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = \"" + email + "\"";
        Cursor cursor = db.rawQuery(SQLQuery, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String SQLQuery = "SELECT " + COLUMN_USER_PASSWORD + " FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = \"" + email + "\" and " + COLUMN_USER_PASSWORD + " = \"" + password + "\"";
        Cursor cursor = db.rawQuery(SQLQuery, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public List<Triple<Integer, Integer, String>> getTasks(String email) {
        List<Triple<Integer, Integer, String>> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String SQLQuery = "SELECT " + COLUMN_TASK_ID + ", " + COLUMN_TASK_FLAG + ", " + COLUMN_TASK_CONTENT + " FROM " + TASK_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = \"" + email + "\"";
        Cursor cursor = db.rawQuery(SQLQuery, null);

        if(cursor.moveToFirst()) {
            do {
                tasks.add(new Triple<>(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public void deleteTask(int id) {
        String SQLQuery = "DELETE FROM " + TASK_TABLE + " WHERE " + COLUMN_TASK_ID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLQuery);
    }

    public void updateTask(int id, String content) {
        String SQLQuery = "UPDATE " + TASK_TABLE + " SET " + COLUMN_TASK_CONTENT + " = \"" + content + "\" WHERE " + COLUMN_TASK_ID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLQuery);
    }

    public void markAsDone(int id) {
        String SQLQuery = "UPDATE " + TASK_TABLE + " SET " + COLUMN_TASK_FLAG + " = 1 WHERE " + COLUMN_TASK_ID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLQuery);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //left empty by alaa
        //hi
    }
}