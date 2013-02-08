package com.tell.tale;

import java.util.HashMap;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ListGroupIOwn extends Activity implements OnClickListener
{
	PopupWindow popupWindow;
	FetchListOfGroups fetchListOfGroups;
	String url;
	RadioGroup rg_groups_i_own;
	Button btn_add_remove_member,btn_delete_group,btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_groups_i_own);
		
		btn_add_remove_member = (Button) findViewById(R.id.btn_add_remove_member);
		btn_add_remove_member.setOnClickListener(this);
		
		btn_delete_group = (Button) findViewById(R.id.btn_delete_group);
		btn_delete_group.setOnClickListener(this);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		
		rg_groups_i_own = (RadioGroup) findViewById(R.id.rg_groups_i_own);
		
		url = Session.baseUrl+"/index.php/group/list_group_from_android";
		fetchListOfGroups = new FetchListOfGroups(this,url,rg_groups_i_own);
		
	}
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent;
		
		int selectedGroupId = rg_groups_i_own.getCheckedRadioButtonId();
		Log.d("GRPID",selectedGroupId+"");
		

		switch (id) 
		{
		case R.id.btn_add_remove_member:
			if(selectedGroupId == -1)
				return;
		
			showAddRemovePopUp(R.layout.add_remve_member_from_group_popup, R.id.btn_add_remove_from_group_popup_close, selectedGroupId);
			break;
		case R.id.btn_delete_group:
			if(selectedGroupId == -1)
				return;
		
			//Log.d("clicked","clicked");
			showDeletePopUp(R.layout.delete_group_confirm_popup,R.id.btn_delete_confirm_popup_close,selectedGroupId);
			break;

		default:
			intent = new Intent(getApplicationContext(),GroupManagementActivity.class);
			startActivity(intent);
			finish();
			
			break;
		}
	
	}
	
	
	public void showAddRemovePopUp(int popupLayout,int dismissButtonId,int grpId) 
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
			    
		View popupView = layoutInflater.inflate(popupLayout, null);  
	    popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);  
			             
	    
	    /*INITIALISE*/
	    String groupName="";
	    GroupInfo[] list =fetchListOfGroups.groupList ;
	    for(int i=0;i<list.length;i++)
	    {
	    	if(list[i].grpid == grpId)
	    	{
	    		groupName = list[i].name;
	    		break;
	    	}
	    }
	    
	    
	    
	    TextView tv_post = (TextView) popupView.findViewById(R.id.tv_add_remove_from_group);	    
	    tv_post.setText("Insert the Mail ID of the person you want to add or remove from " + groupName +"!!");
	    

	    
	    DemoWebServiceUserForAddRemoveMember demoWebServiceUser = new DemoWebServiceUserForAddRemoveMember(this,grpId,popupView,groupName);
	    
	    Button btn_delete_group_confirm  = (Button) popupView.findViewById(R.id.btn_add_member_to_group);
	    btn_delete_group_confirm.setOnClickListener(demoWebServiceUser);
	    
	    Button btn_delete_member  = (Button) popupView.findViewById(R.id.btn_remove_member_from_group);
	    btn_delete_member.setOnClickListener(demoWebServiceUser);
	    
	    
	    Button btnDismiss = (Button)popupView.findViewById(dismissButtonId);
	    btnDismiss.setOnClickListener(new Button.OnClickListener(){

			     public void onClick(View v) {
			      // TODO Auto-generated method stub
			      popupWindow.dismiss();
			     }});
			               
	   // popupWindow.showAsDropDown(btnOpenPopup, 50, -30);
	    popupWindow.showAtLocation(popupView.getRootView(),Gravity.CENTER, 0, 0);

	}

	
	public void showDeletePopUp(int popupLayout,int dismissButtonId,int grpId) 
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
			    
		View popupView = layoutInflater.inflate(popupLayout, null);  
	    popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
			             
	    
	    /*INITIALISE*/
	    String groupName="";
	    GroupInfo[] list =fetchListOfGroups.groupList ;
	    for(int i=0;i<list.length;i++)
	    {
	    	if(list[i].grpid == grpId)
	    	{
	    		groupName = list[i].name;
	    		break;
	    	}
	    }
	    
	    TextView tv_post = (TextView) popupView.findViewById(R.id.tv_delete_group_confirm_group_name);
	    
	    tv_post.setText(groupName);
	    
	    DemoWebServiceUserForDelete demoWebServiceUser = new DemoWebServiceUserForDelete(this,grpId);
	    
	    Button btn_delete_group_confirm  = (Button) popupView.findViewById(R.id.btn_approve_append_suggestion);
	    btn_delete_group_confirm.setOnClickListener(demoWebServiceUser);
	    
	    
	    Button btnDismiss = (Button)popupView.findViewById(dismissButtonId);
	    btnDismiss.setOnClickListener(new Button.OnClickListener(){

			     public void onClick(View v) {
			      // TODO Auto-generated method stub
			      popupWindow.dismiss();
			     }});
			               
	   // popupWindow.showAsDropDown(btnOpenPopup, 50, -30);
	    popupWindow.showAtLocation(popupView.getRootView(),Gravity.CENTER, 0, 0);

	}

	class DemoWebServiceUserForDelete implements WebServiceUser,OnClickListener
	{
		private HashMap<String, Object>  data;
		private HashMap<String, Object>  reply;
		private String replyTokens[];
		private WebServiceAdapter wsu;
		
		public DemoWebServiceUserForDelete(Context c, int grpid) 
		{
			// TODO Auto-generated constructor stub
			
		   	data = new HashMap<String, Object>();    	
	    	data.put("grpid",grpid);    	
		   	
			wsu = new WebServiceAdapter(this,c,"Updating your story!!",Session.baseUrl + "/index.php/group/delete_group_from_android",data,replyTokens);        
	    	
		}
		
		public void startWebService() {
			wsu.startWebService();
		}

		public void processResult(HashMap<String, Object> data) 
		{
			// TODO Auto-generated method stub
			
			Intent intent = new Intent(getApplicationContext(),GroupManagementActivity.class);
			startActivity(intent);
			finish();
		}

		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			startWebService();
		}
		
	}

	
	class DemoWebServiceUserForAddRemoveMember implements WebServiceUser,OnClickListener
	{
		private HashMap<String, Object>  data;
		private HashMap<String, Object>  reply;
		private String replyTokens[];
		private WebServiceAdapter wsu;
		private String grpName;
		
		View popupView;
		
		private String addURL = Session.baseUrl + "/index.php/groupMembership/add_member_from_android";
		private String removeURL = Session.baseUrl + "/index.php/groupMembership/remove_member_from_android";
		
		private Context context;
		private int grpid;
		
		public DemoWebServiceUserForAddRemoveMember(Context c, int grpid,View popupview, String grpnm) 
		{
			// TODO Auto-generated constructor stub

			this.popupView = popupview;
		   	this.grpName = grpnm;
		   	
	    	context = c;
	    	this.grpid = grpid;
	    	
		}
		
		public void startWebService() 
		{
			
			wsu.startWebService();
		}

		public void processResult(HashMap<String, Object> data) 
		{
			// TODO Auto-generated method stub
			
			Intent intent = new Intent(getApplicationContext(),GroupManagementActivity.class);
			startActivity(intent);
			finish();
		}

		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
		 	
			String mail;
		    EditText et = (EditText) popupView.findViewById(R.id.et_add_remove_grom_group_mail);
		    
		    if(et.getText()!=null && et.getText().toString().equals("")==false )
		    {
		    	mail = et.getText().toString();
		    }
		    else 
		    {
		    	Log.d("ET", "NULL");
		    	return;
		    }
		    
		    	
		    data = new HashMap<String, Object>();    	
	    	data.put("grpid",grpid);
	    	data.put("mail",mail);   
	    	
			switch (v.getId()) 
			{
			case R.id.btn_add_member_to_group:
				wsu = new WebServiceAdapter(this,context,"Updating "+grpName+"",addURL,data,replyTokens);        
				break;

			case R.id.btn_remove_member_from_group:
				wsu = new WebServiceAdapter(this,context,"Updating "+grpName+"",removeURL,data,replyTokens);        
				break;
			default:
				break;
			}
			
			
			startWebService();
		}
		
	}

	
}
