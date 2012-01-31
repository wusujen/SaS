package com.christine.inputs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputsActivity extends Activity {
	
	EditText enterPLU;
	Button submitPLU;
	Button scanBarcode;
	TextView test;
	InputMethodManager manager;
	Context inputsContext;
	String contents;
	
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
        inputsContext=getApplicationContext();

        submitPLU.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//get the PLU code
            	String testString=enterPLU.getText().toString();
            	
            	//check to see if the PLU code is at least 5 characters long
            	if(testString.length()<=5 && testString.length()>=4){
	            	//hide the keyboard if the submitPLU button is pressed
	            	manager.hideSoftInputFromWindow(enterPLU.getWindowToken(), 0);
	            	//set the test string to the entered PLU code
	                test.setText(testString);
	                
	                //start the intent to pen the pluInput screen
	                Intent pluInput = new Intent(inputsContext, InputsPLUActivity.class);
	                pluInput.setType("text/plain");
	                pluInput.putExtra("value1",testString);
	                startActivity(pluInput);
	                
            	} else{
            		//if the testString is not at least 5 characters long,
            		//start a Toast that tells the user that it must be that length
            		String incorrectText="A PLU code MUST be 4 to 5 numbers";
            		Toast incorrectLength=Toast.makeText(inputsContext, 
            				incorrectText,Toast.LENGTH_SHORT);
            		incorrectLength.setGravity(Gravity.TOP|Gravity.LEFT,0,150);
            		incorrectLength.show();
            	}
            }
        }); //end submitPLU onClickListener
        
        
        //Handles the Barcode Scanning activity
	    scanBarcode=(Button) findViewById(R.id.scanBarcode);
	    
	    scanBarcode.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//start a scanner intent, using zxing library
				Intent scanner = new Intent("com.google.zxing.client.android.SCAN");
	    		scanner.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    		scanner.putExtra("SCAN_MODE", "PRODUCT_MODE");
	    		startActivityForResult(scanner, 0);
			}
		});//end scanBarcode onClickListener
    }//end onCreate()
        
    public void onActivityResult(int requestCode, int resultCode, Intent scanner) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				contents = scanner.getStringExtra("SCAN_RESULT");
				String format = scanner.getStringExtra("SCAN_RESULT_FORMAT");
				
				//Handle successful scan by showing a toast with the info
				Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
				test.setText(contents);
				Log.d("Set text","contents");
				
				//try another intent
				Intent barcodeInput=new Intent(inputsContext,InputsBarcodeActivity.class);
				barcodeInput.setType("text/plain");
                barcodeInput.putExtra("value1",contents);
                startActivity(barcodeInput);
			}
		} else if (resultCode == RESULT_CANCELED) {
			//Handle cancel
			Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
	}
}