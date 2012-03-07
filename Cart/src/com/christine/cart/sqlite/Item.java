package com.christine.cart.sqlite;

public class Item {
	Integer _id;
	String _itemname;
	float _calories;
	float _protein;
	float _totalfats;
	float _carbs;
	float _fiber;
	float _sugar;
	float _calcium;
	float _iron;
	float _magnesium;
	float _potassium;
	float _sodium;
	float _zinc;
	float _vitC;
	float _vitD;
	float _vitB6;
	float _vitB12;
	float _vitA;
	float _vitE;
	float _vitK;
	float _fatSat;
	float _fatMono;
	float _fatPoly;
	float _cholesterol;
	float _servingWeight;
	String _serving;
	
	public Item(){
		
	}
	
	public Item(String itemname, float calories, float protein, 
			float totalfats, float carbs, float fiber, float sugar, float calcium, float iron, float magnesium, float potassium, float sodium,
			float zinc, float vitC, float vitB6, float vitB12, float vitA, float vitD, float vitE, float vitK, float fatSat,
			float fatMono, float fatPoly, float cholesterol, float servingWeight, String serving){
		this(null, itemname, calories, protein, totalfats, carbs, fiber, sugar, calcium, iron, magnesium, potassium, sodium, zinc, vitC, vitB6, vitB12, vitA, vitD, vitE, 
					vitK, fatSat, fatMono, fatPoly, cholesterol, servingWeight, serving);
	}
	public Item(Integer id, String itemname, float calories, float protein, 
						float totalfats, float carbs, float fiber, float sugar, float calcium, float iron, float magnesium, float potassium, float sodium,
						float zinc, float vitC, float vitB6, float vitB12, float vitA, float vitD, float vitE, float vitK, float fatSat,
						float fatMono, float fatPoly, float cholesterol, float servingWeight, String serving){
		_id = id;
		_itemname = itemname;
		_calories = calories;
		_protein = protein;
		_totalfats = totalfats;
		_carbs = carbs;
		_fiber = fiber;
		_sugar = sugar;
		_calcium = calcium;
		_iron = iron;
		_magnesium = magnesium;
		_potassium = potassium;
		_sodium = sodium;
		_zinc = zinc;
		_vitC = vitC;
		_vitB6 = vitB6;
		_vitB12 = vitB12;
		_vitA = vitA;
		_vitD = vitD;
		_vitE = vitE;
		_vitK = vitK;
		_fatSat = fatSat;
		_fatMono = fatMono;
		_fatPoly = fatPoly;
		_cholesterol = cholesterol;
		_servingWeight = servingWeight;
		_serving = serving;
	}
	
	// get Id
	public float getId(){
		return this._id;
	}
	
	// set Id
	public void setId(Integer id){
		this._id = id;
	}

	// get name
	public String getItemName(){
		return this._itemname;
	}
	
	// set name
	public void setItemName(String itemname){
		this._itemname = itemname;
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
	
	// get fatMono
	public float getFatMono(){
		return this._fatMono;
	}
	
	// set fatMono
	public void setFatMono(float fatMono){
		this._fatMono = fatMono;
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
	
	// get servingWeight
	public float getServingWeight(){
		return this._servingWeight;
	}
	
	// set servingWeight
	public void setServingWeight(float servingWeight){
		this._servingWeight = servingWeight;
	}
	
	// get serving
	public String getServing(){
		return this._serving;
	}
	
	// set serving
	public void setServing(String serving){
		this._serving = serving;
	}
	

}
