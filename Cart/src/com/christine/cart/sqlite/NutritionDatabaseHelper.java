package com.christine.cart.sqlite;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
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
    
    private static final String TABLE_NUTRITION = "nutrition_data";
		private static final String N_ID = "_id";
		private static final String N_NAME = "Shrt_Desc";
		private static final String N_CALORIES = "Energ_Kcal";
		private static final String N_PROTEIN = "Protein_g";
		private static final String N_TOTALFATS = "Lipid_Tot_g";
		private static final String N_CARBS = "Carbohydrt_g";
		private static final String N_FIBER = "Fiber_TD_g";
		private static final String N_SUGAR = "Sugar_Tot_g";
		private static final String N_CALCIUM = "Calcium_mg";
		private static final String N_IRON = "Iron_mg";
		private static final String N_MAGNESIUM = "Magnesium_mg";
		private static final String N_POTASSIUM = "Potassium_mg";
		private static final String N_SODIUM = "Sodium_mg";
		private static final String N_ZINC = "Zinc_mg";
		private static final String N_VITC = "Vit_C_mg";
		private static final String N_VITB6 = "Vit_B6_mg";
		private static final String N_VITB12 = "Vit_B12_mg";
		private static final String N_VITA = "Vit_A_RAE";
		private static final String N_VITE = "Vit_E_mg";
		private static final String N_VITD = "Vit_D_μg";
		private static final String N_VITK = "Vit_K_μg";
		private static final String N_FATSAT = "FA_Sat_g";
		private static final String N_FATMONO = "FA_Mono_g";
		private static final String N_FATPOLY = "FA_Poly_g";
		private static final String N_CHOLESTEROL = "Cholestrl_mg";
		private static final String N_SERVINGWEIGHT = "GmWt_1";
		private static final String N_SERVING = "GmWt_Desc1";
	
	private static final String TABLE_PLU = "plu_data";
		private static final String P_ID = "_id";
		private static final String P_PLU = "PLU";
		private static final String P_COMMODITY = "Commodity";
		private static final String P_VARIETY = "Variety";
		private static final String P_SIZE = "Size";
		
    
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
			item = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Float.parseFloat(cursor.getString(2)),
					Float.parseFloat(cursor.getString(3)), Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(5)),
					Float.parseFloat(cursor.getString(6)), Float.parseFloat(cursor.getString(7)), Float.parseFloat(cursor.getString(8)),
					Float.parseFloat(cursor.getString(9)), Float.parseFloat(cursor.getString(10)), Float.parseFloat(cursor.getString(11)),
					Float.parseFloat(cursor.getString(12)), Float.parseFloat(cursor.getString(13)), Float.parseFloat(cursor.getString(14)),
					Float.parseFloat(cursor.getString(15)), Float.parseFloat(cursor.getString(16)), Float.parseFloat(cursor.getString(17)),
					Float.parseFloat(cursor.getString(18)), Float.parseFloat(cursor.getString(19)), Float.parseFloat(cursor.getString(20)),
					Float.parseFloat(cursor.getString(21)), Float.parseFloat(cursor.getString(22)), Float.parseFloat(cursor.getString(23)),
					Float.parseFloat(cursor.getString(24)), Float.parseFloat(cursor.getString(25)), cursor.getString(28));
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
		Cursor cursor = db.query(TABLE_NUTRITION, new String []{ N_ID, N_NAME, N_SERVINGWEIGHT, N_SERVING} , N_NAME + "=?", 
				new String []{ String.valueOf(itemname) }, null, null, null);
		if(cursor.moveToFirst()){
			gItem = new GroceryItem(Integer.parseInt(cursor.getString(0)), username, cursor.getString(1), cursor.getString(2),
					Float.parseFloat(cursor.getString(3)), 0.0f);
		} else {
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
				Item item = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Float.parseFloat(cursor.getString(2)),
						Float.parseFloat(cursor.getString(3)), Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(5)),
						Float.parseFloat(cursor.getString(6)), Float.parseFloat(cursor.getString(7)), Float.parseFloat(cursor.getString(8)),
						Float.parseFloat(cursor.getString(9)), Float.parseFloat(cursor.getString(10)), Float.parseFloat(cursor.getString(11)),
						Float.parseFloat(cursor.getString(12)), Float.parseFloat(cursor.getString(13)), Float.parseFloat(cursor.getString(14)),
						Float.parseFloat(cursor.getString(15)), Float.parseFloat(cursor.getString(16)), Float.parseFloat(cursor.getString(17)),
						Float.parseFloat(cursor.getString(18)), Float.parseFloat(cursor.getString(19)), Float.parseFloat(cursor.getString(20)),
						Float.parseFloat(cursor.getString(21)), Float.parseFloat(cursor.getString(22)), Float.parseFloat(cursor.getString(23)),
						Float.parseFloat(cursor.getString(24)), Float.parseFloat(cursor.getString(25)), cursor.getString(28));
				
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
	public List<GroceryItem> getAllMatchingItems(String itemname, String username){
		List<GroceryItem> gItemList = new ArrayList<GroceryItem>();
		
		GroceryItem gItem;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NUTRITION, null , N_NAME + "=?", 
				new String []{ String.valueOf(itemname) }, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				gItem = new GroceryItem(Integer.parseInt(cursor.getString(0)), username, cursor.getString(1), cursor.getString(2),
						Float.parseFloat(cursor.getString(3)), 0.0f);
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
		Cursor cursor = db.query(TABLE_PLU, new String[]{P_COMMODITY}, P_COMMODITY + "=?", 
				new String []{ String.valueOf(pluCode)}, null, null, null);
		if(cursor.moveToFirst()){
			itemName = cursor.getString(0);
		} else {
			return null;
		}
		
		//return item name;
		cursor.close();
		db.close();
		return itemName;
	}
}
	
