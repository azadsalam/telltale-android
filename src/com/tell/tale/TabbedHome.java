package com.tell.tale;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabbedHome extends TabActivity 
{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayout);
        
        
		TabHost tabHost = getTabHost(); 
		
		Intent in1 = new Intent(TabbedHome.this,InitiateActivity.class);
		// you can put extras in those intents
		TabSpec tab1Spec = tabHost.newTabSpec("InitiateActivity");
		tab1Spec.setIndicator("Initiate Your Own Story");
		tab1Spec.setContent(in1);
		
		Intent in2 = new Intent(TabbedHome.this,OnGoingFeed.class);
		// you can put extras in those intents
		TabSpec tab2Spec = tabHost.newTabSpec("Ongoing Stories");
		tab2Spec.setIndicator("Ongoing Stories");
		tab2Spec.setContent(in2);
		
		Intent in3 = new Intent(TabbedHome.this,Profile.class);
		// you can put extras in those intents
		TabSpec tab3Spec = tabHost.newTabSpec("Profile");
		tab3Spec.setIndicator("My Profile");
		tab3Spec.setContent(in3);
		
		Intent in4 = new Intent(TabbedHome.this,Profile.class);
		// you can put extras in those intents
		TabSpec tab4Spec = tabHost.newTabSpec("Profile");
		tab4Spec.setIndicator("My Profile");
		tab4Spec.setContent(in4);
		
		
		
		tabHost.addTab(tab1Spec);
		tabHost.addTab(tab2Spec);
		tabHost.addTab(tab3Spec);
		
		tabHost.setCurrentTab(2); // the default selected tab here is 0 , the first one
    }
	
}
