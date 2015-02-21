package com.example.smslf.db;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;

import com.example.smslf.classes.Coding;
import com.example.smslf.classes.GetAddress;
import com.example.smslf.classes.SolarCalendar;

public class Location_M {
	public int id;
	public int id_staff;
	public int date_day;
	public int date_month;
	public int date_year;
	public String message_time;
	public String lat;
	public String lon;
	public String country = null;
	public String city = null;
	public String street = null;

	public static ArrayList<Location_M> getStaffLocations(Context context,
			int staffID) throws Exception {
		// get locations from db_locations by id_staff
		DataBaseHandler db = new DataBaseHandler(context);
		return db.selectLocations(staffID);
	}

	public static void insertLocation(Context context, int staffID,
			String message) throws Exception {
		// get lat and lon from message and insert into db_locations by id_staff

		// id_staff
		Location_M location = new Location_M();
		location.id_staff = staffID;

		Calendar calendar = Calendar.getInstance();

		// date and time
		SolarCalendar sc = new SolarCalendar(context);
		location.date_day = sc.get_date();
		location.date_month = sc.get_month();
		location.date_year = sc.get_year();

		String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":";
		if (calendar.get(Calendar.MINUTE) < 10)
			time += "0" + calendar.get(Calendar.MINUTE);
		else
			time += calendar.get(Calendar.MINUTE);
		location.message_time = time;

		Location_M temp = Coding.rebuildCoordination(Coding.decode(message));
		location.lat = temp.lat;
		location.lon = temp.lon;

		new GetAddress(context, location).execute();
	}

	public String getInfo() {

		return city + "\n\n" + street + "\n" + message_time + " - " + date_year
				+ "/" + date_month + "/" + date_day;
	}

}
