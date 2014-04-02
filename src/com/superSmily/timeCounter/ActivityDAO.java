package com.superSmily.timeCounter;

import com.superSmily.timeCounter.DataBaseHelper.FeedEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_ACTIVITY_NAME, act.name);
		values.put(FeedEntry.COLUMN_NAME_BASE_CHRONO, act.baseChrono);
		values.put(FeedEntry.COLUMN_NAME_IS_RUNNING, (act.isRunning) ? 1 : 0);
		values.put(FeedEntry.COLUMN_NAME_TIME_RUNNING, act.timeRunning);
		
		db.insert(FeedEntry.TABLE_NAME, null, values);
	}
}
