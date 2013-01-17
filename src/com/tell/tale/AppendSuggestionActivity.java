package com.tell.tale;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AppendSuggestionActivity extends Activity implements OnItemClickListener,WebServiceUser, OnClickListener 
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
	int pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.append_suggestion);
		
		Bundle bundle = getIntent().getExtras();
		pid = bundle.getInt("pid");
		
		// FOR VIEWING APPENDED POSTS
        appended_listview = (ListView) findViewById(R.id.lv_append_suggestion_appended); 
        Unappended_listview = (ListView)findViewById(R.id.lv_append_suggestion_unappended);
        Unappended_listview.setOnItemClickListener(this);

		//FETCH STORY FROM SERVER
        prepareData();
		wsu = new WebServiceAdapter(this,this,"Loading Story!!","http://10.0.2.2/telltale/index.php/ongoingstory_feed/getFullStoryFromAndroid",data,replyTokens);        
        startWebService();

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
		    
		    try
		    {
		    	TextView isSuggestedEnd = (TextView) rowView.findViewById(R.id.tv_isSuggestedEnd);
		    	if(data[position].isSuggestedEnd)
	    		{
		    		isSuggestedEnd.setText("SUGGESTED END");
	    		}
		    }
		    catch (Exception e) {
				// TODO: handle exception
			}
    		
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
				dataArray[i].isSuggestedEnd = Boolean.parseBoolean(json.getString("isSuggestedEnd"));
				
				Log.d("isSuggestedEnd " +i,json.getString("isSuggestedEnd") );
			
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
		//Toast.makeText(getApplicationContext(), "HUmm", Toast.LENGTH_LONG).show();
		initiatePopupWindow();
	}
    
	
	private PopupWindow pw;
	private Button btn_close_popup;
	private void initiatePopupWindow() 
	{
		try 
		{
			LayoutInflater inflater = (LayoutInflater) AppendSuggestionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.append_confirm_popup,(ViewGroup) findViewById(R.id.layout_append_confirmation_popup));
			pw = new PopupWindow(layout,250, 200, true);
			pw.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);
			
			btn_close_popup = (Button) layout.findViewById(R.id.btn_append_close_popup);
			btn_close_popup.setOnClickListener(this);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		pw.dismiss();
	}
	
	/* 
	private OnClickListener cancel_button_click_listener = new OnClickListener() {
	    public void onClick(View v) {
	        pw.dismiss();
	    }
	};
	*/
	/* ---------FOR FETCHING DATA FROM SERVER ENDS------*/ 



	
}
