////package com.example.mytourguide.DataObjects;
package com.example.smslf.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.smslf.R;
import com.example.smslf.classes.Toast_M;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.SyncStateContract.Constants;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

public class DataBaseHandler extends SQLiteOpenHelper {

	private final Context context;
	private static String DB_NAME;
	private static String DB_PATH;

	public static enum Tables {
		info, locations, staffs
	};

	private SQLiteDatabase myDataBase;

	public DataBaseHandler(Context context) throws IOException {

		super(context, DB_NAME, null, 1);
		this.context = context;
		PackageManager pm = context.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		DB_NAME = "smslf.db3";
		DB_PATH = "/data/data/" + packageInfo.packageName + "/databases/";
		createDataBase();

	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {

		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.

			this.getReadableDatabase();
			// this.getWritableDatabase();
			// this.getWritableDatabase();
			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {

			File dir = new File(context.getApplicationInfo().dataDir
					+ "/databases");
			dir.mkdirs();

			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		try {
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLException e) {
			throw e;
		}

	}

	public synchronized void closeDataBase() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void Delete() throws IOException {
		context.deleteDatabase(DB_NAME);
		createDataBase();
	}

	public void BackupDatabase() throws IOException {
		// Open your local db as the input stream
		String inFileName = DB_PATH + DB_NAME;
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);

		String outFileName = Environment.getExternalStorageDirectory()
				+ "/Android/" + DB_NAME;
		// Open the empty db as the output stream
		OutputStream output = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		// Close the streams
		output.flush();
		output.close();
		fis.close();
		Toast_M.show("Backup is ready", context);
	}

	private Cursor select(String variables, Tables table_name,
			String whereClause) {
		openDataBase();
		String selectQuery;
		if (whereClause != null)
			selectQuery = "SELECT " + variables + " FROM "
					+ table_name.toString() + " WHERE " + whereClause
					+ " ORDER BY id ASC";
		else
			selectQuery = "SELECT " + variables + " FROM "
					+ table_name.toString() + " ORDER BY id ASC";

		return myDataBase.rawQuery(selectQuery, null);
	}

	private Cursor selectOrderDesc(String variables, Tables table_name,
			String whereClause) {
		openDataBase();
		String selectQuery;
		if (whereClause != null)
			selectQuery = "SELECT " + variables + " FROM "
					+ table_name.toString() + " WHERE " + whereClause
					+ " ORDER BY id DESC";
		else
			selectQuery = "SELECT " + variables + " FROM "
					+ table_name.toString() + " ORDER BY id DESC";

		return myDataBase.rawQuery(selectQuery, null);
	}

	public String selectInfoActivation() throws Exception {

		Cursor cursor = select("*", Tables.info, "id=1");
		String result = null;
		if (cursor.moveToFirst()) {
			result = cursor.getString(1);
		}
		closeDataBase();
		return result;
	}

	public String selectInfoPhoneNumber() throws Exception {

		Cursor cursor = select("*", Tables.info, "id=1");
		String result = null;
		if (cursor.moveToFirst()) {
			result = cursor.getString(2);
		}
		closeDataBase();
		return result;
	}

	public void updateInfo(String phoneNumber) throws IOException {

		openDataBase();
		ContentValues values = new ContentValues();

		values.put("activation", "1");
		values.put("phone_number", phoneNumber);
		try {

			myDataBase.update(Tables.info.toString(), values, "id=1", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDataBase();

	}

	public void deactive() throws IOException {

		openDataBase();
		ContentValues values = new ContentValues();

		values.put("activation", "0");

		try {

			myDataBase.update(Tables.info.toString(), values, "id=1", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDataBase();

	}

	public ArrayList<Location_M> selectLocations(int staffID) throws Exception {

		ArrayList<Location_M> list = new ArrayList<Location_M>();
		Cursor cursor = selectOrderDesc("*", Tables.locations, "id_staff="
				+ staffID);
		if (cursor.moveToFirst()) {
			do {

				Location_M location = new Location_M();
				location.id = cursor.getInt(0);
				location.id_staff = cursor.getInt(1);
				location.date_day = cursor.getInt(2);
				location.date_month = cursor.getInt(3);
				location.date_year = cursor.getInt(4);
				location.message_time = cursor.getString(5);
				location.lat = cursor.getString(6);
				location.lon = cursor.getString(7);
				location.country = cursor.getString(8);
				location.city = cursor.getString(9);
				location.street = cursor.getString(10);

				list.add(location);
			} while (cursor.moveToNext());
		}
		closeDataBase();
		return list;

	}

	public ArrayList<Location_M> selectLocationsMissed(Context context)
			throws Exception {

		ArrayList<Location_M> list = new ArrayList<Location_M>();
		Cursor cursor = select(
				"*",
				Tables.locations,
				"city='"
						+ context.getResources().getString(
								R.string.errGoogleServiceDisabledDestination)
						+ "'");
		if (cursor.moveToFirst()) {
			do {

				Location_M location = new Location_M();
				location.id = cursor.getInt(0);
				location.id_staff = cursor.getInt(1);
				location.date_day = cursor.getInt(2);
				location.date_month = cursor.getInt(3);
				location.date_year = cursor.getInt(4);
				location.message_time = cursor.getString(5);
				location.lat = cursor.getString(6);
				location.lon = cursor.getString(7);
				location.country = cursor.getString(8);
				location.city = cursor.getString(9);
				location.street = cursor.getString(10);

				list.add(location);
			} while (cursor.moveToNext());
		}
		closeDataBase();
		return list;

	}

	public void insertLocation(Location_M location) throws IOException {

		try {
			ArrayList<Location_M> l = selectLocations(1);
			int i = l.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		openDataBase();
		ContentValues values = new ContentValues();

		values.put("id_staff", location.id_staff);
		values.put("date_day", location.date_day);
		values.put("date_month", location.date_month);
		values.put("date_year", location.date_year);
		values.put("message_time", location.message_time);
		values.put("lat", location.lat);
		values.put("lon", location.lon);
		values.put("country", location.country);
		values.put("city", location.city);
		values.put("street", location.street);

		try {
			myDataBase.insert(Tables.locations.toString(), null, values);
		}

		catch (Exception e) {

		}
		closeDataBase();
		try {
			ArrayList<Location_M> l = selectLocations(1);
			int i = l.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateLocationAddress(Location_M location) throws IOException {

		openDataBase();
		ContentValues values = new ContentValues();

		if (location.country != null)
			values.put("country", location.country);
		if (location.city != null)
			values.put("city", location.city);
		if (location.street != null)
			values.put("street", location.street);
		try {

			myDataBase.update(Tables.locations.toString(), values, "id="
					+ location.id, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDataBase();

	}

	public void insertStaff(Staff staff) throws IOException {

		openDataBase();
		ContentValues values = new ContentValues();

		values.put("name", staff.name);
		values.put("phoneNumber", staff.phoneNumber);

		try {
			myDataBase.insert(Tables.staffs.toString(), null, values);
		}

		catch (Exception e) {

		}
		closeDataBase();
	}

	public int deleteStaff(int id) {

		openDataBase();
		int reuslt = myDataBase.delete(Tables.staffs.toString(), "id=" + id,
				null);
		reuslt += myDataBase.delete(Tables.locations.toString(), "id_staff="
				+ id, null);
		closeDataBase();
		return reuslt;
	}

	public ArrayList<Staff> selectAllStaffs() throws Exception {

		ArrayList<Staff> list = new ArrayList<Staff>();
		Cursor cursor = select("*", Tables.staffs, null);
		if (cursor.moveToFirst()) {
			do {

				Staff staff = new Staff();
				staff.id = cursor.getInt(0);
				staff.name = cursor.getString(1);
				staff.phoneNumber = cursor.getString(2);
				list.add(staff);
			} while (cursor.moveToNext());
		}
		closeDataBase();
		return list;

	}

	public Staff selectStaff(String phoneNumber) throws Exception {

		Cursor cursor = select("*", Tables.staffs, "phoneNumber='"
				+ phoneNumber + "'");
		Staff staff = null;
		if (cursor.moveToFirst()) {
			staff = new Staff();
			staff.id = cursor.getInt(0);
			staff.name = cursor.getString(1);
			staff.phoneNumber = cursor.getString(2);
		}
		closeDataBase();
		return staff;

	}

	public Staff selectStaff(int id) throws Exception {

		Cursor cursor = select("*", Tables.staffs, "id=" + id);
		Staff staff = null;
		if (cursor.moveToFirst()) {
			staff = new Staff();
			staff.id = cursor.getInt(0);
			staff.name = cursor.getString(1);
			staff.phoneNumber = cursor.getString(2);
		}
		closeDataBase();
		return staff;

	}

	public void ExportToCSV() throws IOException {
		Cursor c = select("*", Tables.locations, null);
		int rowCount = 0;
		int colCount = 0;
		FileWriter fw;
		BufferedWriter bfw;
		// File sdCardDir = Environment.getExternalStorageDirectory();
		File saveFile = new File(Environment.getExternalStorageDirectory()
				+ "/Android/", "smslf.csv");

		try {

			rowCount = c.getCount();
			colCount = c.getColumnCount();

			// fw = new FileWriter(saveFile);
			// bfw = new BufferedWriter(fw);
			bfw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFile), "UTF-8"));
			if (rowCount > 0) {
				c.moveToFirst();
				//
				for (int i = 0; i < colCount; i++) {
					if (i != colCount - 1)
						bfw.write(c.getColumnName(i) + ',');
					else
						bfw.write(c.getColumnName(i));
				}
				//
				bfw.newLine();
				//
				for (int i = 0; i < rowCount; i++) {
					c.moveToPosition(i);
					// Toast.makeText(mContext, ""+(i+1)+"",
					// Toast.LENGTH_SHORT).show();

					for (int j = 0; j < colCount; j++) {
						if (j != colCount - 1)
							bfw.write(c.getString(j) + ',');
						else
							bfw.write(c.getString(j));
					}
					//
					bfw.newLine();
				}
			}
			//
			bfw.flush();
			//
			bfw.close();
			// Toast.makeText(mContext, "?", Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
			Toast_M.show(e.getMessage(), context);
		} finally {
			c.close();
		}
	}

	public void export() {
		// File dbFile=getDatabasePath("excerDB.db");

		// DbClass DBob = new DbClass(MainActivity.this);

		File exportDir = new File(Environment.getExternalStorageDirectory()
				+ "/Android/", "");

		if (!exportDir.exists())

		{

			exportDir.mkdirs();

		}

		File file = new File(exportDir, "excerDB.csv");

		try

		{

			file.createNewFile();

			CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

			Cursor curCSV = select("*", Tables.locations, null);

			csvWrite.writeNext(curCSV.getColumnNames());

			while (curCSV.moveToNext())

			{

				String arrStr[] = { curCSV.getString(0), curCSV.getString(1),

				curCSV.getString(2), curCSV.getString(3), curCSV.getString(4) };

				csvWrite.writeNext(arrStr);

			}

			csvWrite.close();

			curCSV.close();

		}

		catch (SQLException sqlEx)

		{
			Toast_M.show(sqlEx.getMessage(), context);
		}

		catch (IOException e)

		{

			Toast_M.show(e.getMessage(), context);
		}

	}
}