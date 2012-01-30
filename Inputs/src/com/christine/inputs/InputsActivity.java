package com.christine.inputs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputsActivity extends Activity {
	
	EditText enterPLU;
	Button submitPLU;
	Button scanBarcode;
	TextView test;
	InputMethodManager manager;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Handles the PLU code
        submitPLU=(Button) findViewById(R.id.submitPLU);
        enterPLU=(EditText) findViewById(R.id.enterPLU);
        test=(TextView) findViewById(R.id.test);
        manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        
        submitPLU.setOnClickListener(new View.OnClickListener() {
        	//when the user presses submitPLU
            public void onClick(View v) {
            	
            	//get the PLU code
            	String testString=enterPLU.getText().toString();
            	
            	//check to see if the PLU code is at least 5 characters long
            	if(testString.length()<=5){
	            	//hide the keyboard if the submitPLU button is pressed
	            	manager.hideSoftInputFromWindow(enterPLU.getWindowToken(), 0);
	            	//set the test string to the entered PLU code
	                test.setText(testString);
            	} else{
            		//if the testString is not at least 5 characters long,
            		//start a dialog that tells the user that it must be that length

            	}
            }
        });
        
        
        
    }
}