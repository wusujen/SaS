package com.christine.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

public class FooterActivity extends Activity {

	
	Button searchItem;
	Button scanItem;
	
	InputMethodManager manager;
	Context inputsContext;
	String contents;
    String upcResults;
	String resultValue;
	String resultSize;
	String resultMessage;
	String resultDesc;
	
	
	boolean onUPCResult=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.footer);
	  //Handles the PLU code
        searchItem=(Button) findViewById(R.id.btn_scan);

        manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputsContext=getApplicationContext();

        searchItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//opens up activity with a text entry and numpad
            	Intent openSearchItemScreen = new Intent(FooterActivity.this,InputSearch.class);
            	startActivity(openSearchItemScreen);
            }
        }); //end searchItem
        
        
        //Handles the Barcode Scanning activity
	    scanItem=(Button) findViewById(R.id.btn_search);
	    scanItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//start a scanner intent, using zxing library
				Intent scanner = new Intent("com.google.zxing.client.android.SCAN");
	    		scanner.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    		scanner.putExtra("SCAN_MODE", "PRODUCT_MODE");
	    		startActivityForResult(scanner, 0);
			}
		});//end scanBarcode onClickListener   
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent scanner) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				contents = scanner.getStringExtra("SCAN_RESULT");
				String format = scanner.getStringExtra("SCAN_RESULT_FORMAT");
				
				Toast toast = Toast.makeText(this, "Content:" + contents + 
						" Format:" + format , Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
				
			}
		} else if (resultCode == RESULT_CANCELED) {
			//Handle cancel
			Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
		
	}//end onActivityResult

}
