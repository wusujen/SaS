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

		cartTotals = (TextView) findViewById(R.id.tv_cartTotal);
		Intent cartContents = getIntent();
		act = cartContents.getParcelableExtra("account");
		PreviousHistory pH = cartContents.getParcelableExtra("cartTotals");
		days = cartContents.getIntExtra("days", 1);
		username = act.getName();

		if (pH != null && act != null) {
			pH.setId(null);
			pH.setDays(days);

			// write to the previous history database, if the user doesn't
			// already exist in the db
			adb = new AccountDatabaseHelper(this);

			PreviousHistory existingHistory = adb.getPreviousHistoryFor(pH
					.getUsername());
			if (existingHistory.getCalories()!=0.0f) {
				adb.updatePreviousHistoryFor(pH);

				PreviousHistory thisHistory = adb
						.getPreviousHistoryFor(username);
				Log.d("CheckoutActivity",
						"PH updated, Name: " + thisHistory.getUsername()
								+ " Days: " + thisHistory.getDays()
								+ " Calories: " + thisHistory.getCalories());
			} else {
				adb.addPreviousHistoryFor(pH);

				PreviousHistory thisHistory = adb
						.getPreviousHistoryFor(username);
				Log.d("CheckoutActivity",
						"PH added, Name: " + thisHistory.getUsername()
								+ " Days: " + thisHistory.getDays()
								+ " Calories: " + thisHistory.getCalories());
			}

			// delete all of the current cart items based upon the username
			adb.deleteAllGroceryItemsOf(username);
			List<GroceryItem> gList = adb.getAllGroceryItemsOf(username);
			if (gList.size() == 0) {
				Log.d("CheckoutActivity", "Grocery items successfully cleared!");
			} else {
				Log.d("CheckoutActivity",
						"Grocery items still exist: " + gList.size());
			}

			adb.close();
		} else {
			cartTotals.setText("No Items in Cart");
		}

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
				startActivity(logoutNow);
			}
		});
	}

}
