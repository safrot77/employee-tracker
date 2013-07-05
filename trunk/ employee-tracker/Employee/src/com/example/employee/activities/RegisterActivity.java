package com.example.employee.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.employee.R;
import com.example.employee.services.Network;
import com.example.employee.utilities.ActivityUIHelper;
import com.example.employee.utilities.IConstants;

public class RegisterActivity extends Activity {

	EditText first_name, last_name, mobile_num, email, password, confirm_password;
	String first_name_txt, last_name_txt, mobile_num_txt, email_txt, password_txt, confirm_password_txt;
	private Spinner employer_names_spinner;
	Button register;
	private Network network = new Network();
	private ArrayList<String> employers_names;
	private ArrayList<String> employers_ids;
	private String employer_no;
	private String imei;
	private Drawable error_indicator;
	ProgressDialog dialog;
	private Thread t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		error_indicator = getResources().getDrawable(R.drawable.error);
		
		int left = 0;
		int top = 0;
		int right = error_indicator.getIntrinsicHeight();
		int bottom = error_indicator.getIntrinsicWidth();
		// draw a red rectangle under the Edit text to display the error message in it.
		error_indicator.setBounds(new Rect(left, top, right, bottom));
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			employers_names = extras.getStringArrayList("employers_names");
			employers_ids = extras.getStringArrayList("employers_ids");
		}
		
		for(int i=0; i<employers_names.size(); i++){
			Log.v("------register name no ---------"+i,	 employers_names.get(i));
		}
		
		first_name = (EditText) findViewById(R.id.first_name);
		last_name = (EditText) findViewById(R.id.last_name);
		mobile_num = (EditText) findViewById(R.id.mobile);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);
		employer_names_spinner = (Spinner) findViewById(R.id.employer);
		
		/*first_name.setText("ss");
		last_name.setText("ss");
		mobile_num.setText("12345678");
		email.setText("ss@ss.com");
		password.setText("ss");
		confirm_password.setText("ss");*/
		
		EmptyTextListener textListener = new EmptyTextListener();
		first_name.setOnEditorActionListener(textListener);
		last_name.setOnEditorActionListener(textListener);
		mobile_num.setOnEditorActionListener(textListener);
		password.setOnEditorActionListener(textListener);
		confirm_password.setOnEditorActionListener(textListener);
		
		first_name.setOnFocusChangeListener(textListener);
		last_name.setOnFocusChangeListener(textListener);
		mobile_num.setOnFocusChangeListener(textListener);
		password.setOnFocusChangeListener(textListener);
		confirm_password.setOnFocusChangeListener(textListener);
		
		
		@SuppressWarnings("rawtypes")
		ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
				android.R.layout.simple_dropdown_item_1line, employers_names);

		employer_names_spinner.setAdapter(spinnerArrayAdapter);
		employer_names_spinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						Object item = parent.getItemAtPosition(pos);
						employer_no = employers_ids.get(employers_names
								.indexOf(item.toString()));
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {

			private String phone;

			@Override
			public void onClick(View v) {
				Toast toast;
				String msg;
				first_name_txt = first_name.getText().toString().trim();
				last_name_txt = last_name.getText().toString();
				mobile_num_txt = mobile_num.getText().toString();
				email_txt = email.getText().toString();
				password_txt = password.getText().toString();
				confirm_password_txt = confirm_password.getText().toString();

				if (first_name_txt.equals("") || first_name_txt == null) {
					msg = getString(R.string.first_name_error);
					toast = Toast.makeText(getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
					toast.show();
				} else if (last_name_txt.equals("")|| last_name_txt == null) {
					msg = getString(R.string.last_name_error);
					toast = Toast.makeText(getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
					toast.show();
				} else if (mobile_num_txt.equals("") || mobile_num_txt == null) {
					msg = getString(R.string.mobile_error);
					toast = Toast.makeText(getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
					toast.show();
				} else if (password_txt.equals("")|| password_txt == null) {
					msg = getString(R.string.password_error);
					toast = Toast.makeText(getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
					toast.show();	
				} else if (confirm_password_txt.equals("") || confirm_password_txt == null) {
					msg = getString(R.string.confirm_password_error);
					toast = Toast.makeText(getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
					toast.show();	
				} else if (!password_txt.equals(confirm_password_txt)) {
					msg = getString(R.string.password_mismatch_error);
					toast = Toast.makeText(getApplicationContext(),
							msg, Toast.LENGTH_SHORT);
					toast.show();	
				} else {
						TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						imei = tm.getDeviceId();
						phone = tm.getLine1Number();
						if(imei == null)
							imei = imei+"";
						Log.v("====imei====", imei+"");
						Log.v("====num====", phone+"");
						if (!Network.haveInternet(getApplicationContext())) {
							Log.v("-----------Internet exception------",
									"---------");
							msg = getString(R.string.pingFail_msg);
							toast = Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_LONG);
							toast.show();
						/*} else if (!network.ping()) {
							Log.v("-----------N/W exception------", "---------");
							msg = getString(R.string.serverFail_msg);
							toast = Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_LONG);
							toast.show();*/
						}else{
							showDialog(0);
							t = new Thread() {
								public void run() {
									register(first_name_txt,last_name_txt,email_txt,mobile_num_txt, password_txt,imei, employer_no);
								}
							};
							t.start();
						}
				}}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage(getString(R.string.please_wait_while));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}
	
	private class EmptyTextListener implements OnEditorActionListener,OnFocusChangeListener {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_NEXT || actionId ==EditorInfo.IME_ACTION_DONE) {
				// Called when user press Next button on the soft keyboard
				if (v.getText().toString().equals(""))
					v.setError(getString(R.string.blank_txt), error_indicator);
			}
			return false;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(!hasFocus){
				if (((TextView) v).getText().toString().equals(""))
					((TextView) v).setError(getString(R.string.blank_txt), error_indicator);
			}
		}
	}
	protected void register(String first_name_txt, String last_name_txt, String email_txt, String mobile_num_txt, String password_txt, String imei, String employer_no2){

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(IConstants.URL
					+ "users/register.json");
			if (IConstants.AUTH_STATE)
				httpost.setHeader("Authorization",
						"Basic YmN1c2VyOmJjdXNlcjEyMzQ=");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("first_name", first_name_txt));
			nvps.add(new BasicNameValuePair("last_name", last_name_txt));
			nvps.add(new BasicNameValuePair("email", email_txt));
			nvps.add(new BasicNameValuePair("mobile", mobile_num_txt));
			nvps.add(new BasicNameValuePair("password", password_txt));
			nvps.add(new BasicNameValuePair("imei", imei));
			nvps.add(new BasicNameValuePair("employer_id", employer_no2));

			HttpResponse response;
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpEntity resEntity;
			response = httpclient.execute(httpost);
			resEntity = response.getEntity();
			if (resEntity != null) {
				String jsonResponse = EntityUtils.toString(resEntity);
				System.err.println(jsonResponse);

				if (jsonResponse.equals("1")) {
					removeDialog(0);
					Intent myIntent = new Intent(getApplicationContext(),
							LoginActivity.class);
					myIntent.putExtra("mobile", mobile_num_txt);
					startActivityForResult(myIntent, 0);
					finish();
				} else if(jsonResponse.equals("2")){
					removeDialog(0);
					runOnUiThread(new Runnable() 
			        {                
			            @Override
			            public void run() 
			            {
			            	String msg = getString(R.string.unique_mobile);
			            	ActivityUIHelper.showErrorDialog(msg, RegisterActivity.this);
			            }
			        });
				} else if(jsonResponse.equals("0")){
					removeDialog(0);
					runOnUiThread(new Runnable() 
					{                
						@Override
						public void run() 
						{
							String msg = getString(R.string.not_registered);
							ActivityUIHelper.showErrorDialog(msg, RegisterActivity.this);
						}
					});
				}
			} else {
				removeDialog(0);
				runOnUiThread(new Runnable() 
		        {                
		            @Override
		            public void run() 
		            {
		            	String msg = getString(R.string.serverFail_msg);
		            	ActivityUIHelper.showErrorDialog(msg, RegisterActivity.this);
		            }
		        });
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
