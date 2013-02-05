package com.tell.tale;

import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


//{"initiate_count":"20","comment_count":"35","appended_count":"15","point":"140"}
public class Profile extends Activity implements WebServiceUser
{

	int nid=1;
	
	HashMap<String, Object> data ;    				
	String replyTokens[] = {"initiate_count","comment_count","appended_count","point","name"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		SharedPreferences myPrefs = this.getSharedPreferences("telltaleprefs", MODE_WORLD_READABLE);
		 nid = myPrefs.getInt("nid", 0);
		
		WebServiceAdapter wsu;
		data = new HashMap<String, Object>();
		data.put("nid",nid);
    	
    	//reply tokens
    	
        wsu = new WebServiceAdapter(this,this,"Please Wait!!",Session.baseUrl+"/index.php/profile/get_point_from_android",data,replyTokens);
		wsu.startWebService();
		
	}
	
	
	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		
		if(data != null)
		{
			String initiateCount = (data.get(replyTokens[0]).toString());
			String commentCount = (data.get(replyTokens[1]).toString());
			String appenddedCount = (data.get(replyTokens[2]).toString());
			String point = (data.get(replyTokens[3]).toString());
			String name = (data.get(replyTokens[4]).toString());
			
			
			
			((TextView)findViewById(R.id.tv_pro_name)).setText(name);
			((TextView)findViewById(R.id.tv_pro_points)).setText(point);
			((TextView)findViewById(R.id.tv_pro_init)).setText(initiateCount);
			((TextView)findViewById(R.id.tv_pro_comments)).setText(commentCount);
			((TextView)findViewById(R.id.tv_pro_appends)).setText(appenddedCount);
			//Toast.makeText(this, initiateCount + " "+ commentCount + " "+ appenddedCount + " "+ point + " "+name, Toast.LENGTH_LONG).show();
		}
		
	}

}
