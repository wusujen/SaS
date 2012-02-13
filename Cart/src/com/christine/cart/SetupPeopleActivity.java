package com.christine.cart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SetupPeopleActivity extends Activity {
	
	Button man;
	Button woman;
	Button boy;
	Button girl;
	Button next;
	TextView user_properties;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setup_people);
        
	    Bundle extras = getIntent().getExtras();
	    if(extras==null){
	    	return;
	    }
	    
	    String username = extras.getString("username");
	    String password = extras.getString("password");
	    
	    user_properties=(TextView) findViewById(R.id.user_properties);
	    if(username!=null && password!=null){
	    	user_properties.setText("username: " + username + "/n password: " + password);
	    }
	}

}
