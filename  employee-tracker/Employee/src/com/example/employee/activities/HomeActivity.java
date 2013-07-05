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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.employee.MailSenderActivity;
import com.example.employee.R;
import com.example.employee.R.id;
import com.example.employee.R.layout;
import com.example.employee.R.menu;
import com.example.employee.R.string;
import com.example.employee.services.Network;
import com.example.employee.utilities.ActivityUIHelper;
import com.example.employee.utilities.IConstants;

public class HomeActivity extends Activity {

	private String mobile;
	private String password;
	ProgressDialog dialog;
	private Thread t;
	public static final String PREFS_NAME = "MyPrefsFile";
	private static final String PREF_MOBILE = "mobile";
	private static final String PREF_PASSWORD = "password";
	Button tryAgain;
	Intent intent = null;
	Toast toast = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		mobile = pref.getString(PREF_MOBILE, null);
		password = pref.getString(PREF_PASSWORD, null);
		Log.v("===welcome===", "-----");

		check();
		tryAgain = (Button) findViewById(R.id.tryagain);
		tryAgain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				check();
			}
		});

	}

	private void check() {
		if (!Network.haveInternet(getApplicationContext())) {
			Log.v("-----------Internet exception------",
					"---------");
			String msg = getString(R.string.pingFail_msg);
			toast = Toast.makeText(getApplicationContext(), msg,
					Toast.LENGTH_LONG);
			/*intent = new Intent(HomeActivity.this,
					LoginActivity.class);*/
			toast.show();
//			startActivity(intent);
		}else if (mobile == null || password == null) {
				intent = new Intent(HomeActivity.this,
						LoginActivity.class);
				startActivity(intent);	
				finish();
		} else {
			showDialog(0);
			t = new Thread() {
				public void run() {
					login(mobile, password);
				}
			};
			t.start();
		}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
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
				String jsonResponse = EntityUtils.toString(resEntity);
				System.err.println(jsonResponse);

				if (jsonResponse.equals("1")) {
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
							ActivityUIHelper.showErrorDialog(msg, HomeActivity.this);
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
							ActivityUIHelper.showErrorDialog(msg, HomeActivity.this);
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
							ActivityUIHelper.showErrorDialog(msg, HomeActivity.this);
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
						ActivityUIHelper.showErrorDialog(msg, HomeActivity.this);
					}
				});
			}}catch(Exception e){
				removeDialog(0);
				e.printStackTrace();
			}

	}

}
