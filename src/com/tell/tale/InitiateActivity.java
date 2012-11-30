package com.tell.tale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InitiateActivity extends Activity implements OnClickListener {

	Button submit_btn;
	int nid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initiate);
		
		Bundle bundle = getIntent().getExtras();
		
		nid = bundle.getInt("nid");
		
		
		submit_btn = (Button) findViewById(R.id.submit_initiate);
		
		submit_btn.setOnClickListener(this);
		//Toast.makeText(getApplicationContext(), "NID : "+nid,	Toast.LENGTH_LONG).show();
	}
	
	public void onClick(View v) 
	{
		EditText et ;
		String text;
		// TODO Auto-generated method stub
		if(v.getId() == R.id.submit_initiate)
		{
			et = (EditText) findViewById(R.id.editText_id_initial);
			text = et.getText().toString();
			//Toast.makeText(getApplicationContext(), "Story : "+text,	Toast.LENGTH_LONG).show();
		}
		
	}

}
