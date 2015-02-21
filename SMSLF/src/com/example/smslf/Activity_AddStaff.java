package com.example.smslf;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.smslf.classes.Toast_M;
import com.example.smslf.db.Staff;

public class Activity_AddStaff extends ActionBarActivity {

	Activity activity;
	EditText edtCellphoneNumber;
	EditText edtRepeatCellphoneNumber;
	EditText edtName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_staff);

		activity = this;

		edtName = (EditText) findViewById(R.id.edtName);
		edtCellphoneNumber = (EditText) findViewById(R.id.edtCelphoneNumber);
		edtRepeatCellphoneNumber = (EditText) findViewById(R.id.edtRepeatCellphoneNumber);

		((Button) findViewById(R.id.btnRegisterName))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (checkEditTexts()) {
							Staff s = new Staff();
							s.name = edtName.getText().toString();
							s.phoneNumber = edtCellphoneNumber.getText()
									.toString();
							try {
								Staff.add(activity, s);
								Activity_Staffs.refreshListView();
								finish();
							} catch (Exception e) {
								Toast_M.show(e.getMessage(), activity);
								e.printStackTrace();
							}
						}
					}
				});

	}

	public boolean checkEditTexts() {
		if (edtName.getText().toString().equals("")) {
			Toast_M.show(
					activity.getResources().getString(R.string.errNameEmpty),
					activity);
			return false;
		}
		if (edtCellphoneNumber.getText().toString().equals("")) {
			Toast_M.show(
					activity.getResources().getString(
							R.string.errCellphoneNumberEmpty), activity);
			return false;
		}
		if (edtRepeatCellphoneNumber.getText().toString().equals("")) {
			Toast_M.show(
					activity.getResources().getString(
							R.string.errRepeatCellphoneNumberEmpty), activity);
			return false;
		}
		if (!edtCellphoneNumber.getText().toString()
				.equals(edtRepeatCellphoneNumber.getText().toString())) {
			Toast_M.show(activity.getResources()
					.getString(R.string.errNotEqual), activity);
			return false;
		}
		if (edtCellphoneNumber.getText().toString().length() != 11) {
			Toast_M.show(
					activity.getResources()
							.getString(R.string.errPhoneNumber11), activity);
			return false;
		}
		return true;
	}

}
