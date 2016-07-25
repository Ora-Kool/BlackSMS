package com.etingemabian.blacksms;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;

public class BlacksmsNotifcation extends Activity{
	int notificationID = 1;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationview);
		
		//look up  the notification manager service
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		//cancel  the notification  that we get
		nm.cancel(0);
		nm.notify("message", 0, null);
		
		
		
		
	}

}
