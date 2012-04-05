package com.christine.cart;

import java.util.ArrayList;
import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.Person;
import com.christine.cart.sqlite.PersonActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ProfileActivity extends Activity {

	Button btn_submit;
	Button btn_later;
	TextView tv_age;
	TextView tv_weight;
	TextView tv_height;
	EditText et_name;
	Spinner sp_age;
	Spinner sp_gender;
	Spinner sp_weight;
	Spinner sp_feet;
	Spinner sp_inches;
	Spinner sp_activity;
	
	AccountDatabaseHelper adb;
	Person p;
	Account act;
	String username;
	String gender;
	int totalHeight;
	int inHeight;
	int ftHeight;
	int activityLevel;
	int age;
	int weight;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.profile);
	    
	    adb = new AccountDatabaseHelper(this);
	    
	    tv_age = (TextView) findViewById(R.id.tv_age);
	    tv_weight = (TextView) findViewById(R.id.tv_weight);
	    tv_height = (TextView) findViewById(R.id.tv_height);
	    
	    et_name = (EditText) findViewById(R.id.et_name);
	    sp_age = (Spinner) findViewById(R.id.sp_age);
	    sp_gender = (Spinner) findViewById(R.id.sp_gender);
	    sp_weight = (Spinner) findViewById(R.id.sp_weight);
	    sp_feet = (Spinner) findViewById(R.id.sp_feet);
	    sp_inches = (Spinner) findViewById(R.id.sp_inches);
	    sp_activity = (Spinner) findViewById(R.id.sp_activity);
	    
	    btn_submit = (Button) findViewById(R.id.btn_submit);
	    btn_later = (Button) findViewById(R.id.btn_later);
	    
	    
	    Bundle extras = getIntent().getExtras();
	    act = extras.getParcelable("account");
	    username = act.getName();
	    
	    p = Person.createPerson(PersonActivity.MAN, username);
	    p.setName(username);
	    
	    //setup the default values for each of the UI elements
	    //based upon what was passed via the previous intent
	    et_name.setHint(username);
	    
	    //get the height and divide it
	    totalHeight = p.getHeight();
	    inHeight = totalHeight%12;
	    ftHeight = (totalHeight-inHeight)/12;
	    
	    //setup the Feet spinner
	    List<String> ft = new ArrayList<String>();
	    for(int i=2; i<10; i++){
	    	ft.add(String.valueOf(i) + " feet");
	    }
	    ArrayAdapter<String> feet = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ft);
	    feet.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
	    sp_feet.setAdapter(feet);
	    sp_feet.setSelection(ftHeight-2); 	//the starting selection
	    sp_feet.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ftHeight = arg2+2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    });
	    
	    //setup the inches spinner
	    List<String> in = new ArrayList<String>();
	    for(int i=0; i<13; i++){
	    	if(i == 1){
	    		in.add(i + " inch");
	    	} else {
	    		in.add(i + " inches");
	    	}
	    }
	    ArrayAdapter<String> inch = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, in);
	    inch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_inches.setAdapter(inch);
	    sp_inches.setSelection(inHeight);	//the starting selection
	    sp_inches.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				inHeight = arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    });
	    
	    //setup the Activity Level spinner
	    ArrayAdapter<String> aLevel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
	    		new String[]{"Sedentary", "Lightly Active", "Moderately Active", "Very Active"});
	    aLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_activity.setAdapter(aLevel);
	    sp_activity.setSelection(0);
	    sp_activity.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				activityLevel = arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    	
	    });
	    
	    //to setup the age spinner
	    List<String> year = new ArrayList<String>();
	    age = p.getAge();
	    
    	for(int i=2; i<99; i++){
    		year.add(String.valueOf(i) + " years");
    	}
    	
	    ArrayAdapter<String> years = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
	    years.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_age.setAdapter(years);
	    sp_age.setSelection(age-2);	//the starting selection
	    sp_age.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				age = arg2 + 2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    });
	    
	    List<String> gen = new ArrayList<String>();
	    gender = p.getGender();
	    gen.add("male");
	    gen.add("female");
	    ArrayAdapter<String> genders = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gen);
	    genders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_gender.setAdapter(genders);
	    if(gender.equals("m")){
	    	sp_gender.setSelection(0);	//the starting selection
	    } else {
	    	sp_gender.setSelection(1);
	    }
	    sp_gender.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==0){
					gender = "m" ;
				} else {
					gender = "f";
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    });
	    
	    //setup weight spinner
	    List<String> lb = new ArrayList<String>();
	    weight = p.getWeight();
	    for(int i=35; i<290; i++){
	    	lb.add(String.valueOf(i) + " pounds");
	    }
	    ArrayAdapter<String> lbs = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lb);
	    lbs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_weight.setAdapter(lbs);
	    sp_weight.setSelection(weight-35);	//the starting selection
	    sp_weight.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				weight = arg2 + 35;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    });
	    
	    
	    
	    
	    btn_submit.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				//get the person and store it into the db
				Person newP = p;
				String n = et_name.getText().toString();
				int h = inHeight + (ftHeight*12);
				
				if(n!=null && n!=p.getName() && n.length()!=0){
					newP.setName(n);
				} else {
					newP.setName(username);
				}
				if(age!=0 && age!=p.getAge()){
					//Log.d("Person Activity: Age stored.", "Age: " + age);
					newP.setAge(age);
				} else {
					Toast.makeText(v.getContext(), "Please enter a valid age!", Toast.LENGTH_LONG);
				}
				if(h!=0 && h!=p.getHeight()){
					newP.setHeight(h);
					//Log.d("Person Activity: Height stored.", "Height in inches" + h);
				}
				if(weight!=0 && weight!=p.getWeight()){
					newP.setWeight(weight);
					//Log.d("Person Activity: Weight", "Weight " + weight);
				} else{
					Toast.makeText(v.getContext(), "Please enter a valid weight!",  Toast.LENGTH_LONG);
				}
				newP.setActivity(activityLevel);
				newP.setMain(true);
				newP.setUsername(username);
				
				List<Person> people = adb.getAllPeopleFor(username);
				
				if(people!=null){
					for(int i=0; i<people.size(); i++){
						Person tempP = people.get(i);
						if(tempP.getName().equals(newP.getName())){
							adb.updatePerson(p, newP);
						} else {
							adb.addPerson(newP);
						}
					}
				} else {
					adb.addPerson(newP);
				}
				
				adb.close();
				
				Intent personSaved = new Intent( getApplicationContext(), SetupPeopleActivity.class);
				personSaved.putExtra("username", username);
				personSaved.putExtra("account", act);
				startActivity(personSaved);
			}
		});
	    
	    btn_later.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent defaultUsed = new Intent( getApplicationContext(), SetupPeopleActivity.class);
				defaultUsed.putExtra("username", username);
				defaultUsed.putExtra("account", act);
				startActivity(defaultUsed);
			}
		});
	    
	}

}