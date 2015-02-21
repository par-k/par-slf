package com.example.smslf.classes;

import java.util.Random;

import com.example.smslf.db.Location_M;

public class Coding {

	public static enum messageType {
		activation, deactivation, locationRequest
	};

	public static boolean isVerifiedLocationSMS(String message) {
		// if code from message is equal with phoneNumber return true

		if (message.length() < 23)
			return false;
		if (!message.substring(0, 5).equals("smslf"))
			return false;

		message = message.substring(5);
		int random = Integer.valueOf(message.substring(0, 2));
		message = message.substring(2);
		String checkPart = message.substring(random, random + 2);

		String originalCodedMessage = message.substring(0, random)
				+ message.substring(random + 2);
		String originalDecodedMessage = dechangeString(originalCodedMessage);

		int digitOne = Integer.valueOf(originalDecodedMessage.substring(
				originalDecodedMessage.length() - 3,
				originalDecodedMessage.length() - 2));
		int digitTwo = Integer.valueOf(originalDecodedMessage.substring(
				originalDecodedMessage.length() - 2,
				originalDecodedMessage.length() - 1));
		int digitThree = Integer.valueOf(originalDecodedMessage.substring(
				originalDecodedMessage.length() - 1,
				originalDecodedMessage.length()));

		String strCheck = String.valueOf(digitOne + digitTwo + digitThree);
		if (strCheck.length() == 1)
			strCheck = "0" + strCheck;

		if (checkPart.equals(String.valueOf(strCheck)))
			return true;
		return false;
	}

	public static String code(String str, messageType type) {
		// str lenght must be less than 20

		int digitOne = Integer.valueOf(str.substring(str.length() - 3,
				str.length() - 2));
		int digitTwo = Integer.valueOf(str.substring(str.length() - 2,
				str.length() - 1));
		int digitThree = Integer.valueOf(str.substring(str.length() - 1,
				str.length()));

		String strCheck = String.valueOf(digitOne + digitTwo + digitThree);
		if (strCheck.length() == 1)
			strCheck = "0" + strCheck;

		String codedStr = changeString(str);

		String randomCode = "";
		int random = (new Random()).nextInt(str.length());
		if (random < 10)
			randomCode += "0" + random;
		else
			randomCode += random;
		String arrayCode = "";
		char[] strCharArray = codedStr.toCharArray();
		for (int i = 0; i < strCharArray.length; i++) {
			if (i == random) {
				arrayCode += strCheck;
				arrayCode += String.valueOf(strCharArray[i]);
			} else
				arrayCode += String.valueOf(strCharArray[i]);
		}

		switch (type) {
		case activation:
			return "smslfac" + randomCode + arrayCode;

		case deactivation:

			return "smslfde" + randomCode + arrayCode;

		case locationRequest:

			return "smslfre" + randomCode + arrayCode;

		}
		return "";

	}

	public static String decode(String str) {

		// remove header
		String body = str.substring(5, str.length());
		int random = Integer.valueOf(body.substring(0, 2));

		// get coordination and check value
		String cAndC = dechangeString(body.substring(2, body.length()));

		// remove check value
		String firstPart = cAndC.substring(0, random);
		String secondPart = cAndC.substring(random + 2, cAndC.length());

		return firstPart + secondPart;
	}

	public static Location_M rebuildCoordination(String str) {
		Location_M location = new Location_M();
		location.lat = str.substring(0, str.length() / 2);
		location.lon = str.substring(str.length() / 2, str.length());

		location.lat = location.lat.substring(0, 2) + "."
				+ location.lat.substring(2, location.lat.length());
		location.lon = location.lon.substring(0, 2) + "."
				+ location.lon.substring(2, location.lon.length());

		return location;
	}

	public static String codeSerialNumber(String serialNumber) {
		return changeString(serialNumber);
	}

	private static String changeString(String str) {
		char[] array = str.toCharArray();
		char[] reusltArray = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			reusltArray[i] = changeCharacter(array[i]);
		}
		return String.valueOf(reusltArray);
	}

	private static String dechangeString(String str) {
		char[] array = str.toCharArray();
		char[] reusltArray = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			reusltArray[i] = deChangeCharacter(array[i]);
		}
		return String.valueOf(reusltArray);
	}

	private static char changeCharacter(char c) {

		if (c == '0')
			return '9';
		else if (c == '1')
			return '6';
		else if (c == '2')
			return '5';
		else if (c == '3')
			return '8';
		else if (c == '4')
			return '7';
		else if (c == '5')
			return '0';
		else if (c == '6')
			return '1';
		else if (c == '7')
			return '3';
		else if (c == '8')
			return '4';
		else if (c == '9')
			return '2';

		return 'n';
	}

	private static char deChangeCharacter(char c) {
		if (c == '9')
			return '0';
		else if (c == '6')
			return '1';
		else if (c == '5')
			return '2';
		else if (c == '8')
			return '3';
		else if (c == '7')
			return '4';
		else if (c == '0')
			return '5';
		else if (c == '1')
			return '6';
		else if (c == '3')
			return '7';
		else if (c == '4')
			return '8';
		else if (c == '2')
			return '9';
		return 'n';
	}

}
