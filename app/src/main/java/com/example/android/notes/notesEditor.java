package com.example.android.notes;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.notes.data.NotesContract;

import java.util.HashSet;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class notesEditor extends AppCompatActivity {

    private static final String TAG = "notesEditor";

    //text fields
    EditText titleField;
    EditText descriptionField;

//    //Floating action button
//    FloatingActionButton clear;


    @Override
    public void onBackPressed() {
        String inputTitle = titleField.getText().toString();
        String inputDescription = descriptionField.getText().toString();

        if (inputTitle.length() == 0 ){
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.TaskEntry.COLUMN_TITLE,inputTitle);
        contentValues.put(NotesContract.TaskEntry.COLUMN_DESCRIPTION,inputDescription);

        //Insert content values via a ContentResolver
        Uri uri = getContentResolver().insert(NotesContract.TaskEntry.CONTENT_URI,contentValues);

        if (uri != null){
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }

        finish();
        super.onBackPressed();
    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        //initialising fields
        titleField = (EditText) findViewById(R.id.title);
        descriptionField = (EditText) findViewById(R.id.content);

//        clear = (FloatingActionButton) findViewById(R.id.clear);
//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputTitle = titleField.getText().toString();
//                String inputDescription = descriptionField.getText().toString();
//
//                if (inputTitle.length() == 0 ){
//                    return;
//                }
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(NotesContract.TaskEntry.COLUMN_TITLE,inputTitle);
//                contentValues.put(NotesContract.TaskEntry.COLUMN_DESCRIPTION,inputDescription);
//
//                //Insert content values via a ContentResolver
//                Uri uri = getContentResolver().insert(NotesContract.TaskEntry.CONTENT_URI,contentValues);
//
//                if (uri != null){
//                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
//                }
//
//                finish();
//            }
//        });
    }


}
