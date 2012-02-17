package com.christine.cart;

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
				Intent startCart=new Intent(v.getContext(),CartActivity.class);
				
				Bundle numberOfDays=new Bundle();
				numberOfDays.putInt("days", days);
				
				startCart.putExtras(numberOfDays);
				
				startActivity(startCart);
			}
		});
	}

}
