package com.christine.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.NutritionDatabaseHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends FooterActivity {
	String results;
	TextView outputText;
	Button viewItemList;
	Button checkout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.data);
        ViewGroup.inflate(CartActivity.this, R.layout.cart, vg);
        
        outputText = (TextView) findViewById(R.id.outputText);
        
        Intent dbReturnResults = getIntent();
        if(dbReturnResults != null){
        	results = dbReturnResults.getStringExtra("results");
        	if(results != null){
        		String temp = results;
        		outputText.append("\n" + temp);
        	}
        	else{
        		Toast noMatch=Toast.makeText(inputsContext, "There was no match in the DB",Toast.LENGTH_SHORT);
        		noMatch.setGravity(Gravity.TOP|Gravity.LEFT,0,150);
        		noMatch.show();
        	}
        }
        
        viewItemList = (Button) findViewById(R.id.btn_itemlist);
        viewItemList.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent showItemList = new Intent(CartActivity.this, ItemListActivity.class);
				startActivity(showItemList);
			}
		});  
        
        checkout = (Button) findViewById(R.id.btn_checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goCheckout = new Intent(CartActivity.this, CheckoutActivity.class);
				ArrayList<Item> cTotals = getCartTotalsFor("e");
				goCheckout.putParcelableArrayListExtra("cartTotals", cTotals);
				startActivity(goCheckout);
			}
		});
    }
    
    
    /**
     * TOTAL VALUES
     * Add up all of the values of the
     * current_cart items!
     */
    public ArrayList<Item> getCartTotalsFor(String username){
    	NutritionDatabaseHelper ndb = new NutritionDatabaseHelper(this);
    	AccountDatabaseHelper adb = new AccountDatabaseHelper(this);
    	
    	List<GroceryItem> allGItems = adb.getAllGroceryItemsOf(username);
    	ArrayList<Item> items = new ArrayList<Item>();
    	for(int i=0; i<allGItems.size(); i++){
    		GroceryItem tempItem = allGItems.get(i);
    		items.add(ndb.getItem(tempItem.getItemName()));
    		Log.d("Added: ", "Item: " + tempItem.getItemName());
    	}
    	
    	return items;
    }
}