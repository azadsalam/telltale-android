package com.tell.tale;

import java.util.HashMap;

import com.tell.tale.AppendSuggestionActivity.DemoWebServiceUser;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.content.Context;


public class DeletePost implements OnItemClickListener , OnClickListener
{
	
	Activity activity;
	Context context;
	Data[] appendedPosts;
	AppendSuggestionActivity appendSuggestionActivity;
	PopupWindow popupWindow;
	
	int pid;
	String text;
	
	public DeletePost(Activity a, Context c, AppendSuggestionActivity appendSuggestionActivity) 
	{
		// TODO Auto-generated constructor stub
		activity = a;
		context = c;

		this.appendSuggestionActivity = appendSuggestionActivity;
		
	}

	public void onItemClick(AdapterView<?> parent, View view,int position, long id)  
	{
		// TODO Auto-generated method stub
		//Toast.makeText(context, "CLICKED", Toast.LENGTH_LONG).show();
		//
		appendedPosts = appendSuggestionActivity.getAppendedPostArray();
		 pid = appendedPosts[position].pid;
		 text = appendedPosts[position].text;
		
		 showPopUp(R.layout.delete_confirm_popup, R.id.btn_delete_close_popup,position);
		//Log.d("DELETE POST","CLICKED on "+ text + " -> "+pid);
	}
	
	public void showPopUp(int popupLayout,int dismissButtonId,int position) 
	{
		LayoutInflater layoutInflater = (LayoutInflater)activity.getBaseContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);  
			    
		View popupView = layoutInflater.inflate(popupLayout, null);  
	    popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
			             
	    
	    /*INITIALISE*/
	    
	    TextView tv_post = (TextView) popupView.findViewById(R.id.tv_delete_confirm_post);
	    tv_post.setText(text);
	    
	    
	    Button btn_delete_confirm  = (Button) popupView.findViewById(R.id.btn_delete_confirm);
	    btn_delete_confirm.setOnClickListener(this);
	    
	    
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
		
		
		new DemoWebServiceUser(context);
		
		Intent intent = new Intent(activity.getApplicationContext(),MyOngoingFeedActivity.class);
		context.startActivity(intent);
		activity.finish();
		
	}

	class DemoWebServiceUser implements WebServiceUser
	{
		private HashMap<String, Object>  data;
		private HashMap<String, Object>  reply;
		private String replyTokens[];
		private WebServiceAdapter wsu;
		
		public DemoWebServiceUser(Context c) 
		{
			// TODO Auto-generated constructor stub
			
		   	data = new HashMap<String, Object>();    	
	    	data.put("pid",pid);    	
			wsu = new WebServiceAdapter(this,c,"Deleting!!",Session.baseUrl + "/index.php/postModification/post_delete_from_android",data,replyTokens,false);        
	        wsu.startWebService();
	    	
		}

		public void processResult(HashMap<String, Object> data) 
		{
			// TODO Auto-generated method stub
			
		}
		
	}

}
