package com.christine.cart.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class PreviousHistory implements Parcelable{
	Integer _id;
	String _username;
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
	Integer _days;
	
	public PreviousHistory(){
		
	}
	
	public PreviousHistory(Integer id, String username, float calories, float protein, 
			float totalfats, float carbs, float fiber, float sugar, float calcium, float iron, float magnesium, float potassium, float sodium,
			float zinc, float vitC, float vitB6, float vitB12, float vitA, float vitD, float vitE, float vitK, float fatSat,
			float fatMono, float fatPoly, float cholesterol, Integer days){
		_id = id;
		_username = username;
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
		_days = days;
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
	public String getUsername(){
		return this._username;
	}
	
	// set name
	public void setUsername(String username){
		this._username = username;
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

	// get days
	public float getDays(){
		return this._days;
	}
	
	// set days
	public void setDays(int days){
		this._days = days;
	}
	
	public Float[] getNutritionProperties(){
		Float[] props = new Float[] {
				this._calories, this._protein, this._totalfats, this._carbs, this._fiber, this._sugar, this._calcium, 
				this._iron, this._magnesium, this._potassium, this._sodium, this._zinc, this._vitC, this._vitD, 
				this._vitB6, this._vitB12, this._vitA, this._vitE, this._vitK, this._fatSat, this._fatMono, this._fatPoly,
				this._cholesterol };
		return props;
	}
	
	public String toString(){
		String pString = 
				"User " + this._username + " || Calories " + this._calories + " || Protein " + this._protein + 
				" || Total Fats " + this._totalfats + " || Carbs " +  this._carbs + " || Fiber " + this._fiber + 
				" || Sugar " + this._sugar + " || Calcium " + this._calcium + " || Iron " + this._iron + 
				" || Magnesium " + this._magnesium + " || Potassium " + this._potassium + " || Sodium " + this._sodium +
				" || Zinc" + this._zinc + " || Vit C " + this._vitC + " || Vit D " + this._vitD + " || Vit B6 " + this._vitB6 +
				" || Vit B12 " + this._vitB12 + " || Vit A " + this._vitA + " || Vit E " + this._vitE + " || Vit K " + this._vitK + 
				" || Saturated Fat " + this._fatSat + " || Mono Fat " + this._fatMono + 
				" || Poly Fat " + this._fatPoly + " || Cholesterol " + this._cholesterol;
		return pString;
	}
	
	public Float[] getNutritionPropertiesNoMono(){
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
	
	public PreviousHistory(Parcel in){
		readFromParcel(in);
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		Log.v("Writing to Parcel", "writeToParcel..."+flags);
		dest.writeInt(_id);
		dest.writeString(_username);
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
		dest.writeFloat(_fatMono);
		dest.writeFloat(_fatPoly);
		dest.writeFloat(_cholesterol);
		dest.writeInt(_days);
	}
	
	private void readFromParcel(Parcel in) {
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		_id = in.readInt();
		_username = in.readString();
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
		_fatMono = in.readFloat();
		_fatPoly = in.readFloat();
		_cholesterol = in.readFloat();
		_days = in.readInt();

	}
	
	 public static final Parcelable.Creator<PreviousHistory> CREATOR =
    	new Parcelable.Creator<PreviousHistory>() {
            public PreviousHistory createFromParcel(Parcel in) {
                return new PreviousHistory(in);
            }
 
            public PreviousHistory[] newArray(int size) {
                return new PreviousHistory[size];
            }
        };
}
