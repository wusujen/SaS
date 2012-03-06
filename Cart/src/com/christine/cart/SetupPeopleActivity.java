package com.christine.cart;


import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.PersonActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	
	private static String USERNAME = null;
    private static String PASSWORD = null;
    
    public static final int MAN = 1001;
    public static final int WOMAN = 1002;
    public static final int BOY = 1003;
    public static final int GIRL = 1004;
    
	static int countMan=0;
	static int countWoman=0;
	static int countBoy=0;
	static int countGirl=0;
	
	AccountDatabaseHelper db;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setup_people);
	    
	    //set up gui elements
	    man = (Button) findViewById(R.id.btn_man);
	    woman = (Button) findViewById(R.id.btn_woman);
	    boy = (Button) findViewById(R.id.btn_boy);
	    girl = (Button) findViewById(R.id.btn_girl);
	    user_properties = (TextView) findViewById(R.id.user_properties);
	    
	    
	    man.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countMan++;
				addPersonIntent(v, MAN);
			}
		});
	    
	    woman.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countWoman++;
				addPersonIntent(v, WOMAN);
			}
		});
	    
	    boy.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countBoy++;
				addPersonIntent(v, BOY);
			}
		});
	    
	    girl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				countGirl++;
				addPersonIntent(v, GIRL);
			}
		});
	    
	    
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
	
	@Override
	protected void onResume(){
		super.onResume();
		//setup database helper
	    db = new AccountDatabaseHelper(this);
	    
	    // Get the username from the intent
 		USERNAME = getIntent().getStringExtra("username");
 	    if(USERNAME==null){
 	    	Log.d("Error with UserName:", "username returned null");
 	    	return;
 	    } else{
 	    	Log.d("UserName:", USERNAME);
 	    	Account act = db.getAccount(USERNAME);
 	    	if(act != null){
 	    		USERNAME = act.getName();
 	    		PASSWORD = act.getPassword();
 		    	Log.d("Account Info: ", "username: " + USERNAME + "password: " + PASSWORD);
 		    	user_properties.setText("username: " + USERNAME + "/n password: " + PASSWORD);
 	    	} else{
 	    		Log.d("Fail: ", "unsuccessful retrieval");
 	    	}
 	    }
 	    
 	    user_properties.append(String.valueOf(db.getAllPeopleFor(USERNAME).size()));
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		db.close();
	}
	
	
	// Convenience method to start an intent request
	// to person activity.
	void addPersonIntent(View v, int requestCode){
		Intent addPerson = new Intent(v.getContext(), PersonActivity.class);
		
		Bundle userInfo = new Bundle();
		userInfo.putInt("requestCode", requestCode);
		userInfo.putString("username", USERNAME);
		
		addPerson.putExtras(userInfo);
		startActivityForResult(addPerson, requestCode);
	}
	
}
