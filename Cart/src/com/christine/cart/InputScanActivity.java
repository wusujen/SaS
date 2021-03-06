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

import com.christine.cart.intentResult.IntentResult;
import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.NutritionDatabaseHelper;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputScanActivity extends Activity {
	
	String contents;
	String upcResults;
	String resultDesc;
	
	TextView tv_sp_title;
	TextView tv_sp_prompt;
	EditText enterPLU;
	EditText number;
	Button submitPLU;
	Button btn_yes;
	Button btn_no;
	Button btn_cancel;
	Button btn_add;
	TextView outputText;
	TextView itemName;
	
	ActionBar actionBar;

	Context inputsContext;
	
	LinearLayout ll;
	LayoutInflater li;

	View barcode_confirm;
	View barcode_quantity;
	
	// information required to start the scan intent
	public static final String PACKAGE = "com.christine.cart";
	public static final String SCANNER = "com.google.zxing.client.android.SCAN";
	public static final String SCAN_FORMATS = "UPC_A,UPC_E,EAN_8,EAN_13,CODE_39,CODE_93,CODE_128";
	public static final String SCAN_MODE = "SCAN_MODE";
	public static final int REQUEST_CODE = 1;
	
	// XMLRPC library request arguments for upcdatabase.com
	private static URI uri=URI.create("http://www.upcdatabase.com/xmlrpc");
	private static String rpc_key="2cec7a0d6ee7bcdec8a3a12f48eda85052dfc0ab";
	private static XMLRPCClient client= new XMLRPCClient(uri);
	
	public static String[] ndbNames = new String[] { "CHEESE,COTTAGE,LOWFAT,2% MILKFAT", "EGG,WHL,RAW,FRSH", 
			"SNACKS,GRANOLA BAR,KASHI TLC BAR,CHEWY,MXD FLAVORS", "MILK,RED FAT,FLUID,2% MILKFAT,W/ ADDED VIT A & VITAMIN D",
			"CEREALS RTE,KASHI HONEY SUNSHINE", "CEREALS RTE,KELLOGG'S FRSTD MINI-WHTS TOUCH FRT MDL MXD BER",
			"BREAD,WHEAT", "TURKEY BREAST,LO SALT,PREPACKAGED OR DELI,LUNCHEON MEAT", "BEEF,GROUND,80% LN MEAT / 20% FAT,RAW", 
			"TOFU,SOFT,PREP W/CA SULFATE&MAGNESIUM CHLORIDE (NIGARI)", "CANDIES,TWIZZLERS STRAWBERRY TWISTS CANDY",
			"CANDIES,MARS SNACKFOOD US,M&M'S PNUT CHOC CANDIES"
			};
	
	Account act;
	AccountDatabaseHelper adb;
	NutritionDatabaseHelper ndb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_plu);
	   
	    contents = getIntent().getStringExtra("contents");
	    resultDesc = translateItemNameFromUPC(contents);
	    
	    Account temp= getIntent().getParcelableExtra("account");
	    if(temp!=null){
	    	act = temp;
	    } else{
	    	throw new RuntimeException("InputSearchActivity, account is null");
	    }
	    
		ll = (LinearLayout) findViewById(R.id.ll_sp_window);
	    li= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    barcode_confirm = li.inflate(R.layout.plu_confirm, null);
	    
	    ll.addView(barcode_confirm);
	    
	    //ActionBar
  		actionBar = (ActionBar) findViewById(R.id.actionbar);
  		actionBar.setTitle("Search By Barcode");
  		actionBar.setHomeAction(new backToCartAction());
	
	}
	
	@Override
	protected void onResume(){
		super.onResume();

	    inputsContext=getApplicationContext();
	    
	    tv_sp_title = (TextView) findViewById(R.id.tv_sp_title);
	    tv_sp_prompt = (TextView) findViewById(R.id.tv_sp_prompt);
	    
	    tv_sp_title.setText("SEARCH ITEM BY BARCODE");
	    tv_sp_prompt.setText("Is this the item that you searched for?");
	    
	    outputText = (TextView) findViewById(R.id.tv_sp_pluoutput);
	    
		if (resultDesc != "e" && resultDesc != null) {
			Log.d("InputScanActivity", "result Desc working:" + resultDesc);
			outputText.setText(resultDesc);
		    
			btn_yes = (Button) findViewById(R.id.btn_yes);
			btn_yes.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					barcode_quantity = li.inflate(R.layout.plu_quantity, null);
					ll.addView(barcode_quantity);
					ll.removeView(barcode_confirm);
					
					addEventsToQuantityLayout();
				}
			});
			
			btn_no = (Button) findViewById(R.id.btn_no);
			btn_no.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent scanIntent = new Intent(SCANNER);
					scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					scanIntent.setPackage(PACKAGE);
					scanIntent.addCategory(Intent.CATEGORY_DEFAULT);
					scanIntent.putExtra("SCAN_FORMATS", SCAN_FORMATS);
					scanIntent.putExtra("SCAN_MODE", SCAN_MODE);
					try {
						startActivityForResult(scanIntent, REQUEST_CODE);
					} catch (ActivityNotFoundException e) {
						Toast eToast = Toast.makeText(InputScanActivity.this,
								"Activity Not Found!", Toast.LENGTH_LONG);
						eToast.setGravity(Gravity.TOP, 25, 400);
						eToast.show();
					}
				}
			});
			
			btn_cancel= (Button) findViewById(R.id.btn_cancel);
			btn_cancel.setVisibility(View.GONE);
			
		} else {
			
			Log.d("InputScanActivity", "result Desc not working:" + resultDesc);
			outputText.setText("Sorry, we couldn't find the item you searched for. Would you like to try again?");
			btn_yes = (Button) findViewById(R.id.btn_yes);
			btn_yes.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent  scanIntent= new Intent(SCANNER);
					scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					scanIntent.setPackage(PACKAGE);
					scanIntent.addCategory(Intent.CATEGORY_DEFAULT);
					scanIntent.putExtra("SCAN_FORMATS", SCAN_FORMATS);
					scanIntent.putExtra("SCAN_MODE", SCAN_MODE);
					try {
						startActivityForResult(scanIntent, REQUEST_CODE);
					} catch (ActivityNotFoundException e) {
						Toast eToast = Toast.makeText(InputScanActivity.this,
								"Activity Not Found!", Toast.LENGTH_LONG);
						eToast.setGravity(Gravity.TOP, 25, 400);
						eToast.show();
					}
				}
			});
			
			btn_no = (Button) findViewById(R.id.btn_no);
			btn_no.setText("No Thanks");
			btn_no.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent backToCart = new Intent(InputScanActivity.this, CartActivity.class);
					backToCart.putExtra("account", act);
					startActivity(backToCart);
				}
			});
			
			btn_cancel= (Button) findViewById(R.id.btn_cancel);
			btn_cancel.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * Activity Result from second scan
	 */
	public void onActivityResult(int requestCode, int resultCode,
			Intent scanIntent) {
		IntentResult scanResult = parseActivityResult(requestCode, resultCode,
				scanIntent);
		String contents = scanResult.getContents();
		if(resultCode == RESULT_OK){
			Log.d("InputScanActivity", "contents on Activity result: " + contents);
			resultDesc = translateItemNameFromUPC(contents);
		} else {
			Toast toast = Toast.makeText(inputsContext, "Scan Failed",
					resultCode);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
	}
	
	
	/**
	 * PARSE ACTIVITY RESULT FROM SEARCH AND SCAN
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 * @return
	 */
	public static IntentResult parseActivityResult(int requestCode,
			int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				return new IntentResult(contents, format);
			} else {
				// Handle cancel
				return new IntentResult(null, null);
			}
		}
		return new IntentResult(null, null);
	}// end IntentResult
	
	
	/**
	 * Add event handlers to quantity layout
	 */
	public void addEventsToQuantityLayout(){
		tv_sp_prompt.setText("Enter the quantity of the item that you're planning to purchase.");
		
		itemName = (TextView) findViewById(R.id.tv_sp_itemname);
		itemName.setText(resultDesc);
		
		number = (EditText) findViewById(R.id.et_quantity);
		
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				adb = new AccountDatabaseHelper(InputScanActivity.this);
				
				String qs = number.getText().toString();
				if(qs.length()!=0){
		 	    	Integer newQ = Integer.valueOf(qs);
		 	    	if(newQ!=0){
		 	    		searchAndAddItem(newQ);
		 	    		goBackToCartIntent();
		 	    	} 
				} else {
	 	    		Toast noQ = Toast.makeText( InputScanActivity.this, "Please enter a quantity.", Toast.LENGTH_LONG);
	 	    		noQ.show();
	 	    	}
			}
		});
		
		setupCancelButton();
	}
	
	/**
	 * Search for the item via XML-RPC, or via the 
	 * String functions provided, and add the quantity
	 * the the current cart of the user
	 * 
	 * @param quantity
	 */
	public void searchAndAddItem(int quantity){
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
				
    	}
		ndb = new NutritionDatabaseHelper(InputScanActivity.this);
		adb = new AccountDatabaseHelper(InputScanActivity.this);
		
		String username = act.getName();
		
		GroceryItem resultItem = ndb.getGroceryItem(resultDesc, username);
		GroceryItem gItem = adb.getGroceryItemOf(username, resultDesc);
		if(gItem!=null){
			int oldQuantity = gItem.getQuantity();					
    		resultItem.setQuantity(oldQuantity + quantity);
    		adb.updateGroceryItem(resultItem);
    		
	        adb.close();
	        ndb.close(); 
	        
    	}  else {
    		if(resultItem!=null){
 	    		resultItem.setQuantity(quantity);
 	    		adb.addGroceryItem(resultItem);
 	    	
 	    		adb.close();
		        ndb.close();
    		}
    	} 
	}
	
	/**
	 * ACTIONBAR CART
	 */
	private class backToCartAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_back;
		}
		
		public void performAction(View view){
			Intent backToCart = new Intent(InputScanActivity.this, CartActivity.class);
			backToCart.putExtra("account", act);
			startActivity(backToCart);
		}
	}
	
	/**
	 * Back to Cart Intent
	 */
	
	public void goBackToCartIntent(){
		//Back to Cart Activity
		Intent backToCart=new Intent(inputsContext, CartActivity.class);
		backToCart.setType("text/plain");
		backToCart.putExtra("scanResult",contents); //This is the barcode reading
		backToCart.putExtra("results", resultDesc); //This is the name of the item
		backToCart.putExtra("check", 1);
		backToCart.putExtra("account", act);
        startActivity(backToCart);
	}
	
	/**
	 * TRANSLATE ITEM NAME FROM UPC STRING TO NDB STRING
	 * A list of strings that are returned from the UPC database
	 * that will be forced to match the contents of an item within
	 * the database provided by the USDA
	 */
	public String translateItemNameFromString(String upcName){
		String ndbName = null;
		String[] upcNames = new String[] { "BRKSTONE M/F C/CHSE", "Trader Joe's Brown Eggs 12 count", 
				"Kashi 7 whole grains & Sesame Granola Bar (6 count)", "Lactaid Milk", "Kashi Honey Sunshine Cereal",
				"Kellog's Frosted Mini-Wheats with Raspberry Jam center", "Nature's Own Extra Fiber Whole Wheat bread",
				"Oscar Meyer Fresh Selects Turkey Breast", "GROUND BEEF 80/20", "Mori-Nu Silken Soft Tofu", 
				"Twizzlers Strawberry Twists Candy", "M&M PEANUT CHOC 19.2OZ"};

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
				"018627030003" /*kashi bar*/, "041383090721" /*lactaid*/,"018627536871" /*Kashi h2h*/,
				"038000597039" /*kellogs frstd mini*/, "072250915717" /*wheat bread*/, "044700061459" /*turkey breast*/, 
				"0000002016597" /*ground beef*/, "085696608303"/*tofu*/, "034000560028" /*twizzlers*/, 
				"040000249290" /*M&M's*/
				};
		
		for(int i=0; i<upcCodes.length; i++){
			if(upcScan.equals(upcCodes[i])){
				ndbName = ndbNames[i];  
			} 
		}
		return ndbName;
	}
	
	/*
	 * Convenience function to setup cancel button
	 */
	public void setupCancelButton(){
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent cancel = new Intent(InputScanActivity.this, CartActivity.class);
				cancel.putExtra("check", 0);
				cancel.putExtra("account", act);
				startActivity(cancel);
			}
		});
	}
}
