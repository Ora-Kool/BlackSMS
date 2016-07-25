package com.etingemabian.blacksms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver{

	
	
		public static final String SMS_EXTRA_NAME = "pdus";
		public static final String TABLE_NAME = "SMSINBOX";
		public static final String ADDRESS = "address";
	    public static final String PERSON = "person";
	    public static final String DATE = "date";
	    public static final String READ = "read";
	    public static final String STATUS = "status";
	    public static final String TYPE = "type";
	    public static final String BODY = "body";
	    public static final String SEEN = "seen";
	    
	    public static final int MESSAGE_TYPE_INBOX = 1;
	    public static final int MESSAGE_TYPE_SENT = 2;
	    
	    public static final int MESSAGE_IS_NOT_READ = 0;
	    public static final int MESSAGE_IS_READ = 1;
	    
	    public static final int MESSAGE_IS_NOT_SEEN = 0;
	    public static final int MESSAGE_IS_SEEN = 1;
	
		
	    Context context;

	    SQLiteDatabase mDB;
	    DbHelper mHelper;
	    int notificationID = 1;
	    Cursor mCursor;

		@Override
		public void onReceive( Context context, Intent intent ) 
		{
			// Get SMS map from Intent
	        Bundle extras = intent.getExtras();
	        
	        String messages = "";
	        
	        if ( extras != null )
	        {
	            // Get received SMS array
	            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
	            
	            // Get ContentResolver object for pushing encrypted SMS to incoming folder
	           // ContentResolver contentResolver = context.getContentResolver();
	            
			for (int i = 0; i < smsExtra.length; ++i) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

				String body = sms.getMessageBody().toString();
				String name = sms.getOriginatingAddress();

				messages += "SMS from " + name + " :\n";
				messages += body + "\n";

				if (body.contains("0000")) {
					
					Date now = new Date();
					String instance = new SimpleDateFormat("h:mmaaa, MMM d").format(now);
					mHelper = new DbHelper(context);
					ContentValues cv = new ContentValues();
					mDB = mHelper.getWritableDatabase();
					cv.put(DbHelper.ADDRESS, name);
					cv.put(DbHelper.BODY, body.toString());
					cv.put(DATE, instance);
					mDB.insert(DbHelper.TABLE_NAME, null, cv);
					
					
					Notification(context, "BLACKSMS");
					this.abortBroadcast();
					

				}else{
					
					
				}
	               
	                
	               // Resolving  the contact  name from the  contacts
	                Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(name));
	                Cursor c = context.getContentResolver().query(lookupUri, new String[]
	                		{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
	                
//	                try{
//	                	c.moveToNext();
//	                	String displayName = c.getString(0);
//	                	String ContactName = displayName;
//	                	//Toast.makeText(context, ContactName, Toast.LENGTH_LONG).show();
//	                	AlertDialog dialog = new AlertDialog.Builder(context).create();
//	                	dialog.setTitle("Message from: "+ ContactName);
//	                	dialog.setIcon(android.R.drawable.ic_dialog_alert);
//	                	dialog.setButton(DialogInterface.BUTTON_POSITIVE, "reply", 
//	                			new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								dialog.dismiss();
//								return;
//							}
//						});
//	                	dialog.show();
//	                	
//	                	
//	                }
//	                catch(Exception e){
//	                	e.printStackTrace();
//	                }
//	                finally{
//	                	c.close();
//	                	
//	                }
	                // Here you can add any your code to work with incoming SMS
	                // I added encrypting of all received SMS 
	                
	               // putSmsToDatabase( contentResolver, sms );
	            }
	            
	            // Display SMS message
	           // Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
	            
	        }
	        
	        // WARNING!!! 
	        // If you uncomment next line then received SMS will not be put to incoming.
	        // Be careful!
	         //this.abortBroadcast(); 
		}
	    
	    
	    public void Notification(Context context, String message){
	    	//setting the notification  title
	    	String strTitle = context.getString(R.string.notificationtitle);
	    	
	    	
	    	
	    	Intent intent = new Intent(context, BlackSMSInbox.class);
	    	
	    	
	    	
	    	//open notification class
	    	PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    	//create notification  using NotificationCompat.Builder
	    	Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    	NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
	    	//set icon
	    	.setSmallIcon(R.drawable.inbox)
	    	//set  Ticker message
	    	.setTicker(message)
	    	//set title
	    	.setContentTitle(context.getString(R.string.notificationtitle))
	    	//set text
	    	.setContentText(message)
	    	//add an action button below
	    	
	    	//set pendingintent into notification
	    	.setContentIntent(pIntent)
	    	//vibration
	    	.setVibrate(new long[]{100,250, 100, 500})
	    	//set sound on notification
	    	.setSound(alarmSound)
	    	//dismiss  notification
	    	.setAutoCancel(true);
	    	
	    	
	    	//create notification manager
	    	@SuppressWarnings("static-access")
			NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
	    	//build notification with notification manager
	    	
	    	
	    	nm.notify(0, builder.build());
	    	
	    }
	    
	    
	    public void onResume(){
	    	
	    }
		
//		
}