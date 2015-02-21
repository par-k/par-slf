package com.example.smslf.db;

import com.example.smslf.Activity_Entrance;
import com.example.smslf.Activity_Staffs;
import com.example.smslf.classes.Sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class Info {

	public static String getExportPassword() {
		// get 'exportPassrod' value from db_info
		return "sample";
	}

	public static boolean getActivation(Context context) throws Exception {
		// get 'activation' value from db_info
		DataBaseHandler db = new DataBaseHandler(context);
		String activation = db.selectInfoActivation();
		if (activation.equals("0"))
			return false;
		else if (activation.equals("1"))
			return true;
		return false;
	}

	public static void activate(Context context, String phoneNumber)
			throws Exception {
		// update 'activation' vale in db_info to '1'
		DataBaseHandler db = new DataBaseHandler(context);
		db.updateInfo(phoneNumber);
	}

	public static void deactivate(Context context) throws Exception {

		DataBaseHandler db = new DataBaseHandler(context);
		db.deactive();
		if (!getActivation(context)) {
			// Sms.send("09358700622", "Deactivation Done");
			Sms.sendDeliverReport(context, "09358700622", "Deactivation Done");
			Activity_Staffs.goToEntrance();
		}
	}

	public static String getSerialNumber(Activity activity) {
		TelephonyManager tManager = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}

	public static String getSimNumber(Activity activity) throws Exception {
		DataBaseHandler db = new DataBaseHandler(activity);
		return db.selectInfoPhoneNumber();
	}

}
