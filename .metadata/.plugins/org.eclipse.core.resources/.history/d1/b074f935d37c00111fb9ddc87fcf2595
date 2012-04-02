package com.christine.cart;

import com.christine.cart.sqlite.Account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputSearchActivity extends Activity {

	EditText enterPLU;
	Button submitPLU;
	
	Account act;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_plu);
	    
	    Account temp= getIntent().getParcelableExtra("account");
	    if(temp!=null){
	    	act = temp;
	    } else{
	    	throw new RuntimeException("InputSearchActivity, account is null");
	    }
	    	
	    
	    enterPLU = (EditText) findViewById(R.id.enterPLU);
	    submitPLU = (Button) findViewById(R.id.submitPLU);
	    submitPLU.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String pluCode = enterPLU.getText().toString();
				
				Intent searchForPLUCode = new Intent(InputSearchActivity.this, InputDatabaseSearchActivity.class);
				searchForPLUCode.setType("text/plain");
				searchForPLUCode.putExtra("plu",pluCode);
				searchForPLUCode.putExtra("account", act);
				
				startActivity(searchForPLUCode);
			}
		});
	    
	}

}
