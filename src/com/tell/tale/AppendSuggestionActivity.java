package com.tell.tale;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AppendSuggestionActivity extends Activity implements OnItemClickListener,WebServiceUser,OnClickListener 
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
	
	
	PopupWindow popupWindow;
	int appendedPid;
	
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
		wsu = new WebServiceAdapter(this,this,"Loading Story!!","https://telltale-azad.rhcloud.com/index.php/ongoingStory_feed/getFullStoryFromAndroid",data,replyTokens);        
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
				dataArray[i].isSuggestedEnd = (json.getString("isSuggestedEnd").equals("1"))?true:false;
				Log.d("TRACK END of row "+i,json.getString("isSuggestedEnd"));
				
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
	/* ---------FOR FETCHING DATA FROM SERVER ENDS------*/ 

	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	{
		// TODO Auto-generated method stub
		
		//Toast.makeText(getApplicationContext(), "HUmm", Toast.LENGTH_LONG).show();
		
		appendedPid = unappendedPosts[position].pid;
		showPopUp(R.layout.append_confirm_popup, R.id.btn_append_close_popup,position);
		
	}
	
	
	public void showPopUp(int popupLayout,int dismissButtonId,int position) 
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
			    
		View popupView = layoutInflater.inflate(popupLayout, null);  
	    popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
			             
	    
	    /*INITIALISE*/
	    
	    TextView tv_post = (TextView) popupView.findViewById(R.id.tv_append_confirm_post);
	    tv_post.setText(unappendedPosts[position].text);
	    
	    
	    Button btn_approve_append_suggestion  = (Button) popupView.findViewById(R.id.btn_approve_append_suggestion);
	    btn_approve_append_suggestion.setOnClickListener(this);
	    
	    if(unappendedPosts[position].isSuggestedEnd == false)
	    {
	    	TextView tv_isSuggestedEnd = (TextView) popupView.findViewById(R.id.tv_append_confirm_isSuggestedEnd);
	    	tv_isSuggestedEnd.setText("");
	    }
	    
	    Button btnDismiss = (Button)popupView.findViewById(dismissButtonId);
	    btnDismiss.setOnClickListener(new Button.OnClickListener(){

			     public void onClick(View v) {
			      // TODO Auto-generated method stub
			      popupWindow.dismiss();
			     }});
			               
	   // popupWindow.showAsDropDown(btnOpenPopup, 50, -30);
	    popupWindow.showAtLocation(popupView.getRootView(),Gravity.CENTER, 0, 0);

	}
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		
		//popupWindow.dismiss();
		new DemoWebServiceUser(this);
		
		Intent intent = new Intent(this,AppendSuggestionActivity.class);
		Bundle xtra = new Bundle();
		xtra.putInt("pid", pid);
		intent.putExtras(xtra);
		startActivity(intent);
		//finish();
	}
	

	
	class DemoWebServiceUser implements WebServiceUser
	{
		private HashMap<String, Object>  data;
		private HashMap<String, Object>  reply;
		private String replyTokens[];
	
		
		public DemoWebServiceUser(Context c) 
		{
			// TODO Auto-generated constructor stub
			
		   	data = new HashMap<String, Object>();    	
	    	data.put("pid",appendedPid);    	
			wsu = new WebServiceAdapter(this,c,"Updating your story!!","https://telltale-azad.rhcloud.com/index.php/add_comment/appendFromAndroid",data,replyTokens);        
	        startWebService();

	    	
		}

		public void processResult(HashMap<String, Object> data) 
		{
			// TODO Auto-generated method stub
			
		}
		
	}
	




	
}
