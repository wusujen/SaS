package com.christine.cart;

import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.PreviousHistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckoutActivity extends Activity {
	Button newCart;
	Button logout;
	TextView cartTotals;

	AccountDatabaseHelper adb;
	Account act;
	int days;
	String username;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);

		Intent cartContents = getIntent();
		act = cartContents.getParcelableExtra("account");

		newCart = (Button) findViewById(R.id.btn_newcart);
		newCart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent startNewCart = new Intent(CheckoutActivity.this,
						SetupPeopleActivity.class);
				startNewCart.putExtra("account", act);
				startNewCart.putExtra("username", act.getName());
				startActivity(startNewCart);
			}
		});

		logout = (Button) findViewById(R.id.btn_logout);
		logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent logoutNow = new Intent(CheckoutActivity.this,
						StartActivity.class);
				logoutNow.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(logoutNow);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
	   return; //prevent back button from working
	}

}
