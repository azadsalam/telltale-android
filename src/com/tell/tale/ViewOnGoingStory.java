package com.tell.tale;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ViewOnGoingStory extends Activity {
	int pid;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_ongoing_story);
		
		Bundle bundle = getIntent().getExtras();
		pid = bundle.getInt("pid");
		Log.d("PID : ",""+pid);
	}

}
