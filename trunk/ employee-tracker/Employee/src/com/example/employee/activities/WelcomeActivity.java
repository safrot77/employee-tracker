package com.example.employee.activities;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.employee.R;
import com.example.employee.services.Network;

public class WelcomeActivity extends Activity {

	private Network network = new Network();
	private Timer timer;
	private ImageView myImageView;
	private Drawable img1Drawable;
	// private Drawable img2Drawable;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);

		myImageView = (ImageView) findViewById(R.id.image);
//		img1Drawable = getResources().getDrawable(R.drawable.bg);
//		myImageView.setImageDrawable(img1Drawable);
		timer = new Timer();
		timer.schedule(clearImg, 1000);
	}

	TimerTask clearImg = new TimerTask() {

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				private Thread t;

				@Override
				public void run() {
					timer.cancel();
					myImageView.setImageDrawable(null);
					Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
