package com.christine.cart;

import java.io.IOException;
import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {

	//BUTTONS
    Button join;
    Button login;
    Button guest;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
      //TESTING SQLite
        AccountDatabaseHelper db = new AccountDatabaseHelper(this);
        SQLiteDatabase myDatabase;
        
        try {
        	db.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
        //OPEN THE DATABASE
	 	try {
	 		//myDbHelper.close();
	 		db.openDataBase();
	 	} catch(SQLException sqle){
	 		throw sqle;
	 	}
	 	
        /**
         * CRUD Operations
         * */
        //Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addAccount(new Account("Ravi", "9100000000"));
        //db.addAccount(new Account("Srinivas", "9199999999"));
        //db.addAccount(new Account("Tommy", "9522222222"));
        //db.addAccount(new Account("Karthik", "9533333333"));
 
        // Reading all contacts
        Log.d("Reading: ", "Reading all accounts..");
        List<Account> accounts = db.getAllAccounts();       
 
       for (Account act : accounts) {
            String log = "Id: "+act.getId()+" ,Name: " + act.getName() + " ,Password: " + act.getPassword();
                // Writing Contacts to log
            Log.d("Name: ", log);
       }
      //END TESTING  
        
      //JOIN starts 'JOIN' Activity
 	   join=(Button) findViewById(R.id.btn_join);
 	   join.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View v) {
 				//go to JOIN activity
 			}
 	   });
 	   
 	   
 	   //LOGIN starts 'LOGIN' Activity
 	   login=(Button) findViewById(R.id.btn_login);
 	   login.setOnClickListener(new View.OnClickListener(){
 			public void onClick(View v){
 				Intent startLogin = new Intent(v.getContext(), LoginActivity.class);
 				startActivity(startLogin);
 			}
 	   });
 	   
 	   
 	   //GUEST starts 'GUEST' Activity
 	   guest=(Button) findViewById(R.id.btn_guest);
 	   guest.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View v) {
 				//go to GUEST cart
 			}
 	   });

 	}
}
