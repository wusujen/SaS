package com.christine.cart;

import java.io.IOException;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.NutritionDatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;


public class InputDatabaseSearchActivity extends Activity {
	Context inputsContext;
	//TextView outputText; //for debugging purposes
	
	String pluCode=null;
	String barcodeItem=null;
	
	NutritionDatabaseHelper ndb;
	AccountDatabaseHelper adb;
	
	Account act;
	String username;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // setContentView(R.layout.search_barcode); //for debugging purposes
	    
	    Bundle itemInfo = getIntent().getExtras();
	    if(itemInfo == null){
	    	return;
	    }
	    
	    act = itemInfo.getParcelable("account");
	    if(act!=null){
	    	username = act.getName();
	    } else{
	    	throw new RuntimeException("InputDBSearchActivity account is blank");
	    }
	    
	    //See if it is a BARCODE or PLU intent
	    String ifPLU=itemInfo.getString("plu");
	    if(ifPLU != null){
	    	pluCode = ifPLU;
	    }
	    
	    String ifBarcode=itemInfo.getString("resultDesc");
	    if(ifBarcode != null){
	    	barcodeItem = ifBarcode;
	    	//Log.d("Barcode Code: ", "Barcode :" + barcodeItem);
	    }

        ndb = startNutritionDB();
        adb = startAccountDB();
	 	
	 	if(pluCode != null){ 
	 	    String itemName = ndb.getPLUItem(Integer.parseInt(pluCode));
	 	    GroceryItem resultItem = ndb.getGroceryItem(itemName, username);
	 	    
	 	    if(resultItem!=null){
	 	    	String result = resultItem.getItemName();
	 	    	
	 	    	adb.close();
	 	    	ndb.close();
	 	    	
	 	    	resultItem.setQuantity(-1);
	 	    	
	 	    	Intent returnWithItem = new Intent();
		        returnWithItem.putExtra("itemname", result);
		        returnWithItem.putExtra("gItem", resultItem);
		        setResult(RESULT_OK, returnWithItem);
		        finish();

	 	    } else {
	 	    	adb.close();
	 	    	ndb.close();
	 	    	
	 	    	Intent returnWithNoItem = new Intent();
		        setResult(RESULT_CANCELED, returnWithNoItem);
		        finish();
	 	    }
	 	}
	 	else if(barcodeItem!=null){
	 	    GroceryItem resultItem = ndb.getGroceryItem(barcodeItem, username);
	 	    
	 		//add that item to the user's current cart
 	    	GroceryItem gItem = adb.getGroceryItemOf(username, barcodeItem);
 	    	if(gItem!=null){
 	    		int q = gItem.getQuantity();
 	    		resultItem.setQuantity(q+1);
 	    		
 	    		adb.updateGroceryItem(resultItem);
 	    		//Log.d("Result Item: ", "Result Item: " + resultItem.getItemName() + 
 	    				//"Result Quantity: " + resultItem.getQuantity());
		        adb.close();
		        ndb.close();
		        startShowResultsIntent(barcodeItem);
 	    	}  else {
 	    		resultItem.setQuantity(1);
 	    		
 	    		adb.addGroceryItem(resultItem);
 	    		//Log.d("Result Item: ", "Result Item: " + resultItem.getItemName() + 
 	    				//"Result Quantity: " + resultItem.getQuantity());
 	    		adb.close();
		        ndb.close();
		        startShowResultsIntent(barcodeItem);
 	    	}
	 	} else{
	 		adb.close();
	 		ndb.close();
	 		startShowResultsIntent("e");
	 	}
	}

	
	
	/***
	 * This function starts an intent to return to the main Cart
	 * Activity, and stores any results that may have been returned
	 * from the nutrition database search
	 **/
	void startShowResultsIntent(String results){
		Intent showResults = new Intent(this,CartActivity.class);
		showResults.putExtra("check", 1);
		showResults.putExtra("results", results);
		showResults.putExtra("account", act);
		startActivity(showResults);
	}
	
	/**
	 * Convenience method for creating a database helper
	 * or initializing the database
	 * 
	 * @return NutritionDatabaseHelper
	 */
	public NutritionDatabaseHelper startNutritionDB(){
		NutritionDatabaseHelper db = new NutritionDatabaseHelper(this);
		
		try {
			db.createDataBase();
			db.close();
		} catch (IOException ioe) {
			throw new Error(
					"Unable to create database, or db has been created already");
		}
		// OPEN THE DATABASE
		try {
			// myDbHelper.close();
			db.openDataBase();
			db.close();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		return db;
	}
	
	/**
	 * Convenience method for creating a database helper
	 * or initializing the database
	 * 
	 * @return AccountDatabaseHelper
	 */
	public AccountDatabaseHelper startAccountDB(){
		AccountDatabaseHelper db = new AccountDatabaseHelper(this);
		
		try {
			db.createDataBase();
		} catch (IOException ioe) {
			throw new Error(
					"Unable to create database, or db has been created already");
		}
		// OPEN THE DATABASE
		try {
			db.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		return db;
	}
	
	protected void onPause(){
		super.onPause();
		adb.close();
		ndb.close();
	}
}
