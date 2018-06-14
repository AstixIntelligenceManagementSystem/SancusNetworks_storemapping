package com.example.android.sancusNetworks_storemapping.gcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.android.sancusNetworks_storemapping.DBAdapterLtFoods;
import com.example.android.sancusNetworks_storemapping.R;
import com.example.android.sancusNetworks_storemapping.StorelistActivity;


import java.util.StringTokenizer;

public class NotificationActivity extends Activity 
{

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	
	public TableLayout tbl1_dyntable_For_Notification; 
	public TableRow tr1PG2;
	DBAdapterLtFoods dbengine = new DBAdapterLtFoods(this);
	public int ComeFromActivity=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		
		
		if( getIntent().getExtras() != null)
		{
			
			String str = getIntent().getStringExtra("msg");
			String comeFrom = getIntent().getStringExtra("comeFrom");
			ComeFromActivity=Integer.parseInt(comeFrom);
			System.out.println("Sunil Get Notification in Notification Activity :"+str);
			//TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			//String imei = tManager.getDeviceId();
					
			if (str != null) 
			{
				
				/*long syncTIMESTAMP = System.currentTimeMillis();
				Date dateobj = new Date(syncTIMESTAMP);
				SimpleDateFormat df = new SimpleDateFormat(
						"dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
				String Noti_ReadDateTime = df.format(dateobj);
				
				StringTokenizer tokens = new StringTokenizer(String.valueOf(str), "^");
				String MsgSendingTime=tokens.nextToken().trim();
				String NotificationMessage=tokens.nextToken().trim();
				dbengine.open();
				dbengine.inserttblNotificationMstr(imei,NotificationMessage,MsgSendingTime,1,1,
						Noti_ReadDateTime,3);
				dbengine.close();*/
			}
		}
		
		/*public long inserttblNotificationMstr(String IMEI,String Noti_text,String Noti_DateTime,int Noti_ReadStatus,int Noti_NewOld,
				String Noti_ReadDateTime,int Noti_outStat)*/
		tbl1_dyntable_For_Notification = (TableLayout) findViewById(R.id.dyntable_For_Notification);
		
		dbengine.open();
		int SerialNo=dbengine.countNoRowIntblNotificationMstr();
		System.out.println("Sunil LastNitificationrList SerialNo : "+SerialNo);
		//String LastOrderDetail[]=dbengine.fetchAllDataFromtblFirstOrderDetailsOnLastVisitDetailsActivity(storeID);
		String LastNitificationrList[]=dbengine.LastNitificationrListDB();
		//String LastNitificationrList[]={"10-06-2015_Hi ","11-06-2015_Bye "};
		
		System.out.println("Sunil LastNitificationrList : "+LastNitificationrList.length);
		dbengine.close();
		
		LayoutInflater inflater = getLayoutInflater();
		
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    double x = Math.pow(dm.widthPixels/dm.xdpi,2);
	    double y = Math.pow(dm.heightPixels/dm.ydpi,2);
	    double screenInches = Math.sqrt(x+y);
		
		
		for (int current = 0; current <= (LastNitificationrList.length - 1); current++) 
		{

			final TableRow row = (TableRow)inflater.inflate(R.layout.table_notification, tbl1_dyntable_For_Notification, false);
			
			TextView tv1 = (TextView)row.findViewById(R.id.tvDate);
			TextView tv2 = (TextView)row.findViewById(R.id.tvMessage);
			
			
			if(screenInches>6.5)
			{
				tv1.setTextSize(14);
				tv2.setTextSize(14);
				
			}
			else
			{
				
			}
			
			//System.out.println("Abhinav Raj LTDdet[current]:"+LTDdet[current]);
			StringTokenizer tokens = new StringTokenizer(String.valueOf(LastNitificationrList[current]), "_");
			
			tv1.setText("  "+tokens.nextToken().trim());
			
			tv2.setText("  "+tokens.nextToken().trim());
			
		
			tbl1_dyntable_For_Notification.addView(row);
		}
    	
		
		Button backbutton=(Button)findViewById(R.id.backbutton);
		backbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent submitStoreIntent = new Intent(NotificationActivity.this, StorelistActivity.class);
				startActivity(submitStoreIntent);
				finish();
				
			}
		});
	
	}
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent submitStoreIntent = new Intent(NotificationActivity.this, StorelistActivity.class);
		startActivity(submitStoreIntent);
		finish();	
    }
	
	

}
