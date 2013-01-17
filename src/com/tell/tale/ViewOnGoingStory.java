package com.tell.tale;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.tell.tale.OnGoingFeed.Data;
import com.tell.tale.OnGoingFeed.DataAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		Log.d("PID : ",""+pid);
		
		
		btn_contribute = (Button) findViewById(R.id.btn_contribute);
		btn_contribute.setOnClickListener(this);
		
		// FOR VIEWING APPENDED POSTS
        appended_listview = (ListView) findViewById(R.id.lv_appended_posts); 
        Unappended_listview = (ListView)findViewById(R.id.lv_unappended_posts);


		//FETCH STORY FROM SERVER
        prepareData();
		wsu = new WebServiceAdapter(this,this,"Loading Story!!","http://10.0.2.2/telltale/index.php/ongoingstory_feed/getFullStoryFromAndroid",data,replyTokens);        
        startWebService();

 		
	}
    
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,contributeActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("lastAppendedPostId", lastAppendedPostId);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
    // this class is for your data , customize it , as you want
    class Data
    {

    	int id;
    	
    	int pid;
    	int nid;
    	int likeCount;
    	String text;
    	String username;
    	//( post er pid, text, nid and like count , nid er name)
    	
    }


    class DataAdapter extends ArrayAdapter<Data> 
    {
    	final Context context;
    	final Data[] data;
    	int row_layout;
    	public DataAdapter(Context c,Data[] d,int row_layout) 
    	{
			// TODO Auto-generated constructor stub
    		super(c,row_layout,d);
    		context = c;
    		data = d;
    		this.row_layout= row_layout;
		}
    	
    	// this getView function automatically 
    	// fills up your each row with your data object
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View rowView = inflater.inflate(row_layout, parent, false);
    		
    		
		    TextView text = (TextView) rowView.findViewById(R.id.tv_id_text);
		    text.setText(data[position].text);
		    
		    TextView likeCount = (TextView) rowView.findViewById(R.id.tv_id_likeCount);
		    likeCount.setText(""+data[position].likeCount);
		    

		    TextView username = (TextView) rowView.findViewById(R.id.tv_id_username);
		    username.setText(data[position].username);
    		
    		return rowView;
    	}
    }
	
	
	
    
    private void populateDataArray(JSONObject jsonArray, Data dataArray[],int count) 
    {

    	
    	
		for (int i=0;i<count;i++)
		{

			try 
			{
				String row = jsonArray.get(""+i).toString();
				if(row==null)continue;
				Log.d("ONGOING ROW "+i+" : "," -> "+row);
				

				JSONObject json = new JSONObject(row);
				
				dataArray[i] = new Data();
				dataArray[i].id = (i);
				dataArray[i].username = json.getString("name");
				dataArray[i].text = json.getString("text");
				dataArray[i].pid = Integer.parseInt(json.getString("pid"));
				dataArray[i].nid = Integer.parseInt(json.getString("nid"));
				dataArray[i].likeCount = Integer.parseInt(json.getString("vote"));
				
			
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
    // PARSE SERVER REPLY , POPULATE IN DATA ARRAY
    public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
    	
		// [pid] => 9 [nid] => 1 [text] => HELLO [name] => Azad [vote] => 0
    	
    	reply = data;
		String ans = "The result\n";
		
		if(((Boolean)reply.get("error")).booleanValue()==true)
		{
			ans="ERROR OCCURED";
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
				populateDataArray(json, appendedPosts,appended_post_count);
				json = new JSONObject(reply.get("Unappended_part_array").toString());
				populateDataArray(json, unappendedPosts,unappended_part_count);
				
				
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		
		
		if(appendedPosts != null)
		{
		
	    	DataAdapter appended_adapter = new DataAdapter(this, appendedPosts,R.layout.appended_row);		
			appended_listview.setAdapter(appended_adapter); 
			lastAppendedPostId = appendedPosts[appended_post_count-1].pid;
			Log.d("viewongoing ACTIVITY : "," lastappendedpostid "+lastAppendedPostId);
		}		
	    	
    	if(unappendedPosts != null)
    	{
    		DataAdapter unappended_adapter = new DataAdapter(this, unappendedPosts,R.layout.unappended_row);		
    			Unappended_listview.setAdapter(unappended_adapter); 
    	}
				
 	    
	}
    
    
	/* ---------FOR FETCHING DATA FROM SERVER    ------*/ 
    public void prepareData()
    {
        //to be sent to server 
    	data = new HashMap<String, Object>();    	
    	data.put("pid",pid);    	
    	
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
