package com.christine.cart;

import java.util.ArrayList;

import com.christine.cart.sqlite.Item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckoutActivity extends Activity {
	Button newCart;
	Button logout;
	TextView cartTotals;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.checkout);
	    
	    cartTotals = (TextView) findViewById(R.id.tv_cartTotal);
	    Intent cartContents = getIntent();
	    if(cartContents!=null){
	    	ArrayList<Item> cTotals = cartContents.getParcelableArrayListExtra("cartTotals");
	    	float sum = 0;
	    	for(int i=0; i<cTotals.size(); i++){
	    		Item item= cTotals.get(i);
	    		sum = sum + item.getCalories();
	    		cartTotals.append("Item Name: " + item.getItemName() + "\n" );
	    	}
	    	
	    	cartTotals.append("Caloric Total: " + sum);
	    } else{
    		cartTotals.setText("No Items in Cart");
    	}
	    
	    newCart = (Button) findViewById(R.id.btn_newcart);
	    newCart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent startNewCart = new Intent(CheckoutActivity.this, SetupPeopleActivity.class);
				startActivity(startNewCart);
			}
		});
	    
	    logout = (Button) findViewById(R.id.btn_logout);
	    logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent logoutNow = new Intent(CheckoutActivity.this, StartActivity.class);
				startActivity(logoutNow);
			}
		});
	}

}
