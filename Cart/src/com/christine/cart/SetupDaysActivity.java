package com.christine.cart;


import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.DaysActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SetupDaysActivity extends Activity {
	TextView peopleInfo;
	TextView printDays;
	SeekBar daySetter;
	Button start;
			
	int days;
	private static final int SAVE_PEOPLE = 2;
	private static String USERNAME;
	private Account act;
	
	AccountDatabaseHelper db;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setup_days);
	    
	    Bundle people = getIntent().getExtras();
	    if(people==null){
	    	return;
	    }
	    
	    int man=people.getInt("man");
	    int woman=people.getInt("woman");
	    int boy=people.getInt("boy");
	    int girl=people.getInt("girl");
	    
	    act = people.getParcelable("account");
	    if(act!=null){
	    	USERNAME = act.getName();
	    } else{
	    	throw new RuntimeException("SetupDaysActivity: Passed account is null");
	    }
	    
	    peopleInfo = (TextView) findViewById(R.id.tv_peopleinfo);
		peopleInfo.setText("men: " + man + "\n woman: " + woman + "\n boy: " + boy + "\n girl: " + girl);
		
		printDays = (TextView) findViewById(R.id.tv_printdays);
		daySetter = (SeekBar) findViewById(R.id.seekbar_days);
		daySetter.setMax(31);
		daySetter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	
			public void onStopTrackingTouch(SeekBar seekBar) {
				//nada
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				//nada
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					days=progress;
					printDays.setText("Days: "+ days);
				}
			}
		});
	    
	    start = (Button) findViewById(R.id.btn_start);
	    start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent saveDays=new Intent(v.getContext(),DaysActivity.class);
				
				Bundle numberOfDays=new Bundle();
				numberOfDays.putInt("days", days);
				numberOfDays.putString("username", USERNAME);
				
				saveDays.putExtras(numberOfDays);
				
				startActivityForResult(saveDays, SAVE_PEOPLE);
			}
		});
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
