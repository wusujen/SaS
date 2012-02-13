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
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent startSetupPeople=new Intent(v.getContext(),SetupPeopleActivity.class);
				
				//pass the username & password to the next activity
				startSetupPeople.putExtra("username",username.getText());
				startSetupPeople.putExtra("password",password.getText());
				
				startActivity(startSetupPeople);
			}
		});
		
	}

}
