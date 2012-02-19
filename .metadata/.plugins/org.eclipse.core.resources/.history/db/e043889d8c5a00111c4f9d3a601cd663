package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	//BUTTONS
    Button join;
    Button login;
    Button guest;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
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