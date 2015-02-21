package com.example.smslfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

public class AlertDialog_M {

	public static void show(Context context, String message) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		try {
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
			TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
		} catch (Exception e) {
			String s = e.getMessage();
		}

	}
}
