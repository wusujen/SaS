package com.christine.cart;


import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.Person;
import com.christine.cart.sqlite.PersonActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetupPeopleActivity extends Activity {

	Button next;
	Button add;
	Button edit;
	ListView peopleList;
	
	ActionBar actionBar;
	
	private static String username = null;
    private static String password = null;
    public static final int ADD_PERSON = 1;
    public static final int EDIT_PERSON = 2;
    private static Account act;
    private static Person selectedP;
    
    List<Person> plist = null;
	AccountDatabaseHelper adb;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setup_people);
	    
	    
	    //ActionBar
  		actionBar = (ActionBar) findViewById(R.id.actionbar);
  		actionBar.setTitle("People");
  		actionBar.setHomeAction(new backToDashboardAction());
  		
	    act = getIntent().getParcelableExtra("account");
	    username = act.getName();
	    
	    add = (Button) findViewById(R.id.btn_add);
	    add.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent addPerson = new Intent(SetupPeopleActivity.this, PersonActivity.class);
				addPerson.putExtra("account", act);
				addPerson.putExtra("requestCode", ADD_PERSON);
				startActivityForResult(addPerson, ADD_PERSON);
			}
		});
	    
	    
	    //start SetupDays and pass the user information along
	    //with the number of people when "btn_next" is clicked
	    next = (Button) findViewById(R.id.btn_next);
	    next.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent startSetupDays=new Intent(v.getContext(),SetupDaysActivity.class);
				
				Bundle people = new Bundle();
				people.putParcelable("account", act);
				
				startSetupDays.putExtras(people);
				
				startActivity(startSetupDays);
			}
		});
	    
	    edit = (Button) findViewById(R.id.btn_edit);
	    edit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(selectedP!=null){
					Intent editPerson = new Intent(SetupPeopleActivity.this, PersonActivity.class);
					editPerson.putExtra("requestCode", EDIT_PERSON);
					editPerson.putExtra("account", act);
					editPerson.putExtra("person", selectedP);
					startActivityForResult(editPerson, EDIT_PERSON);
				}
			}
		});
	}
	
	/**
	 * Set up actionbar home
	 */
	private class backToDashboardAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_home_large;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(SetupPeopleActivity.this, DashboardActivity.class);
			startAgain.putExtra("account", act);
			startActivity(startAgain);
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		selectedP = null;
		if(plist==null){
			adb = new AccountDatabaseHelper(this);
			plist = adb.getAllPeopleFor(username);
			adb.close();
			
			if(plist!=null){
				setupPeopleList(SetupPeopleActivity.this, plist);
			} else {
				Log.d("SetupPeopleActivity", "Line 110: Plist was null");
			}
		} else {
			setupPeopleList(SetupPeopleActivity.this, plist);
			Log.d("SetupPeopleActivity", "Line 113: Plist wasn't null, setup is an issue");
		}
		
		
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode==ADD_PERSON){
			if(resultCode == RESULT_OK){
				 adb = new AccountDatabaseHelper(this);
				 plist = adb.getAllPeopleFor(username); 
				 adb.close();
			} else if (resultCode == RESULT_CANCELED){
				Toast sorry = Toast.makeText(SetupPeopleActivity.this, 
						"Sorry, we had trouble adding that person! Please try again.", Toast.LENGTH_LONG);
				sorry.show();
			}
		} else if(requestCode==EDIT_PERSON){
			if(resultCode == RESULT_OK){
				adb = new AccountDatabaseHelper(this);
				plist = adb.getAllPeopleFor(username); 
				adb.close();
			} else if (resultCode == RESULT_CANCELED){
				Toast sorry = Toast.makeText(SetupPeopleActivity.this, 
						"Sorry, we had trouble editing that person! Please try again.", Toast.LENGTH_LONG);
				sorry.show();
			}
		}
	}
	
	/*
	 * Convenience function to setup People list
	 * based upon context and the list of persons
	 * for the current user
	 */
	private void setupPeopleList(Context context, final List<Person> persons){
		
		ArrayAdapter<String> peopleNames = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice);
		for(int i=0; i<persons.size(); i++){
			Person p = persons.get(i);
			
			if(p.getMain()){
				peopleNames.add(String.valueOf(p.getName()) + " (you)");
			} else {
				peopleNames.add(String.valueOf(p.getName()));
			}
		}
		
		peopleList = (ListView) findViewById(R.id.lv_people);
		
		peopleList.setAdapter(peopleNames);
		peopleList.setBackgroundColor(Color.WHITE);
		peopleList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		peopleList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				selectedP = persons.get(position);
			}
			
		});
	}
	
}
