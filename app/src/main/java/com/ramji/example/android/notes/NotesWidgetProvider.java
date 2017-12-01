package com.ramji.example.android.notes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ramji.android.notes.MainActivity;
import com.ramji.android.notes.NotesUpdatedWidget;
import com.ramji.android.notes.R;

/**
 * Implementation of App Widget functionality.
 */
public class NotesWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String Title,String Content,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notes_widget_provider);

        //Create an Intent to launch the MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        if (Title.isEmpty() && Content.isEmpty()){

            views.setTextViewText(R.id.appwidget_title,"No latest notes");

        }else {

            //Update the title and content
            views.setTextViewText(R.id.appwidget_title, Title);
            views.setTextViewText(R.id.appwidget_content, Content);
        }

        //Widgets allow click Handler to only launch pending intents
        views.setOnClickPendingIntent(R.id.appwidget,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        NotesUpdatedWidget.startActionUpdateNotesWidgets(context);

    }

    public static void updateNotesWidgets(Context context,AppWidgetManager appWidgetManager,
                                          String title,String content,int[] appWidgetIds){

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,title,content, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

