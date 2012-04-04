package com.christine.cart.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class GroceryItem implements Parcelable{
	Integer _id;
	String _userName;
	String _itemName;
	String _serving;
	Float _servingWeight;
	Float _servingRatio;
	Integer _quantity;
	
	//constructor
	public GroceryItem(){
		
	}
	
	public GroceryItem(int id, String userName, String itemName, String serving, Float servingWeight, Float servingRatio, Integer quantity){
		_id = id;
		_userName = userName;
		_itemName = itemName;
		_serving = serving;
		_servingWeight = servingWeight;
		_servingRatio = servingRatio;
		_quantity = quantity;
	}
	
	// get Id
	public float getId(){
		return this._id;
	}
	
	// set Id
	public void setId(int id){
		this._id = id;
	}
	
	// get username
	public String getUsername(){
		return this._userName;
	}
	
	// set username
	public void setUsername(String username){
		this._userName = username;
	}

	// get name
	public String getItemName(){
		return this._itemName;
	}
	
	// set name
	public void setItemName(String itemname){
		this._itemName = itemname;
	}
	
	// get serving weight
	public Float getServingWeight(){
		return this._servingWeight;
	}
	
	// set serving
	public void setServingWeight(Float servingWeight){
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
	
	// get servingRatio
	public float getServingRatio(){
		return this._servingRatio;
	}
	
	// set servingRatio
	public void setServingRatio(float servingRatio){
		this._servingRatio = servingRatio;
	}
	
	// get Quantity
	public Integer getQuantity(){
		return this._quantity;
	}
	
	// set quantity
	public void setQuantity(int quantity){
		this._quantity = quantity;
	}
	
	/**
	 * 
	 * AS A PARCELLABLE ITEM
	 * @see http://techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html
	 * @see http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/
	 *
	 *
	 */
	
	public GroceryItem(Parcel in){
		readFromParcel(in);
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		Log.v("Writing to Parcel", "writeToParcel..."+flags);
		dest.writeInt(_id);
		dest.writeString(_userName);
		dest.writeString(_itemName);
		dest.writeString(_itemName);
		dest.writeFloat(_servingWeight);
		dest.writeFloat(_servingRatio);
		dest.writeInt(_quantity);
	}
	
	private void readFromParcel(Parcel in) {
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		_id = in.readInt();
		_userName =  in.readString();
		_itemName =  in.readString();
		_serving =  in.readString();
		_servingWeight = in.readFloat();
		_servingRatio = in.readFloat();
		_quantity = in.readInt();
	}
	
	 public static final Parcelable.Creator<GroceryItem> CREATOR =
    	new Parcelable.Creator<GroceryItem>() {
            public GroceryItem createFromParcel(Parcel in) {
                return new GroceryItem(in);
            }
 
            public GroceryItem[] newArray(int size) {
                return new GroceryItem[size];
            }
        };
}
