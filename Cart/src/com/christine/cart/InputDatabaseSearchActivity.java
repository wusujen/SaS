package com.christine.cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.NutritionDatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.widget.TextView; //for debugging purposes
import android.util.Log;


public class InputDatabaseSearchActivity extends Activity {
	Context inputsContext;
	//TextView outputText; //for debugging purposes
	
	String pluCode=null;
	String barcodeItem=null;
	
	NutritionDatabaseHelper ndb;
	AccountDatabaseHelper adb;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // setContentView(R.layout.search_barcode); //for debugging purposes
	    
	    Bundle itemInfo = getIntent().getExtras();
	    if(itemInfo == null){
	    	return;
	    }
	    
	    //See if it is a BARCODE or PLU intent
	    String ifPLU=itemInfo.getString("plu");
	    if(ifPLU != null){
	    	pluCode = ifPLU;
	    	Log.d("PLU Code: ", pluCode);
	    }
	    
	    String ifBarcode=itemInfo.getString("resultDesc");
	    if(ifBarcode != null){
	    	barcodeItem = ifBarcode;
	    	Log.d("Barcode Code: ", "Barcode :" + barcodeItem);
	    }

        ndb = startNutritionDB();
        adb = startAccountDB();
	 	
	 	if(pluCode != null){ 
	 		Log.d("PLU Code: ", "Plu code: " + pluCode);
	 	    String itemName = ndb.getPLUItem(Integer.parseInt(pluCode));
	 	    Log.d("Item Name from PLU : ", "Item Name " + itemName);
	 	    GroceryItem resultItem = ndb.getGroceryItem(itemName, "e");
	 	    
	 	    if(resultItem!=null){
	 	    	String result = resultItem.getItemName();
	 	    	
		        //add that item to the user's current cart
	 	    	GroceryItem gItem = adb.getGroceryItemOf("e", result);
	 	    	if(gItem!=null){
	 	    		int q = gItem.getQuantity();
	 	    		resultItem.setQuantity(q+1);
	 	    		
	 	    		adb.updateGroceryItem(resultItem);
	 	    		Log.d("Result Item: ", "Result Item: " + resultItem.getItemName() + 
	 	    				"Result Quantity: " + resultItem.getQuantity());
			        adb.close();
			        ndb.close();
			        startShowResultsIntent(result);
	 	    	}  else {
	 	    		resultItem.setQuantity(1);
	 	    		
	 	    		adb.addGroceryItem(resultItem);
	 	    		Log.d("Result Item: ", "Result Item: " + resultItem.getItemName() + 
	 	    				"Result Quantity: " + resultItem.getQuantity());
	 	    		adb.close();
			        ndb.close();
			        startShowResultsIntent(result);
	 	    	}

	 	    } else {
	 	    	throw new RuntimeException("Item was null");
	 	    }
	 	}
	 	else if(barcodeItem!=null){
	 	    GroceryItem resultItem = ndb.getGroceryItem(barcodeItem, "e");
	 	    
	 		//add that item to the user's current cart
 	    	GroceryItem gItem = adb.getGroceryItemOf("e", barcodeItem);
 	    	if(gItem!=null){
 	    		int q = gItem.getQuantity();
 	    		resultItem.setQuantity(q+1);
 	    		
 	    		adb.updateGroceryItem(resultItem);
 	    		Log.d("Result Item: ", "Result Item: " + resultItem.getItemName() + 
 	    				"Result Quantity: " + resultItem.getQuantity());
		        adb.close();
		        ndb.close();
		        startShowResultsIntent(barcodeItem);
 	    	}  else {
 	    		resultItem.setQuantity(1);
 	    		
 	    		adb.addGroceryItem(resultItem);
 	    		Log.d("Result Item: ", "Result Item: " + resultItem.getItemName() + 
 	    				"Result Quantity: " + resultItem.getQuantity());
 	    		adb.close();
		        ndb.close();
		        startShowResultsIntent(barcodeItem);
 	    	}
	 	} else{
	 		adb.close();
	 		ndb.close();
	 		throw new RuntimeException ("All inputs were null.");
	 	}
	}

	
	
	/***
	 * This function starts an intent to return to the main Cart
	 * Activity, and stores any results that may have been returned
	 * from the nurtrition database search
	 **/
	void startShowResultsIntent(ArrayList<String> results){
		Intent showResults = new Intent(this,CartActivity.class);
		showResults.putStringArrayListExtra("results", results);
		startActivity(showResults);
	}
	
	void startShowResultsIntent(String results){
		Intent showResults = new Intent(this,CartActivity.class);
		showResults.putExtra("results", results);
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
