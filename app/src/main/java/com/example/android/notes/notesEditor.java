package com.example.android.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
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

import java.util.HashSet;

public class notesEditor extends AppCompatActivity {
    int noteId;
    EditText content;
    Boolean textBold = false;
    Boolean textItalic= false;
    Boolean textAllCaps = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        EditText title = (EditText) findViewById(R.id.title);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {

            if (title.toString().isEmpty()) {

                MainActivity.notes.remove(noteId);
                MainActivity.arrayAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.notes", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();

            } else {

                title.setText(MainActivity.notes.get(noteId));

            }

        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (String.valueOf(s).isEmpty()) {

                    MainActivity.notes.remove(noteId);
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.notes", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();

                } else {

                    MainActivity.notes.set(noteId, String.valueOf(s));
                    MainActivity.arrayAdapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.notes", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                String Text = s.toString();

                if (Text.equals("")) {

                    MainActivity.notes.remove(noteId);
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.notes", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();

                }


            }
        });

        content = (EditText) findViewById(R.id.content);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String restoredText = sharedPreferences.getString("text", null);
        if (!TextUtils.isEmpty(restoredText)) {

            content.setText(restoredText);

        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("text",content.getText().toString());
        editor.apply();
        super.onBackPressed();
    }
}
