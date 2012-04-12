package com.christine.cart;


import java.util.ArrayList;
import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends Activity {
	
	TextView nutrientTitle;
	TextView nutrientDesc;
	ListView nutrients;

	private static ActionBar actionBar;
	private static Account act;
	private static PreviousHistory pcart;
	private static RecDailyValues rdvTotals;
	private static Integer[] percent;
	private static Float[] pcartNums;
	private static Float[] rdvNums;
	
	private final static String[] reduced = new String[]{
		"calories", "protein", "totalfats", "carbs", "fiber", 
		"sugar", "calcium", "iron", "magnesium", "potassium",
		"sodium", "zinc", "vitamin C", "vitamin D", "vitamin B6",
		"vitamin B12", "vitamin A", "vitamin E", "vitamin K", "saturated fat",
		"cholesterol"};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.history);
	    
	  //ActionBar
  		actionBar = (ActionBar) findViewById(R.id.actionbar);
  		actionBar.setTitle("Nutritional History of Your Previous Cart");
  		actionBar.setHomeAction(new backToDashboardAction());
  		
  		Intent dashboard = getIntent();
  		act = dashboard.getParcelableExtra("account");
  		pcart = dashboard.getParcelableExtra("pcart");
  		rdvTotals = dashboard.getParcelableExtra("rdvTotals");
  		
  		pcartNums = pcart.getNutritionPropertiesNoMono();
  		rdvNums = rdvTotals.getNutritionNeedsNoMono();
  		percent = new Integer[pcartNums.length];
  		Log.d("HistoryActivity", "pcartNums " + pcartNums[0]);
  		Log.d("HistoryActivity", "rdvNums " + rdvNums[0]);
  		
  		nutrientTitle = (TextView) findViewById(R.id.tv_nutrient_title);
  		nutrientDesc = (TextView) findViewById(R.id.tv_nutrient_desc);
  		nutrients = (ListView) findViewById(R.id.lv_nutrients);
  		
  		//get the percentages of nutrients fulfilled
  		for(int i=0; i< pcartNums.length; i++){
  			if(rdvNums[i]!=0.0f){
  				percent[i] = Math.round(pcartNums[i]/rdvNums[i]);
  			} else {
  				percent[i] = 0;
  			}
  		}
  		
  		setupNutrientList();
	}
	
	/**
	 * Set up actionbar home
	 */
	private class backToDashboardAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_home_large;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(HistoryActivity.this, DashboardActivity.class);
			startAgain.putExtra("account", act);
			startActivity(startAgain);
		}
	}
	
	/**
	 * Set up nutrient list
	 */
	private void setupNutrientList(){
	 ArrayAdapter<String> nutrientList = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_single_choice);
  		for(int i=0; i < reduced.length; i++){
  			nutrientList.add(percent[i] + "% " + reduced[i]);
  		}
  		
  		nutrients.setAdapter(nutrientList);
  		nutrients.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
  		
  		nutrients.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				nutrientTitle.setText(reduced[position]);
				nutrientDesc.setText(reduced[position]);
			}
		});
	 }
}