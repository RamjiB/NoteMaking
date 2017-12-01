package com.ramji.android.notes;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramji.android.notes.data.NotesContract;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

    private static final String TAG = "NotesListAdapter";

    private Cursor mCursor;
    private Context mContext;
    private int idIndex;
    private int timeIndex;
    private int titleIndex;
    private int contentIndex;

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");

    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    private ListItemClickListener mOnClickListener;

    /**
     * the interface that receives on click messages
     */

   public interface ListItemClickListener{
      void onListItemClick(String title,String content,int noteID);
   }

    public NotesListAdapter(ListItemClickListener clickListener){
        mOnClickListener = clickListener;
    }
    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.notes_list,parent,false);
        NotesViewHolder viewHolder = new NotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {

        Log.d(TAG,"#"+ position);

        //Indices for the id and Title

        idIndex = mCursor.getColumnIndex(NotesContract.TaskEntry._ID);
        timeIndex = mCursor.getColumnIndex(NotesContract.TaskEntry.COLUMN_CREATED_AT);
        titleIndex = mCursor.getColumnIndex(NotesContract.TaskEntry.COLUMN_TITLE);
        contentIndex = mCursor.getColumnIndex(NotesContract.TaskEntry.COLUMN_DESCRIPTION);

        mCursor.moveToPosition(position);

        //Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        Log.d(TAG,"itemID: " + id);
        String title = mCursor.getString(titleIndex);

        //get time
        Long createdTime = mCursor.getLong(timeIndex);
        String date = "";
        long now = System.currentTimeMillis();

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        if (now - createdTime < (DAY_MILLIS)) {
            if (now - createdTime < (HOUR_MILLIS)) {
                if (now - createdTime < (MINUTE_MILLIS)){
                    long seconds = Math.round((now - createdTime) / SECOND_MILLIS);
                    date = String.valueOf(seconds) + "s";
                }else {
                    long minutes = Math.round((now - createdTime) / MINUTE_MILLIS);
                    date = String.valueOf(minutes) + "m";
                }
            } else {
                long minutes = Math.round((now - createdTime) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(createdTime);
            date = sDateFormat.format(dateDate);
        }
        // Add a dot to the date string
        date = "\u2022 " + date;

        //set values
        holder.itemView.setTag(id);
        holder.notesTitle.setText(title);
        holder.dateText.setText(date);

    }

    @Override
    public int getItemCount() {

        if (mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned


        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    // Inner class for creating ViewHolders
    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView notesTitle;
        TextView dateText;

        public NotesViewHolder(View itemView) {
            super(itemView);

            notesTitle = (TextView) itemView.findViewById(R.id.tv_note_title);
            dateText = (TextView) itemView.findViewById(R.id.date_text_view);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            //Determine the values of the wanted data
            final int id = mCursor.getInt(idIndex);
            String title = mCursor.getString(titleIndex);
            String content = mCursor.getString(contentIndex);

            mOnClickListener.onListItemClick(title,content,id);
        }
    }
}
