package com.christine.cart.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Account implements Parcelable{
	
	int _id;
	String _name;
	String _pwd;
	Integer _days;

	// blank constructor
	public Account(){
		
	}
	
	// constructor
	public Account(String username, String password){
		_name = username;
		_pwd = password;
	}
	
	public Account(int id, String username, String password){
		_id = id;
		_name = username;
		_pwd = password;
		_days = null;
	}
	
	// constructor
	public Account(int id, String username, String password, int days){
		_id = id;
		_name = username;
		_pwd = password;
		_days = days;
	}
	
	public int getDays(){
		return this._days;
	}
	
	public void setDays(int days){
		this._days = days;
	}
	
	// get Id
	public int getId(){
		return this._id;
	}
	
	// set Id
	public void setId(int id){
		this._id = id;
	}
	
	// get name
	public String getName(){
		return this._name;
	}
	
	// set name
	public void setName(String name){
		this._name = name;
	}
	
	// get password
	public String getPassword(){
		return this._pwd;
	}
	
	//set password
	public void setPassword(String pwd){
		this._pwd = pwd;
	}
	
	/**
	 * 
	 * AS A PARCELLABLE ITEM
	 * @see http://techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html
	 * @see http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/
	 *
	 *
	 */
	
	public Account(Parcel in){
		readFromParcel(in);
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		Log.v("Writing to Parcel", "writeToParcel..."+flags);
		dest.writeInt(_id);
		dest.writeString(_name);
		dest.writeString(_pwd);
		dest.writeInt(_days);
	}
	
	private void readFromParcel(Parcel in) {
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		_id = in.readInt();
		_name = in.readString();
		_pwd = in.readString();
		_days = in.readInt();

	}
	
	 public static final Parcelable.Creator<Account> CREATOR =
    	new Parcelable.Creator<Account>() {
            public Account createFromParcel(Parcel in) {
                return new Account(in);
            }
 
            public Account[] newArray(int size) {
                return new Account[size];
            }
        };
	
}
