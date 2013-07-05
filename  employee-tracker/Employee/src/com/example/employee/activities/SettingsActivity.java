package com.example.employee.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.employee.R;

public class SettingsActivity extends Activity {
	
	TextView fname, lname, mobile, imei, employer, confirmed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		fname = (TextView) findViewById(R.id.first_name_txt);
		lname = (TextView) findViewById(R.id.last_name_txt);
		mobile = (TextView) findViewById(R.id.mobile_txt);
		imei = (TextView) findViewById(R.id.imei_txt);
		confirmed = (TextView) findViewById(R.id.confirmed_txt);
		employer = (TextView) findViewById(R.id.employer_txt);
		
		Bundle bundle = getIntent().getExtras();
		
		fname.setText(bundle.getString("first_name"));
		lname.setText(bundle.getString("last_name"));
		mobile.setText(bundle.getString("mobile_num"));
		imei.setText(bundle.getString("imei"));
		confirmed.setText(bundle.getString("confirm"));
		employer.setText(bundle.getString("employer_name"));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
