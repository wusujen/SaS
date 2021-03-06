package com.christine.cart.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.christine.cart.sqlite.PersonActivity;

public class Person implements Parcelable{
	Integer _id;
	String _username;
	String _name;
	int _age;
	String _gender; 	//m or f
	int _height;	 	//in inches
	int _weight; 		//in pounds
	int _activity;		//0=>sedentary, 1=>moderately active, 2=>very active
	boolean _main; 		

	// blank constructor
	public Person(){
		
	}
	
	// general default constructor: user added new person
	public static Person createPerson(int requestCode, String username){
		switch(requestCode){
			case PersonActivity.MAN:
				return new Person(username, 47, "m", 69, 194, 0);
			case PersonActivity.WOMAN:
				return new Person(username, 50, "f", 64, 164, 0);
			case PersonActivity.BOY:
				return new Person(username, 9, "m", 55, 53, 0);
			case PersonActivity.GIRL:
				return new Person(username, 9, "f", 54, 53, 0);
			default:
				return null;
		}
	}
	
	// constructor with default NAME
	// for default implementations of people
	public Person(String username, int age, String gender, int height, int weight, int activity){
		this(null, username, "default", age, gender, height, weight, activity);
	}
	
	// constructor with id: generally use this when reading from DB
	public Person(Integer id, String username, String name, int age, String gender, int height, int weight, int activity){
		_id = id;
		_username = username;
		_name = name;
		_age = age;
		_gender = gender;
		_height = height;
		_weight = weight;
		_activity = activity;
		_main = false;
	}
	
	// get Id
	public int getId(){
		return this._id;
	}
	
	// set Id
	public void setId(int id){
		this._id = id;
	}
	
	// get username
	public String getUsername(){
		return this._username;
	}
	
	// set username
	public void setUsername(String username){
		this._username = username;
	}

	// get name
	public String getName(){
		return this._name;
	}
	
	// set name
	public void setName(String name){
		this._name = name;
	}
	
	// get age
	public int getAge(){
		return this._age;
	}
	
	//set age
	public void setAge(int age){
		this._age = age;
	}
	
	// get gender
	public String getGender(){
		return this._gender;
	}
	
	//set gender
	public void setGender(String gender){
		this._gender = gender;
	}
	
	// get height
	public int getHeight(){
		return this._height;
	}
	
	//set height
	public void setHeight(int height){
		this._height = height;
	}
	
	// get weight
	public int getWeight(){
		return this._weight;
	}
	
	//set weight
	public void setWeight(int weight){
		this._weight = weight;
	}
	
	// get activity
	public int getActivity(){
		return this._activity;
	}
	
	//set activity
	public void setActivity(int activity){
		this._activity = activity;
	}
	
	public int getInchHeight(){
		return _height%12;
	}
	
	public int getFeetHeight(){
		int i = this.getInchHeight();
		return (_height-i)/12;
	}
	
	public boolean getMain(){
		return this._main;
	}
	
	public void setMain(boolean bool){
		this._main = bool;
	}
	
	public String getActivityString(){
		switch(_activity){
		case 1:
			return "Lightly Active";
		case 2:
			return "Moderately Active";
		case 3:
			return "Very Active";
		default:
			return "Sedentary";
		}
	}
	
	public String returnString(){
		String v = "Name: " + _name + "\n" +
					"Age: " + _age  + " years \n" +
					"Gender: " + _gender  + "\n" +
					"Height: " + this.getFeetHeight() + "ft " 
					+ this.getInchHeight() + " in" + "\n" +
					"Weight: " + _weight + " pounds \n" +
					"Activity: " + this.getActivityString() + "\n";
		return v;
	}
	
	/**
	 * 
	 * AS A PARCELLABLE ITEM
	 * @see http://techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html
	 * @see http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/
	 *
	 *
	 */
	
	public Person(Parcel in){
		readFromParcel(in);
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		Log.v("Writing to Parcel", "writeToParcel..."+flags);
		dest.writeInt(_id);
		dest.writeString(_username);
		dest.writeString(_name);
		dest.writeInt(_age);
		dest.writeString(_gender);
		dest.writeInt(_height);
		dest.writeInt(_weight);
		dest.writeInt(_activity);
		dest.writeString(String.valueOf(_main));
	}
	
	private void readFromParcel(Parcel in) {
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		
		_id = in.readInt();
		_username = in.readString();
		_name = in.readString();
		_age = in.readInt();
		_gender = in.readString();
		_height = in.readInt();
		_weight = in.readInt();
		_activity = in.readInt();
		_main = Boolean.valueOf(in.readString());

	}
	
	 public static final Parcelable.Creator<Person> CREATOR =
    	new Parcelable.Creator<Person>() {
            public Person createFromParcel(Parcel in) {
                return new Person(in);
            }
 
            public Person[] newArray(int size) {
                return new Person[size];
            }
        };
	
}
