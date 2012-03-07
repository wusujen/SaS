package com.christine.cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	NutritionDatabaseHelper myDbHelper;
	
	
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
	    

        myDbHelper = startNutritionDB();
	    
	 	
	 	if(pluCode != null){ 
	 		Log.d("PLU Code: ", "Plu code: " + pluCode);
	 	    String itemName = myDbHelper.getPLUItem(Integer.parseInt(pluCode));
	 	   Log.d("Item Name from PLU : ", "Item Name " + itemName);
	       List<GroceryItem> resultGroceryItems = myDbHelper.getAllMatchingItems(itemName, "e");
	       ArrayList<String> results = new ArrayList<String>();
	       for(int i=0; i<resultGroceryItems.size(); i++){
	    	  GroceryItem item = resultGroceryItems.get(i);
	    	  String name = item.getItemName();
	    	  Log.d("Item fetched: ", name);
	    	  results.add(name);
	       }
	       	myDbHelper.close();
	        startShowResultsIntent(results);
	 	}
	 	else{
	 		//if PLU is null, then search for the barcodeItem in the nutrition database
	 		List<GroceryItem> resultGroceryItems = myDbHelper.getAllMatchingItems(barcodeItem, "e");
	 		
		 	ArrayList<String> results = new ArrayList<String>();
		       for(int i=0; i<resultGroceryItems.size(); i++){
		    	  GroceryItem item = resultGroceryItems.get(i);
		    	  results.add(item.getItemName());
		       }
		       myDbHelper.close();
		       startShowResultsIntent(results);
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
		//This is for debugging
		/*if(results != null){
			for(int i=0; i<=results.size(); i++){
	 			outputText.append((String) results.get(i)); 
	 		}
	 	} else{
	 		outputText.setText("There is no matching item in the Nutrition database");
	 	}*/
	}
	
	/**
	 * Convenience method for creating a database helper
	 * or initializing the database
	 * 
	 * @return AccountDatabaseHelper
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
	
	protected void onPause(){
		super.onPause();
		myDbHelper.close();
	}
}
