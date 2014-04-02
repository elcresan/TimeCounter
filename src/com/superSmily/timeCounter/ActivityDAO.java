package com.superSmily.timeCounter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.superSmily.timeCounter.DataBaseHelper.FeedEntry;

public class ActivityDAO {

	private SQLiteDatabase db;
	private DataBaseHelper dbHelp;
	
	public ActivityDAO(Context ctx){
		dbHelp = new DataBaseHelper(ctx);
	}
	
	public void open() throws SQLException{
		db = dbHelp.getWritableDatabase();
	}
	
	public void close(){
		dbHelp.close();
	}
	public void addActivity(Activity act){
		//Estudiar como poner el ID
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_ACTIVITY_NAME, act.getName());
		values.put(FeedEntry.COLUMN_NAME_BASE_CHRONO, act.getBaseChrono());
		values.put(FeedEntry.COLUMN_NAME_IS_RUNNING, (act.isRunning()) ? 1 : 0);
		values.put(FeedEntry.COLUMN_NAME_TIME_RUNNING, act.getTimeRunning());
		
		db.insert(FeedEntry.TABLE_NAME, null, values);
	}
	public ArrayList<Activity> getActivities(){
		ArrayList<Activity> acts = new ArrayList<Activity>();
		String[] projection = {
				FeedEntry.COLUMN_NAME_ENTRY_ID,
				FeedEntry.COLUMN_NAME_ACTIVITY_NAME,
				FeedEntry.COLUMN_NAME_BASE_CHRONO,
				FeedEntry.COLUMN_NAME_TIME_RUNNING,
				FeedEntry.COLUMN_NAME_IS_RUNNING
		};
		// How you want the results sorted in the resulting Cursor
		//String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";
		Cursor c = db.query(
				FeedEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				null //sortOrder
				);
		c.moveToFirst();
		//FALTA
		while(!c.isAfterLast()){
			Activity act = cursor2activity(c);
			acts.add(act);
			c.moveToNext();
		}
		return acts;		
	}

	public Activity cursor2activity(Cursor c){
		Activity act = new Activity();
		act.setId(c.getString(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ID)));
		act.setName(c.getString(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ACTIVITY_NAME)));
		act.setBaseChrono(c.getLong(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_BASE_CHRONO)));
		act.setTimeRunning(c.getLong(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TIME_RUNNING)));
		if(c.getInt(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_IS_RUNNING)) == 1)
				act.setRunning(true);
		else
			act.setRunning(false);

		return act;
	}
}
