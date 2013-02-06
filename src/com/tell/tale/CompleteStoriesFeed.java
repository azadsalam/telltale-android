package com.tell.tale;

import java.util.HashMap;

import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CompleteStoriesFeed extends Activity implements WebServiceUser,OnItemClickListener,OnClickListener
{
	String replyTokens[] ;
	HashMap<String, Object> data ;    				
	FeedHelper feedHelper;
	
	
	
	ListView listView;
	
	
	Button btn_see_more ;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.completed_stories_feed);
		
		data = new HashMap<String, Object>();    				
		//lv = (ListView) findViewById(R.id.listView_id_ongoing_feed);
		
		listView = (ListView) findViewById(R.id.lv_completed_feed);
		btn_see_more = (Button) findViewById(R.id.btn_see_more_completed_feed);
		btn_see_more.setOnClickListener(this);


		String url = (Session.baseUrl+"/index.php/completedStory_feed/getCompletedStoriesFeedFromAndroid");
		feedHelper = new FeedHelper(this,this,this,listView,btn_see_more,data,replyTokens,url);
		
		
		
		
		feedHelper.getData(feedHelper.count,feedHelper.increment);

	}
	/*
    public void getData(int start,int increment)
    {
    	

    	WebServiceAdapter wsu;
		data.put("start",new String(""+start));
    	data.put("count",new String(""+increment));
    	
    	replyTokens = new String[increment];
    	
    	for (int i=0;i < increment;i++)
    	{
    		replyTokens[i] = new String(""+(i+start));
    	}
    	
    	//reply tokens
    	
        wsu = new WebServiceAdapter(this,this,"Downloading Data!!",Session.baseUrl+"/index.php/completedStory_feed/getCompletedStoriesFeedFromAndroid",data,replyTokens);
		wsu.startWebService();
		
    }
	*/
	
	/*
	class DataAdapter extends ArrayAdapter<Data>
	{
		final Context context;
		final Data[] data;
		public DataAdapter(Context c,Data[] d) 
		{
			// TODO Auto-generated constructor stub
			super(c,R.layout.ongoing_feed_row,d);
			context = c;
			data = d;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
		    View rowView = inflater.inflate(R.layout.ongoing_feed_row, parent, false);
		    
		    
		    if(data[position]!=null)
		    {
			    TextView text = (TextView) rowView.findViewById(R.id.tv_id_text);
			    text.setText(data[position].text);
			    
			    TextView likeCount = (TextView) rowView.findViewById(R.id.tv_id_likeCount);
			    likeCount.setText(""+data[position].likeCount);
			    
	
			    TextView username = (TextView) rowView.findViewById(R.id.tv_id_username);
			    username.setText(data[position].username);
		    }
//		    Log.d("UN",data[position].username);
		    
		    return rowView;
		 }
	
		
	
	}

	*/
	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	{ 
		
		Data data = feedHelper.dataArray[position];
		if(data != null)
		{
			Intent intent = new Intent(this,ViewFullStory.class);
			Bundle xtra = new Bundle();
			xtra.putInt("pid", data.pid);
			intent.putExtras(xtra);
			startActivity(intent);
		}
		   // Toast.makeText(getApplicationContext(),"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
	}


	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		
		//Toast.makeText(getApplicationContext(), (CharSequence) data, Toast.LENGTH_LONG).show();
		
		
		// [pid] => 9 [nid] => 1 [text] => HELLO [name] => Azad [vote] => 0
		
		
		
		feedHelper.processResult(data);
		/*
		Data[] temp = new Data[count+increment];
		
		if(dataArray != null)
		{
			System.arraycopy(dataArray, 0, temp, 0, count);
		}
		
		int prevCount=count;
		int i;
		for (int j=0;j<increment;j++)
		{
			
			i = count;
			//String index = new String("R"+(i+start));
			//Log.d("ONGOING ROW: ",replyTokens[i]+"->");		
			Object ret = (data.get(replyTokens[j]));
			if(ret==null)continue;
			String row =ret.toString();
			//Log.d("ONGOING ROW: ",replyTokens[i]+"->"+row);
			
			if(row==null || row.equals(""))continue;
			try 
			{
				JSONObject json = new JSONObject(row);
				
				temp[i] = new Data();
				temp[i].id = (i);
				temp[i].username = json.getString("name");
				temp[i].text = json.getString("text");
				temp[i].pid = Integer.parseInt(json.getString("pid"));
				temp[i].nid = Integer.parseInt(json.getString("nid"));
				temp[i].likeCount = Integer.parseInt(json.getString("vote"));
				
				count++;
				
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		

		if(count - prevCount != increment)
		{
			btn_see_more.setEnabled(false);
		}
    	
		
		
		dataArray = temp;
		//Log.d("count", ""+count);
		
    	DataAdapter adapter = new DataAdapter(this, dataArray);		
		// First paramenter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		//Context, Layout for row, ID of TextView, Data Array

		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		// pass the context(this) and the dataArray to fillup your listView (dataArray)
		listView.setOnItemClickListener(this); 
		*/
	}
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub		
		feedHelper.getData(feedHelper.count	, feedHelper.increment);
	}	
}
