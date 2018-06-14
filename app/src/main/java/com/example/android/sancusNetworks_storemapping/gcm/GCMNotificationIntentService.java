package com.example.android.sancusNetworks_storemapping.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;


import com.example.android.sancusNetworks_storemapping.DBAdapterLtFoods;
import com.example.android.sancusNetworks_storemapping.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class GCMNotificationIntentService extends IntentService 
{
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;
	DBAdapterLtFoods dbengine = new DBAdapterLtFoods(this);

	public GCMNotificationIntentService() 
	{
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent)
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty())
		{
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
			{
				sendNotification("Send error: " + extras.toString());
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
			{
				sendNotification("Deleted messages on server: "+ extras.toString());
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType))
			{
				try
				{
				if(!extras.get(ApplicationConstants.MSG_KEY).toString().equals("") || !extras.get(ApplicationConstants.MSG_KEY).toString().equals("Null"))
				{
					sendNotification(extras.get(ApplicationConstants.MSG_KEY).toString());
					System.out.println("Sunil Recieve MSG :"+extras.get(ApplicationConstants.MSG_KEY));
					String str=extras.get(ApplicationConstants.MSG_KEY).toString();
					String imei="";
					try
					{
						TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						imei = tManager.getDeviceId();
					}
					catch (SecurityException e)
					{

					}

					
					
					
					long syncTIMESTAMP = System.currentTimeMillis();
					Date dateobj = new Date(syncTIMESTAMP);
					SimpleDateFormat df = new SimpleDateFormat(
							"dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
					String Noti_ReadDateTime = df.format(dateobj);
					
					StringTokenizer tokens = new StringTokenizer(String.valueOf(str), "^");
					String MsgSendingTime=tokens.nextToken().trim();
					String NotificationMessage=tokens.nextToken().trim();
					int MsgServerID=Integer.parseInt(tokens.nextToken().trim());
					
					dbengine.open();
					int SerialNo=dbengine.countNoRowIntblNotificationMstr();
					if(SerialNo>=10)
					{
						dbengine.deletetblNotificationMstrOneRow(1);
					}
					else
					{
					SerialNo=SerialNo+1;
					}
					dbengine.inserttblNotificationMstr(SerialNo,imei,NotificationMessage,MsgSendingTime,1,1,
							Noti_ReadDateTime,0,MsgServerID);
					dbengine.close();
				}
				}
				catch(Exception e)
				{
					
				}
				
				
			        
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	public void sendNotification(String msg) {
	        Intent resultIntent = new Intent(this, NotificationActivity.class);
	       // Intent resultIntent = new Intent(this, HomeActivity.class);
	        resultIntent.putExtra("msg", msg);
	        resultIntent.putExtra("comeFrom", "0");
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
	                resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        System.out.println("Sunil Recieve MSG notification :"+msg);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("Alert")
	                .setContentText("You've received new message.")
	                .setSmallIcon(R.drawable.p_logo);
	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        
	        mNotifyBuilder.setDefaults(defaults);
	        // Set the content for Notification 
	        mNotifyBuilder.setContentText("New message from Server :"+msg);
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	       // startActivity(resultIntent);
	}
}
