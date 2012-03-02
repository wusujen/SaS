package com.christine.cart.sqlite;

public class Person {
	int _id;
	String _name;
	int _age;
	String _gender;
	int _height;
	int _weight;

	// blank constructor
	public Person(){
		
	}
	
	// general default constructor
	public Person(String def){
		if(def.equals("default")){
			_name = "default";
			_age = 30;
			_gender = "male";
			_height = 70;
			_weight = 150;
		}
	}
	
	// constructor with default NAME
	// for default implementations of people
	public Person(int age, String gender, int height, int weight){
		_name = "default";
		_age = age;
		_gender = gender;
		_height = height;
		_weight = weight;
	}
	
	// constructor with id
	public Person(int id, String name, int age, String gender, int height, int weight){
		_id = id;
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
