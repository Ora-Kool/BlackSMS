package com.etingemabian.blacksms;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.widget.CursorAdapter;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ComposeBlackSMS extends Activity implements OnClickListener,
		OnItemClickListener, OnItemSelectedListener {

	EditText text;
	ImageButton sendButton;
	BroadcastReceiver smsSentReceiver, smsDelieverdReceiver;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	SimpleCursorAdapter mCursorAdapter;
	Context context;
	SENTHelper sentHelper;
	SQLiteDatabase mDB;
	Cursor myCursor;

	// AutoCompleteTextView textView=null;
	private ArrayAdapter<String> adapter;

	// Store contacts values in these array list

	public static ArrayList<String> phoneValueArr = new ArrayList<String>();
	public static ArrayList<String> nameValueArr = new ArrayList<String>();
	EditText number = null;
	String toNumberValue = "";

	private AutoCompleteTextView textView = null;
	private ListView listView;
	List<String> data = new ArrayList<String>();

	// called when the activity is first created

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.composeblacksms);

		// Finding view
		text = (EditText) findViewById(R.id.body);

		textView = (AutoCompleteTextView) findViewById(R.id.number);

		sendButton = (ImageButton) findViewById(R.id.sendButton);
		sendButton.setOnClickListener(this);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				new ArrayList<String>());
		textView.setThreshold(1);

		// set adapter to auto complete text view
		textView.setAdapter(adapter);
		textView.setOnItemClickListener(this);
		textView.setOnItemSelectedListener(this);

		listView = (ListView) findViewById(R.id.listMessages);
		listView.setOnItemClickListener(this);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, data);

		listView.setAdapter(adapter);

		sentHelper = new SENTHelper(this);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	public void onClick(View v) {
		sendBlackSMS();
		String numb;
		numb = textView.getText().toString();
		data.add(numb);
		adapter.notifyDataSetChanged();
		
		
		

	}


	protected void sendBlackSMS() {
		mDB = sentHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		Date now = new Date();
		String instance = new SimpleDateFormat("h:mmaaa, MMM d").format(now);
		
		String phoneNo = textView.getText().toString(); // get the number
		String message = text.getText().toString(); // get the message
		
		cv.put(SENTHelper.SENT_MESSAGE, message);
		cv.put(SENTHelper.SENDER, phoneNo);
		cv.put(SENTHelper.DATE, instance);
		mDB.insert(SENTHelper.TABLE_NAME, null, cv);
		
		

		try {
			byte[] str = message.getBytes();
			BigInteger encoded = new BigInteger(str);
			SmsManager smsManger = SmsManager.getDefault();
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(
					SENT), 0);
			PendingIntent pd = PendingIntent.getBroadcast(this, 0, new Intent(
					DELIVERED), 0);
			smsManger.sendTextMessage(phoneNo, null,
					encoded.toString().concat("0000"), pi, pd);

		} catch (Exception e) {

		}



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChoice(item);
	}

	private boolean MenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		}
		return false;
	}

	private void CreateMenu(Menu menu) {

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	// i don't think this should be here, because when a message arrives its not
	// displayed automatically
	public void onResume() {
		super.onResume();

		mDB = sentHelper.getReadableDatabase();
		String[] arrCol = new String[] {SENTHelper.SENT_MESSAGE,
				SENTHelper.SENDER, SENTHelper.DATE, "_id" };

		myCursor = mDB.query(SENTHelper.TABLE_NAME, arrCol, null, null, null,
				null, null);
		if (myCursor != null)
			myCursor.moveToNext();

		int[] viewIDs = new int[] { R.id.messageSENT,
				R.id.to, R.id.sendingDate };

		ListView listView = (ListView) findViewById(R.id.listMessages);
		
		
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.sentmessages, myCursor, 
				arrCol, viewIDs);
		
		listView.setAdapter(mCursorAdapter);
		
		
		
		//registerReceiver(new BroadcastReceiver() {
			smsSentReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "sms sent",
								Toast.LENGTH_SHORT).show();
	
						text.setText(null);
						textView.setText(null);
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio off",
								Toast.LENGTH_SHORT).show();
						break;
					}
	
				}
			}; //new IntentFilter(SENT));
	
			// ---- when the SMS has being delved---
			//registerReceiver(new BroadcastReceiver() {
			smsDelieverdReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered",
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered",
								Toast.LENGTH_SHORT).show();
						break;
					}
	
				}
			}; //new IntentFilter(DELIVERED));
			
			registerReceiver(smsDelieverdReceiver, new  IntentFilter(DELIVERED));
			registerReceiver(smsSentReceiver, new IntentFilter(SENT));

	}

	@Override
	public void onPause() {
		super.onPause();
		//unregistering the broadcast receiver
		unregisterReceiver(smsSentReceiver);
		unregisterReceiver(smsDelieverdReceiver);
		
	}

	@Override
	public void onStop() {
		super.onStop();
	
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

}
