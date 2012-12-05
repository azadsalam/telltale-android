package com.tell.tale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OnGoingFeed extends Activity implements OnItemClickListener
{
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ongoing_feed);
		
		//lv = (ListView) findViewById(R.id.listView_id_ongoing_feed);
		
		listView = (ListView) findViewById(R.id.listView_id_ongoing_feed);
		
		
		getData();
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
	
    // this class is for your data , customize it , as you want
    class Data
    {

    	int id;
    	
    	int pid;
    	int nid;
    	int likeCount;
    	String text;
    	String username;
    	//(first post er pid, text, nid and like count , nid er name)
    	
    }
    
	
	Data dataArray[];
	   // function for creating dummydata , just for example , 
    // you will populate your own data (whatever it may be, static data, or fetched from internet)
    public void getData()
    {
    	
    	dataArray = new Data[3];
    	
		for(int i=0;i<3;i++)
		{
    		dataArray[i]=new Data();
    		dataArray[i].id=i;
    		dataArray[i].username = new String("user "+i);
    		dataArray[i].likeCount=i;
    		dataArray[i].pid=i;
    		dataArray[i].nid=i;
    		dataArray[i].text=new String("Story : "+i);
       	}
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
		    
		    TextView username = (TextView) rowView.findViewById(R.id.tv_id_text);
		    username.setText(data[position].username);
		    
		    TextView text = (TextView) rowView.findViewById(R.id.tv_id_text);
		    text.setText(data[position].text);
		    
		    TextView likeCount = (TextView) rowView.findViewById(R.id.tv_id_likeCount);
		    likeCount.setText(""+data[position].likeCount);
		    
		    
		    return rowView;
		 }
	
		
	
	}


	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	{
		    Toast.makeText(getApplicationContext(),"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
	}	
}
