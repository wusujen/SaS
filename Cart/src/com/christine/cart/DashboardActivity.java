package com.christine.cart;

import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashboardActivity extends Activity {
	
	Button addPeople;
	Button help;
	Button history;
	Button logout;
	Button cartLauncher;
	TextView widgetTitle;
	TextView cartStatus;
	
	private static Account act;
	private static String username;
	private static AccountDatabaseHelper adb;
	private static List<Person> people;
	private static int groceryCount;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dashboard);
	    
	    act = getIntent().getParcelableExtra("account");
	    username = act.getName();
	    
	    addPeople = (Button) findViewById(R.id.btn_people);
	    help = (Button) findViewById(R.id.btn_help);
	    history = (Button) findViewById(R.id.btn_history);
	    logout = (Button) findViewById(R.id.btn_logout);
	    cartLauncher = (Button) findViewById(R.id.btn_cartlauncher);
	    widgetTitle = (TextView) findViewById(R.id.tv_cart_widget_title);
	    cartStatus = (TextView) findViewById(R.id.tv_cart_status);
	    
	    //check if the main person has been set on the account
		adb = new AccountDatabaseHelper(DashboardActivity.this);
		people= adb.getAllPeopleFor(username);
		groceryCount = adb.getGroceryCount(username);
		adb.close();
		
		if(people==null){
			addPeople.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_people_highlighted));
			widgetTitle.setText("NO CART YET");
			cartStatus.setText("Please add people before starting a cart!");
		} else {
			addPeople.setBackgroundResource(R.drawable.dashboard_people_normal);
			widgetTitle.setText("");
			cartStatus.setText("Please add people before starting a cart!");
			setOnClickListeners();
			
			if(groceryCount!=0){
				widgetTitle.setText("CURRENT CART");
				cartStatus.setText("You have " + groceryCount + " in your Cart! Keep going!");
			} else {
				widgetTitle.setText("START A NEW CART");
				cartStatus.setText("Start tracking your cart's nutrients!");
			}
		}
		
		addPeople.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				boolean main = false;
				
				if(people!=null){
					for(int i=0; i<people.size(); i++){
						main = people.get(i).getMain();
						if(main){
							Intent startSetupPeople = new Intent(DashboardActivity.this, SetupPeopleActivity.class);
							startSetupPeople.putExtra("account",act);
							startSetupPeople.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							startActivity(startSetupPeople);
						}
					}
				}

				if(!main){
					Intent startSetupProfile = new Intent(DashboardActivity.this, ProfileActivity.class);
					startSetupProfile.putExtra("account",act);
					startSetupProfile.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(startSetupProfile);
				}
				
			}
		});
	   
	    logout.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent logout = new Intent(DashboardActivity.this, StartActivity.class);
				logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(logout);
			}
		});
	    
	}
	
	private void setOnClickListeners(){
		 
		 
		 cartLauncher.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent startCart = new Intent(DashboardActivity.this, CartActivity.class);
					startCart.putExtra("account", act);
					startActivity(startCart);
				}
		});
	}
}
