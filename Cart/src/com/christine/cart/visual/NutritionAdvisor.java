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
			"protein", "total fats", "carbs", "fiber", "sugar", "calcium",
			"iron", "magnesium", "potassium", "sodium", "zinc", "vitamin C",
			"vitamin D", "vitamin B6", "vitamin B12", "vitamin A", "vitamin E",
			"vitamin K", "saturated fat", "monunsaturated fat",
			"polyunsaturated fat", "cholesterol" };

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

		String[] decreaseOn = new String[] { "total fats", "sugar", "sodium",
				"saturated fat", "cholesterol" };
		
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
		
		// arrays keep track of what alerts have been sent!
		// there seem to be two states, a FINAL state and a CURRENT ACTIVE state.
		// [] means no recommendation will be shown, because it is apparent via viz
		goodPrev = new ArrayList<String>();		// user exceeded their previous amount on a good item, or they stayed below their previous amount on a bad item
		badPrev = new ArrayList<String>();		// user exceeded their previous amount on a bad item, or they didn't meet their previous amount on a good item
		goodRec = new ArrayList<String>();		// user exceeded recommended amount of a good item, or stayed below the recommended amount on a bad item
		badRec = new ArrayList<String>();		// user exceeded recommended amount of bad item, or [stayed below the recommended amount of a good item]
		
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
				

				// if current cart amount is greater than recommended amount,
				// and it is a bad item and has not been shown yet
				if(c > r && focus == -1 && bRec == -1) {
					badRec.add(nutrient);
					Log.d("NutritionAdvisor", "Bad Rec: " + nutrient);
				} 
				// if current cart amount is greater than recommended amount,
				// and it is a good item and has not been shown yet
				else if (c > r && focus !=-1 && gRec == -1) {
					goodRec.add(nutrient);
					Log.d("NutritionAdvisor", "Good Rec: " + nutrient);
				}
				
				
				// if current cart amount is greater than previous amount,
				// and it is a bad item and it has not been shown yet
				if(c > p && focus == -1 && bPrev == -1){
					badPrev.add(nutrient);
					Log.d("NutritionAdvisor", "Bad Prev: " + nutrient);
				}
				
				// if current cart amount is greater than previous amount,
				// and it is a good item and has not been shown yet 
				if(c > p && focus != -1 && gPrev == -1){
					goodPrev.add(nutrient);
					Log.d("NutritionAdvisor", "Good Prev: " + nutrient);
				}
			
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
			float r = rdv[i] * _days;
			float c = ccart[i];
			
			if(r!=0.0f && c!=0.0f){
				String nutrient = nutrients[i];
				int focus = nutritionFocus.get(nutrient);
	
				int bRec = badRec.indexOf(nutrient);
				int gRec = goodRec.indexOf(nutrient);
	
				// if current cart amount is greater than recommended amount,
				// and it is a bad item and has not been shown yet
				if(c > r && focus == -1 && bRec == -1) {
					badRec.add(nutrient);
					Log.d("NutritionAdvisor", "Bad Rec: " + nutrient);
				} 
				// if current cart amount is greater than recommended amount,
				// and it is a good item and has not been shown yet
				else if (c > r && focus !=-1 && gRec == -1) {
					goodRec.add(nutrient);
					Log.d("NutritionAdvisor", "Good Rec: " + nutrient);
				}
			}
		}
	}
	
	/*
	 * Accepts a context displays a toast with the positive aspects of the
	 * current state of the cart;
	 */
	public String getPositiveAdviceAboutPrevious(){
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
						} else if(i==goodPrev.size() && prevPos!=null && prevPos.length()!=0){
							prevPos += " and " + prev;
							Log.d("NutritionAdvisor", "Previous Positive is: " + prev);
						} else {
							prevPos += prev;
							Log.d("NutritionAdvisor", "Previous Positive is: " + prev);
						}
						
						announcedPrev.add(prev);
						} 
					}
				
				if(prevPos!=null && prevPos.length()>0){
					positive = "Great job! Compared to your previous shopping trip, you've increased the amount of " + prevPos + " in your cart.";
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
				} else if(i==goodRec.size() && recPos!=null && recPos.length()!=0){
					recPos += " and " + rec;
					Log.d("NutritionAdvisor", "Rec Positive is: " + rec);
				} else {
					recPos += rec;
					Log.d("NutritionAdvisor", "Rec Positive is: " + rec);
				}
				
				announcedRec.add(rec);
			}
		}
		
		if(prevPos!=null && prevPos.length()>0){
			positive += " \n \n";
		}
		
		if(recPos!=null && recPos.length()>0){
			positive += "You've met the nutritional requirements of " + recPos + " for " + _days + " days.";
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
							prevNeg += prev;
							Log.d("NutritionAdvisor", "Previous Negative is: " + prev);
						}
						
						announcedPrev.add(prev);
						} 
					}
				
				if(prevNeg!=null && prevNeg.length()>0){
					negative = "In your previous shopping trip, your cart contained less " + prevNeg + ". Try to reduce if you can.";
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
					recNeg += rec;
					Log.d("NutritionAdvisor", "Rec Negative is: " + rec);
				}
				
				announcedRec.add(rec);
			}
		}
		
		if(prevNeg!=null && prevNeg.length()>0){
			negative += " \n \n";
		}
		if(recNeg!=null && recNeg.length()>0){
			negative += "Careful. You have exceeded the recommended amounts of " + recNeg + ". Try to decrease the amounts of these nutrients in your cart.";
		}
		
		return negative;
	}
	
	
	public String[] getFinalAdvice(){
		String bad = null;
		String good = null;
		
		for(int i=0; i<badRec.size(); i++){
			bad += badRec.get(i) + " \n";
		}
		
		for(int i=0; i<goodRec.size(); i++){
			good += goodRec.get(i) + " \n";
		}
		
		String[] finalAdviceWithoutPCart = new String[]{bad, good};
		return finalAdviceWithoutPCart;
	}
	
	
	public String[] getFinalAdviceWithPrevious(){
		String bad = null;
		String good = null;
		
		for(int i=0; i<badPrev.size(); i++){
			bad += badPrev.get(i) + " \n";
		}
		
		for(int i=0; i<goodPrev.size(); i++){
			good += goodPrev.get(i) + " \n";
		}
		
		String[] finalAdviceWithPCart = new String[]{bad, good};
		return finalAdviceWithPCart;
	}

	
	public void giveAdvice(Context context){
		
		String posA = getPositiveAdviceAboutPrevious();
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
