package com.tell.tale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GroupManagementActivity extends Activity implements OnClickListener {

	Button btn_create_group,btn_list_my_created_groups,btn_list_groups_i_am_in;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_management);
        
        btn_create_group = (Button) findViewById(R.id.btn_create_group);
        btn_list_my_created_groups = (Button) findViewById(R.id.btn_see_my_created_groups);
        btn_list_groups_i_am_in = (Button) findViewById(R.id.btn_see_groups_i_am_in);
        btn_create_group.setOnClickListener(this);
        btn_list_my_created_groups.setOnClickListener(this);
        btn_list_groups_i_am_in.setOnClickListener(this);
     
    }
	public void onClick(View v)
	{
		
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent;
		switch (id) 
		{
			case R.id.btn_create_group:
				intent = new Intent(this,CreateGroupActivity.class);
				startActivity(intent);
				finish();
				break;
			
			case R.id.btn_see_my_created_groups:
				intent = new Intent(this,ListGroupIOwn.class);
				startActivity(intent);
			//
				finish();
				break;
				
			case R.id.btn_see_groups_i_am_in:
				intent = new Intent(this,ListGroupsIAmIn.class);
				startActivity(intent);
				finish();
				
				break;
			
			default:
			break;
		}

	}

}
