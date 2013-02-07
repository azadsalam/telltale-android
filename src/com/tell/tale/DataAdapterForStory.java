package com.tell.tale;

import java.util.HashMap;

import com.tell.tale.R.drawable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;



class DataAdapterForStory extends ArrayAdapter<Data> implements OnClickListener
{
	final Context context;
	final Data[] data;
	int row_layout;
	int buttonId;
	Activity activity;
	int nid;
	boolean showIsSuggestedEndText=false;
	public DataAdapterForStory(Context c,Activity activity,Data[] d,int row_layout,int buttonId) 
	{
		// TODO Auto-generated constructor stub
		super(c,row_layout,d);
		context = c;
		data = d;
		this.row_layout= row_layout;
		this.buttonId = buttonId;
		this.activity = activity;
		
		SharedPreferences myPrefs = context.getSharedPreferences("telltaleprefs",activity.MODE_WORLD_READABLE);
		nid = myPrefs.getInt("nid", 0);
	}
	
	// this getView function automatically 
	// fills up your each row with your data object
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(row_layout, parent, false);
		
		
	    TextView text = (TextView) rowView.findViewById(R.id.tv_id_text);
	    //data[position].text.t
	    text.setText(data[position].text.trim());
	    
	    TextView likeCount = (TextView) rowView.findViewById(R.id.tv_id_likeCount);
	    likeCount.setText(""+data[position].likeCount);
	    
	    TextView username = (TextView) rowView.findViewById(R.id.tv_id_username);
	    username.setText(data[position].username);
		
	    if(showIsSuggestedEndText)
	    {
		    TextView isSuggestedEnd = (TextView) rowView.findViewById(R.id.tv_isSuggestedEnd);
		    try
		    {
	
		    	if(data[position].isSuggestedEnd)
	    		{
		    		isSuggestedEnd.setText("SUGGESTED END");	
	    		}
		    	
		    }
		    catch (Exception e) {
				// TODO: handle exception
			}
	    }

	    
	    Button btn = (Button) rowView.findViewById(buttonId);
		
	    if(data[position].isLikedByUser)
	    {
	    	btn.setBackgroundResource(drawable.liked_after);
	    	btn.setEnabled(false);
		}
	    else
	    {
	    	btn.setTag(data[position]); 
			btn.setOnClickListener(this);
	    }
		return rowView;
	}
	

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		int vid = v.getId();
		
		switch (vid) 
		{
		case R.id.btn_like_appended:
		case R.id.btn_like_unappended:
			
			
			
			Data dd = (Data) v.getTag();  // first get the object attached to the clicked element
										  // then just use the object :D 
			//Toast.makeText(getApplicationContext(),"You liked "+dd.text, Toast.LENGTH_LONG).show();
			
			
			//Toast.makeText(context, v.toString(), Toast.LENGTH_LONG).show();
			
			
			//Log.d("VIEW", v.toString());
			like(dd.pid,(Button)v);
			
			//Button btn = (Button) v;
			//btn.setBackgroundResource(drawable.ic_launcher);
			
			// Do Things to Like
			break;

		default:
			break;
		}
		
	}
	

    
    public void like(int pid,Button btn) 
    {
    	btn.setEnabled(false);
    	
    	new DemoWebServiceUser(context, pid, nid,btn);
    	
		//btn.setBackgroundResource(R.drawable.liked_after);
		
	}
	class DemoWebServiceUser implements WebServiceUser
	{
		private HashMap<String, Object>  data;
		private HashMap<String, Object>  reply;
		private String replyTokens[];
		private Button btn;
		private WebServiceAdapter wsu;
		
		public DemoWebServiceUser(Context c,int pid,int nid,Button btn) 
		{
			// TODO Auto-generated constructor stub
			
		   	data = new HashMap<String, Object>();    	
	    	data.put("pid",pid);
	    	data.put("nid", nid);
	    	this.btn = btn;
			wsu = new WebServiceAdapter(this,c,"Updating your story!!",Session.baseUrl + "/index.php/like/like_from_android",data,replyTokens,false);        
	        wsu.startWebService();

	    	
		}

		public void processResult(HashMap<String, Object> reply) 
		{
			// TODO Auto-generated method stub
			
			if(((Boolean)reply.get("error")).booleanValue()==true)
			{
				Toast.makeText(activity, "Could Not Connect to Server!", Toast.LENGTH_LONG).show();
			}
			else
			{
				if(btn != null)
					btn.setBackgroundResource(R.drawable.liked_after);
				
				
			}
		}
		
	}

}

