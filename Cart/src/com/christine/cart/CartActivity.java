package com.christine.cart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CartActivity extends Activity {
	
	TextView displayDays;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.cart);
	    
	    Bundle numberOfDays = getIntent().getExtras();
	    int days = numberOfDays.getInt("days");
	    
	    displayDays = (TextView) findViewById(R.id.tv_days);
	    displayDays.setText("You're shopping for " + days + " days.");
	}

}
