package com.example.smslf;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastDeliverSMS extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.messageRequestDeliverd),
					Toast.LENGTH_SHORT).show();
			break;
		case Activity.RESULT_CANCELED:
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.messageRequestNotDeliverd),
					Toast.LENGTH_SHORT).show();
			break;
		}

	}
}