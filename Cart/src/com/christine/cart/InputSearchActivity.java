package com.christine.cart;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InputSearchActivity extends Activity {

	EditText enterPLU;
	EditText number;
	Button submitPLU;
	Button btn_yes;
	Button btn_no;
	Button btn_cancel;
	Button btn_add;
	TextView outputText;
	TextView itemName;
	TextView desc;

	ActionBar actionBar;
	
	LinearLayout ll;
	LayoutInflater li;
	
	View plu_enter;
	View plu_confirm;
	View plu_quantity;
	String returnedItemName;
	
	private static Account act;
	private static GroceryItem returnedItem;
	public static final int PLU_SEARCH = 1;
	private static AccountDatabaseHelper adb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_plu);
	    
	    //ActionBar
  		actionBar = (ActionBar) findViewById(R.id.actionbar);
  		actionBar.setTitle("Search For Produce");
  		actionBar.setHomeAction(new backToCartAction());
  		
	    ll = (LinearLayout) findViewById(R.id.ll_sp_window);
	    li= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    plu_enter = li.inflate(R.layout.plu_enter, null);
	    
	    ll.addView(plu_enter);
	    
	    Account temp= getIntent().getParcelableExtra("account");
	    if(temp!=null){
	    	act = temp;
	    } else{
	    	throw new RuntimeException("InputSearchActivity, account is null");
	    }
	    
	    desc = (TextView) findViewById(R.id.tv_sp_prompt);
	    
	    enterPLU = (EditText) findViewById(R.id.enterPLU);
	    submitPLU = (Button) findViewById(R.id.submitPLU);
	    submitPLU.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String pluCode = enterPLU.getText().toString();
				if(pluCode.length()!=0){
					Intent searchForPLUCode = new Intent(InputSearchActivity.this, InputDatabaseSearchActivity.class);
					searchForPLUCode.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					searchForPLUCode.setType("text/plain");
					searchForPLUCode.putExtra("plu",pluCode);
					searchForPLUCode.putExtra("account", act);
					
					startActivityForResult(searchForPLUCode, PLU_SEARCH );
				} else {
					outputText.setText("Please enter a valid PLU code.");
				}
			}
		});
	    
	    outputText = (TextView) findViewById(R.id.tv_sp_pluoutput);
	    
	    setupCancelButton();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent intent){
		if(resultCode == RESULT_OK){
			
			plu_confirm = li.inflate(R.layout.plu_confirm, null);
			ll.addView(plu_confirm);
			ll.removeView(plu_enter);
			
			returnedItemName = intent.getStringExtra("itemname");
			returnedItem = intent.getParcelableExtra("gItem");
			outputText = (TextView) findViewById(R.id.tv_sp_pluoutput);
			outputText.setText(returnedItemName);
			
			desc.setText("Is this the item that you searched for?");
			
			btn_yes = (Button) findViewById(R.id.btn_yes);
			btn_yes.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					plu_quantity = li.inflate(R.layout.plu_quantity, null);
					ll.addView(plu_quantity);
					ll.removeView(plu_confirm);
					
					addEventsToQuantityLayout();
				}
			});
			
			
			btn_no = (Button) findViewById(R.id.btn_no);
			btn_no.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					ll.addView(plu_enter);
					ll.removeView(plu_confirm);
				}
			});
			
			setupCancelButton();
			
		} else if(resultCode == RESULT_CANCELED){
			outputText.setText("Sorry, we couldn't find that item. Please try again.");
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
			Intent backToCart = new Intent(InputSearchActivity.this, CartActivity.class);
			backToCart.putExtra("account", act);
			startActivity(backToCart);
		}
	}
	
	
	
	/**
	 * Add event handlers to quantity layout
	 */
	public void addEventsToQuantityLayout(){
		desc.setText("Enter the quantity of the item that you're planning to purchase.");
		
		itemName = (TextView) findViewById(R.id.tv_sp_itemname);
		itemName.setText(returnedItemName);
		
		number = (EditText) findViewById(R.id.et_quantity);
		
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				adb = new AccountDatabaseHelper(InputSearchActivity.this);
				
				String qs = number.getText().toString();
				if(qs.length()!=0){
		 	    	Integer newQ = Integer.valueOf(qs);
		 	    	if(newQ!=0){
	
						//add that item to the user's current cart
			 	    	GroceryItem gItem = adb.getGroceryItemOf(act.getName(), returnedItem.getItemName());
			 	    	
			 	    	if(gItem!=null){
			 	    		int q = gItem.getQuantity();
			 	    		
			 	    		gItem.setQuantity(q+newQ);
			 	    		
			 	    		adb.updateGroceryItem(gItem);
			 	    	
					        adb.close();
					        successfulSave();
					        
			 	    	}  else {
			 	    		gItem = returnedItem;
			 	    		gItem.setQuantity(newQ);
			 	    		
			 	    		adb.addGroceryItem(gItem);
			 	    		
			 	    		adb.close();
			 	    		successfulSave();
			 	    	}
		 	    	} 
				} else {
	 	    		Toast noQ = Toast.makeText( InputSearchActivity.this, "Please enter a quantity.", Toast.LENGTH_LONG);
	 	    		noQ.show();
	 	    	}
			}
		});
		
		setupCancelButton();
	}
	
	/*
	 * Convenience function to setup cancel button
	 */
	public void setupCancelButton(){
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent cancel = new Intent(InputSearchActivity.this, CartActivity.class);
				cancel.putExtra("check", 0);
				cancel.putExtra("account", act);
				startActivity(cancel);
			}
		});
	}
	
	/*
	 * Convenience Function to go back to Cart Activity
	 */
	public void successfulSave(){
		Intent returnToCart = new Intent(InputSearchActivity.this, CartActivity.class);
		returnToCart.putExtra("check", 1);
		returnToCart.putExtra("results", returnedItemName);
		returnToCart.putExtra("account", act);
		startActivity(returnToCart);
	}

}
