package com.christine.cart.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * RecDailyValues
 * 
 * Calculates Recommended Dietary Intake (RDI) and expected
 * caloric intake of that individual. This sets the standard
 * 100% values of the graph.
 * 
 * Calcuations done with BMI equations and RDI tables from
 * @see http://www.nap.edu/catalog.php?record_id=11537
 * 
 * Fat MONO value is not included.
 * 
 * @author Christine
 *
 */
public class RecDailyValues implements Parcelable{
	float _calories;	//kcal
	float _protein;		//gram(g), should be a % of calories
	float _totalfats;	//varies based on age is a % of calories, base 65
	float _carbs;		//gram(g), should be a % of calories
	float _fiber;		//gram(g)
	float _sugar;		//should be no more than 25% of calories
	float _calcium;		//milligram(mg)
	float _iron;		//milligram(mg)
	float _magnesium;	//milligram(mg)
	float _potassium;	//grams(g)
	float _sodium;		//grams(g)
	float _zinc;		//milligram(mg)
	float _vitC;		//milligram(mg)
	float _vitD;		//microgram(ug)
	float _vitB6;		//milligram(mg)
	float _vitB12;		//microgram(ug)
	float _vitA;		//microgram(ug)
	float _vitE;		//milligram(mg)
	float _vitK;		//microgram(ug)
	float _fatSat;		//20 grams (g) as low as possible!
	float _fatPoly;		//5-10% of energy
	float _cholesterol;	//300 milligrams(mg), or as low as possible
	
	float PA;
	float eer;
	
	public RecDailyValues(){
	
	}
	
