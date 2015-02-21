package com.example.smslfc;

import android.content.Context;
import android.location.Location;

public class Info {

	public static String getPhoneNumber(Context context) {

		try {
			DataBaseHandler db = new DataBaseHandler(context);
			return db.selectPhoneNumber();
		} catch (Exception e) {
			MainActivity.showToast(e.getMessage());
		}
		return null;

	}

	public static void setPhoneNumber(Context context, String phoneNumber) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			db.updatePhoneNumber(phoneNumber);
		} catch (Exception e) {
			MainActivity.showToast(e.getMessage());
		}
	}

	public static void send(Context context) throws Exception {
		DataBaseHandler db = new DataBaseHandler(context);
		Location location = db.selectLocation();

		String lat = String.valueOf(location.getLatitude());
		if (lat.length() > 10)
			lat = lat.substring(0, 10);
		if (lat.length() == 9)
			lat += "0";

		String lon = String.valueOf(location.getLongitude());
		if (lon.length() > 10)
			lon = lon.substring(0, 10);
		if (lon.length() == 9)
			lon += "0";
		lat = lat.replace(".", "");
		lon = lon.replace(".", "");

		SMS.send(Info.getPhoneNumber(context), Coding.code(lat + lon));

	}
}
