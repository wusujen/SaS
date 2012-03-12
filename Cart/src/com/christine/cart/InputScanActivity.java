/*
 * InputScanActivity 
 * 
 * Is an invisible activity that receives the contents of
 * the ZXING scan and searches for it in a UPC database 
 * in order to pass the name onto a database search.
 * 
 */
package com.christine.cart;

import java.net.URI;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.christine.cart.sqlite.Account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class InputScanActivity extends Activity {
	
	String contents;
	String upcResults;
	String resultSize;
	String resultDesc;
	//String resultMessage; //for debugging, see if scan was successful

	Context inputsContext;
	
	//for debug, see if contents are successfully returning an XML search
	//TextView outputText; 
	
	// XMLRPC library request arguments for upcdatabase.com
	private static URI uri=URI.create("http://www.upcdatabase.com/xmlrpc");
	private static String rpc_key="2cec7a0d6ee7bcdec8a3a12f48eda85052dfc0ab";
	private static XMLRPCClient client= new XMLRPCClient(uri);
	
	Account act;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //for debug, see if the contents are successfully returning an XML search
	    //setContentView(R.layout.search_barcode);
	    
	    inputsContext=getApplicationContext();
	    
	    Bundle scanResults = getIntent().getExtras();
	    if(scanResults == null){
	    	return;
	    }
	    contents = scanResults.getString("contents");
	    Account temp= scanResults.getParcelable("account");
	    if(temp!=null){
	    	act = temp;
	    } else{
	    	throw new RuntimeException("InputSearchActivity, account is null");
	    }
	}
	
	public void onStart(){
		super.onStart();
		//search for the barcode's item in the XMLRPC library
		try {
			//for the XML-RPC, upcdatabase.com client
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("rpc_key", rpc_key);
			params.put("upc", contents);
			HashMap<String, String> result = (HashMap<String, String>) client.call("lookup", params);
			System.out.println("This is contents " + contents);
			
			//store these values
			upcResults = result.toString();
			resultSize = result.get("size").toString();
			resultDesc = result.get("description").toString();
			//resultMessage=result.get("message").toString();  //for debugging, see if scan was successful
		}
    	catch (NullPointerException nl) {
    			nl.printStackTrace();
    			Log.d("It's returning null", "This");
			} 
    	catch (XMLRPCException e) {
				e.printStackTrace();
				Log.d("It's failed", "This");
			}
		
		//for debug, see if the contents are successfully returning an XML search
		//outputText=(TextView) findViewById(R.id.outputText);
		//outputText.setText("UPC Results: " + upcResults);
		
		//start a new Intent to pass these values onto
		//the next invisible activity, which searches the database
		Intent databaseSearch=new Intent(inputsContext, InputDatabaseSearchActivity.class);
		databaseSearch.setType("text/plain");
		databaseSearch.putExtra("scanResult",contents); //This is the barcode reading
		databaseSearch.putExtra("resultSize", resultSize); //This is the size of the item
		databaseSearch.putExtra("resultDesc", resultDesc); //This is the name of the item
		databaseSearch.putExtra("account", act); //This is the account associated!
        startActivity(databaseSearch);
	}

}
