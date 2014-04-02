package com.superSmily.timeCounter;

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
}
