package com.example.android.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.Toast;

import com.example.android.notes.data.NotesContract;

import java.util.ArrayList;
import java.util.HashSet;

import static android.R.attr.data;
import static android.R.attr.id;
import static com.example.android.notes.R.drawable.notes;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,NotesListAdapter.ListItemClickListener{

    private static final String TAG = "MainActivity";
    private static final int TASK_LOADER_ID = 0;

    private RecyclerView mNotesList;
    private NotesListAdapter mAdapter;
    private FloatingActionButton newNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Setting toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.titleBar);
        toolbar.setTitle(getString(R.string.title));

        mNotesList = (RecyclerView) findViewById(R.id.rv_list_notes);
        newNote = (FloatingActionButton) findViewById(R.id.addicon);

        //Layout manager for aligning items in recycler view

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNotesList.setLayoutManager(layoutManager);

        // Adpater responsible for displaying each item in list
        mAdapter = new NotesListAdapter(this);
        mNotesList.setAdapter(mAdapter);

        //action on add icon button
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, notesEditor.class);
                intent.putExtra("id",0);
                startActivity(intent);

            }
        });

        //onClick notes List item

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG,"swiped");

                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();
                Log.d(TAG,"Tag id"+ id);

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = NotesContract.TaskEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                Log.d(TAG,"uri"+ uri);

                //Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);
                Log.d(TAG,"item deleted");
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

                //Restart the loader to re-query for all tasks after a deletion
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);
            }
        }).attachToRecyclerView(mNotesList);

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an notesEditor,
     * so this restarts the loader to re-query the underlying data for any changes.
     */

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG,"onResume");

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }


    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG,"onCreateLoader");

        return new AsyncTaskLoader<Cursor>(this) {



            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                // Query and load all task data in the background; sort by priority
                try {
                    return getContentResolver().query(NotesContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }
            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.d(TAG,"onLoadFinished");

        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG,"onLoaderReset");

        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(String title,String content,int id) {
        Intent intent = new Intent(MainActivity.this, notesEditor.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}

