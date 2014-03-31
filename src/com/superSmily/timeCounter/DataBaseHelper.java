package com.superSmily.timeCounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "myDB";
	public static abstract class FeedEntry implements BaseColumns{
		public static final String TABLE_NAME = "activity"; 
		public static final String COLUMN_NAME_ENTRY_ID = "actid";
		public static final String COLUMN_NAME_ACTIVITY_NAME = "activityName";
		public static final String COLUMN_NAME_BASE_CHRONO = "baseChrono";
		public static final String COLUMN_NAME_TIME_RUNNING = "timeRunning";
		public static final String COLUMN_NAME_IS_RUNNING = "isRunning";	
	}
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMA_SEP = ", ";
	private static final String INT_TYPE = " INTEGER";
		
	private static final String ACTIVITY_TABLE_FIELDS = " ("
			+ FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMA_SEP
			+ FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMA_SEP
			+ FeedEntry.COLUMN_NAME_ACTIVITY_NAME + TEXT_TYPE + COMA_SEP
			+ FeedEntry.COLUMN_NAME_BASE_CHRONO + INT_TYPE + COMA_SEP
			+ FeedEntry.COLUMN_NAME_TIME_RUNNING + INT_TYPE + COMA_SEP
			+ FeedEntry.COLUMN_NAME_IS_RUNNING + INT_TYPE
			+ " )";
	
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
			FeedEntry.TABLE_NAME + ACTIVITY_TABLE_FIELDS;

	
	public DataBaseHelper(Context context) {
		super(context,DATABASE_NAME , null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
