package com.christine.cart;


import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.DaysActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SetupDaysActivity extends Activity {

	TextView printDays;
	SeekBar daySetter;
	Button start;
			
	ActionBar actionBar;

	private static final int SAVE_PEOPLE = 2;
	private static String username;
	private static Integer days;
	private static Account act;
	private static int fromCart;
	
	AccountDatabaseHelper db;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setup_days);
	    
	    printDays = (TextView) findViewById(R.id.tv_printdays);
	    
	    act = getIntent().getParcelableExtra("account");
	    fromCart = getIntent().getIntExtra("cart", 0);
	    
	    if(act!=null){
	    	username = act.getName();
	    	days = Integer.valueOf(act.getDays());
	    	if(days!=0 && days!=null && days!=-1){
	    		printDays.setText("I'M SHOPPING FOR " + days + " DAYS.");
	    	} else {
	    		printDays.setText("I'M SHOPPING FOR 1 DAY.");
	    		days = 1;
	    	}
	    } else{
	    	throw new RuntimeException("SetupDaysActivity: Passed account is null");
	    }
	    
	    //ActionBar
  		actionBar = (ActionBar) findViewById(R.id.actionbar);
  		actionBar.setTitle("Days");
  		actionBar.setHomeAction(new backToDashboardAction());
		
		daySetter = (SeekBar) findViewById(R.id.seekbar_days);
		daySetter.setMax(7);
		daySetter.setProgress(days);
		daySetter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	
			public void onStopTrackingTouch(SeekBar seekBar) {
				//nada
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				//nada
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					days=progress + 1;
					printDays.setText("I'M SHOPPING FOR " + days + " DAYS.");
				}
			}
		});
	    
	    start = (Button) findViewById(R.id.btn_start);
	    if(fromCart==1){
	    	start.setText("Back To Cart");
	    }
	    start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent saveDays=new Intent(v.getContext(),DaysActivity.class);
				
				Bundle numberOfDays=new Bundle();
				numberOfDays.putInt("days", days);
				numberOfDays.putString("username", username);
				
				saveDays.putExtras(numberOfDays);
				
				startActivityForResult(saveDays, SAVE_PEOPLE);
			}
		});
	}
	
	/**
	 * Set up actionbar home
	 */
	private class backToDashboardAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_home_large;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(SetupDaysActivity.this, DashboardActivity.class);
			startAgain.putExtra("account", act);
			startActivity(startAgain);
		}
	}
	 
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==SAVE_PEOPLE){
			if(resultCode==RESULT_OK){
				Intent startCart = new Intent(this, CartActivity.class);
				startCart.putExtra("account", data.getParcelableExtra("account"));
				startCart.putExtra("days", days);
				startCart.putExtra("code", 0);
				startActivity(startCart);
			} else {
				throw new RuntimeException("Could not save day. Please try again.");
			}
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
}
