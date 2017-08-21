package com.example.android.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.android.notes.R.id.listView;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    public void addicon(View v){

        Intent indent = new Intent(getApplicationContext(),notesEditor.class);
        startActivity(indent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.add_note){

            Intent indent = new Intent(getApplicationContext(),notesEditor.class);
            startActivity(indent);
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton add = (ImageButton) findViewById(R.id.addicon);

        ListView listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.notes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null){

            notes.add("Example");

        }else {

            notes = new ArrayList<>(set);

        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), notesEditor.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you Sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.notes", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        }).setNegativeButton("No",null).show();

                return true;
            }
        });


    }
}
