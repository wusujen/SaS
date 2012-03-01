package com.christine.cart.sqlite;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
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
  				
  				Log.d("userName:", "username is " + name);
  				
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
	 * Queries the database for the requested account
	 * If the name doesn't exist then write the account 
	 * into the database. If name matches another name,
	 * then return "0";
	 * 
	 * @param username
	 * @param password
	 */
	public String createAccount(String username, String password){
        AccountDatabaseHelper db = new AccountDatabaseHelper(this);
        final String NAME_EXISTS = "0";
        String name = null;
	 	
        try {
        	db.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database, or db has been created already");
	 	}
        //OPEN THE DATABASE
	 	try {
	 		//myDbHelper.close();
	 		db.openDataBase();
	 	} catch(SQLException sqle){
	 		throw sqle;
	 	}
	 
		 	Account accountExists = db.getAccount(username);
		 	
		    if(accountExists.getName()!=null){
		    	Log.d("Name is : ",NAME_EXISTS);	
		    	return NAME_EXISTS;
	        } else {
	        	name = username;
	        	Log.d("Insert: ", "Inserting ..");
	    	    db.addAccount(new Account(name, password));
	    	    db.close();
	        }

	    return name;
	}
}
