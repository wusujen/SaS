package com.christine.cart.sqlite;

public class Account {
	
	int _id;
	String _name;
	String _pwd;

	// blank constructor
	public Account(){
		
	}
	
	// constructor
	public Account(String username, String password){
		_name = username;
		_pwd = password;
	}
	
	// constructor
	public Account(int id, String username, String password){
		_id = id;
		_name = username;
		_pwd = password;
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