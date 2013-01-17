package com.tell.tale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener {

	Button initiate,ongoing_feed,btn_completed_stories;
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
        
        initiate.setOnClickListener(this);
        ongoing_feed.setOnClickListener(this);
        btn_completed_stories.setOnClickListener(this);
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
		default:
			break;
		}

	}

}
