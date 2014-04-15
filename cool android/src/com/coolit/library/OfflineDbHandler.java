package com.coolit.library;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfflineDbHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "mCardOfflineDB.db";

	// Contacts table name
	private static final String TABLE_OFFLINE = "mCard_offline";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_EVENT = "event";
	private static final String KEY_DATA = "data";
	private static final String KEY_RESULT = "result";

	private static final String KEY_PROFILE_IMAGE = "profile_image";

	private static final String KEY_QRCODE_IMAGE = "qr_image";

	public OfflineDbHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_OFFLINE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_EVENT
				+ " TEXT," + KEY_DATA + " TEXT," + KEY_RESULT + " TEXT,"
				+ KEY_PROFILE_IMAGE + " BLOB," + KEY_QRCODE_IMAGE + " BLOB"
				+ ")";

		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addData(String event, String data, String result) {
		String row_id = getDataID(event, data);

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EVENT, event); // Name
		values.put(KEY_DATA, data); // Price
		values.put(KEY_RESULT, result); // Quantity

		// Inserting Row
		if (row_id == null)
			db.insert(TABLE_OFFLINE, null, values);
		else
			db.update(TABLE_OFFLINE, values, KEY_ID + " =? ",
					new String[] { row_id });
		db.close(); // Closing database connection
	}

	public String getData(String event, String data) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_EVENT
				+ " = \"" + event + "\" AND " + KEY_DATA + " = \"" + data
				+ "\"";

		try {

			Cursor cursor = db.rawQuery(query, null);

			if (cursor.moveToFirst()) {
				return cursor.getString(3);
			}

		} catch (Exception e) {
		}

		return null;
	}

	public List<String> getAllData(String event, String data) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_EVENT
				+ " != \"" + event + "\"";

		Cursor cursor = db.rawQuery(query, null);

		List<String> resultValList = new ArrayList<String>();

		int pos = cursor.getCount();

		for (int i = 0; i < pos; i++) {
			cursor.moveToPosition(i);

			String result = "";

			try {
				result = cursor.getString(3);
			} catch (Exception e) {

			}

			resultValList.add(result);
		}

		return resultValList;

	}

	// Get All User ID
	public List<String> getAllUserNameID(String event, String data) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_EVENT
				+ " = \"" + event + "\"";

		Cursor cursor = db.rawQuery(query, null);

		List<String> resultValList = new ArrayList<String>();

		int pos = cursor.getCount();

		for (int i = 0; i < pos; i++) {
			cursor.moveToPosition(i);

			String result = "";

			try {
				result = cursor.getString(2);
			} catch (Exception e) {

			}

			resultValList.add(result);
		}

		return resultValList;

	}

	public String getDataID(String event, String data) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_EVENT
				+ " = \"" + event + "\" AND " + KEY_DATA + " = \"" + data
				+ "\"";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}

		return null;
	}

	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFLINE, null, null);
		db.close();
	}

	public int totalCount() {

		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM " + TABLE_OFFLINE;

		Cursor cursor = db.rawQuery(query, null);
		cursor.close();

		// return count
		return cursor.getCount();

	}

	// Update Profile Image
	public void saveProfileImage(String event, String data, byte[] result) {
		String row_id = getDataID(event, data);

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EVENT, event); // Name
		values.put(KEY_DATA, data); // Price
		values.put(KEY_PROFILE_IMAGE, result); // Quantity

		// Inserting Row
		if (row_id == null)
			db.insert(TABLE_OFFLINE, null, values);
		else
			db.update(TABLE_OFFLINE, values, KEY_ID + " =? ",
					new String[] { row_id });
		db.close(); // Closing database connection
	}

	// get Profile Image
	public byte[] getProfileImage(String event, String data) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		if (data.equalsIgnoreCase("all_record")) {
			query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_ID
					+ " = \"" + event + "\"";
		} else {
			query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_EVENT
					+ " = \"" + event + "\" AND " + KEY_DATA + " = \"" + data
					+ "\"";
		}

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			return cursor.getBlob(4);
		}
		return null;
	}

	// Update QR Image
	public void saveQRImage(String event, String data, byte[] result) {
		String row_id = getDataID(event, data);

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EVENT, event); // Name
		values.put(KEY_DATA, data); // Price
		values.put(KEY_QRCODE_IMAGE, result); // Quantity

		// Inserting Row
		if (row_id == null)
			db.insert(TABLE_OFFLINE, null, values);
		else
			db.update(TABLE_OFFLINE, values, KEY_ID + " =? ",
					new String[] { row_id });
		db.close(); // Closing database connection
	}

	// get QR Image
	public byte[] getQRImage(String event, String data) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		if (data.equalsIgnoreCase("all_record")) {
			query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_ID
					+ " = \"" + event + "\"";
		} else {
			query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_EVENT
					+ " = \"" + event + "\" AND " + KEY_DATA + " = \"" + data
					+ "\"";
		}

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			return cursor.getBlob(5);
		}
		return null;
	}

	// Delete Only Specified Record
	public void deleteOnlyOneRecord(String event, String data) {

		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_OFFLINE, KEY_EVENT + " =? AND " + KEY_DATA + " =? ",
				new String[] { event, data });

		db.close();

	}

	// get Added User Name Image
	public String getUserIDName(String userName) {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "";

		query = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_DATA
				+ " = \"" + userName + "\" COLLATE NOCASE";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			return cursor.getString(2);
		}
		return null;
	}

}