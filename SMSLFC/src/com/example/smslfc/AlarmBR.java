package com.example.smslfc;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBR extends BroadcastReceiver {
  
	@Override
	public void onReceive(Context context, Intent intent) {

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			return;
		
		if (hour < 8 || hour > 18)
			return;

		if (!Info.getPhoneNumber(context).equals("null")) {
			try {

				// Info.send(context);

				LocationTracker lt = new LocationTracker(context);
				lt.getLocation();

				// SMS.send(Info.getPhoneNumber(context),
				// Coding.code(Location_M.getLocation(context)));
			} catch (Exception e) {
				MainActivity.showToast(e.getMessage());
				e.printStackTrace();
			}

		}

	}

}
