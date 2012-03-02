package com.christine.cart.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * Handles all interactions with Table account_info in Database accounts_db
 * 
 * @see sqlite database tutorial by Ravi Tamada:
 * 		http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 *
 */
public class AccountDatabaseHelper extends DatabaseHelper{
	
	// All static variables
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "accounts_db";
	
	// Accounts Table info
	private static final String TABLE_ACCOUNTS = "account_info";
		//column names
		private static final String ACCOUNT_ID = "_id";
		private static final String ACCOUNT_NAME = "username";
		private static final String ACCOUNT_PASSWORD = "password";
	
	//People Table info
	private static final String TABLE_P = "_people";
	private static String TABLE_PEOPLE = null;
		//column names
		private static final String PEOPLE_ID = "_id";
		private static final String PEOPLE_NAME = "username";
		private static final String PEOPLE_AGE = "age";
		private static final String PEOPLE_GENDER = "gender";
		private static final String PEOPLE_HEIGHT = "height";
		private static final String PEOPLE_WEIGHT = "weight";
	
	// Private variables
	private SQLiteDatabase myDataBase; 
	private Context myContext;
	
	
	// When an AccountDatabaseHelper is created, use
	// the generic DatabaseHelper class
	public AccountDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
		this.myContext = context;
	}
	
	// Create the table
	public void onCreate(SQLiteDatabase db){
		String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PEOPLE + "("
                + PEOPLE_ID + " INTEGER PRIMARY KEY," + PEOPLE_NAME + " TEXT,"
                + PEOPLE_GENDER + " TEXT," + PEOPLE_AGE + " NUMERIC," 
                + PEOPLE_HEIGHT + " NUMERIC," + PEOPLE_WEIGHT + " NUMERIC" 
                + ")";
        db.execSQL(CREATE_PEOPLE_TABLE);
	}
			
	/**
	 * 
	 * ACCOUNT TABLE
	 * all account CRUD operations
	 * 
	 */
	
	// Adding new account
	public void addAccount(Account account){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_NAME, account.getName()); // username
		values.put(ACCOUNT_PASSWORD, account.getPassword()); // password
		
		// insert a new row
		db.insert(TABLE_ACCOUNTS, null, values);
		db.close(); //close database connection
	}
	
	
	// Getting single Account via username
	public Account getAccount(String username){
		SQLiteDatabase db = this.getReadableDatabase();
		Account account = new Account();
		Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { ACCOUNT_ID, ACCOUNT_NAME, ACCOUNT_PASSWORD}, ACCOUNT_NAME + "=?", 
				new String []{ String.valueOf(username) }, null, null, null);
		if(cursor.moveToFirst()){
			account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		} else {
			account.setId(0);
			account.setName(null);
			account.setPassword(null);
		}
		
		//return account
		cursor.close();
		db.close();
		return account;
	}
	
	// Get All Accounts
	public List<Account> getAllAccounts(){
		List<Account> accountList = new ArrayList<Account>();
		
		//Select all query
		String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Account account = new Account();
				account.setId(Integer.parseInt(cursor.getString(0)));
				account.setName(cursor.getString(1));
				account.setPassword(cursor.getString(2));
				
				// Adding account to list
				accountList.add(account);
			} while (cursor.moveToNext());
		}
		
		//return account list
		cursor.close();
		db.close();
		return accountList;
	}
	
	// Get a count of all Accounts
	public int getAccountCount(){
		String countQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}
	
	// Update a single Account
	public int updateAccount(Account account){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_NAME, account.getName());
		values.put(ACCOUNT_PASSWORD, account.getPassword());
		
		//update row
		return db.update(TABLE_ACCOUNTS, values, ACCOUNT_ID + " = ?", new String[] { String.valueOf(account.getId())});
		
	}
	
	//Delete single Account
	public void deleteAccount(Account account){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ACCOUNTS, ACCOUNT_ID + " = ?", new String[] { String.valueOf(account.getId())});
		db.close();
	}
	
	/**
	 * PEOPLE TABLE
	 * All CRUD operations, including creation of table
	 */
	
	//Create a table based upon the input username, so that it is user-specific
	public void createPeopleTable(String username){
		SQLiteDatabase db = this.getWritableDatabase();
		if(username!=null){
			TABLE_PEOPLE= username + TABLE_P;
			
			String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PEOPLE + "("
	                + PEOPLE_ID + " INTEGER PRIMARY KEY," + PEOPLE_NAME + " TEXT,"
	                + PEOPLE_AGE + " NUMERIC," + PEOPLE_GENDER + " TEXT,"
	                + PEOPLE_HEIGHT + " NUMERIC," + PEOPLE_WEIGHT + " NUMERIC" 
	                + ")";
	        db.execSQL(CREATE_PEOPLE_TABLE);
	        db.close();
		}
	}
	
	public void addPerson(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(PEOPLE_NAME, person.getName()); // Name
	    values.put(PEOPLE_AGE, person.getAge()); // Age
	    values.put(PEOPLE_GENDER, person.getGender()); // Gender
	    values.put(PEOPLE_HEIGHT, person.getHeight()); // Height
	    values.put(PEOPLE_WEIGHT, person.getWeight()); // Weight
	 
	    // Inserting Row
	    Log.d("Inserting...", "Inserting into" + TABLE_PEOPLE);
	    db.insert(TABLE_PEOPLE, null, values);
	    db.close(); // Closing database connection
	}
	
	public Person getPerson(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    Cursor cursor = db.query(TABLE_PEOPLE, new String[] { PEOPLE_ID,
	            PEOPLE_NAME, PEOPLE_AGE, PEOPLE_GENDER, PEOPLE_HEIGHT, PEOPLE_WEIGHT }, PEOPLE_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Person person = new Person(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), 
	            Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
	    // return person
	    return person;
	}
	
	public List<Person> getAllPeople(){
		 List<Person> personList = new ArrayList<Person>();
		    // Select All Query
		    String selectQuery = "SELECT  * FROM " + TABLE_PEOPLE;
		 
		    SQLiteDatabase db = this.getReadableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		 
		    // looping through all rows and adding to list
		    if (cursor.moveToFirst()) {
		        do {
		            Person person = new Person();
		            person.setId(Integer.parseInt(cursor.getString(0)));
		            person.setName(cursor.getString(1));
		            person.setAge(Integer.parseInt(cursor.getString(2)));
		            person.setGender(cursor.getString(3));
		            person.setWeight(Integer.parseInt(cursor.getString(4)));
		            person.setHeight(Integer.parseInt(cursor.getString(5)));
		            // Adding person to list
		            personList.add(person);
		        } while (cursor.moveToNext());
		    }
		 
		    // return person list
		    return personList;
	}
	
	public int getPersonCount(){
		String countQuery = "SELECT  * FROM " + TABLE_PEOPLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}
	
	public int updatePerson(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(PEOPLE_NAME, person.getName()); // Name
	    values.put(PEOPLE_AGE, person.getAge()); // Age
	    values.put(PEOPLE_GENDER, person.getGender()); // Gender
	    values.put(PEOPLE_HEIGHT, person.getHeight()); // Height
	    values.put(PEOPLE_WEIGHT, person.getWeight()); // Weight
	 
	    // updating row
	    return db.update(TABLE_PEOPLE, values, PEOPLE_ID + " = ?",
	            new String[] { String.valueOf(person.getId()) });
	}
	
	public void deletePerson(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_PEOPLE, PEOPLE_ID + " = ?",
	            new String[] { String.valueOf(person.getId()) });
	    Log.d("Delete Person: ",person.getName() + "deleted from database");
	    db.close();
	}
}
