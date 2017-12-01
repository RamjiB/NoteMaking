package com.ramji.android.notes;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.ramji.android.notes.data.NotesContract;
import com.ramji.example.android.notes.NotesWidgetProvider;

public class NotesUpdatedWidget extends IntentService {


    public static final String ACTION_UPDATE_NOTES_WIDGETS = "com.ramji.android.notes.update_notes_widgets";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotesUpdatedWidget() {
        super("NotesUpdatedWidget");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        final String action = intent.getAction();
        if (ACTION_UPDATE_NOTES_WIDGETS.equals(action)){
            handleActionUpdateNotesWidgets();
        }
    }

    private void handleActionUpdateNotesWidgets() {
        //Query for the latest note

        String title = "";
        String content = "";
        Cursor cursor = getContentResolver().query(NotesContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                NotesContract.TaskEntry.COLUMN_CREATED_AT);
        if (cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            int createdTimeIndex = cursor.getColumnIndex(NotesContract.TaskEntry.COLUMN_CREATED_AT);
            long timeNow = System.currentTimeMillis();
            long createdAt = cursor.getLong(createdTimeIndex);
            if (timeNow - createdAt < 86400000 ){
                title = cursor.getString(cursor.getColumnIndex(NotesContract.TaskEntry.COLUMN_TITLE));
                content = cursor.getString(cursor.getColumnIndex(NotesContract.TaskEntry.COLUMN_DESCRIPTION));
                cursor.close();
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NotesWidgetProvider.class));
            //Now update all widgets
            NotesWidgetProvider.updateNotesWidgets(this,appWidgetManager,title,content,appWidgetIds);

        }
    }

    public static void startActionUpdateNotesWidgets(Context context){
        Intent intent = new Intent(context,NotesUpdatedWidget.class);
        intent.setAction(ACTION_UPDATE_NOTES_WIDGETS);
        context.startService(intent);
    }
}
