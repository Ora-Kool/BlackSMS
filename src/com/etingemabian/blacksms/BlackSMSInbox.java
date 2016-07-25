package com.etingemabian.blacksms;

import java.math.BigInteger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BlackSMSInbox extends Activity implements OnItemClickListener {

	TextView tv_message, tv_number;
	ListView inboxMessages;
	Context context;
	String feedback = "NOT A BlackSMS MESSAGE!!!";
	SQLiteDatabase mDB;
	DbHelper mHelper;
	Cursor mCursor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blacksmsinbox);
		context = this;
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		mHelper = new DbHelper(this);
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(0);
		
	}
		
		@SuppressWarnings("deprecation")
		@Override
		public void onResume(){
			super.onResume();
		
		// arrayColumn is the column name in my cursor where i am getting the
		// data
		// here i am displaying SMSsender Number i.e address and SMSbody i.e
		// body
		//String[] arrayColumns = new String[] { "address", "body" };
		mDB = mHelper.getReadableDatabase();
		String[] arrCol = new String[]{"_id", DbHelper.ADDRESS, DbHelper.BODY, DbHelper.DATE};
		
		mCursor = this.mDB.query(DbHelper.TABLE_NAME, arrCol, null, null, null, null, null);
		if(mCursor != null)
			mCursor.moveToFirst();


		// arrayViewID contains the id of textViews
		// more views can be added as per required
		// textViewSMSSender is connected to "address" of the arrayColumns
		// textViewMessageBody is connected to "body" of the arrayColumns
		
		int[] arrayViewIDs = new int[] { R.id.textViewSMSSender,
				R.id.textViewMessageBody, R.id.textDate };

		String[] header = new String[]{DbHelper.ADDRESS, DbHelper.BODY, DbHelper.DATE};
		
		


			
		
		ListView smsListView = (ListView) findViewById(R.id.inboxlist);
		


		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.listview_each_item, mCursor, header, arrayViewIDs);
		
		smsListView.setAdapter(adapter);
		
		mCursor.requery();
		adapter.notifyDataSetChanged();
		// here i handle the click on a list view item
		smsListView.setOnItemClickListener(this);
		
	
}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// when you click on ListView item, onItemClick is called
		// with the position and view of the item which is clicked
		// the position parameter can be use to get the index of the clicked
		// item
		
		
		
		TextView textViewSMSSender = (TextView) view
				.findViewById(R.id.textViewSMSSender);
		TextView textVuewSMSBody = (TextView) view
				.findViewById(R.id.textViewMessageBody);
		TextView txtDate = (TextView)view.findViewById(R.id.textDate);
		
		String smsSender = textViewSMSSender.getText().toString();
		String smsBody = textVuewSMSBody.getText().toString();
		String smsDate = txtDate.getText().toString();
		
		String newSMS = method(smsBody);
		//String date = textViewCurrentDate.getText().toString();
		
		//Resolving  the contact  name from the  contacts
        Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(smsSender));
        Cursor c = context.getContentResolver().query(lookupUri, new String[]
        		{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        
        c.moveToFirst();
		String displayName = c.getString(0);
		String contactName = displayName;
		
		// Dialog to show the selected SMS
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle("SMS From: " + contactName);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		try {
			
			
			BigInteger bigsms = new BigInteger(newSMS);
			String sms = new String(bigsms.toByteArray());
			dialog.setMessage(sms.toString()+ "\n"+smsDate);
			
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
				
				@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							return;
						}

				
					});
			dialog.show();

			
		} catch (Exception e) {
			
			Toast.makeText(this, feedback, Toast.LENGTH_LONG).show();
		}
		
	}
	public static String method(String str){
		if(str.contains("0000")){
			return str.substring(0, str.length()-4);
		
	}
		return str.substring(0, str.length()-4);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);
	}


	private boolean MenuChoice(MenuItem item) {
		switch(item.getItemId()){
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

}