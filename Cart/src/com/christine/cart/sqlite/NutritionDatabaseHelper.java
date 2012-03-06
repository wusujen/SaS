package com.christine.cart.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Create database for nutrition database and PLU codes
 * 
 * @see Tutorial by Fluxa for creating own SQLite database in Android using pre-existing tables
 * 		http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 *
 */

public class NutritionDatabaseHelper extends DatabaseHelper {
	
    private static String DB_NAME = "nutrition_db";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;

	public NutritionDatabaseHelper(Context context) {
    	super(context, DB_NAME, 1);
        this.myContext = context;
	}
	
}
	
