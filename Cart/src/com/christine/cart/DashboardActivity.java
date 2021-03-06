package com.christine.cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.NutritionDatabaseHelper;
import com.christine.cart.sqlite.Person;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DashboardActivity extends Activity {
	
	Button addPeople;
	Button help;
	Button nutrient;
	Button logout;
	Button cartLauncher;
	TextView widgetTitle;
	TextView cartStatus;
	TextView welcome;
	
	private static Account act;
	private static String username;
	private static AccountDatabaseHelper adb;
	private static List<Person> people;
	private static int groceryCount;
	private static PreviousHistory pcart;
	private static RecDailyValues rdvTotals;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dashboard);
	    
	    act = getIntent().getParcelableExtra("account");
	    username = act.getName();
	    
	    addPeople = (Button) findViewById(R.id.btn_people);
	    help = (Button) findViewById(R.id.btn_help);
	    nutrient = (Button) findViewById(R.id.btn_nutrient);
	    logout = (Button) findViewById(R.id.btn_logout);
	    cartLauncher = (Button) findViewById(R.id.btn_cartlauncher);
	    widgetTitle = (TextView) findViewById(R.id.tv_cart_widget_title);
	    cartStatus = (TextView) findViewById(R.id.tv_cart_status);
	    welcome = (TextView) findViewById(R.id.tv_welcome);
	    
	    //set the welcome text
	    String welcomeText = "welcome " + username;
	    welcome.setText(welcomeText.toUpperCase());
	    
	    //check if the main person has been set on the account
		adb = startAccountDB();
		people= adb.getAllPeopleFor(username);
		groceryCount = adb.getGroceryCountFor(username);
		pcart = adb.getPreviousHistoryFor(username);
		rdvTotals = getRDVTotalsFor(username);
		adb.close();
		
		if(people==null){
			widgetTitle.setText("YOU HAVEN'T STARTED A CART YET");
			cartStatus.setText("Start by telling us about you.");
			
			cartLauncher.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent startSetupProfile = new Intent(DashboardActivity.this, ProfileActivity.class);
					startSetupProfile.putExtra("account",act);
					startActivity(startSetupProfile);
				}
			});
		} else {
			widgetTitle.setText("");
			cartStatus.setText("Please add people before starting a cart!");
			
			if(groceryCount==1){
				widgetTitle.setText("CURRENT CART HAS " + groceryCount + " ITEM");
				cartStatus.setText("You're shopping for " + people.size() + " people, " + act.getDays() + " days.");
				startCartActivity();
				
			} else if (groceryCount>1){
				
				widgetTitle.setText("CURRENT CART HAS " + groceryCount + " ITEMS");
				cartStatus.setText("You're shopping for " + people.size() + " people, " + act.getDays() + " days.");
				startCartActivity();
				
			} else {
				widgetTitle.setText("START A NEW CART");
				cartStatus.setText("Start tracking your cart's nutrients!");
				startDaysActivity();
			}
		}
		
		startNutrientActivity();
		
		addPeople.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				boolean main = false;
				
				if(people!=null){
					for(int i=0; i<people.size(); i++){
						main = people.get(i).getMain();
						if(main){
							Intent startSetupPeople = new Intent(DashboardActivity.this, SetupPeopleActivity.class);
							startSetupPeople.putExtra("account",act);
							startActivity(startSetupPeople);
						} else {
							Intent startSetupProfile = new Intent(DashboardActivity.this, ProfileActivity.class);
							startSetupProfile.putExtra("account",act);
							startActivity(startSetupProfile);
						}
					}
				} else {
					Intent startSetupProfile = new Intent(DashboardActivity.this, ProfileActivity.class);
					startSetupProfile.putExtra("account",act);
					startActivity(startSetupProfile);
				}
				
			}
		});
		
	   
	    logout.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showLogoutDialog();
			}
		});
	    
	}
	
	private void startDaysActivity(){
		 cartLauncher.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					Intent startCart = new Intent(DashboardActivity.this, SetupDaysActivity.class);
					startCart.putExtra("account", act);
					startActivity(startCart);
				}
		});
	}
	
	private void startCartActivity(){
		 cartLauncher.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					Intent startCart = new Intent(DashboardActivity.this, CartActivity.class);
					startCart.putExtra("account", act);
					startActivity(startCart);
				}
		});
	}
	
	public void startNutrientActivity(){
		nutrient.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent goToNutrient = new Intent(DashboardActivity.this, NutrientActivity.class);
				if (pcart != null && pcart.getCalories() != 0.0f && rdvTotals != null
						&& rdvTotals.getCalories() != 0.0f) {
					goToNutrient.putExtra("pcart", pcart);
					goToNutrient.putExtra("rdvTotals", rdvTotals);
				} else {
					pcart = new PreviousHistory();
					rdvTotals = new RecDailyValues();
				}
				goToNutrient.putExtra("account", act);
				startActivity(goToNutrient);
			}
		});
	}
	
	/**
	 * CHECKOUT ALERT DIAOLOG
	 */
	private void showLogoutDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you really want to log out? We'll miss you!");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        		Intent logout = new Intent(DashboardActivity.this, StartActivity.class);
						logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(logout);
		           }
		       });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog endLogin = builder.create();
		endLogin.show();
	}
	
	/**
	 * TOTAL RDR Get the total values of all of the RecDailyValues
	 * 
	 */
	public static RecDailyValues getRDVTotalsFor(String username) {
		List<Person> p = adb.getAllPeopleFor(username);
		List<RecDailyValues> rdvList = new ArrayList<RecDailyValues>();
		if (p != null) {
			for (int i = 0; i < p.size(); i++) {
				RecDailyValues tempRDV = new RecDailyValues(p.get(i));
				rdvList.add(tempRDV);
			}
		} else {
			adb.close();
			return null;
		}

		adb.close();
		RecDailyValues total = getTotalRDVOf(rdvList);
		return total;
	}

	/**
	 * @param rdvList
	 * @return RecDailyValue Contains all of the totaled values for the group of
	 *         people the user has set
	 */
	public static RecDailyValues getTotalRDVOf(List<RecDailyValues> rdvList) {

		RecDailyValues total = new RecDailyValues();

		for (int i = 0; i < rdvList.size(); i++) {
			// create a temporary RDV object
			RecDailyValues temp = rdvList.get(i);

			// set all of the properties based on the current and the sum of the
			total.setCalories(total.getCalories() + temp.getCalories());
			total.setProtein((total.getProtein() + temp.getProtein()));
			total.setFat((total.getFat() + temp.getFat()));
			total.setCarbohydrate((total.getCarbohydrate() + temp
					.getCarbohydrate()));
			total.setFiber((total.getFiber() + temp.getFiber()));
			total.setSugar((total.getSugar() + temp.getSugar()));
			total.setCalcium((total.getCalcium() + temp.getCalcium()));
			total.setIron((total.getIron() + temp.getIron()));
			total.setMagnesium((total.getMagnesium() + temp.getMagnesium()));
			total.setPotassium((total.getPotassium() + temp.getPotassium()));
			total.setSodium((total.getSodium() + temp.getSodium()));
			total.setZinc((total.getZinc() + temp.getZinc()));
			total.setVitC((total.getVitC() + temp.getVitC()));
			total.setVitB6((total.getVitB6() + temp.getVitB6()));
			total.setVitB12((total.getVitB12() + temp.getVitB12()));
			total.setVitA((total.getVitA() + temp.getVitA()));
			total.setVitE((total.getVitE() + temp.getVitE()));
			total.setVitD((total.getVitD() + temp.getVitD()));
			total.setVitK((total.getVitK() + temp.getVitK()));
			total.setFatSat((total.getFatSat() + temp.getFatSat()));
			total.setFatPoly((total.getFatPoly() + temp.getFatPoly()));
			total.setCholesterol((total.getCholesterol() + temp
					.getCholesterol()));
		}
		
		return total;
	}
	
	
	/**
	 * Convenience method for creating a database helper
	 * or initializing the database
	 * 
	 * @return AccountDatabaseHelper
	 */
	public AccountDatabaseHelper startAccountDB(){
		AccountDatabaseHelper db = new AccountDatabaseHelper(this);
		
		try {
			db.createDataBase();
		} catch (IOException ioe) {
			throw new Error(
					"Unable to create database, or db has been created already");
		}
		// OPEN THE DATABASE
		try {
			db.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		return db;
	}
	
}
