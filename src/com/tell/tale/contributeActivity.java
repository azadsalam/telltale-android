package com.tell.tale;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ContributeActivity extends Activity implements OnClickListener, WebServiceUser
{
 
	int nid;
	int lastAppendedPostId;
	
	Button btn_submit_suggestion;
	EditText et_suggestion;
	CheckBox cb_is_this_end;
	
	HashMap<String, Object> data ;    				
	String replyTokens[] = {""};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contribute);
		
		
		Bundle bundle = getIntent().getExtras();
		lastAppendedPostId = bundle.getInt("lastAppendedPostId");
		
		SharedPreferences myPrefs = this.getSharedPreferences("telltaleprefs", MODE_WORLD_READABLE);
		nid = myPrefs.getInt("nid", 0);
		
		btn_submit_suggestion = (Button) findViewById(R.id.btn_submit_suggestion);
		btn_submit_suggestion.setOnClickListener(this);
		et_suggestion = (EditText) findViewById(R.id.et_contribution_txt);
		cb_is_this_end = (CheckBox) findViewById(R.id.cb_is_this_end);
		
		//Log.d("CONTRIBUTION ACTIVITY : ", "nid "+nid+" lastappendedpostid "+lastAppendedPostId);
	}


	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		
		String text = et_suggestion.getEditableText().toString();
		Boolean isThisEnd = cb_is_this_end.isChecked();
		
		//Log.d("CONTRIBUTION ACTIVITY : ", "text : "+text + " end?? "+isThisEnd.toString());
		
		//        public function add_comment_to_story($nid,$parent,$text,$isSuggestedEnd)
		
		WebServiceAdapter wsu;
		data = new HashMap<String, Object>();
		data.put("nid",nid);
    	data.put("parent",lastAppendedPostId);
    	data.put("text", text);
    	data.put("isSuggestedEnd", isThisEnd);
    	
    	//reply tokens
    	
        wsu = new WebServiceAdapter(this,this,"Adding your Contribution!!","http://10.0.2.2/telltale/index.php/add_comment/addSuggestionFromAndroid",data,replyTokens);
        wsu.startWebService();
		
	}


	public void processResult(HashMap<String, Object> data) 
	{
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Added your suggestion", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
	}
}

