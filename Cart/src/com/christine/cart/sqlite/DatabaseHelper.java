package com.christine.cart.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * General DatabaseHelper to access databases within Cart.
 * Returns a database helper, and attempts to create a database from
 * existing resources in Assets
 * 
 * @see Tutorial by Fluxa for creating own SQLite database in Android using pre-existing tables
 * 		http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	// All static variables
	private static final String DATABASE_PATH = "/data/data/com.christine.cart/databases/";
	private static String DATABASE_NAME;
	private static int DATABASE_VERSION;
	
	// Private variables
	private SQLiteDatabase myDataBase; 
	private Context myContext;
	
	// Constructor
	public DatabaseHelper(Context context, String databaseName, int databaseVersion) {
		super(context, databaseName, null, databaseVersion);
		
		DATABASE_NAME = databaseName;
		DATABASE_VERSION = databaseVersion;
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
    	myDataBase.close();
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
