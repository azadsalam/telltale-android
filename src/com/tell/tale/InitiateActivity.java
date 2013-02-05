package com.tell.tale;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InitiateActivity extends Activity implements OnClickListener,WebServiceUser 
{

	Button submit_btn;
	int nid=1;
	String text;
	HashMap<String, Object> data ;  
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initiate);
		
		//Bundle bundle = getIntent().getExtras();
		
		//nid = Session.nid;
		submit_btn = (Button) findViewById(R.id.submit_initiate);
		
		submit_btn.setOnClickListener(this);
		//Toast.makeText(getApplicationContext(), "NID : "+nid,	Toast.LENGTH_LONG).show();
	}
	
	public void onClick(View v) 
	{
		EditText et ;
		
		// TODO Auto-generated method stub
		if(v.getId() == R.id.submit_initiate)
		{
			et = (EditText) findViewById(R.id.editText_id_initial);
			
			text = et.getText().toString();
			
			SharedPreferences myPrefs = this.getSharedPreferences("telltaleprefs", MODE_WORLD_READABLE);
			int nid = myPrefs.getInt("nid", 0);
	        
			WebServiceAdapter wsu;
			data = new HashMap<String, Object>();
			data.put("nid",nid);
	    	data.put("text",text);
	    	//reply tokens
	        wsu = new WebServiceAdapter(this,this,"Posting your story!!",Session.baseUrl+"/index.php/initiate/androidQuery",data,null);
			wsu.startWebService();
			
			
			//Toast.makeText(getApplicationContext(), "Story : "+text,	Toast.LENGTH_LONG).show();
		}
		
	}

	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Story Posted\n: "+text,	Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

}
