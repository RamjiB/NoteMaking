package com.example.android.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.notes.data.NotesContract;

public class NotesDbHelper extends SQLiteOpenHelper{
    private static final String TAG = "NotesDbHelper";


    //Database name
    private static final String DATABASE_NAME = "notesDB.db";

    //Change in database schema, must change database version
    private static final int VERSION = 1;


    //Constructor

    NotesDbHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Create Tasks table
        final String CREATE_TABLE = "CREATE TABLE "          + NotesContract.TaskEntry.TABLE_NAME + " (" +
                NotesContract.TaskEntry._ID                 + " INTEGER PRIMARY KEY, " +
                NotesContract.TaskEntry.COLUMN_TITLE        + " TEXT NOT NULL, " +
                NotesContract.TaskEntry.COLUMN_DESCRIPTION  + " TEXT NOT NULL);";

        Log.d(TAG,"create table :"+ CREATE_TABLE);

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotesContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
