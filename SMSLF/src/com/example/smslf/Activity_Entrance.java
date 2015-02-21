package com.example.smslf;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.smslf.classes.Activation;
import com.example.smslf.classes.Toast_M;
import com.example.smslf.db.Info;

public class Activity_Entrance extends ActionBarActivity implements
		OnClickListener {

	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_entrance);

		activity = this;

		// get 'activated' value from 'db_info'
		try {
			if (Info.getActivation(activity)) {
				Intent intent = new Intent(getApplicationContext(),
						Activity_Staffs.class);
				startActivity(intent);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((Button) findViewById(R.id.btnRequestCode)).setOnClickListener(this);
		((Button) findViewById(R.id.btnRegister)).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRequestCode:
			try {
				Activation.sendRequest(activity);
			} catch (JSONException e) {
				Toast_M.show(e.getMessage(), activity);
				e.printStackTrace();
			}
			break;

		case R.id.btnRegister:
			try {

				String phoneNumber = ((EditText) findViewById(R.id.edtPhoneNumber))
						.getText().toString();
				String code = ((EditText) findViewById(R.id.edtCode)).getText()
						.toString();

				if (code.equals(""))
					Toast_M.show(
							activity.getResources().getString(
									R.string.errCodeEmpty), activity);
				else if (phoneNumber.equals(""))
					Toast_M.show(
							activity.getResources().getString(
									R.string.errPhoneNumberEmpty), activity);
				else if (phoneNumber.length() != 11)
					Toast_M.show(
							activity.getResources().getString(
									R.string.errPhoneNumber11), activity);

				else if (!Activation.isVerified(activity, code))
					Toast_M.show(
							activity.getResources().getString(
									R.string.errWrongCode), activity);
				else {

					Info.activate(activity, phoneNumber);
					Intent intent = new Intent(getApplicationContext(),
							Activity_Staffs.class);
					startActivity(intent);
				}

			} catch (Exception e) {
				Toast_M.show(e.getMessage(), activity);
				e.printStackTrace();
			}
			break;

		}
	}
}
