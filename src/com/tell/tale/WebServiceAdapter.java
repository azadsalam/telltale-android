package com.tell.tale;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class WebServiceAdapter 
{
	protected ProgressDialog pd;
	protected Context context;
	protected WebServiceAsyncTask asyncTask;
	protected WebServiceUser user;
	protected String key[];
	protected String value[];
	protected String replyTokens[];
	protected String loadingText;
	protected HashMap<String, Object> data;
	protected HashMap<String, Object> reply;
	
	public WebServiceAdapter(WebServiceUser user,Context ct,String loadText,String url,HashMap<String, Object> data,String replyTokens[]) 
	{
		// TODO Auto-generated constructor stub	
		this.user = user; 
		context=ct;
		loadingText	 = loadText;
		this.data = data;
		this.replyTokens = replyTokens;
		asyncTask = new WebServiceAsyncTask(this,url,data);
		reply = new HashMap<String, Object>();
		
	}
	
	
	
	public void startWebService()
	{
		asyncTask.execute("");
	}
	
	//process the json data replied from server
	public void processReply(String jsonData)
	{	

		reply.put("error", false);
		if(replyTokens != null && replyTokens.length>0 && jsonData != null)
		{
		
			try 
			{
				JSONObject jsonObject = new JSONObject(jsonData);
				
				for(int i=0;i<replyTokens.length;i++)
				{
					if(jsonObject.get(replyTokens[i]) != null)
						reply.put(replyTokens[i], jsonObject.get(replyTokens[i]));
					else
						reply.put(replyTokens[i],null);
					
					Log.d("SERVER REPLY",replyTokens[i] + " ->"+jsonObject.get(replyTokens[i]));
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				reply.put("error", true);
			}
		}
		//Log.d("ME", jsonData);
		
		user.processResult(reply);
		pd.dismiss();
	}
	
	public void setContext(Context ct)
	{
		context=ct;
	}
	
	public void showProcessDialog()
	{
		pd = ProgressDialog.show(context, "", loadingText);
	}

}
