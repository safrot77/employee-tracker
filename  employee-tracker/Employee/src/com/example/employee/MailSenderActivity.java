package com.example.employee;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employee.activities.LoginActivity;
import com.example.employee.activities.SettingsActivity;
import com.example.employee.utilities.ActivityUIHelper;
import com.example.employee.utilities.IConstants;

public class MailSenderActivity extends Activity implements LocationListener{
	LocationManager locationManager ;
	String provider;
	Double longitude;
	Double latitude;
	private String mobile;
	private String id;
	private String first_name;
	private String last_name;
	private String mobile_num;
	private String imei;
	private int confirmed;
	private String employer_name;
	private String confirm;


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	Bundle bundle = getIntent().getExtras();
    	mobile = bundle.getString("mobile");
    	//        getEmployeeData(mobile);

    	final TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);

    	// Getting reference to TextView tv_latitude
    	final TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);
    	//date:dd/mmm/yyyy,time:HH:mm 
    	String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


    	final TextView tvDate  = (TextView)findViewById(R.id.Date);
    	// textView is the TextView view that should display it
    	tvDate.setText(currentDateTimeString);

    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
    	String currentDate = sdf.format(new Date());
    	final TextView tvDate1  = (TextView)findViewById(R.id.x);
    	// textView is the TextView view that should display it
    	tvDate1.setText(currentDate);


    	SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
    	String currentTime = sd.format(new Date());
    	final TextView tvTime  = (TextView)findViewById(R.id.y);
    	// textView is the TextView view that should display it
    	tvTime.setText(currentTime);


    	TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
    	String number = tm.getLine1Number();

    	final   TextView tvNumber = (TextView)findViewById(R.id.PhoneNumber);
    	tvNumber.setText(number);
    	// Getting LocationManager object
    	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);        

    	// Creating an empty criteria object
    	Criteria criteria = new Criteria();

    	// Getting the name of the provider that meets the criteria
    	String provider = locationManager.getBestProvider(criteria, false);


    	if(provider!=null && !provider.equals("")){

    		// Get the location from the given provider 
    		Location location = locationManager.getLastKnownLocation(provider);

    		locationManager.requestLocationUpdates(provider, 20000, 1, this);


    		if(location!=null)
    			onLocationChanged(location);
    		/*   String doublelong= Double.toString(longitude);
                String doublelat= Double.toString(latitude);
            tvLongitude.setText(doublelong);
            tvLatitude.setText(doublelat);*/
    		else
    			Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

    	}else{
    		Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
    	}










    	final Button send = (Button) this.findViewById(R.id.send);
    	send.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			// TODO Auto-generated method stub

    			//Att,long:x,lati:y,date:dd/mmm/yyyy,time:HH:mm,Phone:961z

    			try {   
    				// GMailSender sender = new GMailSender("chadihazzoury@gmail.com", "Beast#14");
    				//                	GMailSender sender = new GMailSender("cits.sender@gmail.com", "cits12345678");
    				GMailSender sender = new GMailSender("ime.sender@gmail.com", "ime123456");

    				sender.sendMail("Att,long:"+ tvLongitude.getText().toString()+",lati:"+tvLatitude.getText().toString()+",date:"+tvDate1.getText().toString()+",time:"+tvTime.getText().toString()+",Phone:"+tvNumber.getText().toString(),
    						//"This is the Subject",   
    						//"bla bla ",
    						//http://maps.google.com/?q=latitude,longitude
    						//"http://maps.google.com/?q="+ tvLatitude.getText().toString()","+tvLongitude.getText().toString(),
    						//"this is the latitude : " +latitude + " this is the longitude: "+longitude,
    						"this is the latitude : " + tvLatitude.getText().toString() + " this is the longitude: "+ tvLongitude.getText().toString() +"\n http://maps.google.com/?q="+tvLatitude.getText().toString()+","+tvLongitude.getText().toString() +"\n Time : "+tvDate.getText().toString() +"\n Phone Number: "+tvNumber.getText().toString(),
    						//tvLongitude.getText().toString(),   
    						/*"cits.receive@gmail.com"*/
    						"ime.receive@gmail.com",
    						/*"cits.receive@gmail.com"*/
    						"ime.receive@gmail.com"
    						);   
    			} catch (Exception e) {   
    				Log.e("SendMail", e.getMessage(), e);   
    			} 

    		}
    	});

    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			ProgressDialog dialog = new ProgressDialog(this);
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
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.option_menu, menu);
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
		case R.id.menu_settings:
			showSettings();
			return true;
		case R.id.menu_logout:
			showDialog(0);
			Thread t = new Thread() {
				public void run() {
					logout();
				}
			};
			t.start();
		
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
					
					Intent intent = new Intent(MailSenderActivity.this, SettingsActivity.class);
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
    
    
    private void logout() {

    	HttpClient httpclient = new DefaultHttpClient();
//    	HttpPost httpost = new HttpPost(IConstants.URL + "users/log_out.json");
    	HttpGet httpost = new HttpGet(IConstants.URL + "users/log_out.json"+"&mobile="+mobile);
    	/*if (IConstants.AUTH_STATE)
    		httpost.setHeader("Authorization", "Basic YmN1c2VyOmJjdXNlcjEyMzQ=");*/
//    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    	Log.v("--------mobile---------", mobile);
//    	nvps.add(new BasicNameValuePair("mobile", mobile));
    	try{
    		HttpResponse response;
//    		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    		HttpEntity resEntity;
    		response = httpclient.execute(httpost);
    		resEntity = response.getEntity();
    		if (resEntity != null) {
    			Log.v("-----------------", "----NOT NULL-----");
    			String jsonResponse = EntityUtils.toString(resEntity);
    			System.err.println(jsonResponse);

    			if (jsonResponse.equals("true")) {
    				Log.v("-------response----------", "----true-----");
    				removeDialog(0);
    				getSharedPreferences(IConstants.PREFS_NAME, MODE_PRIVATE)
    				.edit().clear().commit();
    				Intent myIntent = new Intent(getApplicationContext(),
    						LoginActivity.class);
    				startActivityForResult(myIntent, 0);
    				finish();

    			} else {
    				removeDialog(0);
    				runOnUiThread(new Runnable() 
    				{                
    					@Override
    					public void run() 
    					{
    						String msg = getString(R.string.serverFail_msg);
    						ActivityUIHelper.showErrorDialog(msg, MailSenderActivity.this);
    					}
    				});
    			}}}catch(Exception e){
    				removeDialog(0);
    				e.printStackTrace();
    			}

    }




	private void showSettings() {
		showDialog(0);
		Thread t = new Thread() {
			public void run() {
				getEmployeeData(mobile);
			}
		};
		t.start();
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
    
    private void showHelp() {
		int title = R.string.help;
		int icon = R.drawable.help;
		String message = getString(R.string.help_msg);
		ActivityUIHelper.showAlertDialog(title, icon, message, this);
	}
    
    @Override
    public void onBackPressed() {
    	finish();
    	super.onBackPressed();
    }

    
    @Override
	public void onLocationChanged(Location location) {
    	// Getting reference to TextView tv_longitude
    	TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);
    	
    	// Getting reference to TextView tv_latitude
    	TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);
		
		// Setting Current Longitude
		tvLongitude.setText("Longitude: "+location.getLongitude());
		 //longitude =location.getLongitude(); 
		// Setting Current Latitude
		tvLatitude.setText("Latitude: "+location.getLatitude());
		// latitude = location.getLatitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}
   
}
