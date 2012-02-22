package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckoutActivity extends Activity {
	Button newCart;
	Button logout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.checkout);
	    
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
