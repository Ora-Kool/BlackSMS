package com.etingemabian.blacksms;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener{
	ImageButton btnComposeSMS, sent;
	BlackSMSInbox bsi;
	

	//This is call when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //---intent to filter for SMS messages received--
        IntentFilter filter;
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        registerReceiver(new SMSReceiver(), filter);
      
      

       btnComposeSMS = (ImageButton)findViewById(R.id.btnComposeSMS);
        btnComposeSMS.setOnClickListener(this);
      ImageButton inbox =(ImageButton)findViewById(R.id.btninbox);
        inbox.setOnClickListener(this);

        
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        
//        //get  the current display info
//        WindowManager wm = getWindowManager();
//        Display d = wm.getDefaultDisplay();
//        if(d.getWidth() > d.getHeight()){
//        	//-- landscape mode
//        	Fra
//        }
        
    
    }
 
    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int pos,
			long id) {
//		
		}		
			@Override
			public void onClick(View v) {
				if(v == btnComposeSMS){
				Intent compose = new Intent(v.getContext(), ComposeBlackSMS.class);
				startActivity(compose);
				}
				else if( v.getId() == R.id.btninbox){
					
					Intent inbox = new Intent(v.getContext(), BlackSMSInbox.class);
					startActivity(inbox);
//					
				
		}
//				else if(v == sent){
//					Intent sentBox = new Intent(v.getContext(), BlackSMSmyChat.class);
//					startActivity(sentBox);
//				}
				
	}

		    @Override
		    public boolean onCreateOptionsMenu(Menu menu) {
		        // Inflate the menu; this adds items to the action bar if it is present.
		        getMenuInflater().inflate(R.menu.main, menu);
		        return true;
		    }

			
}
				
	
			
		
    	
    	
   

