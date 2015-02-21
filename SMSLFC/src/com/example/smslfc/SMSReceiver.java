package com.example.smslfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	// private final String DEBUG_TAG = getClass().getSimpleName().toString();
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	// private Context mContext;

	// Retrieve SMS
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {

			String phoneNumber = "", message = "";

			SmsMessage[] msgs = getMessagesFromIntent(intent);
			if (msgs != null) {
				for (int i = 0; i < msgs.length; i++) {
					phoneNumber = msgs[i].getOriginatingAddress();
					// contactId = ContactsUtils.getContactId(mContext, address,
					// "address");
					message += msgs[i].getMessageBody().toString();    
				}
			}  
			if (phoneNumber.substring(0, 3).equals("+98"))  
				phoneNumber = "0" 
						+ phoneNumber.substring(3, phoneNumber.length());   
			if (!Coding.isVerifiedSMS(message))
				return;

			if (Coding.decode(message).equals(phoneNumber)) {
				if (message.substring(5, 7).equals("ac")) {   
					Info.setPhoneNumber(context, phoneNumber);

					//Alarm_M alarm = new Alarm_M();
					try {
						//commented in version 1.1 because it is not accurate
						//alarm.set(context);
						
					} catch (Exception e) {
						MainActivity.showToast(e.getMessage());
						e.printStackTrace();
					}
					SMS.send(Info.getPhoneNumber(context), context
							.getResources().getString(R.string.numberAdd));

				} else if (message.substring(5, 7).equals("de")) {

					SMS.send(Info.getPhoneNumber(context), context
							.getResources().getString(R.string.numberRemove));
					Info.setPhoneNumber(context, "null");

				} else if (message.substring(5, 7).equals("re")) {

					if (!Info.getPhoneNumber(context).equals("null")) {
						try {
							// SMS.send(Info.getPhoneNumber(context), Coding
							// .code(Location_M.getLocation(context)));

							LocationTracker lt = new LocationTracker(context);
							lt.getLocation();

							//Info.send(context);
						} catch (Exception e) {
							MainActivity.showToast(e.getMessage());
							e.printStackTrace();
						}

					}
				}
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

	/**
	 * The notification is the icon and associated expanded entry in the status
	 * bar.
	 */
	protected void showNotification(int contactId, String message) {
		// Display notification...
	}
}
