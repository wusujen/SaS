package com.christine.cart.sqlite;

import com.christine.cart.SetupPeopleActivity;

public class Person {
	Integer _id;
	String _username;
	String _name;
	int _age;
	String _gender;
	int _height;
	int _weight;

	// blank constructor
	public Person(){
		
	}
	
	// general default constructor: user added new person
	public static Person createPerson(int requestCode, String username){
		if(requestCode==SetupPeopleActivity.MAN){
			return new Person(username, 30, "male", 70, 150);
		}
		return null;
	}
	
	// constructor with default NAME
	// for default implementations of people
	public Person(String username, int age, String gender, int height, int weight){
		this(null, username, "default", age, gender, height, weight);
	}
	
	// constructor with id: generally use this when reading from DB
	public Person(Integer id, String username, String name, int age, String gender, int height, int weight){
		_id = id;
		_username = username;
		_name = name;
		_age = age;
		_gender = gender;
		_height = height;
		_weight = weight;
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
}