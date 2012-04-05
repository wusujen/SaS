package com.christine.cart.visual;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;

public class NutritionAdvisor {

	private static RecDailyValues _rdv;
	private static PreviousHistory _ccart;
	private static PreviousHistory _pcart;
	private static int _days;

	private static HashMap<String, Integer> nutritionFocus;
	
	// These keep track of whether or not the nutrient has been
	// exceeded by the current cart, prev = from previous cart
	// Rec is recommended amount
	private static ArrayList<String> goodPrev;
	private static ArrayList<String> badPrev;
	private static ArrayList<String> goodRec;
	private static ArrayList<String> badRec;
	
	private static ArrayList<String> announcedPrev = new ArrayList<String>();
	private static ArrayList<String> announcedRec = new ArrayList<String>();
	
	String positive = new String();
	String negative = new String();
	

	private final static String[] nutrients = new String[] { "calories",
			"protein", "totalfats", "carbs", "fiber", "sugar", "calcium",
			"iron", "magnesium", "potassium", "sodium", "zinc", "vitamin C",
			"vitamin D", "vitamin B6", "vitamin B12", "vitamin A", "vitamin E",
			"vitamin K", "Saturated Fat", "Monunsaturated Fat",
			"Polyunsaturated Fat", "cholesterol" };

	public NutritionAdvisor() {
		_rdv = new RecDailyValues();
		_ccart = new PreviousHistory();
		_pcart = new PreviousHistory();
	}
	
	public void setDays(int days){
		_days = days;
	}
	
	public void setRecDailyValues(RecDailyValues rdv){
		_rdv = rdv;
	}
	
	public void setCurrCart(PreviousHistory currentCart){
		_ccart = currentCart;
	}
	
	public void setPastCart(PreviousHistory pastCart){
		_pcart = pastCart;
	}
	

	/*
	 * Returns a hashmap of nutrients and whether or not they should be limited
	 * or eagerly consumed & sets the previous alert given and recommended alert
	 * given to 0;
	 * 
	 * -1=> limit, 0=>none
	 */
	public void getNutritionFocus() {
		nutritionFocus = new HashMap<String, Integer>();

		String[] decreaseOn = new String[] { "totalfats", "sugar", "sodium",
				"cholesterol" };
		
		for (int i = 0; i < nutrients.length; i++) {
			nutritionFocus.put(nutrients[i], 0);
		}

		for (int k = 0; k < decreaseOn.length; k++) {
			nutritionFocus.put(decreaseOn[k], -1);
		}

	}

	/*
	 * If previous cart exists, use this function to keep track of the advances
	 * in nutrition and warn users if they exceed previous history/ recommended
	 * values based upon nutrition focus
	 */
	public void getCurrentStateWithPastCart() {
		getNutritionFocus();
		
		Float[] rdv = _rdv.getNutritionNeeds();
		Float[] ccart = _ccart.getNutritionProperties();
		Float[] pcart = _pcart.getNutritionProperties();
		
		goodPrev = new ArrayList<String>();
		badPrev = new ArrayList<String>();
		goodRec = new ArrayList<String>();
		badRec = new ArrayList<String>();
		
		for (int i = 0; i < nutrients.length; i++) {
			float r = rdv[i] * _days;
			float p = pcart[i];
			float c = ccart[i];
			
			if(r!=0.0f && p!=0.0f && c!=0.0f){
				String nutrient = nutrients[i];
				int focus = nutritionFocus.get(nutrient);
				
				int bRec = badRec.indexOf(nutrient);
				int gRec = goodRec.indexOf(nutrient);
				int gPrev = goodPrev.indexOf(nutrient);
				int bPrev = badPrev.indexOf(nutrient);
				
				switch (focus) {
				case -1:
					if (c >= r && c < p && bRec==-1) {
						badRec.add(nutrient);
					} else if (c < r && c >= p && bPrev==-1) {
						badPrev.add(nutrient);
					} else if (r == p && c >= r
							&& bPrev != -1
							&& bRec != -1) {
						badRec.add(nutrient);
						badPrev.add(nutrient);
					}
					break;
				default: // =>0
					if (c >= r && c < p && gRec== -1) {
						goodRec.add(nutrient);
					} else if (c < r && c >= p && gPrev == -1) {
						goodPrev.add(nutrient);
					} else if (r == p && c >= r
							&& gPrev == -1
							&& gRec == -1) {
						goodPrev.add(nutrient);
						goodRec.add(nutrient);
					}
					
					break;
				}
			}
		}
		
		Log.d("NutritionAdvisor", "Get Current State with Past, Good Rec: " + goodRec.toString());
		Log.d("NutritionAdvisor", "Get Current State with Past, Bad Rec: " + badRec.toString());
		Log.d("NutritionAdvisor", "Get Current State with Past, Good Prev: " + goodPrev.toString());
		Log.d("NutritionAdvisor", "Get Current State with Past, Bad Prev: " + badPrev.toString());
		
	}

