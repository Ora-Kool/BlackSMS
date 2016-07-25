package com.etingemabian.blacksms;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BlackSMSmyChat extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox);
		
//		TextView no = (TextView)findViewById(R.id.No);
//		TextView message = (TextView)findViewById(R.id.message);
//		
//		Bundle incoming = this.getIntent().getExtras();
//		no.setText("Sent to: "+incoming.getString("no"));
//		message.setText(incoming.getString("message"));
//		
//		Toast.makeText(this, "message sent", Toast.LENGTH_LONG).show();
		//create  Sent box uri
		Uri sentURI = Uri.parse("content://sms/sent");
		
		//List required colms
		String[] reqCols = new String[]{"_id", "address", "body"};
		
		//get content resolver object, which will deal with content provider
		ContentResolver cr = getContentResolver();
		
		//Fetch Sent SMS Message  from built-in Content provider
		Cursor c = cr.query(sentURI, reqCols, null, null, null);
		
		//Attached cursor with  adapter and display  in list view
	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_each_item, c, new String[]{ "address", "body"},
				new int[]{R.id.textViewSMSSender, R.id.textViewMessageBody});
		
		ListView sentList = (ListView)findViewById(R.id.sentlist);
		sentList.setAdapter(adapter);
		
		
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
