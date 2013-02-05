package com.tell.tale;

import org.json.JSONObject;

import android.util.Log;

public class PopulateDataArrayForStory 
{
	public static void populateDataArray(JSONObject jsonArray, Data dataArray[],int count) 
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
				int tmp = Integer.parseInt(json.getString("is_liked"));
				dataArray[i].isLikedByUser = (tmp==0)?false:true;
				
				
			
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
