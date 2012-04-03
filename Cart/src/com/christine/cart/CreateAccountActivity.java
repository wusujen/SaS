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

public class CreateAccountActivity extends Activity {

	Button btn_start;
	EditText username;
	EditText password;
	TextView errorText;
	
	private static int ADD_ACCOUNT = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
	    setContentView(R.layout.join);
		
		username = (EditText) findViewById(R.id.textedit_username);
		password = (EditText) findViewById(R.id.textedit_password);
		errorText = (TextView) findViewById(R.id.textview_error);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// get the entered user information
				String enteredName = username.getText().toString();
				String enteredPwd = password.getText().toString();
				
				// first check if there are illegal characters in
				// the entered information
				Intent startAddAccount = new Intent(v.getContext(),AccountActivity.class);
				
				//bundle up the information
				Bundle userInfo = new Bundle();
				userInfo.putInt("requestCode", ADD_ACCOUNT);
				userInfo.putString("username",enteredName);
				userInfo.putString("password",enteredPwd);
				
				//give the bundle to the intent
				startAddAccount.putExtras(userInfo);
				
				startActivityForResult(startAddAccount,ADD_ACCOUNT);
			}
		});
		
	} // end onCreate
	
	
	/**
	 * Starts SetupPeople activity if the username and password were
	 * successfully saved into the database. 
	 * 
	 * Otherwise, if username is a duplicate or if the result was cancelled,
	 * put an error into the fail.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==ADD_ACCOUNT){
			switch(resultCode){
				case RESULT_OK:
					// start setup people activity
					// Get the user name from the intent
					String name = data.getStringExtra("username");
					Account act = data.getParcelableExtra("account");
					Log.d("CreateAccountActivity", "Account name created: " + act.getName());
				    if(name.equals(null)){
				    	// log the error
				    	Log.d("CreateAccountActivity:", "username returned null");
				    	errorText.setText("Sorry, this username is invalid. Please try another.");
				    	return;
				    } else if(name.equals("0")){
				    	Log.d("CreateAccountActivity:", "username has been taken");
				    	errorText.setText("Sorry, that username has already been taken. Please enter a new username.");
				    	return;
				    }
				    else{
				    	// 
				    	Intent startProfileActivity = new Intent(this,ProfileActivity.class);
				    	startProfileActivity.putExtra("account", act);
				    	startActivity(startProfileActivity);
				    }
					break;
				case RESULT_CANCELED:
					// display error
					Bundle failInfo = getIntent().getExtras();
					String fail = failInfo.getString("fail");
					errorText.setText(fail);
					break;
				default:
					errorText.setText("Couldn't save your information. Please try again!");
					
			} //end switch
		} // end if requestCode
	} // end onActivityResult

	
}
