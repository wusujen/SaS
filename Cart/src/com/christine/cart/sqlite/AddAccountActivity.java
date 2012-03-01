package com.christine.cart.sqlite;


import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AddAccountActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
    	//Retrieve information from the passed Bundle to 
  		//get user information and display it
  		Bundle userInfo = getIntent().getExtras();
  	    if(userInfo==null){
  	    	return;
  	    }
  	    else{
  		    String username = userInfo.getString("username");
  		    String password = userInfo.getString("password");
  		    
  			if(username!=null && password!=null){
  				//fetch the account that was created
  				String name= createAccount(username,password);
  				
  				Log.d("userName:", "username is" + name);
  				
  				//start an intent to return to create account activity
  				Intent accountCreated = new Intent();

  				//attach integer to intent
  				accountCreated.putExtra("username", name);
  				
  				//return to original activity
  				setResult(RESULT_OK,accountCreated);
  				finish();
  			}
  	    }
	}
	
	
	/**
	 * Creates the database if it does not exist
	 * Opens the database to store user information
	 * Returns the int ID
	 * 
	 * @param username
	 * @param password
	 */
	public String createAccount(String username, String password){
        AccountDatabaseHelper db = new AccountDatabaseHelper(this);
        int id=0;
        String n=null;
        String p=null;
	 	
        /**
         * CRUD Operations
         * */
        // Reading all account
        Log.d("Reading: ", "Reading all accounts..");
        List<Account> accounts = db.getAllAccounts();       
        
        for (Account act : accounts) {
        	id = act.getId();
            n = act.getName();
            p = act.getPassword();
            
            String log = "Id: "+ id +" ,Name: " + n + " ,Password: " + p;
            // Writing Contacts to log
            Log.d("User fetched: ",log);
            
            if(username == n){
            	n="0";
            	break;
            } else{
            	//Inserting User Information
                Log.d("Insert: ", "Inserting ..");
                db.addAccount(new Account(username, password));
            }
        }
        
        db.close();
        return n;
	}
}
