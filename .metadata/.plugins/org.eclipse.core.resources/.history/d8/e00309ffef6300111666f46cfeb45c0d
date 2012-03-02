package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	Button btn_login;
	EditText username;
	EditText password;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		username = (EditText) findViewById(R.id.textedit_username);
		password = (EditText) findViewById(R.id.textedit_password);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent startSetupPeople=new Intent(v.getContext(),SetupPeopleActivity.class);
				
				//bundle up the information
				Bundle userInfo = new Bundle();
				userInfo.putString("username",username.getText().toString());
				userInfo.putString("password",password.getText().toString());
				
				//give the bundle to the intent
				startSetupPeople.putExtras(userInfo);
				
				startActivity(startSetupPeople);
			}
		});
		
	}
}
