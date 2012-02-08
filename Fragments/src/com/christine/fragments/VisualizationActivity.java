package com.christine.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class VisualizationActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.visualization_activity_layout);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String s = extras.getString("value");
			TextView view = (TextView) findViewById(R.id.menu_text);
			view.setText(s);
		}
	}

}
