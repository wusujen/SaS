package com.christine.cart;

import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;
import com.christine.cart.visual.NutritionAdvisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SummaryActivity extends Activity {

	TextView tv_summary;
	Button btn_goals;

	AccountDatabaseHelper adb;
	Account act;
	int days;
	String username;
	NutritionAdvisor advisor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.summary);

		tv_summary = (TextView) findViewById(R.id.tv_summary);
		btn_goals = (Button) findViewById(R.id.btn_goals);

		Intent cartContents = getIntent();
		act = cartContents.getParcelableExtra("account");
		PreviousHistory pH = cartContents.getParcelableExtra("cartTotals");
		RecDailyValues rdv = cartContents.getParcelableExtra("rdv");
		days = cartContents.getIntExtra("days", 1);
		username = act.getName();
		
		advisor = new NutritionAdvisor();
		advisor.setCurrCart(pH);
		advisor.setRecDailyValues(rdv);
		advisor.setDays(days);
		advisor.clearPreviouslyShownToasts();

		if (pH != null && act != null) {
			pH.setId(null);
			pH.setDays(days);

			// write to the previous history database, if the user doesn't
			// already exist in the db
			adb = new AccountDatabaseHelper(this);

			PreviousHistory existingHistory = adb.getPreviousHistoryFor(pH
					.getUsername());
			if (existingHistory.getCalories() != 0.0f) {
				adb.updatePreviousHistoryFor(pH);

				PreviousHistory thisHistory = adb
						.getPreviousHistoryFor(username);
				Log.d("SummaryActivity",
						"PH updated, Name: " + thisHistory.getUsername()
								+ " Days: " + thisHistory.getDays()
								+ " Calories: " + thisHistory.getCalories());
				advisor.setPastCart(existingHistory);
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
				Log.d("SummaryActivity", "Grocery items successfully cleared!");
			} else {
				Log.d("SummaryActivity",
						"Grocery items still exist: " + gList.size());
			}

			adb.close();
			
			//give the advice!
			String summary = advisor.getNegativeAdvice() + " \n \n" + advisor.getPositiveAdvice();
			tv_summary.setText(summary);
			
		} else {
			tv_summary.setText("No Items in Cart");
		}
		
		btn_goals.setOnClickListener( new View.OnClickListener(){
			public void onClick(View v){
				Intent goCheckout = new Intent(SummaryActivity.this,
						CheckoutActivity.class);
				goCheckout.putExtra("account", act);
				goCheckout.putExtra("days", days);
				goCheckout.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(goCheckout);
			}
			
		});
	
	}
	
	@Override
	public void onBackPressed() {
	   return; //prevent back button from working
	}

}
