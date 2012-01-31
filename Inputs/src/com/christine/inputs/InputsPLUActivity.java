package com.christine.inputs;

import java.io.IOException;

import com.christine.inputs.DataBaseHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class InputsPLUActivity extends Activity {
		Bundle extras; //get the plu code of interest fromt the previous Activity
		DataBaseHelper myDbHelper;
		SQLiteDatabase myDatabase;
	    TextView outputText;
	    String pluSearch;
	    
	    public static String PLU_TABLE_NAME="plu_code";
	    public static String NUTRITION_TABLE_NAME="nutrition_data";
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.search_plu);
	        
	        //initialize
	        outputText=(TextView) findViewById(R.id.outputText);
	        extras = getIntent().getExtras(); //get the extras of the bundle from prev activity
	        myDbHelper = new DataBaseHelper(this);
	        
	        //if the extras from the previous activity
	        //are not null, then store it in pluSearch
	        if (extras == null) {
	        		return;
	        }
	        String value1 = extras.getString("value1");
	        if (value1 != null) {
	        	outputText.setText("Code Searched: " + value1 + "\n");
	        	pluSearch=value1;
	        }
	        
	        try {
	        	myDbHelper.createDataBase();
		 	} 
	        catch (IOException ioe) {
		 		throw new Error("Unable to create database");
		 	}
	        
		 
		 	try {
		 		//myDbHelper.close();
		 		myDbHelper.openDataBase();
		 	}
		 	catch(SQLException sqle){
		 		throw sqle;
		 	}
		 	
		 	if(pluSearch!=null){
			 	myDatabase=myDbHelper.getDatabase();
			 	
			 	Cursor cursor=myDatabase.query(PLU_TABLE_NAME,new String[]{"Commodity"},
			 			"PLU='"+ pluSearch +"'",null,null,null,null);
			 	
			 	cursor.moveToFirst();
		        String text = cursor.getString(0);
		        outputText.append("PLU Name: " + text +"\n");
		        
		        Cursor cursor2=myDatabase.query(NUTRITION_TABLE_NAME,new String[]{"Shrt_Desc","Energ_Kcal"},
		        		"Shrt_Desc like'"+text+"%'",null,null,null,null);
			 	//Cursor cursor2=myDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
			 	
			 	cursor2.moveToFirst();
			 	while(!cursor2.isAfterLast()){
			 		String name=cursor2.getString(0);
			 		String kcal = cursor2.getString(1);
			 		outputText.append("Name: " + name + "calories:" + kcal + "\n");
			 		cursor2.moveToNext();
			 	}
			 	cursor2.close();
		 	}
		 	else{
		 		outputText.append("PLU doesn't exist in Database \n");
		 	}

	    }

}
