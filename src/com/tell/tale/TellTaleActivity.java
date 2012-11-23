package com.tell.tale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TellTaleActivity extends Activity implements OnClickListener
{
	Button login;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        login = (Button) findViewById(R.id.login);
        
        
        login.setOnClickListener(this);
    }

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent;
		
		switch (id) {
		case R.id.login:
			//Toast.makeText(getApplicationContext(), "Logging In...", Toast.LENGTH_SHORT).show();
			
			intent = new Intent(this,HomeActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
}