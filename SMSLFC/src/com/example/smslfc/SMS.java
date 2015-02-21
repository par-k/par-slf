package com.example.smslfc;

import android.telephony.SmsManager;

public class SMS {

	public static void send(String number, String text) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, text, null, null);
	}

}
   