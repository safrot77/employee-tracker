package com.example.employee.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;

import com.example.employee.R;

public class NotificationActivity extends Activity {
	
	private static final int NOTIFY_ME_ID = 1987;
	private NotificationManager notificationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		
		notificationManager = (NotificationManager) 
				  getSystemService(NOTIFICATION_SERVICE); 
		
		
		
		
//		initializeUIElements();
	}
 
    private void initializeUIElements() {
 
        Notification note = new Notification(R.drawable.help,
                "Welcome to MyDoople.com", System.currentTimeMillis());
        PendingIntent i = PendingIntent.getActivity(this, 0, new Intent(
                this, NotificationActivity.class), Notification.FLAG_ONGOING_EVENT);
 
        note.setLatestEventInfo(this, "MyDoople.com", "An Android Portal for Development",
                i);
        // note.number = ++count;
        note.flags |= Notification.FLAG_ONGOING_EVENT;
 
        notificationManager.notify(NOTIFY_ME_ID, note);
 
    }
	
	
	public void createNotification(View view) {
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(this, NotificationReceiverActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		String longText = "A PendingIntent is a token that you give to another application (e.g. Notification Manager, Alarm Manager or other 3rd party applications), which allows this other application to use the permissions of your application to execute a predefined piece of code.To perform a broadcast via a pending intent so get a PendingIntent via PendingIntent.getBroadcast(). To perform an activity via an pending intent you receive the activity via PendingIntent.getActivity().";
		// Build notification
		// Actions are just fake
		/*Notification n = new Notification.Builder(this)
				.setContentTitle("New mail from " + "test@gmail.com")
				.setContentText("Subject")
				.setSmallIcon(R.drawable.help)
				.setContentIntent(pIntent).build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Hide the notification after its selected

		notificationManager.notify(0, n);*/

	}
	
	
	
	public void notifyUser(){
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(this)
			    .setSmallIcon(R.drawable.help)
			    .setContentTitle("My notification")
			    .setContentText("Hello World!");
		
		
		Intent resultIntent = new Intent(this, NotificationActivity.class);
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    this,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		mBuilder.setContentIntent(resultPendingIntent);
		
		
		
		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = 
		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

}
