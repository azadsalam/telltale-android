package com.tell.tale;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewFullStory extends Activity implements WebServiceUser
{
	
	// FOR FETCHING DATA FROM SERVER
	private WebServiceAdapter wsu;
	private HashMap<String, Object>  data;
	private HashMap<String, Object>  reply;
	private String replyTokens[];

	
	
	//FOR VIEWING POSTS
	private Data[] posts;
	private ListView listview;
	int post_count;
	

	int nid;
	int pid;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_full_story);

		Bundle bundle = getIntent().getExtras();
		pid = bundle.getInt("pid");
		
		SharedPreferences myPrefs = getSharedPreferences("telltaleprefs",MODE_WORLD_READABLE);
		nid = myPrefs.getInt("nid", 0);

		
		// FOR VIEWING APPENDED POSTS
        listview = (ListView) findViewById(R.id.lv_full_story); 

		//FETCH STORY FROM SERVER
        prepareData();
		wsu = new WebServiceAdapter(this,this,"Loading Story!!",Session.baseUrl+"/index.php/completedStory_feed/getFullStoryFromAndroid",data,replyTokens);        
        startWebService();

	}	
	
	/*
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
	
	*/
	
	/*
    private void populateDataArray(JSONObject jsonArray, Data dataArray[],int count) 
    {
		for (int i=0;i<count;i++)
		{

			try 
			{
				String row = jsonArray.get(""+i).toString();
				if(row==null)continue;
				//Log.d("ONGOING ROW "+i+" : "," -> "+row);
				

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
	*/
    
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
				
			post_count = Integer.parseInt(reply.get("post_count").toString());
			
			posts = new Data[post_count];
						
			try 
			{
				JSONObject json = new JSONObject(reply.get("post_array").toString());
				PopulateDataArrayForStory.populateDataArray(json, posts,post_count);
				
				
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		

		if(posts != null)
		{
		
	    	DataAdapterForStory	 appended_adapter = new DataAdapterForStory(this,this, posts,R.layout.appended_row,R.id.btn_like_appended);		
			listview.setAdapter(appended_adapter); 
			
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
    	replyTokens = new String[2];
    	replyTokens[0] = "post_array";
    	replyTokens[1] = "post_count";

    }
    
    public void startWebService() 
    { 
    	wsu.startWebService();
    }
	
    
	/* ---------FOR FETCHING DATA FROM SERVER ENDS------*/ 


	
	
}
