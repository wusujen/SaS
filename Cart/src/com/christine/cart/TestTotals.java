package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestTotals extends Activity {

	TextView outputText;
	TextView tv_rdv;
	TextView tv_peoplelist;
	Button btn_goback;
	
	Intent passedIntent;
	
	private static String NAME;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.test_totals);
	    
	    outputText = (TextView) findViewById(R.id.output_text);
        passedIntent = getIntent();
        if(passedIntent != null){
	        NAME = passedIntent.getStringExtra("username");
	        outputText.setText("Information for " + NAME);
	    } else {
	    	outputText.setText("No name passed.");
	    }
        
        tv_rdv = (TextView) findViewById(R.id.tv_rdv);
        tv_peoplelist = (TextView) findViewById(R.id.tv_peoplelist);
        if(NAME!=null){
        	String rdvTotals = CartActivity.getRDVTotalsFor(NAME);
        	String pTotals = CartActivity.getAllPeopleDescFor(NAME);
        	tv_rdv.setText(rdvTotals);
        	tv_peoplelist.setText(pTotals);
        }
        
        btn_goback = (Button) findViewById(R.id.btn_goback);
        btn_goback.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent backToCart = new Intent();
				setResult(RESULT_OK, backToCart);
				finish();
			}
		});

    }

}
