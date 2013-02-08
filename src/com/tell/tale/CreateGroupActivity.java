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

public class CreateGroupActivity extends Activity implements WebServiceUser,OnClickListener 
{
	HashMap<String, Object> data ;
	EditText et_group_name;
	
	Button btn_create_group,btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group);
		
		et_group_name = (EditText)findViewById(R.id.et_group_name);
		btn_back = (Button) findViewById(R.id.btn_back);
		
		btn_create_group = (Button)findViewById(R.id.btn_create_group_from_name);
		btn_create_group.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		
	}

	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,GroupManagementActivity.class);
		startActivity(intent);
		finish();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent;
		
		switch (id) {
		case R.id.btn_create_group_from_name:
			WebServiceAdapter wsu;
			data = new HashMap<String, Object>();
			
			SharedPreferences myPrefs = getSharedPreferences("telltaleprefs", MODE_WORLD_READABLE);
    		int nid = myPrefs.getInt("nid", 0);
    		
			data.put("nid",nid);
	    	data.put("name",et_group_name.getText().toString());
	    	
	    	
	    	
	        wsu = new WebServiceAdapter(this,this,"Creating Group!!",Session.baseUrl+"/index.php/group/create_group_from_android",data,null);
			wsu.startWebService();
			
			

			
			break;

		
		default:
			intent = new Intent(this,GroupManagementActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		
	}

}
