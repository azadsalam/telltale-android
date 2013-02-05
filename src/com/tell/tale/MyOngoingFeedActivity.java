package com.tell.tale;

import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyOngoingFeedActivity extends Activity implements OnItemClickListener,WebServiceUser
{
	String replyTokens[] ;
	HashMap<String, Object> data ;    				
	
	int start = 0;
	int count = 10;
	
	ListView listView;
	
	int nid;
	Data dataArray[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_ongoing_feed);
		
		SharedPreferences myPrefs = this.getSharedPreferences("telltaleprefs", MODE_WORLD_READABLE);
		nid = myPrefs.getInt("nid", 0);
		
		data = new HashMap<String, Object>();    				
	       
		//lv = (ListView) findViewById(R.id.listView_id_ongoing_feed);
		
		listView = (ListView) findViewById(R.id.lv_my_ongoing_feed);
		getData();

	}
	
	   // function for creating dummydata , just for example , 
	 // you will populate your own data (whatever it may be, static data, or fetched from internet)
	 public void getData()
	 {
	 	
	
	 	WebServiceAdapter wsu;
		data.put("start",new String(""+start));
	 	data.put("count",new String(""+count));
	 	data.put("nid", new String(""+nid));
	 	replyTokens = new String[count];
	 	
	 	for (int i=0;i < count;i++)
	 	{
	 		replyTokens[i] = new String(""+(i+start));
	 	}
	 	
	 	//reply tokens
	 	
	     wsu = new WebServiceAdapter(this,this,"Downloading Data!!",Session.baseUrl+"/index.php/personalOngoingStory_feed/my_ongoing_feed_from_android",data,replyTokens);
		 wsu.startWebService();
		
			
	 	
	 }
	
		
	
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
		    
		    View rowView = inflater.inflate(R.layout.my_ongoing_feed_row, parent, false);

		    if(data[position]!=null)
		    {
			    TextView text = (TextView) rowView.findViewById(R.id.tv_my_ongoing_row_text);
			    text.setText(data[position].text);
			    
			    TextView likeCount = (TextView) rowView.findViewById(R.id.tv_my_ongoing_row_like_count);
			    likeCount.setText(""+data[position].likeCount);
			    
		    }
		    return rowView;
		 }

	}


	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	{ 
		if(dataArray[position] != null)
		{
			Data data = dataArray[position];
			Intent intent = new Intent(this,AppendSuggestionActivity.class);
			Bundle xtra = new Bundle();
			xtra.putInt("pid", data.pid);
			intent.putExtras(xtra);
			startActivity(intent);
			finish();
		}
		   // Toast.makeText(getApplicationContext(),"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
	}

	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		
		//Toast.makeText(getApplicationContext(), (CharSequence) data, Toast.LENGTH_LONG).show();
		
		
		// [pid] => 9 [nid] => 1 [text] => HELLO [name] => Azad [vote] => 0
		dataArray = new Data[count];
		for (int i=0;i<count;i++)
		{
			
			Object ret = (data.get(replyTokens[i]));
			if(ret==null)continue;
			String row =ret.toString();
			//Log.d("ONGOING ROW: ",replyTokens[i]+"->"+row);
			
			if(row==null || row.equals(""))continue;
			try 
			{
				JSONObject json = new JSONObject(row);
				
				dataArray[i] = new Data();
				dataArray[i].id = (i+start);
				dataArray[i].text = json.getString("text");
				dataArray[i].pid = Integer.parseInt(json.getString("pid"));
				dataArray[i].likeCount = Integer.parseInt(json.getString("vote"));
				
				Log.d("my ongoing ", dataArray[i].text);
			
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

 	
		
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

	}
	

}
