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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.notes.data.NotesContract;

import java.util.HashSet;

import static android.R.attr.extractNativeLibs;
import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class notesEditor extends AppCompatActivity {

    private static final String TAG = "notesEditor";

    //text fields
    EditText titleField;
    EditText descriptionField;
    Bundle extras;
    ContentValues contentValues;

    String title;
    String content;
    int id;

    /**
     * Hide Keyboard
     * @param event
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        //initialising fields
        titleField = (EditText) findViewById(R.id.title);
        descriptionField = (EditText) findViewById(R.id.content);

        extras = getIntent().getExtras();
        id = extras.getInt("id");
        Log.d(TAG,"id: "+ id);
        if (id != 0){
            title = extras.getString("title");
            content = extras.getString("content");

            titleField.setText(title);
            descriptionField.setText(content);
        }
    }

    @Override
    public void onBackPressed() {

        String inputTitle = titleField.getText().toString();
        String inputDescription = descriptionField.getText().toString();

        if (id != 0){
            Log.d(TAG,"id: " + id);
            // Build appropriate uri with String row id appended
            String stringId = Integer.toString(id);
            Uri uri = NotesContract.TaskEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            Log.d(TAG,"uri"+ uri);

            Log.d(TAG,"inputTitle: "+inputTitle);
            Log.d(TAG,"inputDescription: "+inputDescription);

            contentValues = new ContentValues();
            contentValues.put(NotesContract.TaskEntry.COLUMN_TITLE,inputTitle);
            contentValues.put(NotesContract.TaskEntry.COLUMN_DESCRIPTION,inputDescription);

            if (inputTitle.length() == 0 ){
                Log.d(TAG,"title is null");
                //Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);
            }else{
                //Update content values via a  content resolver
                getContentResolver().update(uri,contentValues,null,null);
                Log.d(TAG,"item updated");
                Toast.makeText(notesEditor.this, "Note updated", Toast.LENGTH_SHORT).show();
            }
        }else{
            contentValues = new ContentValues();
            contentValues.put(NotesContract.TaskEntry.COLUMN_TITLE,inputTitle);
            contentValues.put(NotesContract.TaskEntry.COLUMN_DESCRIPTION,inputDescription);

            //Insert content values via a ContentResolver
            Uri uri = getContentResolver().insert(NotesContract.TaskEntry.CONTENT_URI,contentValues);

            if (uri != null){
                if (inputTitle.length() == 0 ){
                    Log.d(TAG,"title is null");
                    //Delete a single row of data using a ContentResolver
                    getContentResolver().delete(uri, null, null);
                }else{
                    Toast.makeText(getBaseContext(), "Added a new note " + inputTitle , Toast.LENGTH_SHORT).show();
                }
            }
        }
        finish();
        super.onBackPressed();
    }


}
