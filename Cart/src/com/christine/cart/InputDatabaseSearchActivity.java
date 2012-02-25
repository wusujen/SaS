package com.christine.cart;

import java.io.IOException;
import java.util.ArrayList;

import com.christine.cart.intentResult.DataBaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.widget.TextView; //for debugging purposes


public class InputDatabaseSearchActivity extends Activity {
	Context inputsContext;
	//TextView outputText; //for debugging purposes
	
	String pluCode=null;
	String barcodeItem=null;
	
	DataBaseHelper myDbHelper;
	SQLiteDatabase myDatabase;
	
	public static String PLU_TABLE_NAME="plu_data";
    public static String NUTRITION_TABLE_NAME="nutrition_data";
	
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
	    }
	    
	    String ifBarcode=itemInfo.getString("resultDesc");
	    if(ifBarcode != null){
	    	barcodeItem = ifBarcode;
	    }
	    

        myDbHelper = new DataBaseHelper(this);
	    //CREATE THE DATABASE
	    try {
        	myDbHelper.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
        //OPEN THE DATABASE
	 	try {
	 		//myDbHelper.close();
	 		myDbHelper.openDataBase();
	 	} catch(SQLException sqle){
	 		throw sqle;
	 	}
	    
	 	
	 	//outputText = (TextView) findViewById(R.id.outputText);  //for debugging purposes
	 	if(pluCode != null){ 
	 		//search for the PLU code in the database
		 	myDatabase=myDbHelper.getDatabase();
		 	String itemName;
		 	
		 	Cursor getPLUCursor=myDatabase.query(PLU_TABLE_NAME,new String[]{"Commodity"},
		 			"PLU='"+ pluCode +"'",null,null,null,null);
		 	
	 		getPLUCursor.moveToFirst();
		    itemName = getPLUCursor.getString(0);
		    // outputText.append("PLU Name: " + itemName +"\n"); //for debugging purposes
	 		// myDbHelper.close();
	 		
	 		/* This was meant to catch the error if the cursor returned a null value..but not sure
	 		 * how to implement it yet...
	 		Intent noSuchPLUCode= new Intent(this,CartActivity.class);
	 		noSuchPLUCode.setType("text/plain");
	 		noSuchPLUCode.putExtra("alert", "Sorry, this PLU code doesn't exist!");
	 		startActivity(noSuchPLUCode);
			*/
	 		
	        ArrayList<String> results = searchNutritionDatabase(itemName);
	        startShowResultsIntent(results);
	 	}
	 	else{
	 		//if PLU is null, then search for the barcodeItem in the nutrition database
		 	myDatabase=myDbHelper.getDatabase();
		 	
		 	ArrayList<String> results = searchNutritionDatabase(barcodeItem);
		 	startShowResultsIntent(results);
	 	}
	}

	
	/***
	 * Retrieve nutrition information from the nutrition data table 
	 * based on the (String) name of the item that is passed in
	 ***/
	private ArrayList<String> searchNutritionDatabase(String itemName){
		//if there are any results, it will be stored in arraylist
		ArrayList<String> output=new ArrayList<String>();
		
		//search for the item in the nutrition database
       Cursor getNutritionInfoCursor=myDatabase.query(NUTRITION_TABLE_NAME,new String[]{"Shrt_Desc","Energ_Kcal"},
       		"Shrt_Desc like'"+itemName+"%'",null,null,null,null);
	 	//Cursor cursor2=myDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
	 	
	 	getNutritionInfoCursor.moveToFirst();
	 	while(!getNutritionInfoCursor.isAfterLast()){
	 		String name = getNutritionInfoCursor.getString(0);
	 		String kcal = getNutritionInfoCursor.getString(1);
	 		String itemDesc = "Name: " + name + "\n calories:" + kcal + "\n";
	 		output.add(itemDesc);
	 		getNutritionInfoCursor.moveToNext();
	 		
	 		return output;
	 	}
	 	getNutritionInfoCursor.close();
	 	return null;
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
}
