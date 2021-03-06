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

public class LoginActivity extends Activity {
	
	Button btn_login;
	EditText username;
	EditText password;
	ActionBar actionBar;
	
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
		
		
		//ActionBar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Log In");
		actionBar.setHomeAction(new backToStartAction());
		actionBar.addAction(new signUpAction());
				
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
				    	String error = "Sorry, we couldn't log you in! Please try again.";
				    	Toast loginError = Toast.makeText(LoginActivity.this , error , Toast.LENGTH_LONG);
				    	loginError.show();
				    } else{
				    	Intent goToDashboard= new Intent(this,DashboardActivity.class);
				    	// add the account object
				    	goToDashboard.putExtra("account", act);
				    	startActivity(goToDashboard);
				    }
					break;
				case RESULT_CANCELED:
					String fail = data.getStringExtra("Fail");
					Toast failLogin = Toast.makeText(LoginActivity.this , fail, Toast.LENGTH_LONG);
			    	failLogin.show();
					break;
				default:
					String noInfo = "Couldn't get your information. Please try again!";
					Toast noInformation = Toast.makeText(LoginActivity.this , noInfo, Toast.LENGTH_LONG);
			    	noInformation.show();
			} //end switch
		} // end if requestCode
	} // end onActivityResult
	
	private class signUpAction implements Action{
		
		public int getDrawable(){
			return R.drawable.ab_sign_up;
		}
		
		public void performAction(View view){
			Intent backToSignUp = new Intent(LoginActivity.this, CreateAccountActivity.class);
			backToSignUp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(backToSignUp);
		}
	}
	
	private class backToStartAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_back;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(LoginActivity.this, StartActivity.class);
			startAgain.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(startAgain);
		}
	}
	
}
