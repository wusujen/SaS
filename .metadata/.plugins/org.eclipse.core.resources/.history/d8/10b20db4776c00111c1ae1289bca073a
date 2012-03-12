package com.christine.cart.sqlite;

import com.christine.cart.R;
import com.christine.cart.SetupPeopleActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PersonActivity extends Activity {

	Button btn_submit;
	TextView tv_title;
	EditText et_name;
	EditText et_age;
	EditText et_gender;
	EditText et_height;
	EditText et_weight;
	
	AccountDatabaseHelper db;
	Person p;
	String username;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.person);
	    
	    db = new AccountDatabaseHelper(this);
	    
	    tv_title = (TextView) findViewById(R.id.tv_title);
	    et_name = (EditText) findViewById(R.id.et_name);
	    et_age = (EditText) findViewById(R.id.et_age);
	    et_gender = (EditText) findViewById(R.id.et_gender);
	    et_height = (EditText) findViewById(R.id.et_height);
	    et_weight = (EditText) findViewById(R.id.et_weight);
	    
	    btn_submit = (Button) findViewById(R.id.btn_submit);
	    
	    
	    Bundle extras = getIntent().getExtras();
	    int requestCode = extras.getInt("requestCode");
	    username = extras.getString("username");
	    
	    switch(requestCode){
	    	case SetupPeopleActivity.MAN:
	    		p = Person.createPerson(requestCode, username);
	    		break;
	    	case SetupPeopleActivity.WOMAN:
	    		p = Person.createPerson(requestCode, username);
	    		break;
	    	case SetupPeopleActivity.BOY:
	    		p = Person.createPerson(requestCode, username);
	    		break;
	    	case SetupPeopleActivity.GIRL:
	    		p = Person.createPerson(requestCode, username);
	    		break;
	    	default:
	    		throw new RuntimeException("Not implemented");
	    }
	    
	    //db.addPerson(p);
	    
	    et_name.setText(p.getName());
	    et_age.setText(Integer.toString(p.getAge()));
	    et_gender.setText(p.getGender());
	    et_height.setText(Integer.toString(p.getHeight()));
	    et_weight.setText(Integer.toString(p.getWeight()));
	    
	    
	    btn_submit.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				db.addPerson(p);
				Log.d("Insert..","Person Added");
				Intent personSaved = new Intent();
				personSaved.putExtra("username", username);
				setResult(RESULT_OK, personSaved);
				finish();
			}
		});
	    
	}

}
