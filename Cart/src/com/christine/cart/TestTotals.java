package com.christine.cart;


import com.christine.cart.sqlite.Account;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestTotals extends Activity {

	TextView tv_rdv;
	TextView tv_peoplelist;
	
	ActionBar actionBar;
	
	Intent passedIntent;
	
	private static Account act;
	private static String username;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.test_totals);
	    
	    //ActionBar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Total Nutritional Needs");
		actionBar.setHomeAction(new backToPeopleAction());
	  		
        act = getIntent().getParcelableExtra("account");
        if(act != null){
	        username = act.getName();
	    } 
        
        tv_rdv = (TextView) findViewById(R.id.tv_rdv);
        tv_peoplelist = (TextView) findViewById(R.id.tv_peoplelist);
        if(username!=null){
        	String rdvTotals = CartActivity.getStringRDVTotalsFor(username);
        	String pTotals = CartActivity.getAllPeopleDescFor(username);
        	tv_rdv.setText(rdvTotals);
        	tv_peoplelist.setText(pTotals);
        }

    }
	
	/**
	 * Set up actionbar home
	 */
	private class backToPeopleAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_back;
		}
		
		public void performAction(View view){
			Intent backToPeople = new Intent(TestTotals.this, SetupPeopleActivity.class);
			backToPeople.putExtra("account", act);
			startActivity(backToPeople);
		}
	}


}
