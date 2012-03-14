package com.christine.cart;

import java.util.ArrayList;
import java.util.List;

import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.NutritionDatabaseHelper;
import com.christine.cart.sqlite.Person;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;
import com.christine.cart.visual.GraphView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends FooterActivity {
	String results;
	Button viewItemList;
	Button checkout;
	Button test;
	
	Button searchItem;
	Button scanItem;

	Intent passedIntent;
	GraphView graph;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.data);
        ViewGroup.inflate(CartActivity.this, R.layout.cart, vg);
        
        graph = (GraphView) this.findViewById(R.id.graphview);
        graph.setDays(days);
        
        test = (Button) findViewById(R.id.btn_test);
        test.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent startTest = new Intent(CartActivity.this, TestTotals.class);
				startTest.putExtra("username", NAME);
				startActivityForResult(startTest, 0);
			}
		});
        
        
        checkout = (Button) findViewById(R.id.btn_checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goCheckout = new Intent(CartActivity.this, CheckoutActivity.class);
				PreviousHistory cTotals = getCartTotalsFor(NAME);
				goCheckout.putExtra("cartTotals", cTotals); //pass the parceable!
				goCheckout.putExtra("account", act);
				startActivity(goCheckout);
			}
		});

    }
    
    protected void onResume(){
    	super.onResume();
    	
    	passedIntent = getIntent();
    	
    	
    	results = passedIntent.getStringExtra("results");
    	int check = passedIntent.getIntExtra("check",0);
    	if(check==1){
	    	if(results != null){
	    		String temp = results;
	    		Toast result=Toast.makeText(inputsContext, "You just added " + temp,Toast.LENGTH_SHORT);
	    		result.setGravity(Gravity.TOP|Gravity.LEFT,0,150);
	    		result.show();

	    	} else{
	    		Toast noMatch=Toast.makeText(inputsContext, "There was no match in the DB",Toast.LENGTH_SHORT);
	    		noMatch.setGravity(Gravity.TOP|Gravity.LEFT,0,150);
	    		noMatch.show();
	    	}
    	}  
    	

    	
    	PreviousHistory pH = getCartTotalsFor(NAME);
    	if(pH!=null ){
    		int nH = Integer.valueOf(Math.round(pH.getCalories()));
	    	graph.setnH(nH);
	    	 Log.d("CartActivity", "set to" + nH);
	    	graph.setDays(days);
	        graph.postInvalidate();
	        
    	} else {
    		graph.setnH(0);
    	}
    	
    }
    
    public static String getAllPeopleDescFor(String username){
    	List<Person> p = db.getAllPeopleFor(username);
    	String allPeopleDesc = new String();
    	if(p!=null){
	    	for(int i=0; i<p.size(); i++){
	    		Person temp = p.get(i);
	    		allPeopleDesc = allPeopleDesc + "\n" + temp.returnString();
	    	} 
    	} else {
    		db.close();
    		return "No one has been added to Cart yet. Please check preferences.";
    	}
	    
    	db.close();
    	return allPeopleDesc;
    }
    
    /**
     * TOTAL RDR
     * Get the total values of all of the
     * RecDailyValues
     * 
     */
    public static RecDailyValues getRDVTotalsFor(String username){
    	List<Person> p = db.getAllPeopleFor(username);
    	List<RecDailyValues> rdvList = new ArrayList<RecDailyValues>();
    	if(p!=null){
	    	for(int i=0; i<p.size(); i++){
	    		RecDailyValues tempRDV = new RecDailyValues(p.get(i));
	    		rdvList.add(tempRDV);
	    	} 
    	} else {
    		db.close();
    		return null;
    	}
	    
    	db.close();
    	RecDailyValues total = getTotalRDVOf(rdvList);
    	return total;
    }
    
    public static String getStringRDVTotalsFor(String username){
    	List<Person> p = db.getAllPeopleFor(username);
    	List<RecDailyValues> rdvList = new ArrayList<RecDailyValues>();
    	if(p!=null){
	    	for(int i=0; i<p.size(); i++){
	    		RecDailyValues tempRDV = new RecDailyValues(p.get(i));
	    		rdvList.add(tempRDV);
	    	} 
    	} else {
    		db.close();
    		return "No one has been added to Cart yet. Please check preferences.";
    	}
	    
    	db.close();
    	RecDailyValues total = getTotalRDVOf(rdvList);
    	return total.returnString();
    }
    
    public static RecDailyValues getTotalRDVOf(List<RecDailyValues> rdvList){
    	
    	RecDailyValues total = new RecDailyValues();
    	
    	for(int i=0; i<rdvList.size(); i++){
    		//create a temporary RDV object
    		RecDailyValues temp = rdvList.get(i);
    		
    		//set all of the properties based on the current and the sum of the new
    		total.setCalories(total.getCalories() + temp.getCalories());
    		total.setCalories((total.getCalories() + temp.getCalories()));
    		total.setProtein((total.getProtein() + temp.getProtein()));
    		total.setFat((total.getFat() + temp.getFat()));
    		total.setCarbohydrate((total.getCarbohydrate() + temp.getCarbohydrate()));
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
    		total.setCholesterol((total.getCholesterol() + temp.getCholesterol()));
    	}
    	return total;
    }
    
    
    
    
 
}