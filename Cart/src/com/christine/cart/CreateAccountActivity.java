package com.christine.cart;


import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

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
import android.widget.Toast;

public class CreateAccountActivity extends Activity {

	Button btn_start;
	EditText username;
	EditText password;
	ActionBar actionBar;
	
	private static int ADD_ACCOUNT = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
	    setContentView(R.layout.join);
		
		username = (EditText) findViewById(R.id.textedit_username);
		password = (EditText) findViewById(R.id.textedit_password);
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
		
		//ActionBar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Sign up");
		actionBar.setHomeAction(new backToStartAction());
		actionBar.addAction(new logInAction());
		
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
				    if(name.equals(null)){
				    	String error = "Sorry, this username is invalid. Please try another.";
				    	Toast loginError = Toast.makeText(this , error , Toast.LENGTH_LONG);
				    	loginError.show();
				    	return;
				    } else if(name.equals("0")){
				    	String takenError = "Sorry, that username has already been taken. Please enter a new username.";
				    	Toast nameTaken = Toast.makeText(this , takenError, Toast.LENGTH_LONG);
				    	nameTaken.show();
				    	return;
				    }
				    else{
				    	Intent goToDashboard = new Intent(this, DashboardActivity.class);
				    	goToDashboard.putExtra("account", act);
				    	startActivity(goToDashboard);
				    }
					break;
				case RESULT_CANCELED:
					// display error
					Bundle failInfo = getIntent().getExtras();
					String fail = failInfo.getString("fail");
					Toast failLogin = Toast.makeText(this , fail, Toast.LENGTH_LONG);
			    	failLogin.show();
					break;
				default:
					String noInfo = "Couldn't save your information. Please try again!";
					Toast noInformation = Toast.makeText(this , noInfo, Toast.LENGTH_LONG);
			    	noInformation.show();	
			} //end switch
		} // end if requestCode
	} // end onActivityResult

	private class logInAction implements Action{
		
		public int getDrawable(){
			return R.drawable.ab_log_in;
		}
		
		public void performAction(View view){
			Intent goLogin = new Intent(CreateAccountActivity.this, LoginActivity.class);
			goLogin.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(goLogin);
		}
	}
	
	private class backToStartAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_back;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(CreateAccountActivity.this, StartActivity.class);
			startAgain.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(startAgain);
		}
	}
}
