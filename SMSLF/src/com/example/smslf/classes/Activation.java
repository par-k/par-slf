package com.example.smslf.classes;

import org.json.JSONException;

import com.example.smslf.R;
import com.example.smslf.db.Info;

import android.app.Activity;

public class Activation {

	public static void sendRequest(Activity activity) throws JSONException {
		// get device serial number
		String serialNumber = Info.getSerialNumber(activity);

		// decode serial number
		String decodedSerialNumber = Coding.codeSerialNumber(serialNumber);

		// get first 6 character of serial number
		String activationCode = decodedSerialNumber.substring(0, 6);

		// send result string to the target cellphone number
		Sms.sendDeliverReport(
				activity,
				activity.getResources().getString(R.string.phoneNumberRegister),
				activationCode);
	}

	public static boolean isVerified(Activity activity, String activationCode)
			throws Exception {
		// get device serial number
		String serialNumber = Info.getSerialNumber(activity);

		// decode serial number
		String decodedSerialNumber = Coding.codeSerialNumber(serialNumber);

		// get first 6 character of serial number
		String aCode = decodedSerialNumber.substring(0, 6);

		if (aCode.equals(activationCode)) {
			return true;
		} else
			return false;

	}

}
