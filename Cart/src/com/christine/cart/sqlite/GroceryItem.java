package com.christine.cart.sqlite;

public class GroceryItem {
	Integer _id;
	String _userName;
	String _itemName;
	String _servingWeight;
	String _serving;
	Float _servingRatio;
	
	//constructor
	public GroceryItem(){
		
	}
	
	public GroceryItem(int id, String userName, String itemName, String serving, String servingWeight, Float servingRatio){
		_id = id;
		_userName = userName;
		_itemName = itemName;
		_servingWeight = servingWeight;
		_serving = serving;
		_servingRatio = servingRatio;
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
	public String getUserName(){
		return this._userName;
	}
	
	// set username
	public void setUserName(String username){
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
	public String getServingWeight(){
		return this._servingWeight;
	}
	
	// set serving
	public void setServingWeight(String servingWeight){
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
}