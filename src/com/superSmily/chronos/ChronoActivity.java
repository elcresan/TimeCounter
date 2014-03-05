package com.superSmily.chronos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class ChronoActivity extends Activity {
	Chronometer chrono;
	Button but;
	Long timeRunning;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chronoactivity);	
				
		TextView tvActivity = (TextView) findViewById(R.id.textViewActivity);
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String name = extras.getString("name");
		
		but = (Button) findViewById(R.id.buttonStart);
		chrono = (Chronometer) findViewById(R.id.chrono);
		tvActivity.setText(name);
		
	}

	public void startChrono(View view){
		// Chrono didn't start
		if(but.getText().equals("Start")){ 
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.start();
			but.setText("Pause");
		}else{
			// Chrono is running
			if(but.getText().equals("Pause")){
				// Storing the time that chrono spent runnning
				timeRunning = SystemClock.elapsedRealtime() - chrono.getBase();
				chrono.stop();
				but.setText("Resume");
			}
			// Chrono is paused
			else{
				// Set the base time to the time when was stopped
				chrono.setBase(SystemClock.elapsedRealtime() - timeRunning);
				chrono.start();
				but.setText("Pause");
			}
		}
	}
}
