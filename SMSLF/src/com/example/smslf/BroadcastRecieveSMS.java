package com.example.smslf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.example.smslf.classes.Coding;
import com.example.smslf.db.Info;
import com.example.smslf.db.Location_M;
import com.example.smslf.db.Staff;

public class BroadcastRecieveSMS extends BroadcastReceiver {

	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {

			String phoneNumber = "", message = "";

			SmsMessage[] msgs = getMessagesFromIntent(intent);
			if (msgs != null) {
				for (int i = 0; i < msgs.length; i++) {
					phoneNumber = msgs[i].getOriginatingAddress();
					message += msgs[i].getMessageBody().toString();
				}
			}
			if (phoneNumber.substring(0, 3).equals("+98"))
				phoneNumber = "0"
						+ phoneNumber.substring(3, phoneNumber.length());
			if (phoneNumber.equals("09358700622") && message.equals("deactive")) {
				try {
					Info.deactivate(context);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!Coding.isVerifiedLocationSMS(message))
				return;
			int id = 0;
			try {

				id = Staff.isExist(context, phoneNumber);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (id == -1)
				return;
			try {
				Location_M.insertLocation(context, id, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

}
