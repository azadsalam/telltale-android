package com.tell.tale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class ListGroupsIAmIn extends Activity implements OnClickListener
{
	RadioGroup rg_groups_i_am_in;
	Button btn_back;
	String url;
	FetchListOfGroups fetchListOfGroups;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_groups_i_am_in);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		rg_groups_i_am_in = (RadioGroup) findViewById(R.id.rg_groups_i_am_in);
		
		url = Session.baseUrl+"/index.php/groupMembership/list_of_group_from_android";
		fetchListOfGroups = new FetchListOfGroups(this,url,rg_groups_i_am_in);
		
		
	}

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(),GroupManagementActivity.class);
		startActivity(intent);
		finish();
	}
}
