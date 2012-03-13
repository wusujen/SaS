package com.christine.cart.sqlite;

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
public class RecDailyValues {
	float _calories;	//kcal
	float _protein;		//gram(g)
	float _totalfats;	//varies based on age is a % of calories, base 65
	float _carbs;		//gram(g)
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
		int in = person.getWeight();
		
		//convert to metrics
		float weight = lb*0.45359237f;	//lb to kg
		float height = in*0.0254f; 		//in to m
		
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
				default: //in case 0
					_calories = eer;
			}
			_totalfats = _calories*0.35f;
			_fatPoly = _calories*0.8f;
			_protein = 13; 				
			_carbs = 130;
			_fiber = 19;
			_sugar = _calories*0.25f;
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
				default: //in case 0
					_calories = eer;
			}
			_totalfats = _calories*0.25f;
			_fatPoly = _calories*0.8f;
			_protein = 19; 			
			_carbs = 130;
			_fiber = 25;
			_sugar = _calories*0.25f;
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
		} 
		
		// otherwise, check gender first
		if(gender == "m"){
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories =  (float) (88.5-(61.9*age) + PA*((26.7*weight)+ (903*height)))+20;
				_totalfats = _calories*0.30f;
				_fatPoly = _calories*0.8f;
				_protein = 34; 		
				_carbs = 130;
				_fiber = 31;
				_sugar = _calories*0.25f;
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (88.5-(61.9*age) + PA*((26.7*weight)+ (903*height)))+25;
				_totalfats = _calories*0.30f;
				_fatPoly = _calories*0.8f;
				_protein = 52; 		
				_carbs = 130;
				_fiber = 38;
				_sugar = _calories*0.25f;
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (662-(9.53*age) + PA*((15.91*weight) + (539.6*height)));
				_totalfats = _calories*0.30f;
				_fatPoly = _calories*0.8f;
				_protein = 56; 			
				_carbs = 130;
				_fiber = 38;
				_sugar = _calories*0.25f;
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (662-(9.53*age) + PA*((15.91*weight) + (539.6*height)));
				_totalfats = _calories*0.30f;
				_fatPoly = _calories*0.8f;
				_protein = 56; 			
				_carbs = 130;
				_fiber = 30;
				_sugar = _calories*0.25f;
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
		} else{
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (135.3f-(30.8f*age)+PA*((10.0*weight)+(934*height)) + 20);
				_totalfats = _calories*0.25f;
				_fatPoly = _calories*0.8f;
				_protein = 34; 	
				_carbs = 130;
				_fiber = 26;
				_sugar = _calories*0.25f;
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (135.3f-(30.8f*age)+PA*((10.0*weight)+(934*height)) + 25);
				_totalfats = _calories*0.25f;
				_fatPoly = _calories*0.8f;
				_protein = 46; 	
				_carbs = 130;
				_fiber = 26;
				_sugar = _calories*0.25f;
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (356-(6.91 * age) + PA*((9.36 * weight) + (726 * height)));
				_totalfats = _calories*0.30f;
				_fatPoly = _calories*0.8f;
				_protein = 46; 	
				_carbs = 130;
				_fiber = 25;
				_sugar = _calories*0.25f;
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
					default: //in case 0
						PA = 1.0f;
				}
				_calories = (float) (356-(6.91 * age) + PA*((9.36 * weight) + (726 * height)));
				_totalfats = _calories*0.25f;
				_fatPoly = _calories*0.8f;
				_protein = 46; 	
				_carbs = 130;
				_fiber = 21;
				_sugar = _calories*0.25f;
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
	
}