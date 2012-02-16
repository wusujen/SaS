package com.christine.cart.inputs;

import java.net.URI;
import java.util.HashMap;

import org.apache.http.conn.HttpHostConnectException;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;
import org.xmlrpc.android.XMLRPCSerializable;

import com.christine.cart.R;

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
	Button submitBarcode;
	TextView test;
	InputMethodManager manager;
	Context inputsContext;
	String contents;
    String upcResults;
	String resultValue;
	String resultSize;
	String resultMessage;
	String resultDesc;
	
	// XMLRPC library request arguments for upcdatabase.com
	private URI uri=URI.create("http://www.upcdatabase.com/xmlrpc");
	private String rpc_key="2cec7a0d6ee7bcdec8a3a12f48eda85052dfc0ab";
	private XMLRPCClient client= new XMLRPCClient(uri);
	
	boolean onUPCResult=false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputs);
        
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
	    
	    
	    
	    //Handles the Barcode search activity
	    submitBarcode=(Button) findViewById(R.id.submitBarcode);
	    submitBarcode.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//store the results of the scan & display them on the screen
				String scanResult=test.getText().toString();
				
				//search for the barcode's item in the XMLRPC library
				try {
	    			//for the XML-RPC, upcdatabase.com client
	    			HashMap<String, String> params = new HashMap<String, String>();
	    			params.put("rpc_key", rpc_key);
	    			params.put("upc", scanResult);
	    			HashMap result = (HashMap) client.call("lookup", params);
	    			System.out.println("This is contents " + scanResult);
	    			
	    			//store these values
	    			upcResults = result.toString();
	    			resultSize = result.get("size").toString();
	    			resultDesc = result.get("description").toString();
	    			resultMessage=result.get("message").toString();
	    			resultValue=result.get("value").toString();
				}
	        	catch (NullPointerException nl) {
		    			nl.printStackTrace();
		    			Log.d("It's returning null", "This");
	    			} 
	        	catch (XMLRPCException e) {
						e.printStackTrace();
						Log.d("It's failed", "This");
	    			}
				
				//start a new Intent to pass these values onto
    			//the next screen to display: InputsBarcodeSearchActivity
    			Intent searchForBarcode=new Intent(inputsContext,InputsBarcodeSearchActivity.class);
				searchForBarcode.setType("text/plain");
				searchForBarcode.putExtra("scanResult",scanResult);
				searchForBarcode.putExtra("resultSize", resultSize);
				searchForBarcode.putExtra("resultDesc", resultDesc);
				searchForBarcode.putExtra("resultMessage",resultMessage);
				searchForBarcode.putExtra("resultValue", resultValue);
                startActivity(searchForBarcode);
	
			}
		});
	    
	    
    }//end onCreate()
   
    
    public void onActivityResult(int requestCode, int resultCode, Intent scanner) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				contents = scanner.getStringExtra("SCAN_RESULT");
				String format = scanner.getStringExtra("SCAN_RESULT_FORMAT");
                
				test.setText(contents);
				
				Toast toast = Toast.makeText(this, "Content:" + contents + 
						" Format:" + format , Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
				
			}
		} else if (resultCode == RESULT_CANCELED) {
			//Handle cancel
			Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
		
	}//end onActivityResult
}