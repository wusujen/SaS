package com.christine.cart;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	Button btn_login;
	EditText username;
	EditText password;
	TextView errorText;
	
	private static int GET_ACCOUNT = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.login);
		
		username = (EditText) findViewById(R.id.textedit_username);
		password = (EditText) findViewById(R.id.textedit_password);
		errorText = (TextView) findViewById(R.id.textview_error);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// get the entered user information
				String enteredName = username.getText().toString();
				String enteredPwd = password.getText().toString();
				
				// first check if there are illegal characters in
				// the entered information
				Intent startAddAccount = new Intent(v.getContext(),AccountActivity.class);
				
				//bundle up the information
				Bundle userInfo = new Bundle();
				userInfo.putInt("requestCode", GET_ACCOUNT);
				userInfo.putString("username",enteredName);
				userInfo.putString("password",enteredPwd);
				
				//give the bundle to the intent
				startAddAccount.putExtras(userInfo);
				
				startActivityForResult(startAddAccount,GET_ACCOUNT);
			}
		});	
	} // end OnCreate
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==GET_ACCOUNT){
			switch(resultCode){
				case RESULT_OK:
					// start setup people activity
					// Get the user name from the intent
					String name = data.getStringExtra("username");
					
					Account act = data.getParcelableExtra("account");
				    if(name.equals(null)){
				    	// log the error
				    	Log.d("LoginActivity:", "username returned null");
				    	errorText.setText("Sorry, we couldn't log you in! Please try again.");
				    } else{
				    	Intent startSetupDays = new Intent(this,SetupDaysActivity.class);
				    	// add the account object
				    	startSetupDays.putExtra("username", name);
				    	startSetupDays.putExtra("account", act);
				    	startActivity(startSetupDays);
				    }
					break;
				case RESULT_CANCELED:
					// display error
					
					String fail = data.getStringExtra("Fail");
					errorText.setText(fail);
					break;
				default:
					errorText.setText("Couldn't get your information. Please try again!");
					
			} //end switch
		} // end if requestCode
	} // end onActivityResult
}
