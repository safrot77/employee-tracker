package com.example.employee.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.employee.MailSenderActivity;
import com.example.employee.R;
import com.example.employee.services.Network;
import com.example.employee.utilities.ActivityUIHelper;
import com.example.employee.utilities.IConstants;

public class LoginActivity extends Activity {

	private EditText mobile;
	private EditText password;
	private Button loginButton;
	private Button register;
	private TextView forgetPassword;
	private Network network = new Network();
	private List<String> employers_names = new ArrayList<String>();
	private List<String> employers_ids = new ArrayList<String>();
	private Drawable error_indicator;
	ProgressDialog dialog;
	private Thread t;
	private String first_name;
	private String last_name;
	private String mobile_num;
	private String imei;
	private int confirmed;
	private String employer_name;
	private String confirm;
	String mobile_number_bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		/*Bundle bundle = getIntent().getExtras();
		mobile_number_bundle = bundle.getString("mobile");*/
		error_indicator = getResources().getDrawable(R.drawable.error);
		int left = 0;
		int top = 0;
		int right = error_indicator.getIntrinsicHeight();
		int bottom = error_indicator.getIntrinsicWidth();
		// draw a red rectangle under the Edit text to display the error message
		// in it.
		error_indicator.setBounds(new Rect(left, top, right, bottom));

		mobile = (EditText) findViewById(R.id.mobile_txt);
		// mobile.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

		password = (EditText) findViewById(R.id.password_txt);
		// password.setTypeface(Typeface.DEFAULT);
		// password.setTransformationMethod(new PasswordTransformationMethod());

		EmptyTextListener textListener = new EmptyTextListener();
		mobile.setOnEditorActionListener(textListener);
		password.setOnEditorActionListener(textListener);

		mobile.setOnFocusChangeListener(textListener);
		password.setOnFocusChangeListener(textListener);

		loginButton = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
//		forgetPassword = (TextView) findViewById(R.id.forgotPassword);

