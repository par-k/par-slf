package com.example.smslfc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm_M {

	AlarmManager alarmMgr;
	PendingIntent alarmIntent;

	public void set(Context context) throws Exception {

		/*alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmBR.class);  
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), AlarmManager.INTERVAL_HALF_HOUR,
				alarmIntent);
*/
	}

	public void cancel() {
		alarmMgr.cancel(alarmIntent);
	}

}
