package com.christine.cart.sqlite;

public class Account {
	
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
	
}
