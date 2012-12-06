package com.tell.tale;

import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TellTaleActivity extends Activity implements OnClickListener,WebServiceUser
{
	Button login;
	EditText mail,password;
	HashMap<String, Object> data ;    				
	
	String replyTokens[] = {"nid"};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        data = new HashMap<String, Object>();    				
        login = (Button) findViewById(R.id.login);
        mail = (EditText) findViewById(R.id.editText_id_mail);
        password = (EditText) findViewById(R.id.editText_id_password);
        
        mail.setText("salam_azad223@yahoo.com");
        password.setText(":p");
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
			
			/* FOR NOW DISABLE LOG IN
			WebServiceAdapter wsu;
			data.put("mail",mail.getText().toString());
	    	data.put("password",password.getText().toString());
	    	
	    	//reply tokens
	    	
	        wsu = new WebServiceAdapter(this,this,"Authenticating!!","http://10.0.2.2/telltale/index.php/userAuthentication/androidQuery",data,replyTokens);
			wsu.startWebService();
			*/
			break;

		default:
			break;
		}
		
	}

	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		
		Object nid = data.get(replyTokens[0]);
		
		if(nid == null ||nid.toString().equals("null") || ((Boolean)data.get("error")).booleanValue()==true)
		{
			Toast.makeText(this, "Authectication Failed", Toast.LENGTH_LONG).show();
		}
		
		else
		{
			Session.nid = (new Integer((String)nid)).intValue();
			Toast.makeText(this, "Authectication Successful\nnid : "+nid, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this,HomeActivity.class);
			startActivity(intent);
		}
		

		
	}
}
