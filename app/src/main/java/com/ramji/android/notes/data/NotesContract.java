package com.ramji.android.notes.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class NotesContract {

    //The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.ramji.android.notes";

    //public static final Uri = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Path for the notes directory
    public static final String PATH_TASKS = "NotesTable";

    public static final class TaskEntry implements BaseColumns{

        //TaskEntry content URI = base content uri + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        //Task table name
        public static final String TABLE_NAME = "notes";

        //Table Columns
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_CREATED_AT = "Created_at";

    }
}
