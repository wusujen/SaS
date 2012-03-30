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
	
	String positive = null;
	String negative = null;
	

	private final static String[] nutrients = new String[] { "calories",
			"protein", "totalfats", "carbs", "fiber", "sugar", "calcium",
			"iron", "magnesium", "potassium", "sodium", "zinc", "vitamin C",
			"vitamin D", "vitamin B6", "vitamin B12", "vitamin A", "vitamin E",
			"vitamin K", "Saturated Fat", "Mononsaturated Fat",
			"Polyunsaturated Fat", "cholesterol" };

	public NutritionAdvisor() {
		_rdv = new RecDailyValues();
		_ccart = new PreviousHistory();
		_pcart = new PreviousHistory();
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

			String nutrient = nutrients[i];
			float c = ccart[i];
			float r = rdv[i];
			float p = pcart[i];
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
			String nutrient = nutrients[i];
			int focus = nutritionFocus.get(nutrient);
			float c = ccart[i];
			float r = rdv[i];
			
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
	
	/*
	 * Accepts a context displays a toast with the positive aspects of the
	 * current state of the cart;
	 */
	public void givePositiveAdvice(Context context){
				
		if(_pcart!=null && _pcart.getCalories()!=0.0f){
			getCurrentStateWithPastCart();
			
			if(goodPrev.size()!=0 && goodPrev.get(0)!=null){
				positive = "You've improved on ";
				Log.d("NutritionAdvisor", "The non-blank: " + goodPrev.get(0));
			
				for(int i=0; i<goodPrev.size(); i++){
					String prev = goodPrev.get(i);
					
					if(announcedPrev.indexOf(prev)==-1){
						if(i<goodPrev.size()-1){
							positive += prev + ", ";
							Log.d("NutritionAdvisor", "Previous Positive is: " + prev);
						} else {
							positive += prev + ".";
							Log.d("NutritionAdvisor", "Previous Positive is: " + prev);
						}
						
						announcedPrev.add(prev);
						} 
					}
				}
		} else{
			getCurrentStateWithoutPastCart();
		}
		
		if(goodRec.size()!=0 && positive!=null){
			positive += "Great job meeting your goals on ";
		} else if(goodRec.size()!=0 && goodRec.get(0)!=null){
			positive = "Great job meeting your goals on ";
		}
		
		for(int i=0; i<goodRec.size(); i++){
			String rec = goodRec.get(i);
			
			if(announcedRec.indexOf(rec)==-1){
				if(i<goodRec.size()-1){
					positive += rec + " ,";
					Log.d("NutritionAdvisor", "Rec Positive is: " + rec);
				} else {
					positive += rec + "!";
					Log.d("NutritionAdvisor", "Rec Positive is: " + rec);
				}
				
				announcedRec.add(rec);
			}
		}
		
		if(positive!=null){
			Toast pos = Toast.makeText(context, positive, Toast.LENGTH_LONG);
			pos.show();
		}
		
		positive = null;
	}
	
	/*
	 * Accepts a context displays a toast with the negative aspects of the
	 * current state of the cart;
	 */
	public void giveNegativeAdvice(Context context){

		if(_pcart!=null && _pcart.getCalories()!=0.0f){
			getCurrentStateWithPastCart();
			
			if(badPrev.size()!=0 && badPrev.get(0)!=null){
				negative = "You've failed to improve on ";
				Log.d("NutritionAdvisor", "The non-blank: " + badPrev.get(0));
				
				for(int i=0; i<badPrev.size(); i++){
					String prev = badPrev.get(i);
					
					if(announcedPrev.indexOf(prev)==-1){
						if(i<badPrev.size()-1){
							negative += prev + ", ";
						} else {
							negative += prev + ".";
						}
						
						announcedPrev.add(prev);
					} 
				}
			}
		} else{
			getCurrentStateWithoutPastCart();
		}
		
		if(badRec.size()!=0 && negative!=null){
			negative += "You've exceeded the necessary amounts of ";
		} else if(badRec.size()!=0 && badRec.get(0)!=null){
			negative = "You've exceeded the necessary amounts of ";
		}
		
		for(int i=0; i<badRec.size(); i++){
			String rec = badRec.get(i);
			
			if(announcedRec.indexOf(rec)==-1){
				if(i<badRec.size()-1){
					negative += rec + " ,";
					Log.d("NutritionAdvisor", "Rec Negative is: " + rec);
				} else {
					negative += rec + "!";
					Log.d("NutritionAdvisor", "Rec Negative is: " + rec);
				}
				
				announcedRec.add(rec);
			}
		}
		
		if(negative!=null){
			Toast neg = Toast.makeText(context, negative, Toast.LENGTH_LONG);
			neg.show();
		}
		
		negative = null;
	}

}
