package com.tell.tale;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.Toast;

public class FetchListOfGroups implements WebServiceUser
{
	String url;
	int nid;
	WebServiceAdapter wsu;
	Context context;
	RadioGroup radioGroup;
	
	private HashMap<String, Object>  data;
//	private HashMap<String, Object>  reply;
	private String replyTokens[];

	public GroupInfo groupList[];
	
	
	public boolean populateRadioGroup=true;
	FetchListOfGroups(Context context,String url,RadioGroup rg)
	{
			this.url = url;
			this.context = context;
			radioGroup = rg;
			
			init();
			
	}
	
	public void init() 
	{
		SharedPreferences myPrefs = context.getSharedPreferences("telltaleprefs", Context.MODE_WORLD_READABLE);
		nid = myPrefs.getInt("nid", 0);

		data = new HashMap<String, Object>();
		
		data.put("nid", nid);
		
		replyTokens = new String[2];
		replyTokens[0] = "count";
		replyTokens[1] = "data";
		

		
        wsu = new WebServiceAdapter(this,context,"Listing Story Circles !!",url,data,replyTokens);
        
		wsu.startWebService();
	}

	public void fetch() 
	{
		wsu = new WebServiceAdapter(this,context,"Listing Story Circles !!",url,data,replyTokens);
		wsu.startWebService();
	}


	public void processResult(HashMap<String, Object> reply) 
	{
		// TODO Auto-generated method stub
		
		
		//Log.d("Count",count + "");
		
		try 
		{
			int count = Integer.parseInt(reply.get(replyTokens[0]).toString());
			
			if(count ==0) return;
		
			JSONObject groupListJson = new JSONObject(reply.get(replyTokens[1]).toString());
			
			groupList = new GroupInfo[count];
			
			for(int i = 0; i<count;i++)
			{
				JSONObject groupInfo = new JSONObject(groupListJson.get(""+i).toString());
				
				int grpid = Integer.parseInt(groupInfo.get("grpid").toString());
				String name = groupInfo.getString("name");
				
				groupList[i] = new GroupInfo();
				groupList[i].grpid = grpid;
				groupList[i].name = name;
				
				
				//groupList[i].ownerNid = nid;
				
				//Log.d("grpid",""+grpid);
				//Log.d("name",name);
				
			}
			
			populateRadioGroup();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			Toast.makeText(context, "Couldnt Connect To Server", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
	}
	
	public void populateRadioGroup() 
	{
		if(radioGroup==null || populateRadioGroup==false)
			return;
		
		
	    for (int i=0 ; i<groupList.length;i++) 
	    {
	        RadioButton radioButton = new RadioButton(context);
            radioButton.setText(groupList[i].name);
            radioButton.setId(groupList[i].grpid);
            radioButton.setTextColor(R.color.text_dark_green);

            radioGroup.addView(radioButton);
        }
	    
        radioGroup.invalidate();
	}

}
