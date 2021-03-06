package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class StartActivity extends Activity {

	//BUTTONS
    Button join;
    Button login;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.start);
        
      //JOIN starts 'JOIN' Activity 
 	   join=(Button) findViewById(R.id.btn_join);
 	   join.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View v) {
 				Intent startCreateAccount = new Intent(v.getContext(), CreateAccountActivity.class);
 				startCreateAccount.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
 				startActivity(startCreateAccount);
 			}
 	   });
 	   
 	   
 	   //LOGIN starts 'LOGIN' Activity
 	   login=(Button) findViewById(R.id.btn_login);
 	   login.setOnClickListener(new View.OnClickListener(){
 			public void onClick(View v){
 				Intent startLogin = new Intent(v.getContext(), LoginActivity.class);
 				startLogin.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
 				startActivity(startLogin);
 			}
 	   });
 	   
 	}
}
