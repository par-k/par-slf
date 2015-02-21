package com.example.smslf.classes;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smslf.R;
import com.example.smslf.db.Staff;

public class ListAdapterStaffs extends ArrayAdapter<Staff> {

	Activity activity;

	ArrayList<Staff> staffs = null;

	public ListAdapterStaffs(Activity activity, ArrayList<Staff> staffs) {
		super(activity, R.layout.item_list_staffs, staffs);
		this.activity = activity;
		this.staffs = staffs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.item_list_staffs, parent, false);

		}
		((TextView) row.findViewById(R.id.txt_city)).setText(staffs
				.get(position).name);

		return row;
	}

}
