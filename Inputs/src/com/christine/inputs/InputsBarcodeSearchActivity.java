package com.christine.inputs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InputsBarcodeSearchActivity extends Activity {
	Bundle extras; //get the code of interest from the previous Activity
	TextView outputText;
	String upcCode;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_barcode);
        
        //initialize
        outputText=(TextView) findViewById(R.id.outputText);
        extras = getIntent().getExtras(); //get the extras of the bundle from prev activity
        
        //if the extras from the previous activity
        //are not null, then store it in pluSearch
        if (extras == null) {
        		return;
        }
        String value1 = extras.getString("value1");
        if (value1 != null) {
        	outputText.setText("Code Searched: " + value1 + "\n");
        	upcCode=value1;
        }
	}

}
