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

public class AccountDatabaseHelper extends SQLiteOpenHelper{

	// All static variables
	private static final String DATABASE_PATH = "/data/data/com.christine.cart/databases/";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "accounts_db";
	
	// Accounts table name
	private static final String TABLE_ACCOUNTS= "account_info";
	
	// Table account_info column names
	private static final String ACCOUNT_ID = "_id";
	private static final String ACCOUNT_NAME = "username";
	private static final String ACCOUNT_PASSWORD = "password";
	
	// Private variables
	private SQLiteDatabase myDataBase; 
	private Context myContext;
	
	// Constructor
	public AccountDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}
	
	
	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
    	SQLiteDatabase db_Read = null;
    	
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will
    		//be created into the default system path
            //of your application so we are gonna be able to
    		//overwrite that database with our database.
        	db_Read = this.getReadableDatabase();
        	db_Read.close();
        	
        	try {
        		
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
    }
    
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

    	File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
    	return dbFile.exists();
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DATABASE_PATH + DATABASE_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
    
    public SQLiteDatabase getDatabase(){
    	return myDataBase;
    }
 
    @Override
	public synchronized void close() {
    	    if(myDataBase != null){
    		    myDataBase.close();
    	    }
    	    super.close();
	}
 
	
	
	/**
	 * All CRUD Operations
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
	
	
	// Getting single Account
	public Account getAccount(String username){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { ACCOUNT_ID, ACCOUNT_NAME, ACCOUNT_PASSWORD}, ACCOUNT_NAME + "=?", 
				new String []{ String.valueOf(username) }, null, null, null);
		if(cursor != null){
			cursor.moveToFirst();
		}
		
		Account account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		//return account
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
	 * Set methods of SQLiteHelper
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// table pre-created
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// table 
	}

}