	//make RDI's from People
	public RecDailyValues (Person person){
		String gender = person.getGender();
		int activity = person.getActivity();
		int age = person.getAge();
		int lb = person.getWeight();
		int in = person.getHeight();
		
		//convert to metrics
		float weight = lb*0.45359237f;	//lb to kg
		float height = in*0.0254f; 		//in to m
		
		//set saturated fat for all cases
		_fatSat = 20; // in grams
		
		//check for the kids below 8
		if(age >=1 && age<=3){
			float eer = (89 * weight-100)+ 175;
			switch(activity){
				case 1: 
					_calories = eer*1.13f;
					break;
				case 2:
					_calories = eer*1.25f;
					break;
				case 3:
					_calories = eer*1.42f;
					break;
				default: //in case 0
					_calories = eer;
			}
			_totalfats = (_calories*0.35f)/9;
			_fatPoly = (_calories*0.8f)/9;
			_protein = (_calories*0.15f)/4; 				
			_carbs = (_calories*0.50f)/4;
			_fiber = 19;
			_sugar = (_calories*0.25f)/4;
			_calcium = 500;
			_iron = 3.0f;
			_magnesium = 65;
			_potassium = 3.0f;
			_sodium = 1.0f;
			_zinc = 2.5f;
			_vitC = 13;					
			_vitB6 = 0.4f;			
			_vitB12 = 0.7f;					
			_vitA = 210;
			_vitD = 5;
			_vitE = 5;				
			_vitK = 30;
			_cholesterol = 300;
			return;
		} else if(age>=4 && age<=8){
			float eer = (89 * weight-100)+ 175;
			switch(activity){
				case 1: 
					_calories = eer*1.13f;
					break;
				case 2:
					_calories = eer*1.26f;
					break;
				case 3:
					_calories = eer*1.42f;
					break;
				default: //in case 0
					_calories = eer;
			}
			_totalfats = (_calories*0.25f)/9;
			_fatPoly = (_calories*0.8f)/9;
			_protein = (_calories*0.15f)/4; 			
			_carbs = (_calories*0.50f)/4;
			_fiber = 25;
			_sugar = (_calories*0.25f)/4;
			_calcium = 800;
			_iron = 4.1f;
			_magnesium = 110;
			_potassium = 3.8f;
			_sodium = 1.2f;
			_zinc = 4.0f;
			_vitC = 22;					
			_vitB6 = 0.5f;				
			_vitB12 = 1.0f;			
			_vitA = 275;
			_vitD = 5;
			_vitE = 6;						
			_vitK = 55;
			_cholesterol = 300;
			return;
		} 
		
		// otherwise, check gender first
		if(gender.equals("m")){
			if(age>=9 && age<=13){
				switch(activity){
					case 1: 
						PA = 1.13f;
						break;
					case 2:
						PA = 1.25f;
						break;
					case 3:
						PA = 1.42f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories =  (float) (88.5-(61.9*age) + PA*((26.7*weight)+ (903*height)))+20;
				_totalfats = (_calories*0.35f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.3f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 31;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1300;
				_iron = 5.9f;
				_magnesium = 200;
				_potassium = 4.5f;
				_sodium = 1.5f;
				_zinc = 7.0f;
				_vitC = 39;			
				_vitB6 = 0.8f;			
				_vitB12 = 1.5f;		
				_vitA = 445;
				_vitD = 5;
				_vitE = 9;					
				_vitK = 60;
				_cholesterol = 300;
			} else if(age>=14 && age<=18){
				switch(activity){
					case 1: 
						PA = 1.13f;
						break;
					case 2:
						PA = 1.25f;
						break;
					case 3:
						PA = 1.42f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (88.5-(61.9*age) + PA*((26.7*weight)+ (903*height)))+25;
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.3f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 38;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1300;
				_iron = 7.7f;
				_magnesium = 340;
				_potassium = 4.7f;
				_sodium = 1.5f;
				_zinc = 8.5f;
				_vitC = 63;		
				_vitB6 = 1.1f;			
				_vitB12 = 2.0f;			
				_vitA = 630;
				_vitD = 5;
				_vitE = 12;					
				_vitK = 75;
				_cholesterol = 300;
			} else if(age>=19 && age<=50){
				switch(activity){
					case 1: 
						PA = 1.11f;
						break;
					case 2:
						PA = 1.25f;
						break;
					case 3:
						PA = 1.48f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (662-(9.53*age) + PA*((15.91*weight) + (539.6*height)));
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.30f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 38;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1000;
				_iron = 6.0f;
				_magnesium = 330;
				_potassium = 4.7f;
				_sodium = 1.5f;
				_zinc = 9.4f;
				_vitC = 75;		
				_vitB6 = 1.1f;			
				_vitB12 = 2.0f;			
				_vitA = 625;
				_vitD = 5;
				_vitE = 12;				
				_vitK = 120;
				_cholesterol = 300;
			} else{
				switch(activity){
					case 1: 
						PA = 1.11f;
						break;
					case 2:
						PA = 1.25f;
						break;
					case 3:
						PA = 1.48f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (662-(9.53*age) + PA*((15.91*weight) + (539.6*height)));
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.35f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 30;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1200;
				_iron = 6.0f;
				_magnesium = 350;
				_potassium = 4.7f;
				_sodium = 1.2f;
				_zinc = 9.4f;
				_vitC = 75;		
				_vitB6 = 1.4f;			
				_vitB12 = 2.4f;			
				_vitA = 625;
				_vitD = 10;
				_vitE = 12;				
				_vitK = 120;
				_cholesterol = 300;
			}
		} else if(gender.equals("f")){
			//for females
			if(age>=9 && age<=13){
				switch(activity){
					case 1: 
						PA = 1.16f;
						break;
					case 2:
						PA = 1.31f;
						break;
					case 3:
						PA = 1.56f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (135.3f-(30.8f*age)+PA*((10.0*weight)+(934*height)) + 20);
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.3f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 26;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1300;
				_iron = 5.7f;
				_magnesium = 200;
				_potassium = 4.5f;
				_sodium = 1.5f;
				_zinc = 7.0f;
				_vitC = 39;				
				_vitB6 = 0.8f;			
				_vitB12 = 1.5f;		
				_vitA = 420;
				_vitD = 5;
				_vitE = 9;				
				_vitK = 60;
				_cholesterol = 300;
			} else if(age>=14 && age<=18){
				switch(activity){
					case 1: 
						PA = 1.16f;
						break;
					case 2:
						PA = 1.31f;
						break;
					case 3:
						PA = 1.56f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (135.3f-(30.8f*age)+PA*((10.0*weight)+(934*height)) + 25);
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.3f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 26;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1300;
				_iron = 7.7f;
				_magnesium = 340;
				_potassium = 4.7f;
				_sodium = 1.5f;
				_zinc = 8.5f;
				_vitC = 56;				
				_vitB6 = 1.0f;			
				_vitB12 = 2.0f;		
				_vitA = 485;
				_vitD = 5;
				_vitE = 12;				
				_vitK = 75;
				_cholesterol = 300;
			} else if(age>=19 && age<=50){
				switch(activity){
					case 1: 
						PA = 1.12f;
						break;
					case 2:
						PA = 1.27f;
						break;
					case 3:
						PA = 1.45f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (356-(6.91 * age) + PA*((9.36 * weight) + (726 * height)));
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.3f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 25;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1000;
				_iron = 8.1f;
				_magnesium = 255;
				_potassium = 4.7f;
				_sodium = 1.5f;
				_zinc = 6.8f;
				_vitC = 60;				
				_vitB6 = 1.1f;			
				_vitB12 = 2.0f;		
				_vitA = 500;
				_vitD = 5;
				_vitE = 12;				
				_vitK = 90;
				_cholesterol = 300;
			} else{
				switch(activity){
					case 1: 
						PA = 1.12f;
						break;
					case 2:
						PA = 1.27f;
						break;
					case 3:
						PA = 1.45f;
						break;
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (356-(6.91 * age) + PA*((9.36 * weight) + (726 * height)));
				_totalfats = (_calories*0.25f)/9;
				_fatPoly = (_calories*0.8f)/9;
				_protein = (_calories*0.35f)/4; 			
				_carbs = (_calories*0.65f)/4;
				_fiber = 21;
				_sugar =(_calories*0.25f)/4;
				_calcium = 1200;
				_iron = 8.1f;
				_magnesium = 265;
				_potassium = 4.7f;
				_sodium = 1.2f;
				_zinc = 6.8f;
				_vitC = 60;				
				_vitB6 = 1.3f;			
				_vitB12 = 2.0f;		
				_vitA = 500;
				_vitD = 10;
				_vitE = 12;				
				_vitK = 90;
				_cholesterol = 300;
			}
		} else {
			throw new RuntimeException("unexpected gender: " + gender);
		}
	}
	
	// get calories
		public float getCalories(){
			return this._calories;
		}
		
		// set calories
		public void setCalories(float calories){
			this._calories = calories;
		}
		
		// get protein
		public float getProtein(){
			return this._protein;
		}
		
		// set protein
		public void setProtein(float protein){
			this._protein = protein;
		}
		
		// get fat
		public float getFat(){
			return this._totalfats;
		}
		
		// set fat
		public void setFat(float totalfat){
			this._totalfats = totalfat;
		}
		
		// get carbohydrate
		public float getCarbohydrate(){
			return this._carbs;
		}
		
		// set carbohydrate
		public void setCarbohydrate(float carbohydrate){
			this._carbs = carbohydrate;
		}
		
		// get fiber
		public float getFiber(){
			return this._fiber;
		}
		
		// set fiber
		public void setFiber(float fiber){
			this._fiber = fiber;
		}
		
		// get sugar
		public float getSugar(){
			return this._sugar;
		}
		
		// set sugar
		public void setSugar(float sugar){
			this._sugar = sugar;
		}
		
		// get calcium
		public float getCalcium(){
			return this._calcium;
		}
		
		// set calcium
		public void setCalcium(float calcium){
			this._calcium = calcium;
		}
		
		// get iron
		public float getIron(){
			return this._iron;
		}
		
		// set iron
		public void setIron(float iron){
			this._iron = iron;
		}
		
		// get magnesium
		public float getMagnesium(){
			return this._magnesium;
		}
		
		// set magnesium
		public void setMagnesium(float magnesium){
			this._magnesium = magnesium;
		}
		
		// get potassium
		public float getPotassium(){
			return this._potassium;
		}
		
		// set potassium
		public void setPotassium(float potassium){
			this._potassium = potassium;
		}
		
		// get sodium
		public float getSodium(){
			return this._sodium;
		}
		
		// set sodium
		public void setSodium(float sodium){
			this._sodium = sodium;
		}
		
		// get zinc
		public float getZinc(){
			return this._zinc;
		}
			
		// set zinc
		public void setZinc(float zinc){
			this._zinc = zinc;
		}
		
		// get vitC
		public float getVitC(){
			return this._vitC;
		}
		
		// set vitC
		public void setVitC(float vitC){
			this._vitC = vitC;
		}
		
		// get vitB6
		public float getVitB6(){
			return this._vitB6;
		}
		
		// set vitB6
		public void setVitB6(float vitB6){
			this._vitB6 = vitB6;
		}
		
		// get vitB12
		public float getVitB12(){
			return this._vitB12;
		}
		
		// set vitB12
		public void setVitB12(float vitB12){
			this._vitB12 = vitB12;
		}
		
		// get vitA
		public float getVitA(){
			return this._vitA;
		}
		
		// set vitA
		public void setVitA(float vitA){
			this._vitA = vitA;
		}
		
		// get vitE
		public float getVitE(){
			return this._vitE;
		}
		
		// set vitE
		public void setVitE(float vitE){
			this._vitE = vitE;
		}
		
		// get vitD
		public float getVitD(){
			return this._vitD;
		}
		
		// set vitD
		public void setVitD(float vitD){
			this._vitD = vitD;
		}
		
		// get vitK
		public float getVitK(){
			return this._vitK;
		}
		
		// set vitK
		public void setVitK(float vitK){
			this._vitK = vitK;
		}
		
		// get fatSat
		public float getFatSat(){
			return this._fatSat;
		}
		
		// set fatSat
		public void setFatSat(float fatSat){
			this._fatSat = fatSat;
		}
		
		// get fatPoly
		public float getFatPoly(){
			return this._fatPoly;
		}
		
		// set fatPoly
		public void setFatPoly(float fatPoly){
			this._fatPoly = fatPoly;
		}
		
		// get cholesterol
		public float getCholesterol(){
			return this._cholesterol;
		}
		
		// set cholesterol
		public void setCholesterol(float cholesterol){
			this._cholesterol = cholesterol;
		}
		
		public String returnString(){
			String v = "Calories: " + Math.round(_calories) + " kcal \n" +
						"Protein: " + Math.round(_protein) + " g \n" +
						"Total Fat: " + Math.round(_totalfats) + " g \n" +
						"Carbohydrates: " + Math.round(_carbs) + " g \n" + 
						"Fiber: " + Math.round(_fiber) + " g \n" + 
						"Sugar: " + Math.round(_sugar) + " g \n" +
						"Calcium: " + Math.round(_calcium) + " mg \n" +
						"Iron: " + Math.round(_iron) + " mg \n" +
						"Magnesium: " + Math.round(_magnesium) + " mg \n" +
						"Potassium: " + Math.round(_potassium) + " g \n" +
						"Sodium: " + Math.round(_sodium) + " g \n" +
						"Zinc: " + Math.round(_zinc) + " mg \n" +
						"Vitamin C: " + Math.round(_vitC) + " mg \n" +
						"Vitamin D: " + Math.round(_vitD) + " ug \n" +
						"Vitamin B6: " + Math.round(_vitB6) + " mg \n" +
						"Vitamin B12: " + Math.round(_vitB12) + " mg \n" +
						"Vitamin A: " + Math.round(_vitA) + " ug \n" +
						"Vitamin E: " + Math.round(_vitE) + " mg \n" +
						"Vitamin K: " + Math.round(_vitK) + " ug \n" +
						"Saturated Fats: " + Math.round(_fatSat) + " g \n" +
						"PolyUnsaturated Fats: " + Math.round(_fatPoly) + " g \n" +
						"Cholesterol: " + Math.round(_cholesterol) + " mg \n";
			return v;
		}
		
		public Float[] getNutritionNeeds(){
			Float[] props = new Float[] {
					this._calories, this._protein, this._totalfats, this._carbs, this._fiber, this._sugar, this._calcium, 
					this._iron, this._magnesium, this._potassium, this._sodium, this._zinc, this._vitC, this._vitD, 
					this._vitB6, this._vitB12, this._vitA, this._vitE, this._vitK, this._fatSat, /*MONOFAT*/ 0.0f, this._fatPoly,
					this._cholesterol };
			return props;
		}
		
		public Float[] getNutritionNeedsNoMono(){
			Float[] props = new Float[] {
					this._calories, this._protein, this._totalfats, this._carbs, this._fiber, this._sugar, this._calcium, 
					this._iron, this._magnesium, this._potassium, this._sodium, this._zinc, this._vitC, this._vitD, 
					this._vitB6, this._vitB12, this._vitA, this._vitE, this._vitK, this._fatSat,
					this._cholesterol };
			return props;
		}
	
		/**
		 * 
		 * AS A PARCELLABLE ITEM
		 * @see http://techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html
		 * @see http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/
		 *
		 *
		 */
		
		public RecDailyValues(Parcel in){
			readFromParcel(in);
		}
		
		public int describeContents() {
			return 0;
		}

		public void writeToParcel(Parcel dest, int flags) {
			Log.v("Writing to Parcel", "writeToParcel..."+flags);
			
			dest.writeFloat(_calories);
			dest.writeFloat(_protein);
			dest.writeFloat(_totalfats);
			dest.writeFloat(_carbs);
			dest.writeFloat(_fiber);
			dest.writeFloat(_sugar);
			dest.writeFloat(_calcium);
			dest.writeFloat(_iron);
			dest.writeFloat(_magnesium);
			dest.writeFloat(_potassium);
			dest.writeFloat(_sodium);
			dest.writeFloat(_zinc);
			dest.writeFloat(_vitC);
			dest.writeFloat(_vitD);
			dest.writeFloat(_vitB6);
			dest.writeFloat(_vitB12);
			dest.writeFloat(_vitA);
			dest.writeFloat(_vitE);
			dest.writeFloat(_vitK);
			dest.writeFloat(_fatSat);
			dest.writeFloat(_fatPoly);
			dest.writeFloat(_cholesterol);

		}
		
		private void readFromParcel(Parcel in) {
			// We just need to read back each
			// field in the order that it was
			// written to the parcel

			_calories = in.readFloat();
			_protein = in.readFloat();
			_totalfats = in.readFloat();
			_carbs = in.readFloat();
			_fiber = in.readFloat();
			_sugar = in.readFloat();
			_calcium = in.readFloat();
			_iron = in.readFloat();
			_magnesium = in.readFloat();
			_potassium = in.readFloat();
			_sodium = in.readFloat();
			_zinc = in.readFloat();
			_vitC = in.readFloat();
			_vitD = in.readFloat();
			_vitB6 = in.readFloat();
			_vitB12 = in.readFloat();
			_vitA = in.readFloat();
			_vitE = in.readFloat();
			_vitK = in.readFloat();
			_fatSat = in.readFloat();
			_fatPoly = in.readFloat();
			_cholesterol = in.readFloat();

		}
		
		 public static final Parcelable.Creator<RecDailyValues> CREATOR =
	    	new Parcelable.Creator<RecDailyValues>() {
	            public RecDailyValues createFromParcel(Parcel in) {
	                return new RecDailyValues(in);
	            }
	 
	            public RecDailyValues[] newArray(int size) {
	                return new RecDailyValues[size];
	            }
	        };
}
