package com.example.smslfc;

import android.content.Context;
import android.widget.Toast;

public class Toast_M {
	public static void show(String msg, Context context) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
