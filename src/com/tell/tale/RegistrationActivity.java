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

public class RegistrationActivity extends Activity implements OnClickListener,WebServiceUser
{
	
	Button btn_registration;
	EditText et_mail,et_name,et_pass;
	String mail,pass,name;
	
	HashMap<String, Object> data ;    				
	String replyTokens[] = {"success","msg","nid"};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.registration);
		
		
		btn_registration = (Button) this.findViewById(R.id.btn_reg_submit);
		btn_registration.setOnClickListener(this);
		
		et_mail = (EditText)findViewById(R.id.et_mail);
		et_name = (EditText)findViewById(R.id.et_name);
		et_pass = (EditText)findViewById(R.id.et_password);
	}

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		
		
		mail = et_mail.getText().toString();
		pass = et_pass.getText().toString();
		name = et_name.getText().toString();
		
		//Toast.makeText(this, mail+pass+name, Toast.LENGTH_LONG).show();
		
		WebServiceAdapter wsu;
		data = new HashMap<String, Object>();
		data.put("mail",mail);
    	data.put("password",pass);
    	data.put("name", name);
    	
    	//reply tokens
    	
    	
    	
        wsu = new WebServiceAdapter(this,this,"Please Wait!!","http://10.0.2.2/telltale/index.php/registration/do_registration_from_android",data,replyTokens);
		wsu.startWebService();
		
	}

	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		
		
		String success = data.get(replyTokens[0]).toString();
		String msg = data.get(replyTokens[1]).toString();
		
		if(success.equals("true"))
		{
			int nid = Integer.parseInt(data.get(replyTokens[2]).toString());
			
			SharedPreferences myPrefs = this.getSharedPreferences("telltaleprefs", MODE_WORLD_READABLE);
	        SharedPreferences.Editor prefsEditor = myPrefs.edit();
	        prefsEditor.putInt("nid",nid);
	        prefsEditor.commit();
	        
			Toast.makeText(this, "Registration Successfull \n"  , Toast.LENGTH_LONG).show();
			
			Intent intent = new Intent(this,HomeActivity.class);
			startActivity(intent);
			this.finish();
		}
		else
		{
			Toast.makeText(this, "Registration Failed \n" + msg , Toast.LENGTH_LONG).show();
		}
		
		
		
	}
}
