package com.christine.inputs;

import java.net.URI;
import java.util.HashMap;

import org.apache.http.conn.HttpHostConnectException;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;
import org.xmlrpc.android.XMLRPCSerializable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InputsBarcodeActivity extends Activity {
	
	Bundle extras; //get the upc code of interest from the previous Activity
	String upcCode;
	TextView outputText;
	String resultValue;
	String resultSize;
	String resultMessage;
	String resultDesc;
	
	// XMLRPC library request arguments for upcdatabase.com
	private URI uri=URI.create("http://www.upcdatabase.com/xmlrpc");
	private String rpc_key="2cec7a0d6ee7bcdec8a3a12f48eda85052dfc0ab";
	private XMLRPCClient client= new XMLRPCClient(uri);
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_barcode);
	    
	    outputText=(TextView) findViewById(R.id.outputText);
	    
	    //if the extras from the previous activity
        //are not null, then store it in upcCode
        if (extras == null) {
        		return;
        }
        String value1 = extras.getString("value1");
        if (value1 != null) {
        	outputText.setText("Code Searched: " + value1 + "\n");
        	upcCode=value1;
        }
        
        if(upcCode!=null){
	        //Search for the upcCode in the database!
	        try {
				//for the XML-RPC, upcdatabase.com client
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("rpc_key", rpc_key);
				params.put("upc", upcCode);
				HashMap result = (HashMap) client.call("lookup", params);
				System.out.println("This is contents " + upcCode);
				
				resultSize = result.get("size").toString();
				resultDesc = result.get("description").toString();
				resultMessage=result.get("message").toString();
				resultValue=result.get("value").toString();
			} catch (NullPointerException nl) {
				nl.printStackTrace();
				Log.d("It's returning null", "This");
			} catch (XMLRPCException e) {
				e.printStackTrace();
				Log.d("It's failed", "This");
			}

	        outputText.append("result description: " + resultDesc + "result size: " + resultSize + 
				"resultMessage: " + resultMessage + "resultValue: " + resultValue);
        }
        else{
        	outputText.append("There is no data on this item. \n");
        }
	}

}
