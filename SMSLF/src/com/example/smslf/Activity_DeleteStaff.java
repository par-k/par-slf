package com.example.smslf;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.smslf.classes.ListAdapterDelete;
import com.example.smslf.classes.Toast_M;
import com.example.smslf.db.Staff;

public class Activity_DeleteStaff extends ActionBarActivity {

	Activity activity;
	ArrayList<Staff> staffs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_delete_staffs);

		activity = this;
		try {
			staffs = Staff.getAllStaffs(activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView lv = (ListView) findViewById(R.id.lvDeleteStaffs);
		final ListAdapterDelete adapter = new ListAdapterDelete(activity,
				staffs);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				adapter.setSelected(position);
			}
		});

		((Button) findViewById(R.id.btnDelete))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (adapter.isNoneSelected()) {
							Toast_M.show(
									activity.getResources().getString(
											R.string.errSelectAtLeastOne),
									activity);
							return;
						}
						try {
							Staff.deleteStaffs(activity, staffs,
									adapter.getSelectedIndexs());
							Activity_Staffs.refreshListView();
							finish();
						} catch (Exception e) {
							Toast_M.show(e.getMessage(), activity);
							e.printStackTrace();
						}

					}
				});

	}
}
