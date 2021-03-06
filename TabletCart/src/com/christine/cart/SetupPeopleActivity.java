package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetupPeopleActivity extends Activity {
	
	Button man;
	Button woman;
	Button boy;
	Button girl;
	Button next;
	TextView user_properties;
	
	static int countMan=0;
	static int countWoman=0;
	static int countBoy=0;
	static int countGirl=0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setup_people);
	    
	    //set up buttons
	    man = (Button) findViewById(R.id.btn_man);
	    woman = (Button) findViewById(R.id.btn_woman);
	    boy = (Button) findViewById(R.id.btn_boy);
	    girl = (Button) findViewById(R.id.btn_girl);
	    
	    man.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countMan++;
			}
		});
	    
	    woman.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countWoman++;
			}
		});
	    
	    boy.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countBoy++;
			}
		});
	    
	    girl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countGirl++;
			}
		});
	    
		//Retrieve information from the passed Bundle to 
		//get user information and display it
		Bundle userInfo = getIntent().getExtras();
	    if(userInfo==null){
	    	return;
	    }
	    
	    String username = userInfo.getString("username");
	    String password = userInfo.getString("password");
	    
	    user_properties=(TextView) findViewById(R.id.user_properties);
	    if(username!=null && password!=null){
	    	user_properties.setText("username: " + username + "\n password: " + password);
	    }
	    
	    
	    //start SetupDays and pass the user information along
	    //with the number of people when "btn_next" is clicked
	    next = (Button) findViewById(R.id.btn_next);
	    next.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent startSetupDays=new Intent(v.getContext(),SetupDaysActivity.class);
				
				Bundle people = new Bundle();
				people.putInt("man", countMan);
				people.putInt("woman", countWoman);
				people.putInt("boy", countBoy);
				people.putInt("girl", countGirl);
				
				startSetupDays.putExtras(people);
				
				startActivity(startSetupDays);
			}
		});
	    
	}

}
