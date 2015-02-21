package com.example.smslf.db;

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.smslf.R;
import com.example.smslf.classes.Coding;
import com.example.smslf.classes.Sms;

public class Staff {

	public int id;
	public String name;
	public String phoneNumber;

	public static ArrayList<Staff> getAllStaffs(Context context)
			throws Exception {
		DataBaseHandler db = new DataBaseHandler(context);
		return db.selectAllStaffs();
	}

	public static void add(Activity activity, Staff staff) throws Exception {
		if (isExist(activity, staff.phoneNumber) == -1)
			insertStaff(activity, staff);
		else
			throw new Exception(activity.getResources().getString(
					R.string.errNumberRegisterdBefore));

		Sms.sendDeliverReport(activity, staff.phoneNumber, Coding.code(
				Info.getSimNumber(activity), Coding.messageType.activation));
	}

	public static void requestLocation(Activity activity, int staffID)
			throws Exception {
		DataBaseHandler db = new DataBaseHandler(activity);

		Sms.sendDeliverReport(activity, db.selectStaff(staffID).phoneNumber,
				Coding.code(Info.getSimNumber(activity),
						Coding.messageType.locationRequest));

		// Sms.send(db.selectStaff(staffID).phoneNumber,
		// Coding.code(Info.getSimNumber(activity),
		// Coding.messageType.locationRequest));
	}

	private static void insertStaff(Context context, Staff staff)
			throws Exception {
		// insert into the db_staffs
		DataBaseHandler db = new DataBaseHandler(context);
		db.insertStaff(staff);
	}

	public static void deleteStaff(Activity activity, Staff staff)
			throws Exception {
		// delete the recode in db_staffs by id
		DataBaseHandler db = new DataBaseHandler(activity);

		if (db.deleteStaff(staff.id) == 0)
			throw new Exception("Delete was not successful for" + staff.name);
		Sms.sendDeliverReport(activity, staff.phoneNumber, Coding.code(
				Info.getSimNumber(activity), Coding.messageType.deactivation));

	}

	public static void deleteStaffs(Activity activity, ArrayList<Staff> staffs,
			int[] selectedIndexs) throws Exception {
		for (int i = 0; i < selectedIndexs.length; i++) {
			if (selectedIndexs[i] == 1)
				deleteStaff(activity, staffs.get(i));
		}
	}

	public static int isExist(Context context, String phoneNumber)
			throws Exception {
		// get staff from db_staff by phoneNumber
		DataBaseHandler db = new DataBaseHandler(context);
		Staff staff = db.selectStaff(phoneNumber);

		// if staff exist return id
		// else return -1
		if (staff != null)
			return staff.id;
		else
			return -1;
	}

}
