package com.tell.tale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener {

	Button initiate,ongoing_feed,btn_completed_stories,btn_my_ongoing,btn_my_completed,btn_profile,btn_group_management;
	int nid=1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        
        initiate = (Button) findViewById(R.id.initiate);
        ongoing_feed = (Button) findViewById(R.id.btn_id_see_ongoing_stories);
        btn_completed_stories = (Button) findViewById(R.id.btn_completed_story_feed);
        btn_my_ongoing = (Button) findViewById(R.id.btn_my_initiated_ongoing);
        btn_my_completed = (Button) findViewById(R.id.btn_my_initiated_stories);
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_group_management = (Button) findViewById(R.id.btn_group_management);
        
        initiate.setOnClickListener(this);
        ongoing_feed.setOnClickListener(this);
        btn_completed_stories.setOnClickListener(this);
        btn_my_ongoing.setOnClickListener(this);
        btn_my_completed.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_group_management.setOnClickListener(this);
    }
	public void onClick(View v)
	{
		
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent;
		switch (id) {
		case R.id.initiate:
			intent = new Intent(HomeActivity.this,InitiateActivity.class); 
			//Bundle bundle = new Bundle();
			//bundle.putInt("nid", nid);
			//intent.putExtras(bundle);
			startActivity(intent);
			break;

		case R.id.btn_id_see_ongoing_stories:
			intent = new Intent(HomeActivity.this,OnGoingFeed.class);
			startActivity(intent);
			break;
			
		case R.id.btn_completed_story_feed:
			intent = new Intent(HomeActivity.this,CompleteStoriesFeed.class);
			startActivity(intent);
			break;
			
		case R.id.btn_my_initiated_stories:
			intent = new Intent(HomeActivity.this,MyCompleteStoriesFeed.class);
			startActivity(intent);
			break;
			
		case R.id.btn_my_initiated_ongoing:
			intent = new Intent(HomeActivity.this, MyOngoingFeedActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btn_profile:
			intent = new Intent(HomeActivity.this, Profile.class);
			startActivity(intent);
			//finish();
			break;
			
		case R.id.btn_group_management:
			intent = new Intent(HomeActivity.this, GroupManagementActivity.class);
			startActivity(intent);
			finish();
		default:
			break;
		}

	}

}
