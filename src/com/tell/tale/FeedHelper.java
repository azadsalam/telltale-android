package com.tell.tale;

import java.util.HashMap;

import org.json.JSONObject;


import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class FeedHelper 
{
	
	//int start = 0;
	int count = 0;
	int increment = 3;
	
	WebServiceUser user;
	Context context;
	HashMap<String, Object> data;
	String [] replyTokens;
	String url;
	Data dataArray[];
	Button btn_see_more;
	ListView listView;
	OnItemClickListener onItemClickListener; 
	
	Boolean myOwn=false;
	public FeedHelper(WebServiceUser user,Context context,OnItemClickListener onItemClickListener ,ListView listView,Button btn_see_more, HashMap<String, Object> data,String [] replyTokens,String url) 
	{
		// TODO Auto-generated constructor stub
		this.user = user;
		this.context = context;
		this.data = data;
		this.replyTokens = replyTokens;
		this.url = url;
		//this.dataArray = dataArray;
		this.btn_see_more = btn_see_more;
		this.listView = listView;
		this.onItemClickListener = onItemClickListener;
	}
	
	
	public void getData( int start,int increment)
    {
    	

    	WebServiceAdapter wsu;
		data.put("start",new String(""+start));
    	data.put("count",new String(""+increment));
    	if(myOwn)
    	{
    		SharedPreferences myPrefs = context.getSharedPreferences("telltaleprefs", context.MODE_WORLD_READABLE);
    		int nid = myPrefs.getInt("nid", 0);
    		data.put("nid", new String(""+nid));
    	}
    	
    	replyTokens = new String[increment];
    	
    	for (int i=0;i < increment;i++)
    	{
    		replyTokens[i] = new String(""+(i+start));
    	}
    	
    	//reply tokens
    	
        wsu = new WebServiceAdapter(user,context,"Downloading Data!!",url,data,replyTokens);
        
		wsu.startWebService();
		
    }
	
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub		
		getData(count, increment);
	}	
	
	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		
		//Toast.makeText(getApplicationContext(), (CharSequence) data, Toast.LENGTH_LONG).show();
		
		
		// [pid] => 9 [nid] => 1 [text] => HELLO [name] => Azad [vote] => 0
		
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
				
				if(json.has("name")) temp[i].username = json.getString("name");
				
				temp[i].text = json.getString("text");
				temp[i].pid = Integer.parseInt(json.getString("pid"));
				
				if(json.has("nid")) temp[i].nid = Integer.parseInt(json.getString("nid"));
				
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
		
    	DataAdapter adapter = new DataAdapter(context, dataArray);		
		// First paramenter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		//Context, Layout for row, ID of TextView, Data Array

		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		// pass the context(this) and the dataArray to fillup your listView (dataArray)
		listView.setOnItemClickListener(onItemClickListener); 

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
		    
		    View rowView = inflater.inflate(R.layout.ongoing_feed_row, parent, false);
		    
		    
		    if(data[position]!=null)
		    {
			    TextView text = (TextView) rowView.findViewById(R.id.tv_id_text);
			    text.setText(data[position].text);
			    
			    if(myOwn == false)
			    {
				    TextView likeCount = (TextView) rowView.findViewById(R.id.tv_id_likeCount);
				    likeCount.setText(""+data[position].likeCount);
				    
		
				    TextView username = (TextView) rowView.findViewById(R.id.tv_id_username);
				    username.setText(data[position].username);
			    }
		    }
//		    Log.d("UN",data[position].username);
		    
		    return rowView;
		 }
	
		
	
	}


}
