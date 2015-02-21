package com.example.smslf.classes;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smslf.R;
import com.example.smslf.db.Staff;

public class ListAdapterDelete extends ArrayAdapter<Staff> {

	Activity activity;

	ArrayList<Staff> staffs = null;
	int[] selectedIndexs;

	public void setSelected(int index) {
		if (selectedIndexs[index] == 0)
			selectedIndexs[index] = 1;
		else
			selectedIndexs[index] = 0;
		notifyDataSetChanged();
	}

	public int[] getSelectedIndexs() {
		return this.selectedIndexs;
	}

	public boolean isNoneSelected() {
		for (int i = 0; i < selectedIndexs.length; i++) {
			if (selectedIndexs[i] == 1)
				return false;
		}
		return true;
	}

	public ListAdapterDelete(Activity activity, ArrayList<Staff> staffs) {
		super(activity, R.layout.item_list_delete, staffs);
		this.activity = activity;
		this.staffs = staffs;
		selectedIndexs = new int[staffs.size()];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.item_list_delete, parent, false);

		}
		((TextView) row.findViewById(R.id.txt_city)).setText(staffs
				.get(position).name);
		if (selectedIndexs[position] == 0)
			row.setBackgroundColor(0x00000000);
		else
			row.setBackgroundColor(Color.rgb(126, 203, 231));

		return row;
	}

}
