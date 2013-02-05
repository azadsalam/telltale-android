package com.tell.tale;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewOnGoingStory extends Activity implements WebServiceUser,OnClickListener
{
	// FOR FETCHING DATA FROM SERVER
	private WebServiceAdapter wsu;
	private HashMap<String, Object>  data;
	private HashMap<String, Object>  reply;
	private String replyTokens[];

	
	
	//FOR VIEWING POSTS
	private Data[] appendedPosts,unappendedPosts;
	private ListView appended_listview,Unappended_listview;
	int appended_post_count;
	int unappended_part_count;
	
	
	
	Button btn_contribute;
	TextView tv_test;
	
	
	//
	int nid;
	//Last appended
	int lastAppendedPostId; 	
	int pid;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_ongoing_story);
		
		Bundle bundle = getIntent().getExtras();
		pid = bundle.getInt("pid");
		//Log.d("PID : ",""+pid);
		
		
		SharedPreferences myPrefs = getSharedPreferences("telltaleprefs",MODE_WORLD_READABLE);
		nid = myPrefs.getInt("nid", 0);


		
		btn_contribute = (Button) findViewById(R.id.btn_contribute);
		btn_contribute.setOnClickListener(this);
		
		// FOR VIEWING APPENDED POSTS
        appended_listview = (ListView) findViewById(R.id.lv_appended_posts); 
        Unappended_listview = (ListView)findViewById(R.id.lv_unappended_posts);


		//FETCH STORY FROM SERVER
        prepareData();
		wsu = new WebServiceAdapter(this,this,"Loading Story!!",Session.baseUrl+"/index.php/ongoingStory_feed/getFullStoryFromAndroid",data,replyTokens);        
        startWebService();

 		
	}
    
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ContributeActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("lastAppendedPostId", lastAppendedPostId);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
    // this class is for your data , customize it , as you want
  

        // PARSE SERVER REPLY , POPULATE IN DATA ARRAY
    public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
    	
		// [pid] => 9 [nid] => 1 [text] => HELLO [name] => Azad [vote] => 0
    	
    	reply = data;
		String ans = "The result\n";
		
		if(((Boolean)reply.get("error")).booleanValue()==true)
		{
			Toast.makeText(this, "Could Not Connect to Server!", Toast.LENGTH_LONG).show();
		}
		else
		{
				
			appended_post_count = Integer.parseInt(reply.get("appended_post_count").toString());
			unappended_part_count = Integer.parseInt(reply.get("Unappended_part_count").toString());
			
			appendedPosts = new Data[appended_post_count];
			unappendedPosts = new Data[unappended_part_count];
						
			try 
			{
				JSONObject json = new JSONObject(reply.get("appended_post_array").toString());
				PopulateDataArrayForStory.populateDataArray(json, appendedPosts,appended_post_count);
				json = new JSONObject(reply.get("Unappended_part_array").toString());
				PopulateDataArrayForStory.populateDataArray(json, unappendedPosts,unappended_part_count);
				
				
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		
		
		if(appendedPosts != null)
		{
		
	    	DataAdapterForStory appended_adapter = new DataAdapterForStory(this,this, appendedPosts,R.layout.appended_row,R.id.btn_like_appended);		
			appended_listview.setAdapter(appended_adapter); 
			lastAppendedPostId = appendedPosts[appended_post_count-1].pid;
			//Log.d("viewongoing ACTIVITY : "," lastappendedpostid "+lastAppendedPostId);
		}		
	    	
    	if(unappendedPosts != null)
    	{
    		DataAdapterForStory unappended_adapter = new DataAdapterForStory(this,this, unappendedPosts,R.layout.unappended_row,R.id.btn_like_unappended);		
    		Unappended_listview.setAdapter(unappended_adapter); 
    	}
				
 	    
	}
    
    
	/* ---------FOR FETCHING DATA FROM SERVER    ------*/ 
    public void prepareData()
    {
        //to be sent to server 
    	data = new HashMap<String, Object>();    	
    	data.put("pid",pid);   
    	data.put("nid", nid);
    	
    	//reply tokens
    	replyTokens = new String[4];
    	replyTokens[0] = "appended_post_count";
    	replyTokens[1] = "Unappended_part_count";
    	replyTokens[2] = "appended_post_array";
    	replyTokens[3] = "Unappended_part_array";	    	
    }
    
    public void startWebService() 
    { 
    	wsu.startWebService();
    }
	
    
	/* ---------FOR FETCHING DATA FROM SERVER ENDS------*/ 



}
