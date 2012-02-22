package com.christine.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ItemListActivity extends Activity {

	Button cart;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.item_list);
	   
	    cart = (Button) findViewById(R.id.btn_cart);
	    cart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent backToCart = new Intent(ItemListActivity.this,CartActivity.class);
				startActivity(backToCart);
			}
		});
	}

}
