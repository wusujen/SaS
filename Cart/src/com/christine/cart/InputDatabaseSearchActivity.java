package com.christine.cart;

import java.io.IOException;
import java.util.ArrayList;

import com.christine.cart.intentResult.DataBaseHelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;


public class InputDatabaseSearchActivity extends Activity {
	Context inputsContext;
	TextView outputText;
	
	String pluCode=null;
	String barcodeItem=null;
	
	DataBaseHelper myDbHelper;
	SQLiteDatabase myDatabase;
	
	public static String PLU_TABLE_NAME="plu_code";
    public static String NUTRITION_TABLE_NAME="nutrition_data";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_barcode);
	    
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
	    
	 	
	 	outputText = (TextView) findViewById(R.id.outputText);
	 	if(pluCode != null){ 
	 		//search for the PLU code in the database
		 	myDatabase=myDbHelper.getDatabase();
		 	
		 	Cursor getPLUCursor=myDatabase.query(PLU_TABLE_NAME,new String[]{"Commodity"},
		 			"PLU='"+ pluCode +"'",null,null,null,null);
		 	
		 	getPLUCursor.moveToFirst();
	        String itemName = getPLUCursor.getString(0);
	        outputText.append("PLU Name: " + itemName +"\n");
	        getPLUCursor.close();
	        
	        ArrayList results = searchNutritionDatabase(itemName);
		 	if(results != null){
		 		for(int i=0; i<=results.size(); i++){
		 			outputText.append((String) results.get(i));
		 		}
		 	} else{
		 		outputText.setText("There is no matching item in the Nutrition database");
		 	}
	 	}
	 	else{
	 		//if PLU is null, then search for the barcodeItem in
	 		//the nutrition database
		 	myDatabase=myDbHelper.getDatabase();
		 	
		 	ArrayList results = searchNutritionDatabase(barcodeItem);
		 	if(results != null){
		 		for(int i=0; i<=results.size(); i++){
		 			outputText.append((String) results.get(i));
		 		}
		 	} else{
		 		outputText.setText("There is no matching item in the Nutrition database");
		 	}
	 	}
	}

	private ArrayList searchNutritionDatabase(String itemName){
		//if there are any results, it will be stored in arraylist
		ArrayList output=new ArrayList();
		
		//search for the item in the nutrition database
       Cursor getNutritionInfoCursor=myDatabase.query(NUTRITION_TABLE_NAME,new String[]{"Shrt_Desc","Energ_Kcal"},
       		"Shrt_Desc like'"+itemName+"%'",null,null,null,null);
	 	//Cursor cursor2=myDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
	 	
	 	getNutritionInfoCursor.moveToFirst();
	 	while(!getNutritionInfoCursor.isAfterLast()){
	 		String name = getNutritionInfoCursor.getString(0);
	 		String kcal = getNutritionInfoCursor.getString(1);
	 		String itemDesc = "Name: " + name + "calories:" + kcal + "\n";
	 		output.add(itemDesc);
	 		getNutritionInfoCursor.moveToNext();
	 		
	 		return output;
	 	}
	 	getNutritionInfoCursor.close();
	 	return null;
	}
}