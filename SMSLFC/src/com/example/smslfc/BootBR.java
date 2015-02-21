package com.example.smslfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBR extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		//Alarm_M alarm = new Alarm_M();
		try {
			//alarm.set(context);
		} catch (Exception e) {
			MainActivity.showToast(e.getMessage());
			e.printStackTrace();
		}
	}

}
