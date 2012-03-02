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

public class AccountTableHelper extends DatabaseHelper{
	
	// All static variables
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "accounts_db";
	
	// Accounts table name
	private static final String TABLE_ACCOUNTS = "account_info";
	
	// Table account_info column names
	private static final String ACCOUNT_ID = "_id";
	private static final String ACCOUNT_NAME = "username";
	private static final String ACCOUNT_PASSWORD = "password";
	
	// Private variables
	private SQLiteDatabase myDataBase; 
	private Context myContext;
	
	
	// When an AccountDatabaseHelper is created, use
	// the generic DatabaseHelper class
	public AccountTableHelper(Context context) {
		super(context, DATABASE_NAME,DATABASE_VERSION);
        this.myContext = context;
	}
	
	/**
	 * 
	 * ACCOUNT
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

}
