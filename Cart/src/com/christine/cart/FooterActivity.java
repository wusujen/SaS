package com.christine.cart;


import java.util.List;

import com.christine.cart.intentResult.IntentResult;
import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.NutritionDatabaseHelper;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.visual.GraphView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class FooterActivity extends Activity {

	
	Button searchItem;
	Button scanItem;
	SlidingDrawer sd_itemlist;
	ListView sd_list;

	InputMethodManager manager;
	Context inputsContext;

    String upcResults;
	String resultValue;
	String resultSize;
	String resultMessage;
	String resultDesc;
	
	GraphView graph;
	
	//information required to start the scan intent
	public static final String PACKAGE = "com.christine.cart";
	public static final String SCANNER= "com.google.zxing.client.android.SCAN";
	public static final String SCAN_FORMATS = "UPC_A,UPC_E,EAN_8,EAN_13,CODE_39,CODE_93,CODE_128";
	public static final String SCAN_MODE = "SCAN_MODE";
	public static final int REQUEST_CODE = 1;
	
	public static String NAME;
	Account act;
	
	protected static AccountDatabaseHelper db;
	boolean onUPCResult=false;
	public static int days;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.footer);
        
	    Intent passedIntent = getIntent();
	    
        if(passedIntent != null){
    		Account tempAccount = passedIntent.getParcelableExtra("account");
	        if(tempAccount!=null){
	        	act = tempAccount;
	        	Log.d("FooterActivity: ", "This is act name " + act.getName());
	        	NAME = act.getName();
	        	int tdays = tempAccount.getDays();
	        	if(tdays!=0){
	    			days=tdays;
	    			Log.d("days", "days are set " + days);
	    		} 
	        } else{
	        	throw new RuntimeException("CartActivity: account passed was null");
	        }
        }
        
	    
        //start the db
        db = new AccountDatabaseHelper(this);
        
        //initiates the listview
        sd_list = (ListView) findViewById(R.id.sd_list);
        
        //get the information for listView--all of the items that are in currentcart for that user
        List<GroceryItem> ccart = db.getAllGroceryItemsOf(NAME);
        db.close();
        if(ccart!=null){
        	ArrayAdapter<String> ccartList = new ArrayAdapter<String>(this, 
            		android.R.layout.simple_list_item_checked);
	        for(int i=0; i<ccart.size(); i++){
	        	GroceryItem temp = ccart.get(i);
	        	ccartList.add(String.valueOf(temp.getQuantity()) + " " + temp.getItemName());
	        }
	        
	        sd_list.setAdapter(ccartList);
	        sd_list.setBackgroundColor(Color.WHITE);
	        
	        //for the sliding drawer in footer activity
	        //populates controls the sliding drawer
	          sd_itemlist = (SlidingDrawer) findViewById(R.id.sd_itemlist);
	          sd_itemlist.setOnDrawerOpenListener( new OnDrawerOpenListener(){
	          	public void onDrawerOpened(){
	          		Log.d("FooterActivity", "Drawer Opened");
	          	}
	          });
	          sd_itemlist.setOnDrawerCloseListener(new OnDrawerCloseListener(){
	          	public void onDrawerClosed(){
	          		Log.d("FooterActivity", "Drawer Closed");
	          	}
	          });    
        } 
        
          
	    //Handles the PLU code
        searchItem=(Button) findViewById(R.id.btn_search);

        manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputsContext=getApplicationContext();

        searchItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//opens up activity with a text entry and numpad
            	Intent openSearchItemScreen = new Intent(FooterActivity.this,InputSearchActivity.class);
            	openSearchItemScreen.putExtra("account", act);
            	startActivity(openSearchItemScreen);
            }
        }); //end searchItem
        
      //Handles the Barcode Scanning activity
	    scanItem=(Button) findViewById(R.id.btn_scan);
	    scanItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//start a scanner intent, using zxing library
				//refer back to PACKAGE settings
				Intent scanIntent = new Intent(SCANNER);
				scanIntent.setPackage(PACKAGE);
				scanIntent.addCategory(Intent.CATEGORY_DEFAULT);
	    		scanIntent.putExtra("SCAN_FORMATS", SCAN_FORMATS);
	    		scanIntent.putExtra("SCAN_MODE", SCAN_MODE);
	    		scanIntent.putExtra("account", act);
	    		try{
	    			startActivityForResult(scanIntent, REQUEST_CODE);
	    		}
	    		catch(ActivityNotFoundException e){
	    			Toast eToast = Toast.makeText(FooterActivity.this, "Activity Not Found!", Toast.LENGTH_LONG);
	    			eToast.setGravity(Gravity.TOP, 25, 400);
	    			eToast.show();
	    		}
			}
		});//end scanBarcode onClickListener  
        
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent scanIntent){
		IntentResult scanResult = parseActivityResult(requestCode, resultCode, scanIntent);
		String contents=scanResult.getContents();
		String format= scanResult.getFormatName();

		if(contents != null) {
			Toast toast = Toast.makeText(this, "Content:" + contents + 
					" Format:" + format , Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
			
			//start the InputScanActivity and pass this intent the contents and formats
			Intent startInputScanActivity = new Intent(this, InputScanActivity.class);
			startInputScanActivity.setType("text/plain");
			startInputScanActivity.putExtra("contents", contents);
			startInputScanActivity.putExtra("format", format);
			
			startActivity(startInputScanActivity);
			
		} else {
			Toast toast = Toast.makeText(inputsContext, "Scan Failed", resultCode);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
	}

	
	public static IntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				
				return new IntentResult(contents,format);
			} else {
				//Handle cancel
				return new IntentResult(null, null);
			}
		} 
		return new IntentResult(null,null);
	}//end IntentResult

	/**
     * TOTAL VALUES
     * Add up all of the values of the
     * current_cart items!
     */
    public PreviousHistory getCartTotalsFor(String username){
    	NutritionDatabaseHelper ndb = new NutritionDatabaseHelper(this);
    	AccountDatabaseHelper adb = new AccountDatabaseHelper(this);
    	
    	List<GroceryItem> allGItems = adb.getAllGroceryItemsOf(username);
    	PreviousHistory cartTotals = new PreviousHistory();
    	if(allGItems!=null){
	    	cartTotals.setId(-1);
	    	cartTotals.setUsername(username);
	    	
	    	for(int i=0; i<allGItems.size(); i++){
	    		// retrieve the grocery item from the array
	    		GroceryItem tempGrocery = allGItems.get(i);
	    		
	    		// get the Quantity of the item
	    		int quantity = tempGrocery.getQuantity();
	    		
	    		// locate the item in the nutrition database
	    		Item tempItem = ndb.getItem(tempGrocery.getItemName());
	    		
	    		// add the totals to the current cartTotal, remembering
	    		// to multiply by the quantity!
	    		cartTotals.setCalories((cartTotals.getCalories() + tempItem.getCalories())*quantity);
	    		cartTotals.setProtein((cartTotals.getProtein() + tempItem.getProtein())*quantity);
	    		cartTotals.setFat((cartTotals.getFat() + tempItem.getFat())*quantity);
	    		cartTotals.setCarbohydrate((cartTotals.getCarbohydrate() + tempItem.getCarbohydrate())*quantity);
	    		cartTotals.setFiber((cartTotals.getFiber() + tempItem.getFiber())*quantity);
	    		cartTotals.setSugar((cartTotals.getSugar() + tempItem.getSugar())*quantity);
	    		cartTotals.setCalcium((cartTotals.getCalcium() + tempItem.getCalcium())*quantity);
	    		cartTotals.setIron((cartTotals.getIron() + tempItem.getIron())*quantity);
	    		cartTotals.setMagnesium((cartTotals.getMagnesium() + tempItem.getMagnesium())*quantity);
	    		cartTotals.setPotassium((cartTotals.getPotassium() + tempItem.getPotassium())*quantity);
	    		cartTotals.setSodium((cartTotals.getSodium() + tempItem.getSodium())*quantity);
	    		cartTotals.setZinc((cartTotals.getZinc() + tempItem.getZinc())*quantity);
	    		cartTotals.setVitC((cartTotals.getVitC() + tempItem.getVitC())*quantity);
	    		cartTotals.setVitB6((cartTotals.getVitB6() + tempItem.getVitB6())*quantity);
	    		cartTotals.setVitB12((cartTotals.getVitB12() + tempItem.getVitB12())*quantity);
	    		cartTotals.setVitA((cartTotals.getVitA() + tempItem.getVitA())*quantity);
	    		cartTotals.setVitE((cartTotals.getVitE() + tempItem.getVitE())*quantity);
	    		cartTotals.setVitD((cartTotals.getVitD() + tempItem.getVitD())*quantity);
	    		cartTotals.setVitK((cartTotals.getVitK() + tempItem.getVitK())*quantity);
	    		cartTotals.setFatSat((cartTotals.getFatSat() + tempItem.getFatSat())*quantity);
	    		cartTotals.setFatMono((cartTotals.getFatMono() + tempItem.getFatMono())*quantity);
	    		cartTotals.setFatPoly((cartTotals.getFatPoly() + tempItem.getFatPoly())*quantity);
	    		cartTotals.setCholesterol((cartTotals.getCholesterol() + tempItem.getCholesterol())*quantity);
	    		cartTotals.setDays(-1);
	    	}
	    	
	    	Log.d("Created: ", "Cart Total for : " + cartTotals.getUsername() 
	    			+ "Total Calories: " + cartTotals.getCalories());
	    	adb.close();
	    	ndb.close();
	    	return cartTotals;
    	} 
    	ndb.close();
    	adb.close();
    	return null;
    }
}
