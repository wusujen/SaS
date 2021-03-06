package com.christine.cart.visual;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.christine.cart.R;
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
	
	String[] positive;
	String[] negative;
	

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
	 * Generates a string array describing the
	 * current nutritional state of the cart;
	 * 0=> previous
	 * 1=> recommended
	 */
	public String[] getPositiveAdvice(){
		positive = new String[2];
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
						} else if(i==goodPrev.size() && prevPos!=null && prevPos.length()!=0){
							prevPos += " and " + prev;
						} else {
							prevPos += prev;
						}
						
						announcedPrev.add(prev);
						} 
					}
				
				if(prevPos!=null && prevPos.length()>0){
					positive[0] = "Compared to your previous shopping trip, you've increased the amount of " + prevPos + " in your cart.";
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
				} else if(i==goodRec.size() && recPos!=null && recPos.length()!=0){
					recPos += " and " + rec;
				} else {
					recPos += rec;
				}
				
				announcedRec.add(rec);
			}
		}
		
		
		if(recPos!=null && recPos.length()>0){
			if(prevPos!=null && prevPos.length()>0){
				positive[0] += " \n \n";
			}
			positive[1] += "You've met the nutritional requirements of " + recPos + " for " + _days + " days.";
		}
		
		return positive;
		
	}
	
	/*
	 * Generates a string array describing the
	 * current nutritional state of the cart;
	 * 0=> previous
	 * 1=> recommended
	 */
	public String[] getNegativeAdvice(){
		negative = new String[2];
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
						} else {
							prevNeg += prev;
						}
						
						announcedPrev.add(prev);
						} 
					}
				
				if(prevNeg!=null && prevNeg.length()>0){
					negative[0] = "In your previous shopping trip, your cart contained less " + prevNeg + ". Try to reduce if you can.";
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
				} else {
					recNeg += rec;
				}
				
				announcedRec.add(rec);
			}
		}
		
		
		if(recNeg!=null && recNeg.length()>0){
			if(prevNeg!=null && prevNeg.length()>0){
				negative[0] += " \n \n";
			}
			negative[1] += "You have exceeded the recommended amounts of " + recNeg + ". Try to decrease the amounts of these nutrients in your cart.";
		}
		
		return negative;
	}
	
	
	public String[] getFinalAdvice(){
		String bad = null;
		String good = null;
		
		if(badRec!=null && badRec.size()!=0){
			for(int i=0; i<badRec.size(); i++){
				bad += badRec.get(i) + " \n";
			}
		} else {
			bad = "";
		}
		
		if(goodRec!=null && goodRec.size()!=0){
			for(int i=0; i<goodRec.size(); i++){
				good += goodRec.get(i) + " \n";
			}
		} else {
			good = "";
		}
		
		String[] finalAdviceWithoutPCart = new String[]{bad.replaceAll("null", ""), good.replaceAll("null", "")};
		
		return finalAdviceWithoutPCart;
	}
	
	
	public String[] getFinalAdviceWithPrevious(){
		String bad = null;
		String good = null;
		
		if(badPrev!=null && badPrev.size()!=0){
			for(int i=0; i<badPrev.size(); i++){
				bad += badPrev.get(i) + " \n";
			}
		} else {
			bad = "";
		}
		
		if(goodPrev!=null && goodRec.size()!=0){
			for(int i=0; i<goodPrev.size(); i++){
				good += goodPrev.get(i) + " \n";
			}
		} else {
			good = "";
		}
		
		String[] finalAdviceWithPCart = new String[]{bad.replaceAll("null", ""), good.replaceAll("null", "")};
		
		return finalAdviceWithPCart;
	}

	
	public void givePositiveDialogAdvice(Context context, View layout){
		
		String[] pos = getPositiveAdvice();
		String totalPos = null;
		
		if(pos[0]!=null){
			totalPos = pos[0];
		}
		if(pos[1]!=null){
			totalPos += pos[1];
		}
		
		if(totalPos!=null && totalPos.length()!=0) {
			totalPos = totalPos.replaceAll("null", "");
			
			Dialog dialog = new Dialog(context);

			dialog.setContentView(layout);
			dialog.setTitle("GREAT JOB!");
			
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.cart_notification_complete);
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(totalPos);
			
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		}
	}
		
	public void giveNegativeDialogAdvice(Context context, View layout){
	
		String[] neg = getNegativeAdvice();
		String totalNeg = null;
		
		if(neg[0]!=null){
			totalNeg = neg[0];
		}
		if(neg[1]!=null){
			totalNeg = neg[1];
		}
		
		
		if(totalNeg!=null && totalNeg.length()!=0) {
			totalNeg = totalNeg.replaceAll("null", "");

			Dialog dialog = new Dialog(context);

			dialog.setContentView(layout);
			dialog.setTitle("IMPROVE ON");
			
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.cart_notification_decrease);
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(totalNeg);
			
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		}
	}
	
	public String giveNegStringAdvice(){
		String[] neg = getNegativeAdvice();
		String totalNeg = null;
		
		if(neg[0]!=null){
			totalNeg = neg[0];
		}
		if(neg[1]!=null){
			totalNeg = neg[1];
		}
		
		String finalNeg = null;
		if(totalNeg!=null && totalNeg.length()!=0) {
			finalNeg = totalNeg.replaceAll("null", "");
		}
		
		return finalNeg;
	}
	
	public String givePosStringAdvice(){
		String[] pos = getPositiveAdvice();
		String totalPos = null;
		
		if(pos[0]!=null){
			totalPos = pos[0];
		}
		if(pos[1]!=null){
			totalPos += pos[1];
		}
		
		String finalPos = null;
		if(totalPos!=null && totalPos.length()!=0) {
			finalPos = totalPos.replaceAll("null", "");
		}
		
		return finalPos;
	}
	
	public void clearPreviouslyShownDialogs(){
		announcedPrev = new ArrayList<String>();
		announcedRec = new ArrayList<String>();
	}
	
	
}
