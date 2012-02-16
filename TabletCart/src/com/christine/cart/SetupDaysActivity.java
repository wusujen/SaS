package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SetupDaysActivity extends Activity {
	TextView peopleInfo;
	NumberPicker date;
	Button start;
	
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
	    
	    peopleInfo = (TextView) findViewById(R.id.people_info);
		peopleInfo.setText("men: " + man + "\n woman: " + woman + "\n boy: " + boy + "\n girl: " + girl);

		date = (NumberPicker) findViewById(R.id.np_days);
	    date.setMinValue(0);
	    date.setMaxValue(31);
	    
	    start = (Button) findViewById(R.id.btn_start);
	    start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent startCart=new Intent(v.getContext(),CartActivity.class);
				
				Bundle numberOfDays=new Bundle();
				numberOfDays.putInt("days", date.getValue());
				
				startCart.putExtras(numberOfDays);
				
				startActivity(startCart);
			}
		});
	}

}
