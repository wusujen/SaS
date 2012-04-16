package com.christine.cart.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.christine.cart.DashboardActivity;
import com.christine.cart.ProfileActivity;
import com.christine.cart.R;
import com.christine.cart.SetupPeopleActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonActivity extends Activity {
	
	Button btn_submit;
	Button btn_next;
	TextView tv_person_step;
	TextView tv_person_desc;
	EditText et_name;
	Spinner sp_age;
	Spinner sp_gender;
	Spinner sp_weight;
	Spinner sp_feet;
	Spinner sp_inches;
	Spinner sp_activity;
	
	ActionBar actionBar;
	
	LinearLayout ll;
	LayoutInflater li;
	
	View person_gender;
	View person_define;
	
	AccountDatabaseHelper adb;
	Person p;
	Person newP;
	String username;
	Account act;
	int totalHeight;
	int inHeight;
	int ftHeight;
	int activityLevel;
	int age;
	String gender;
	int weight;
	int requestCode;
	
	public static final int MAN = 1001;
    public static final int WOMAN = 1002;
    public static final int BOY = 1003;
    public static final int GIRL = 1004;
    
    public static boolean main;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.person);
	    
	    //ActionBar
  		actionBar = (ActionBar) findViewById(R.id.actionbar);
  		actionBar.setTitle("Add Someone You Shop For");
  		actionBar.setHomeAction(new backToSetupPeopleAction());
	  	
  		tv_person_step = (TextView) findViewById(R.id.tv_person_step);
  		tv_person_desc = (TextView) findViewById(R.id.tv_person_desc);

	    act = getIntent().getParcelableExtra("account");
	    username = act.getName();
	    requestCode = getIntent().getIntExtra("requestCode", SetupPeopleActivity.ADD_PERSON);
	    if(requestCode==SetupPeopleActivity.EDIT_PERSON) {
	    	addEventsToDefinePersonLayout(SetupPeopleActivity.EDIT_PERSON);
	    } else {
	    	addEventsToDefinePersonLayout(MAN);
	    	main = false;
	    	actionBar.setTitle("Add Someone You Shop For");
    		tv_person_step.setText("ADD SOMEONE \nSTEP 1 OF 2");
    		tv_person_desc.setText("Tell us about someone that you shop for so we can figure out their nutritional needs.");
	    }
	    
	}
	
	
	/**
	 * Set up actionbar home
	 */
	private class backToSetupPeopleAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_back;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(PersonActivity.this, SetupPeopleActivity.class);
			startAgain.putExtra("account", act);
			startActivity(startAgain);
		}
	}
	
	
	// Convenience method to start an intent request
	// to person activity.
	void addEventsToDefinePersonLayout(final int personType){
		ll = (LinearLayout) findViewById(R.id.ll_define_person);
	    li= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    person_define = li.inflate(R.layout.person_define, null);
	    
	    ll.addView(person_define);
	    
	    ScrollView pScroll = (ScrollView) findViewById(R.id.person_scrollview);
	    pScroll.setScrollbarFadingEnabled(false);
	    
	    adb = new AccountDatabaseHelper(this);
	    
	    et_name = (EditText) findViewById(R.id.et_name);
	    sp_age = (Spinner) findViewById(R.id.sp_age);
	    sp_gender = (Spinner) findViewById(R.id.sp_gender);
	    btn_next = (Button) findViewById(R.id.btn_next);
	    
	    
	    switch(personType){
	    	case PersonActivity.MAN:
	    		p = Person.createPerson(personType, username);
	    		break;
	    	case PersonActivity.WOMAN:
	    		p = Person.createPerson(personType, username);
	    		break;
	    	case PersonActivity.BOY:
	    		p = Person.createPerson(personType, username);
	    		break;
	    	case PersonActivity.GIRL:
	    		p = Person.createPerson(personType, username);
	    		break;
	    	case SetupPeopleActivity.EDIT_PERSON:
	    		p = getIntent().getParcelableExtra("person");
	    		String n = p.getName();
	    		main = p.getMain();
	    		if(main){
		    		actionBar.setTitle("Edit your profile");
		    		tv_person_step.setText("EDIT YOUR PROFILE \nSTEP 1 OF 2");
		    		tv_person_desc.setText("Edit your profile so we can better gauge what your nutritional needs are.");
	    		} else {
	    			actionBar.setTitle("Edit " + n + "'s profile");
		    		tv_person_step.setText("EDIT " + n + "\nSTEP 1 OF 2");
		    		tv_person_desc.setText("Edit " + n + "\'s profile");
	    		}
	    		et_name.setText(n);
	    		break;
	    	default:
	    		throw new RuntimeException("Not implemented");
	    }
	    
	    //to setup the age spinner
	    List<String> year = new ArrayList<String>();
	    age = p.getAge();
	    
    	for(int i=2; i<99; i++){
    		year.add(String.valueOf(i) + " years");
    	}
    	
	    ArrayAdapter<String> years = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
	    years.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_age.setPrompt("Age (we'll keep it a secret)");
	    sp_age.setAdapter(years);
	    sp_age.setSelection(p.getAge()-2);	//the starting selection
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
	    sp_gender.setPrompt("Gender");
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
	    
	    btn_next.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				newP = p;

				String n = et_name.getText().toString();
				if(n!=null && n!=p.getName() && n.length()!=0){
					newP.setName(n);
				} else {
					newP.setName(username);
				}
				if(age!=0 && age!=p.getAge()){
					newP.setAge(age);
				} else {
					Toast.makeText(v.getContext(), "Please enter a valid age!", Toast.LENGTH_LONG);
				}
				
				newP.setGender(gender);
				
				addEventsToDefinePersonLayout2(personType);
			}
		});

	}

	/**
	 * Inflate the rest of the layout form
	 */
	public void addEventsToDefinePersonLayout2(final int personType){
	    View form02 = li.inflate(R.layout.person_define_02, null);
	    
	    ll.removeView(person_define);
	    ll.addView(form02);
		
	    sp_weight = (Spinner) findViewById(R.id.sp_weight);
	    sp_feet = (Spinner) findViewById(R.id.sp_feet);
	    sp_inches = (Spinner) findViewById(R.id.sp_inches);
	    sp_activity = (Spinner) findViewById(R.id.sp_activity);
	    btn_submit = (Button) findViewById(R.id.btn_submit);
	    
	    if(personType ==  SetupPeopleActivity.EDIT_PERSON){
	    	if(newP.getMain()){
	    		tv_person_step.setText("EDIT YOUR PROFILE" + "\nSTEP 2 OF 2");
	    		tv_person_desc.setText("Edit your profile so we can gauge what your nutritional needs are.");
	    	} else {
		    	tv_person_step.setText("EDIT " + newP.getName() + "\nSTEP 2 OF 2");
	    		tv_person_desc.setText("Edit " + newP.getName() + "\'s profile so we can figure out what their nutritional needs are.");
	    	}
	    } else {
		    tv_person_step.setText("ADDING " + newP.getName() + "\nSTEP 2 OF 2");
		    tv_person_desc.setText("Good to know a bit about " + newP.getName() + ".\n Just a little bit more about them and we'll be done!");
	    }
	    
	    //setup weight spinner
	    List<String> lb = new ArrayList<String>();
	    weight = p.getWeight();
	    for(int i=35; i<290; i++){
	    	lb.add(String.valueOf(i) + " pounds");
	    }
	    ArrayAdapter<String> lbs = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lb);
	    lbs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_weight.setAdapter(lbs);
	    sp_weight.setPrompt("Weight");
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
	    sp_feet.setPrompt("Height");
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
	    sp_inches.setPrompt("Your height is " + ftHeight + " feet and");
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
	    sp_activity.setPrompt("How active are you daily?");
	    sp_activity.setSelection(p.getActivity());
	    sp_activity.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				activityLevel = arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
	    	
	    });
	    
	    
	    btn_submit.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				
				int h = inHeight + (ftHeight*12);
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
				newP.setMain(main);
				newP.setUsername(username);
				
				if(personType == SetupPeopleActivity.EDIT_PERSON){
					adb.updatePerson(p, newP);
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
	}
}