	/*
	 * If previous cart does not exist, use this function to ensure that current
	 * cart nutrients follow the recommended amounts
	 */
	public void getCurrentStateWithoutPastCart() {
		getNutritionFocus();
		
		Float[] rdv = _rdv.getNutritionNeeds();
		Float[] ccart = _ccart.getNutritionProperties();

		goodRec = new ArrayList<String>();
		badRec = new ArrayList<String>();
		
		for (int i = 0; i < nutritionFocus.size(); i++) {
			float r = rdv[i] * _days;
			float c = ccart[i];
			
			if(r!=0.0f && c!=0.0f){
				String nutrient = nutrients[i];
				int focus = nutritionFocus.get(nutrient);
	
				int bRec = badRec.indexOf(nutrient);
				int gRec = goodRec.indexOf(nutrient);
	
				if (bRec==-1 || gRec==-1) {
					switch (focus) {
					case -1:
						if (c >= r) {
							badRec.add(nutrient);
						}
						break;
					default: // =>0
						if (c >= r) {
							goodRec.add(nutrient);
						}
						break;
					}
				}
			}
		}
		
		Log.d("NutritionAdvisor", "Get Current State without Past, Good Rec: " + goodRec.toString());
		Log.d("NutritionAdvisor", "Get Current State without Past, Bad Rec: " + badRec.toString());
	}
	
	/*
	 * Accepts a context displays a toast with the positive aspects of the
	 * current state of the cart;
	 */
	public String getPositiveAdvice(){
		positive = new String();
		String prevPos = new String();
		String recPos = new String();
		
		if(_pcart!=null && _pcart.getCalories()!=0.0f){
			getCurrentStateWithPastCart();
			
			if(goodPrev.size()!=0 && goodPrev.get(0)!=null){
			
				for(int i=0; i<goodPrev.size(); i++){
					String prev = goodPrev.get(i);
					
					if(announcedPrev.indexOf(prev)==-1){
						if(i<goodPrev.size()-1){
							prevPos += prev + ", ";
							Log.d("NutritionAdvisor", "Previous Positive is: " + prev);
						} else {
							prevPos += prev + ".";
							Log.d("NutritionAdvisor", "Previous Positive is: " + prev);
						}
						
						announcedPrev.add(prev);
						} 
					}
				
				if(prevPos!=null && prevPos.length()>0){
					positive = "You've improved on " + prevPos;
				}
				
			}
			
		} else{
			getCurrentStateWithoutPastCart();
		}
		
		
		for(int i=0; i<goodRec.size(); i++){
			String rec = goodRec.get(i);
			
			if(announcedRec.indexOf(rec)==-1){
				if(i<goodRec.size()-1){
					recPos += rec + " ,";
					Log.d("NutritionAdvisor", "Rec Positive is: " + rec);
				} else {
					recPos += rec + "!";
					Log.d("NutritionAdvisor", "Rec Positive is: " + rec);
				}
				
				announcedRec.add(rec);
			}
		}
		
		if(recPos!=null && recPos.length()>0){
			positive += "\nGreat job! You've met the requirements on " + recPos;
		}
		
		return positive;
		
	}
	
	/*
	 * Accepts a context displays a toast with the negative aspects of the
	 * current state of the cart;
	 */
	public String getNegativeAdvice(){
		negative = new String();
		String prevNeg = new String();
		String recNeg = new String();
		
		if(_pcart!=null && _pcart.getCalories()!=0.0f){
			getCurrentStateWithPastCart();
			
			if(badPrev.size()!=0 && badPrev.get(0)!=null){
			
				for(int i=0; i<badPrev.size(); i++){
					String prev = badPrev.get(i);
					
					if(announcedPrev.indexOf(prev)==-1){
						if(i<badPrev.size()-1){
							prevNeg += prev + ", ";
							Log.d("NutritionAdvisor", "Previous Negative is: " + prev);
						} else {
							prevNeg += prev + ".";
							Log.d("NutritionAdvisor", "Previous Negative is: " + prev);
						}
						
						announcedPrev.add(prev);
						} 
					}
				
				if(prevNeg!=null && prevNeg.length()>0){
					negative = " You did better last time on " + prevNeg;
				}
				
			}
			
		} else{
			getCurrentStateWithoutPastCart();
		}
		
		
		for(int i=0; i<badRec.size(); i++){
			String rec = badRec.get(i);
			
			if(announcedRec.indexOf(rec)==-1){
				if(i<badRec.size()-1){
					recNeg += rec + " ,";
					Log.d("NutritionAdvisor", "Rec Negative is: " + rec);
				} else {
					recNeg += rec + "!";
					Log.d("NutritionAdvisor", "Rec Negative is: " + rec);
				}
				
				announcedRec.add(rec);
			}
		}
		
		if(recNeg!=null && recNeg.length()>0){
			negative += "\nTry to reduce the amounts of " + recNeg;
		}
		
		return negative;
	}
	
	public void giveAdvice(Context context){
		
		String posA = getPositiveAdvice();
		String negA = getNegativeAdvice();
		
		if(posA!=null && negA!=null && posA.length()!=0 && negA.length()!=0) {
			Toast neg = Toast.makeText(context, negA, Toast.LENGTH_LONG);
			neg.show();
			
			Toast pos = Toast.makeText(context, posA, Toast.LENGTH_LONG);
			pos.show();
		}
		
	}
	
	public void clearPreviouslyShownToasts(){
		announcedPrev = new ArrayList<String>();
		announcedRec = new ArrayList<String>();
	}
	
	
}