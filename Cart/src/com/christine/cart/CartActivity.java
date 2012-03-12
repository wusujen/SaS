package com.christine.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.NutritionDatabaseHelper;
import com.christine.cart.sqlite.PreviousHistory;

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
				PreviousHistory cTotals = getCartTotalsFor("e");
				goCheckout.putExtra("cartTotals", cTotals); //pass the parceable!
				startActivity(goCheckout);
			}
		});
    }
    
    
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
}