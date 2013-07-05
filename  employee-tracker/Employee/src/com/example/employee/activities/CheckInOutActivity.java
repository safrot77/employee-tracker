package com.example.employee.activities;

import android.app.Activity;
import java.awt.font.*;
import android.os.Bundle;
import android.view.Menu;

import com.example.employee.R;

public class CheckInOutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_in_out, menu);
		return true;
	}

}
