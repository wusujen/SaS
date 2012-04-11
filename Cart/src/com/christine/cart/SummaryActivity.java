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
			Log.d("Summary Activity", "Existing History " + existingHistory.toString());
			if (existingHistory.getCalories() != 0.0f) {
				adb.updatePreviousHistoryFor(pH);

				PreviousHistory thisHistory = adb
						.getPreviousHistoryFor(username);
				Log.d("SummaryActivity",
						"PH updated, Name: " + thisHistory.getUsername()
								+ " Days: " + thisHistory.getDays()
								+ " Calories: " + thisHistory.getCalories());
				advisor.setPastCart(existingHistory);
				
				//give the advice!
				String summary = advisor.getNegativeAdvice() + " \n \n" + advisor.getPositiveAdvice();
				String cleaned = summary.replaceAll("\\s","");
				if(cleaned.length() == 0) {
					summary = "You didn't improve or digress this time! Staying level is pretty good--but remember, you can always be a better you!";
				}
				tv_summary.setText(summary);
				
			} else {
				adb.addPreviousHistoryFor(pH);

				PreviousHistory thisHistory = adb
						.getPreviousHistoryFor(username);
				Log.d("CheckoutActivity",
						"PH added, Name: " + thisHistory.getUsername()
								+ " Days: " + thisHistory.getDays()
								+ " Calories: " + thisHistory.getCalories());
				
				String summary = advisor.getNegativeAdvice() + " \n \n" + advisor.getPositiveAdvice();
				String firstTime = "Great job finishing your first cart!\n\n" +
						"The next time you come in you will see <font color='#E57716'>ORANGE LINES</font> that keep track of your" +
						"previous shopping cart's nutritional content. \n\n" +
						"Try to keep track and improve each time!"; 
				String cleaned = summary.replaceAll("\\s","");
				if(cleaned.length() == 0) {
					summary = firstTime;
				} else {
					summary += "\n\n" + firstTime;
				}
				tv_summary.setText(summary);
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
		
			
		} else {
			tv_summary.setText("Dude, you didn't buy anything, so we can't tell you anything. Try again next time!");
		}
		
		btn_goals.setOnClickListener( new View.OnClickListener(){
			public void onClick(View v){
				advisor.clearPreviouslyShownToasts();
				Intent goToDashboard = new Intent(SummaryActivity.this,
						DashboardActivity.class);
				goToDashboard.putExtra("account", act);
				startActivity(goToDashboard);
			}
			
		});
	
	}
	
	@Override
	public void onBackPressed() {
	   return; //prevent back button from working
	}

}
