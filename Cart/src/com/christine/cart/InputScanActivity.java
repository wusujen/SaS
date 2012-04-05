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
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.NutritionDatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class InputScanActivity extends Activity {
	
	String contents;
	String upcResults;
	String resultDesc;
	//String resultMessage; //for debugging, see if scan was successful

	Context inputsContext;
	
	//for debug, see if contents are successfully returning an XML search
	//TextView outputText; 
	
	// XMLRPC library request arguments for upcdatabase.com
	private static URI uri=URI.create("http://www.upcdatabase.com/xmlrpc");
	private static String rpc_key="2cec7a0d6ee7bcdec8a3a12f48eda85052dfc0ab";
	private static XMLRPCClient client= new XMLRPCClient(uri);
	
	public static String[] ndbNames = new String[] { "CHEESE,COTTAGE,LOWFAT,2% MILKFAT", "EGG,WHL,RAW,FRSH", 
			"SNACKS,GRANOLA BAR,KASHI TLC BAR,CHEWY,MXD FLAVORS", "MILK,RED FAT,FLUID,2% MILKFAT,W/ ADDED VIT A & VITAMIN D"};
	
	Account act;
	AccountDatabaseHelper adb;
	NutritionDatabaseHelper ndb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //for debug, see if the contents are successfully returning an XML search
	    //setContentView(R.layout.search_barcode);
	    
	    inputsContext=getApplicationContext();
	    
	    contents = getIntent().getStringExtra("contents");
	    Account temp= getIntent().getParcelableExtra("account");
	    if(temp!=null){
	    	act = temp;
	    } else{
	    	throw new RuntimeException("InputSearchActivity, account is null");
	    }
	}
	
	public void onStart(){
		super.onStart();
		//search for the barcode's item in the XMLRPC library
		resultDesc = translateItemNameFromUPC(contents);
		Log.d("InputScanActivity", "translated " + resultDesc + " from contents " + contents);
		if(resultDesc == "e") {
			try {
				//for the XML-RPC, upcdatabase.com client
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("rpc_key", rpc_key);
				params.put("upc", contents);
				HashMap result = (HashMap) client.call("lookup", params);
				
				//store these values
				upcResults = result.toString();
				String resultString = result.get("description").toString();
				resultDesc = translateItemNameFromString(resultString);
			}
			catch (NullPointerException nl) {
				Log.d("InputScanActivity", "Fail to return upc Results. ResultDesc: " + resultDesc);
				
				// return the string for lookup in the ndb
			} 
	    	catch (XMLRPCException e) {
					Log.d("InputScanActivity", "The search for the upc database retruned null." + resultDesc);
					
					// return the string for lookup in the ndb
				}
		} else if (resultDesc != null) {
			ndb = new NutritionDatabaseHelper(InputScanActivity.this);
			adb = new AccountDatabaseHelper(InputScanActivity.this);
			
			String username = act.getName();
			
			GroceryItem resultItem = ndb.getGroceryItem(resultDesc, username);
			GroceryItem gItem = adb.getGroceryItemOf(username, resultDesc);
			if(gItem!=null){
 	    		int q = gItem.getQuantity();
 	    		resultItem.setQuantity(q+1);
 	    		Log.d("InputScanActivity", "113;ResultItem Name" + resultItem.getItemName());
 	    		adb.updateGroceryItem(resultItem);
 	    		
		        adb.close();
		        ndb.close(); 
		        
 	    	}  else {
 	    		if(resultItem!=null){
	 	    		resultItem.setQuantity(1);
	 	    		Log.d("InputScanActivity", "122: ResultItem Name" + resultItem.getItemName());
	 	    		adb.addGroceryItem(resultItem);
	 	    	
	 	    		adb.close();
			        ndb.close();
 	    		}
 	    	} 
		} 
		
		//start a new Intent to pass these values onto
		//the next invisible activity, which searches the database
		Intent databaseSearch=new Intent(inputsContext, CartActivity.class);
		databaseSearch.setType("text/plain");
		databaseSearch.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		databaseSearch.putExtra("scanResult",contents); //This is the barcode reading
		databaseSearch.putExtra("results", resultDesc); //This is the name of the item
		databaseSearch.putExtra("check", 1);
		databaseSearch.putExtra("account", act);
        startActivity(databaseSearch);
	}
	
	/**
	 * TRANSLATE ITEM NAME FROM UPC STRING TO NDB STRING
	 * A list of strings that are returned from the UPC database
	 * that will be forced to match the contents of an item within
	 * the database provided by the USDA
	 */
	public String translateItemNameFromString(String upcName){
		String ndbName = null;
		String[] upcNames = new String[] { "BRKSTONE M/F C/CHSE", "FARMHOUSE GRDE A LRG BRWN", 
				"Kashi 7 whole grains & Sesame Granola Bar (6 count)", "Lactaid Milk" };

		for(int i=0; i<upcNames.length; i++){
			if(upcName.equals(upcNames[i])){
				ndbName = ndbNames[i];  
			} 
		}
		
		return ndbName;
	}
	
	/**
	 * TRANSLATE UPC CODE DIRECTLY TO NDB STRING
	 * In the case that the XML RPC database CANNOT be contacted on the day
	 * of the presentation, this method will provide matching strings for NDB
	 * based on the scanned number
	 */
	public String translateItemNameFromUPC(String upcScan){
		String ndbName = null;
		String[] upcCodes = new String[] { "021000123544" /*Breakstones*/, "028621123588" /*eggs*/, 
				"018627030003" /*kashi bar*/, "041383090721" /*lactaid*/, };
		
		for(int i=0; i<upcCodes.length; i++){
			if(upcScan.equals(upcCodes[i])){
				ndbName = ndbNames[i];  
			} 
		}
		return ndbName;
	}

}
