package com.christine.cart.sqlite;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Create database for nutrition database and PLU codes
 * 
 * @see Tutorial by Fluxa for creating own SQLite database in Android using pre-existing tables
 * 		http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 *
 */

public class NutritionDatabaseHelper extends DatabaseHelper {
	
    private static final String DATABASE_NAME = "nutrition_db";
    private static final int DATABASE_VERSION = 1;
    
    public static final String TABLE_NUTRITION = "nutrition_data";
		private static final String N_ID = "_id";
		private static final String N_NAME = "Shrt_Desc";

		private static final String N_SERVINGWEIGHT = "GmWt_1";
		private static final String N_SERVING = "GmWt_Desc1";
	
	private static final String TABLE_PLU = "plu_data";
		private static final String P_PLU = "PLU";
		private static final String P_COMMODITY = "Commodity";
		
    
	public NutritionDatabaseHelper(Context context) {
    	super(context, DATABASE_NAME, DATABASE_VERSION);
	}
	
	
	
	/**
	 * NUTRITION_TABLE 
	 * READING 
	 * 
	 */
	// Getting first matching Item via itemName
	public Item getItem(String itemname){
		SQLiteDatabase db = this.getReadableDatabase();
		Item item = new Item();
		Cursor cursor = db.query(TABLE_NUTRITION, null , N_NAME + "=?", 
				new String []{ String.valueOf(itemname) }, null, null, null);
		if(cursor.moveToFirst()){
			item = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getFloat(2),
					cursor.getFloat(3), cursor.getFloat(4), cursor.getFloat(5), cursor.getFloat(6), 
					cursor.getFloat(7), cursor.getFloat(8), cursor.getFloat(9), cursor.getFloat(10),
					//Potassium & Sodium unit conversion from (mg) to (g)
					cursor.getFloat(11)/(float) 1000,
					cursor.getFloat(12)/(float) 1000, 
					cursor.getFloat(13), cursor.getFloat(14),
					cursor.getFloat(15), cursor.getFloat(16),
					cursor.getFloat(17), cursor.getFloat(18),  
					cursor.getFloat(19), cursor.getFloat(20), cursor.getFloat(21), cursor.getFloat(22),
					cursor.getFloat(23), cursor.getFloat(24), cursor.getFloat(25), cursor.getString(26));
		} else {
			return null;
		}
		
		//return account
		cursor.close();
		db.close();
		return item;
	}
	
	// Getting first matching Item via itemName, but return GroceryItem
	public GroceryItem getGroceryItem(String itemname, String username){
		SQLiteDatabase db = this.getReadableDatabase();
		GroceryItem gItem = new GroceryItem();
		Cursor cursor = db.query(TABLE_NUTRITION, new String []{ N_ID, N_NAME, N_SERVINGWEIGHT, N_SERVING} , N_NAME + " LIKE ? ", 
				new String []{ "%" + itemname + "%" }, null, null, null);
		if(cursor.moveToFirst()){
			gItem = new GroceryItem(Integer.parseInt(cursor.getString(0)), username, cursor.getString(1), cursor.getString(3),
					cursor.getFloat(2), 0.0f, null);
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
	
	// Get All Matching Items
	public List<Item> getAllMatchingItems(String itemname){
		List<Item> itemList = new ArrayList<Item>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NUTRITION, null , N_NAME + "=?", 
				new String []{ String.valueOf(itemname) }, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				Item item = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getFloat(2),
						cursor.getFloat(3), cursor.getFloat(4), cursor.getFloat(5), cursor.getFloat(6), 
						cursor.getFloat(7), cursor.getFloat(8), cursor.getFloat(9), cursor.getFloat(10), 
						//Potassium & Sodium unit conversion from (mg) to (g)
						cursor.getFloat(11)/(float) 1000,
						cursor.getFloat(12)/(float) 1000, 
						cursor.getFloat(13), cursor.getFloat(14),
						cursor.getFloat(15), cursor.getFloat(16), cursor.getFloat(17), cursor.getFloat(18),  
						cursor.getFloat(19), cursor.getFloat(20), cursor.getFloat(21), cursor.getFloat(22),
						cursor.getFloat(23), cursor.getFloat(24), cursor.getFloat(25), cursor.getString(26));
				
				// Adding item to List
				itemList.add(item);
			} while (cursor.moveToNext());
		}
		
		//return account list
		cursor.close();
		db.close();
		return itemList;
	}
	
	// Get All Matching Grocery Items
	public List<GroceryItem> getAllMatchingGroceryItems(String itemname, String username){
		List<GroceryItem> gItemList = new ArrayList<GroceryItem>();
		
		GroceryItem gItem;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " +  N_ID + ", " + N_NAME + ", " + N_SERVINGWEIGHT + ", " +  N_SERVING + 
								" FROM " + TABLE_NUTRITION + " WHERE " + N_NAME + " LIKE ? ", new String[] { "%" + itemname + "%" });
		
		if(cursor.moveToFirst()){
			do{
				gItem = new GroceryItem(Integer.parseInt(cursor.getString(0)), username, cursor.getString(1), cursor.getString(3),
						cursor.getFloat(2), 0.0f, null);
				// Adding item to List
				gItemList.add(gItem);
			} while (cursor.moveToNext());
		}
			
			//return account list
			cursor.close();
			db.close();
			return gItemList;
		}
	
	/**
	 * PLU TABLE
	 * READING
	 * 
	 */
	// Getting single item via PLU Code
	public String getPLUItem(int pluCode){
		SQLiteDatabase db = this.getReadableDatabase();
		String itemName;
		Cursor cursor = db.query(TABLE_PLU, new String[]{P_COMMODITY}, P_PLU + "=?", 
				new String []{ String.valueOf(pluCode)}, null, null, null);
		if(cursor.moveToFirst()){
			itemName = cursor.getString(0);
		} else {
			cursor.close();
			db.close();
			return null;
		}
		
		//return item name;
		cursor.close();
		db.close();
		return itemName;
	}
}
	
