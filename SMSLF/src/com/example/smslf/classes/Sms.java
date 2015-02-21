package com.example.smslf.classes;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.example.smslf.BroadcastSentSMS;
import com.example.smslf.BroadcastDeliverSMS;

public class Sms {

//	public static void send(String number, String text) {
//
//		SmsManager smsManager = SmsManager.getDefault();
//		smsManager.sendTextMessage(number, null, text, null, null);
//	}

	public static void sendDeliverReport(Context context, String number, String text) {

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		Intent sentIntent = new Intent(SENT);
		sentIntent.putExtra("type", "smslf");

		Intent deliverdIntent = new Intent(DELIVERED);
		deliverdIntent.putExtra("type", "smslf");

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				sentIntent, 0);
		BroadcastSentSMS sentBR = new BroadcastSentSMS();
		context.registerReceiver(sentBR, new IntentFilter(SENT));

		PendingIntent deliverdPI = PendingIntent.getBroadcast(context, 0,
				deliverdIntent, 0);
		BroadcastDeliverSMS deliverdBR = new BroadcastDeliverSMS();
		context.registerReceiver(deliverdBR, new IntentFilter(DELIVERED));

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, text, sentPI, deliverdPI);

	}
	
	

}
