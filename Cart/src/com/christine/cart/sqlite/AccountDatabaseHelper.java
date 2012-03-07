package com.christine.cart.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
		private static final String ACCOUNT_DAYS = "days";
	
	//People Table info
	private static final String TABLE_PEOPLE = "people";
		//column names
		private static final  String PEOPLE_ID = "_id";
		private static final String PEOPLE_USER = "_username";
		private static final String PEOPLE_NAME = "name";
		private static final String PEOPLE_AGE = "age";
		private static final String PEOPLE_GENDER = "gender";
		private static final String PEOPLE_HEIGHT = "height";
		private static final String PEOPLE_WEIGHT = "weight";
	
	//Current_Cart Table Info
	private static final String TABLE_CCART = "current_cart";
		//column names
		private static final String CCART_ID = "_id";
		private static final String CCART_USER = "_username";
		private static final String CCART_ITEMNAME = "item_name";
		private static final String CCART_SERVING = "serving";
		private static final String CCART_SERVINGWEIGHT = "serving_weight";
		private static final String CCART_SERVINGRATIO = "serving_ratio";
		private static final String CCART_QUANTITY = "quantity";
	
	// Account_History Table Info
	private static final String TABLE_PHISTORY = "previous_history";
		//column names
		private static final String PHISTORY_ID = "_id";
		private static final String PHISTORY_USER = "_username";
		
		
	
	// When an AccountDatabaseHelper is created, use
	// the generic DatabaseHelper class
	public AccountDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
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
	
	public int updateAccountDays(Account account, int days){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ACCOUNT_NAME, account.getName());
		values.put(ACCOUNT_PASSWORD, account.getPassword());
		values.put(ACCOUNT_DAYS, String.valueOf(days));
		
		//update row
		return db.update(TABLE_ACCOUNTS, values, ACCOUNT_NAME + " = ?", new String[] { String.valueOf(account.getName())});	
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
	
	public void addPerson(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(PEOPLE_USER, person.getUsername()); //username
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
	
	public List<Person> getAllPeopleFor(String username){
		SQLiteDatabase db = this.getReadableDatabase();
		List<Person> personList = new ArrayList<Person>();
	    Cursor cursor = db.query(TABLE_PEOPLE, new String[] { PEOPLE_ID, PEOPLE_USER,
	            PEOPLE_NAME, PEOPLE_AGE, PEOPLE_GENDER, PEOPLE_HEIGHT, PEOPLE_WEIGHT }, PEOPLE_USER + "=?",
	            new String[] { username.toString() }, null, null, null, null);
	    if (cursor != null){
	        cursor.moveToFirst();
	    }
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Person person = new Person();
	            person.setId(Integer.parseInt(cursor.getString(0)));
	            person.setName(cursor.getString(1));
	            person.setName(cursor.getString(2));
	            person.setAge(Integer.parseInt(cursor.getString(3)));
	            person.setGender(cursor.getString(4));
	            person.setWeight(Integer.parseInt(cursor.getString(5)));
	            person.setHeight(Integer.parseInt(cursor.getString(6)));
	            // Adding person to list
	            personList.add(person);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    return personList;
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
		            person.setName(cursor.getString(2));
		            person.setAge(Integer.parseInt(cursor.getString(3)));
		            person.setGender(cursor.getString(4));
		            person.setWeight(Integer.parseInt(cursor.getString(5)));
		            person.setHeight(Integer.parseInt(cursor.getString(6)));
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
	    values.put(PEOPLE_USER, person.getUsername()); //username
	    values.put(PEOPLE_NAME, person.getName()); // Name
	    values.put(PEOPLE_AGE, person.getAge()); // Age
	    values.put(PEOPLE_GENDER, person.getGender()); // Gender
	    values.put(PEOPLE_HEIGHT, person.getHeight()); // Height
	    values.put(PEOPLE_WEIGHT, person.getWeight()); // Weight
	 
	    // updating row
	    return db.update(TABLE_PEOPLE, values, PEOPLE_USER + " = ?",
	            new String[] { String.valueOf(person.getUsername()) });
	}
	
	public void deletePerson(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_PEOPLE, PEOPLE_USER + " = ?",
	            new String[] { person.getUsername() });
	    Log.d("Delete Person: ",person.getName() + "deleted from database");
	    db.close();
	}
	
	
	/**
	 * CURRENT_CART Table
	 * CRUD
	 */
	
	// Adds a grocery item if it is new to that particular USER or
	// updates an existing entree (generally update of QUANTITY).
	public void addGroceryItem(GroceryItem gItem){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
	    values.put(CCART_USER, gItem.getUsername()); 
	    values.put(CCART_ITEMNAME, gItem.getItemName());
	    values.put(CCART_SERVING, gItem.getServing()); 
	    values.put(CCART_SERVINGWEIGHT, gItem.getServingWeight());
	    values.put(CCART_SERVINGRATIO, gItem.getServingRatio());
	    values.put(CCART_QUANTITY, gItem.getQuantity());
	    

		// insert if item does not exist in database
	    Log.d("Inserting...", "Inserting into" + TABLE_CCART);
	    db.insert(TABLE_CCART, null, values);
		
		db.close(); // Closing database connection
	}
	
	public void updateGroceryItem(GroceryItem gItem){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
	    values.put(CCART_USER, gItem.getUsername()); 
	    values.put(CCART_ITEMNAME, gItem.getItemName());
	    values.put(CCART_SERVING, gItem.getServing()); 
	    values.put(CCART_SERVINGWEIGHT, gItem.getServingWeight());
	    values.put(CCART_SERVINGRATIO, gItem.getServingRatio());
	    values.put(CCART_QUANTITY, gItem.getQuantity());
	    
	    // otherwise, update the item in the database
	    Log.d("Updating...", "Updating " + TABLE_CCART + " with " + gItem.getItemName() + " for user " + gItem.getUsername());
	    db.update(TABLE_CCART, values, CCART_USER + "=? AND " + CCART_ITEMNAME + " =? ", 
			new String[]{gItem.getUsername(), gItem.getItemName()});
	}
	
	
	// Getting single Grocery Item via username
	public GroceryItem getGroceryItemOf(String username, String itemName){
		SQLiteDatabase db = this.getReadableDatabase();
		GroceryItem gItem;
		Cursor cursor = db.query(TABLE_CCART, null , CCART_USER + "=? AND " + CCART_ITEMNAME + "=?", 
				new String []{ String.valueOf(username), String.valueOf(itemName)}, null, null, null);
		if(cursor.moveToFirst()){
			gItem = new GroceryItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getFloat(4), cursor.getFloat(5), cursor.getInt(6));
		} else {
			cursor.close();
			db.close();
			return null;
		}
		
		//return account
		cursor.close();
		db.close();
		return gItem;
	}
	
	// Get all of the Grocery Items associated with a user
	public List<GroceryItem> getAllGroceryItemsOf(String username){
		List<GroceryItem> groceryList = new ArrayList<GroceryItem>();
		 
	    SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_CCART, null , CCART_USER + "=? ", 
				new String []{ String.valueOf(username)}, null, null, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            GroceryItem gItem = new GroceryItem();
	            gItem.setId(Integer.parseInt(cursor.getString(0)));
	            gItem.setUsername(cursor.getString(1));
	            gItem.setItemName(cursor.getString(2));
	            gItem.setServing(cursor.getString(3));
	            gItem.setServingWeight(cursor.getFloat(4));
	            gItem.setServingRatio(cursor.getFloat(5));
	            gItem.setQuantity(cursor.getInt(6));
	            // Adding groceryItem to list
	            groceryList.add(gItem);
	        } while (cursor.moveToNext());
	    } else{
	    	cursor.close();
	    	db.close();
	    	return null;
	    }
	    
	    cursor.close();
	    db.close();
	    // return grocery List
	    return groceryList;
	}
	
	// delete all grocery items associated with one user
	public void deleteAllGroceryItemsOf(String username){
		SQLiteDatabase db = this.getWritableDatabase();
		int numDeleted = db.delete(TABLE_CCART, CCART_USER + " = ? ",
	            new String[] { String.valueOf(username) });
		Log.d("Delete Person: ","Row Number removed: " + numDeleted + " from User: " 
	            + username + " deleted from database");
	    db.close();
	}
	
	// delete one single grocery item associated with one user
	public void deleteGroceryItem(GroceryItem gItem){
		SQLiteDatabase db = this.getWritableDatabase();
		int numDeleted = db.delete(TABLE_CCART, CCART_USER + " = ? AND " + CCART_ITEMNAME + " = ?",
	            new String[] { gItem.getUsername(), gItem.getItemName() });
	    Log.d("Delete Person: ","Row Number removed: " + numDeleted + " from User: " 
	            + gItem.getUsername() + "Item: " + gItem.getItemName() + " deleted from database");
	    db.close();
	}
}
