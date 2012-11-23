package com.tell.tale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener {

	Button initiate;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        
        initiate = (Button) findViewById(R.id.initiate);
        
        
        initiate.setOnClickListener(this);
        
    }
	public void onClick(View v)
	{
		
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent;
		switch (id) {
		case R.id.initiate:
			intent = new Intent(HomeActivity.this,InitiateActivity.class); 
			startActivity(intent);
			break;

		default:
			break;
		}

	}

}
