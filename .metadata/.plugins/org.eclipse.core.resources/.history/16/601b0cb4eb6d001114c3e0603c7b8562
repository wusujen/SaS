package com.christine.cart.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DaysActivity extends Activity {
	
	AccountDatabaseHelper db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    db = new AccountDatabaseHelper(this);
	    Bundle dayInfo = getIntent().getExtras();
	    
	    int dayNumber = dayInfo.getInt("days");
	    String username = dayInfo.getString("username");
	    
	    Account act = db.getAccount(username);
	    
	    int updateDB = db.updateAccountDays(act, dayNumber);
	    Log.d("Updated: ", "Update successful, inserted " + updateDB + " into row");
	    db.close();
	    
	    if(updateDB!=0){
	    	Intent startCart = new Intent();
	    	startCart.putExtra("username", username);
	    	setResult(RESULT_OK, startCart);
	    	finish();
	    } else {
	    	throw new RuntimeException("Update failed");
	    	/*
	    	Intent updateFailed = new Intent(this, SetupDaysActivity.class);
	    	updateFailed.putExtra("failed", "Days failed to Add. Please try again!");
	    	startActivity(updateFailed);
	    	*/
	    }  
	}
}
