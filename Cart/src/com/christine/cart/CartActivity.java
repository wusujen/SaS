package com.christine.cart;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends FooterActivity {
	ArrayList<String> results;
	TextView outputText;
	Button viewItemList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.data);
        ViewGroup.inflate(CartActivity.this, R.layout.cart, vg);
        
        outputText = (TextView) findViewById(R.id.outputText);
        
        Intent dbReturnResults = getIntent();
        if(dbReturnResults != null){
        	results = dbReturnResults.getStringArrayListExtra("results");
        	if(results != null){
        		for(int i=0; i<results.size(); i++){
        			String temp = (String) results.get(i);
        			outputText.append("\n" + temp);
        		}
        	}
        	else{
        		Toast noMatch=Toast.makeText(inputsContext, "There was no match in the DB",Toast.LENGTH_SHORT);
        		noMatch.setGravity(Gravity.TOP|Gravity.LEFT,0,150);
        		noMatch.show();
        	}
        }
        
        viewItemList = (Button) findViewById(R.id.btn_itemlist);
        viewItemList.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent showItemList = new Intent(CartActivity.this, ItemListActivity.class);
				startActivity(showItemList);
			}
		});   
    }
}