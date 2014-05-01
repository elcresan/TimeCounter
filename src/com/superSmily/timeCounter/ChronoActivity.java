package com.superSmily.timeCounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class ChronoActivity extends Activity {
	Chronometer chrono;
	Button but;
	Long timeRunning;
	Context ctx;
	com.superSmily.timeCounter.Activity act;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chronoactivity);	
		ctx = getApplicationContext();
		TextView tvActivity = (TextView) findViewById(R.id.textViewActivity);
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		
		String name = extras.getString("name");
		String text = new String();
		//setActivity can fail, it will be handled in next versions
		setActivity(name);
		//Change it to get the entire activity
		timeRunning = act.getTimeRunning();
		but = (Button) findViewById(R.id.buttonStart);
		chrono = (Chronometer) findViewById(R.id.chrono);
		text = getTextFromLong(timeRunning);
		
		chrono.setText(text);
		tvActivity.setText(name);
	}
	@Override
	public void onResume(){
		super.onResume();
		
		if(act.isRunning()){
			but.setText(R.string.chrono_pause_button);
			chrono.setBase(act.getBaseChrono());
			chrono.start();
		}else if(timeRunning > 0)
			but.setText(R.string.chrono_resume_button);
	}
	@Override
	public void onPause(){
		super.onPause();
		ActivityDAO dao = new ActivityDAO(ctx);
		try{
			dao.open();
			dao.updateActivity(act);
		}catch(Exception e){
    		Log.e("ChronoActivity", e.toString());
		}finally{
			dao.close();
		}		
	}
	@Override
	public void onBackPressed(){
		// This is necessary to refresh the state (counting...) of an activity on the list
		// when user press back button it acts like the up button of the ActionBar
		NavUtils.navigateUpFromSameTask(this);
	}
	
	public void setActivity(String name){
		ActivityDAO dao = new ActivityDAO(ctx);
		try{
			dao.open();
			act = dao.getActivityByName(name);
		}catch(Exception e){
    		Log.e("ChronoActivity", e.toString());
		}finally{
			dao.close();
		}
	}

	public String timetoString(int time){
		if(time < 10)
			return "0"+String.valueOf(time);
		else
			return String.valueOf(time);
	}

	
	public String getTextFromLong(Long time){

		int seconds = time.intValue()/1000;
		int hour = seconds/3600;
		int minutes = (seconds/60)%60;
		seconds = seconds % 60;
		if(hour > 0)
			return timetoString(hour) + ":"
					+ timetoString(minutes) + ":" 
					+ timetoString(seconds);
		else
			return timetoString(minutes) + ":" 
					+ timetoString(seconds);
	}
		
	public void resetChrono(View view){
		chrono.stop();	
		chrono.setText("00:00");
		act.setBaseChrono(-1);
		act.setTimeRunning(0);
		timeRunning = act.getTimeRunning();
		act.setRunning(false);
		but.setText("Start");
	}
	
	public void startChrono(View view){
		// Change the condition to state of the chrono.
		// Chrono didn't start
		if(act.getTimeRunning() == 0 && !act.isRunning()){ 
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.start();
			act.setBaseChrono(chrono.getBase());
			act.setRunning(true);
			but.setText(R.string.chrono_pause_button);
		}else{
			// Chrono is running
			if(act.isRunning()){
				// Storing the time that chrono spent runnning
				timeRunning = SystemClock.elapsedRealtime() - chrono.getBase();
				chrono.stop();
				act.setTimeRunning(timeRunning);
				act.setRunning(false);
				but.setText(R.string.chrono_resume_button);

			}
			// Chrono is paused
			else{
				// Set the base time to the time when was stopped
				chrono.setBase(SystemClock.elapsedRealtime() - timeRunning);
				chrono.start();
				act.setBaseChrono(chrono.getBase());
				act.setRunning(true);
				but.setText(R.string.chrono_pause_button);
			}
		}
	}
}
