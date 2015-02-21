package com.example.smslf;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class BroadcastSentSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (!intent.getExtras().getString("type").equals("smslf"))
			return;
		int resultCode = getResultCode();
		switch (resultCode) {

		case Activity.RESULT_OK:
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.messageRequestSent), Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.messageRequestNotSent), Toast.LENGTH_LONG)
					.show();
			break;

		}

	}

}
