////package com.example.mytourguide.DataObjects;
package com.example.smslfc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.android.gms.internal.cu;

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
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;

public class DataBaseHandler extends SQLiteOpenHelper {

	private final Context context;
	private static String DB_NAME;
	private static String DB_PATH;

	public static enum Tables {
		info
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
				+ "/MYDB";
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

	public String selectPhoneNumber() throws Exception {

		Cursor cursor = select("*", Tables.info, "id=1");
		String result = null;
		if (cursor.moveToFirst()) {
			result = cursor.getString(1);
		}
		closeDataBase();
		return result;
	}

	public void updatePhoneNumber(String phoneNumber) throws IOException {

		openDataBase();
		ContentValues values = new ContentValues();

		values.put("phone_number", phoneNumber);
		try {

			myDataBase.update(Tables.info.toString(), values, "id=1", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDataBase();

	}

	public Location selectLocation() throws Exception {

		Location location = new Location(LocationManager.NETWORK_PROVIDER);

		Cursor cursor = select("*", Tables.info, "id=1");
		if (cursor.moveToFirst()) {
			location.setLatitude(Double.valueOf(cursor.getString(2)));
			location.setLongitude(Double.valueOf(cursor.getString(3)));
		}
		closeDataBase();
		return location;
	}

	public void updateLocation(Location location) throws IOException {

		openDataBase();
		ContentValues values = new ContentValues();

		values.put("latitude", location.getLatitude());
		values.put("longitude", location.getLongitude());
		try {

			myDataBase.update(Tables.info.toString(), values, "id=1", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDataBase();

	}

}