		loginButton.setOnClickListener(new OnClickListener() {

			Toast toast = null;
			private String msg;
			private String mobileText;
			private String passwordText;

			@Override
			public void onClick(View v) {
				mobileText = mobile.getText().toString().trim();
				passwordText = password.getText().toString().trim();
				if (mobileText.equals("") || mobileText == null) {
					msg = getString(R.string.mobile_error);
					toast = Toast.makeText(getApplicationContext(), msg,
							Toast.LENGTH_SHORT);
					toast.show();

				} else if (passwordText.equals("") || passwordText == null) {
					Log.v("-----------------", "----NULL-----");
					msg = getString(R.string.password_error);
					toast = Toast.makeText(getApplicationContext(), msg,
							Toast.LENGTH_SHORT);
					toast.show();

				} else {
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
					} else {
						Log.v("-----------------", "----loginnnn-----");
							showDialog(0);
							t = new Thread() {
								public void run() {
									login(mobileText, passwordText);
								}
							};
							t.start();
					}
				}
			}
		});

		register.setOnClickListener(new OnClickListener() {

			Thread t;

			@Override
			public void onClick(View v) {

				Toast toast;
				String msg;
				if (!Network.haveInternet(getApplicationContext())) {
					Log.v("-----------Internet exception------", "---------");
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
				} else {
					showDialog(0);
					t = new Thread() {
						public void run() {
							getEmployersNames();
						}
					};
					t.start();
				}}


		});

		/*forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ForgetPasswordActivity.class);
				startActivity(intent);
			}
		});*/
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
	private class EmptyTextListener implements OnEditorActionListener,
			OnFocusChangeListener {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_NEXT
					|| actionId == EditorInfo.IME_ACTION_DONE) {
				// Called when user press Next button on the soft keyboard
				if (v.getText().toString().equals(""))
					v.setError(getString(R.string.blank_txt), error_indicator);
			}
			return false;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if (((TextView) v).getText().toString().equals(""))
					((TextView) v).setError(getString(R.string.blank_txt),
							error_indicator);
			}
		}
	}

	protected void getEmployersNames(){

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(IConstants.URL
				+ "users/get_employers.json");
		if (IConstants.AUTH_STATE)
			httpost.setHeader("Authorization", "Basic YmN1c2VyOmJjdXNlcjEyMzQ=");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		try{
			HttpResponse response;
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpEntity resEntity;
			response = httpclient.execute(httpost);
			resEntity = response.getEntity();
			if (resEntity != null) {
				String jsonResponse = EntityUtils.toString(resEntity);
				Log.v("JSONResponse_getReport_timeLine", jsonResponse);
				JSONArray fetchedData = new JSONArray(jsonResponse);
				try {

					for (int count = 0; count < fetchedData.length(); count++) {

						JSONObject employers = fetchedData.getJSONObject(count)
								.getJSONObject("Employer");
						String id = employers.getString("id");
						System.err.println(id);
						String name = employers.getString("employer_name");
						System.err.print("fetched employer  size" + " "
								+ Integer.toString(employers.length()) + "");

						employers_names.add(name);
						employers_ids.add(id);
					}

					for (int i = 0; i < employers_names.size(); i++) {
						Log.v("------name no " + i, employers_names.get(i));
					}
					removeDialog(0);
					Intent intent = new Intent(getApplicationContext(),
							RegisterActivity.class);
					intent.putStringArrayListExtra("employers_names",
							(ArrayList<String>) employers_names);
					intent.putStringArrayListExtra("employers_ids",
							(ArrayList<String>) employers_ids);
					startActivity(intent);
				} catch (JSONException e) {
					removeDialog(0);
					Log.v("fetch null", "null");
					e.printStackTrace();
				}
			} else {
				removeDialog(0);
				runOnUiThread(new Runnable() 
		        {                
		            @Override
		            public void run() 
		            {
		            	String msg = getString(R.string.serverFail_msg);
		            	ActivityUIHelper.showErrorDialog(msg, LoginActivity.this);
		            }
		        });
			}}catch (Exception e) {
				removeDialog(0);
				e.printStackTrace();
			}

	}

	protected void login(String mobileText, String passwordText){

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(IConstants.URL + "users/login.json");
		if (IConstants.AUTH_STATE)
			httpost.setHeader("Authorization", "Basic YmN1c2VyOmJjdXNlcjEyMzQ=");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", mobileText));
		nvps.add(new BasicNameValuePair("password", passwordText));
		try{
			HttpResponse response;
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpEntity resEntity;
			response = httpclient.execute(httpost);
			resEntity = response.getEntity();
			if (resEntity != null) {
				Log.v("-----------------", "----NOT NULL-----");
				String jsonResponse = EntityUtils.toString(resEntity);
				System.err.println(jsonResponse);

				if (jsonResponse.equals("1")) {
					Log.v("-------response----------", "----1-----");
					removeDialog(0);
					Intent myIntent = new Intent(getApplicationContext(),
							MailSenderActivity.class);
					myIntent.putExtra("mobile", mobileText);
					startActivityForResult(myIntent, 0);

					getSharedPreferences(IConstants.PREFS_NAME, MODE_PRIVATE)
					.edit().putString(IConstants.PREF_MOBILE, mobileText)
					.putString(IConstants.PREF_PASSWORD, passwordText)
					.commit();

					finish();
				}else if (jsonResponse.equals("2")){
					removeDialog(0);
					runOnUiThread(new Runnable() 
					{                
						@Override
						public void run() 
						{
							String msg = getString(R.string.notconfirmed);
							ActivityUIHelper.showErrorDialog(msg, LoginActivity.this);
						}
					});
				} else if (jsonResponse.equals("3")){
					removeDialog(0);
					runOnUiThread(new Runnable() 
					{                
						@Override
						public void run() 
						{
							String msg = getString(R.string.wrong_mobile);
							ActivityUIHelper.showErrorDialog(msg, LoginActivity.this);
						}
					});
				}else if (jsonResponse.equals("4")){
					removeDialog(0);
					runOnUiThread(new Runnable() 
					{                
						@Override
						public void run() 
						{
							String msg = getString(R.string.wrong_password);
							ActivityUIHelper.showErrorDialog(msg, LoginActivity.this);
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
						ActivityUIHelper.showErrorDialog(msg, LoginActivity.this);
					}
				});
			}}catch(Exception e){
				removeDialog(0);
				e.printStackTrace();
			}

	}
	
	private void getEmployeeData(String mobile) {
    	Log.v("----mobile get----", mobile);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(IConstants.URL
				+ "users/get_user.json");
		if (IConstants.AUTH_STATE)
			httpost.setHeader("Authorization", "Basic YmN1c2VyOmJjdXNlcjEyMzQ=");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", mobile));
		try{
			HttpResponse response;
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpEntity resEntity;
			response = httpclient.execute(httpost);
			resEntity = response.getEntity();
			if (resEntity != null) {
				String jsonResponse = EntityUtils.toString(resEntity);
				Log.v("JSONResponse_getReport_timeLine", jsonResponse);
				JSONArray fetchedData = new JSONArray(jsonResponse);
				try {

					for (int count = 0; count < fetchedData.length(); count++) {
						JSONObject user = fetchedData.getJSONObject(count)
								.getJSONObject("User");
						first_name = user.getString("first_name");
						last_name = user.getString("last_name");
						mobile_num = user.getString("mobile");
						imei = user.getString("imei");
						confirmed = user.getInt("confirmed");
						System.err.print("fetched employer  size" + " "
								+ Integer.toString(user.length()) + "");
						JSONObject employer = fetchedData.getJSONObject(count)
								.getJSONObject("Employer");
						employer_name = employer.getString("employer_name");
					}
					
					if(confirmed == 1)
						confirm = "Yes";
					else
						confirm = "No";
					
					Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
					intent.putExtra("first_name", first_name);
					intent.putExtra("last_name", last_name);
					intent.putExtra("mobile_num", mobile_num);
					intent.putExtra("imei", imei);
					intent.putExtra("confirm", confirm);
					intent.putExtra("employer_name", employer_name);
					startActivity(intent);

					
					
				} catch (JSONException e) {
					Log.v("fetch null", "null");
					e.printStackTrace();
				}
			} else {/*
				removeDialog(0);
				runOnUiThread(new Runnable() 
		        {                
		            @Override
		            public void run() 
		            {
		            	String msg = getString(R.string.serverFail_msg);
		            	ActivityUIHelper.showErrorDialog(msg, LoginActivity.this);
		            }
		        });
			*/}}catch (Exception e) {
				removeDialog(0);
				e.printStackTrace();
			}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			showAbout();
			return true;
		case R.id.menu_help:
			showHelp();
			return true;
		/*case R.id.menu_settings:
			showSettings();
			return true;	*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showSettings() {

		showDialog(0);
		Thread t = new Thread() {
			public void run() {
				getEmployeeData(mobile_number_bundle);
			}
		};
		t.start();
	}

	private void showHelp() {
		int title = R.string.help;
		int icon = R.drawable.help;
		String message = getString(R.string.help_msg);
		ActivityUIHelper.showAlertDialog(title, icon, message, this);
	}

	private void showAbout() {
		int title = R.string.about;
		int icon = R.drawable.about;
		String message = "\n" + getString(R.string.contactUs) + "\n"
				+ getString(R.string.mail) + "\n\n"
				+ getString(R.string.version) + ": 0.1" + "\n\n"
				+ getString(R.string.copyrights) + "\n"
				+ getString(R.string.companyName) + "\n";
		ActivityUIHelper.showAlertDialog(title, icon, message, this);

	}
	
	
	private InputStream openHttpConnection(String urlStr) {
		InputStream in = null;
		int resCode = -1;
		try {


			URL url = new URL(urlStr);
			URLConnection urlConn = url.openConnection();

			if (!(urlConn instanceof HttpURLConnection)) {
				throw new IOException ("URL is not an Http URL");
			}

			HttpURLConnection httpConn = (HttpURLConnection)urlConn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect(); 
			resCode = httpConn.getResponseCode(); 


			if (resCode == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

}
