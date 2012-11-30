package com.tell.tale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class WebServiceAsyncTask extends AsyncTask<String, String, String>
{
	
	protected WebServiceAdapter webServiceUser;
	protected String url;
	protected HashMap<String, Object> data;
	public WebServiceAsyncTask(WebServiceAdapter user,String url,HashMap<String	, Object> data)  
	{
		// TODO Auto-generated constructor stub
		
		webServiceUser = user;
		this.url = url;
		this.data=data;
	}
	
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		webServiceUser.showProcessDialog();
	}
	
	@Override
	protected String doInBackground(String... params) 
	{
		//HashMap<String, Object> data= params[0];
		// TODO Auto-generated method stub
		String text =null;
	       // Create a new HttpClient and Post Header

       HttpClient httpclient = new DefaultHttpClient();
       HttpPost httppost = new HttpPost(url);

       JSONObject json = new JSONObject();

       try 
       {

	           // JSON data:

           //json.put("lat", 1);
           //json.put("long", 2);
    	   
    	   
    	 ///  json.put("lat", data.get("lat"));
    	   //json.put("lon", data.get("lon"));
    	   
    	   
    
    	   for (Map.Entry entry : data.entrySet()) 
    	   {
    	        String key = (String)entry.getKey();
    	       Object value = entry.getValue();
    	       json.put(key, value);
    	       
    	       Log.d("ME", key + " : " + value.toString());
    	   }

           JSONArray postjson=new JSONArray();
           postjson.put(json);


           // Post the data:

           httppost.setHeader("json",json.toString());

           httppost.getParams().setParameter("jsonpost",postjson);

	           // Execute HTTP Post Request

           //System.out.print(json);

           HttpResponse response = httpclient.execute(httppost);

	           // for JSON:

           if(response != null)
           {

               InputStream is = response.getEntity().getContent();
               BufferedReader reader = new BufferedReader(new InputStreamReader(is));
               StringBuilder sb = new StringBuilder();
               String line = null;
               
               try 
               {
                   while ((line = reader.readLine()) != null) 
                   {
                       sb.append(line + "\n");
                   }

               } 
               catch (IOException e) 
               {
                   e.printStackTrace();
               } 
               finally {
                   try {
                       is.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
	               text = sb.toString();
	           }
	           
	           
	           
	           return text;
	          
	          // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

	   
       }
       
	   catch (Exception e) 
       {
	   		// TODO Auto-generated catch block
	   		
	   		e.printStackTrace();
	   		//WebServiceAdapter.publishResult("CAN NOT CONNECT TO SERVER");
	   		return null;
	   		
	   	}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);	
		Log.d("ME", "res :"+result);
		webServiceUser.processReply(result);
	}

}
