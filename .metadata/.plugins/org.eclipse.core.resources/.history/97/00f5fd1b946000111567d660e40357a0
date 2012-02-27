package com.christine.cart.inputs;

import com.christine.cart.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InputsBarcodeSearchActivity extends Activity {
	Bundle extras; //get the code of interest from the previous Activity
	TextView outputText;
	String upcCode;
	String resultValue;
	String resultSize;
	String resultMessage;
	String resultDesc;
	
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
        
        String scanResult = extras.getString("scanResult");
        resultDesc = extras.getString("resultDesc");
        resultSize = extras.getString("resultSize");
        resultMessage = extras.getString("resultMessage");
        resultValue = extras.getString("resultValue");
        
        if (scanResult != null) {
        	outputText.setText("Code Searched: " + scanResult + "\n");
        	upcCode=scanResult;
        	outputText.append("result description: " + resultDesc + "\nresult size: " + resultSize 
    				+ "\nresultMessage: " + resultMessage + "\nresultValue: " + resultValue);
        }
	}
}
