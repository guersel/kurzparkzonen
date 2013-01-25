package org.saabo.parkingzone.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.saabo.parkingzone.ParkingzoneApplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ParkingzoneService {
	
	private static final String TAG = ParkingzoneService.class.getName();
	
	private ParkingzoneOpenHelper dbHelper;
	private SQLiteDatabase db;
	
	public ParkingzoneService() {
		dbHelper = new ParkingzoneOpenHelper(ParkingzoneApplication.getAppContext());
	}
	
	public void open() {
		if (db == null || !db.isOpen()) {
			db = dbHelper.getWritableDatabase();
		}
	}
	
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	
	public int findId() {
		Cursor cursor = db.query(DBDefinitions.InformationResources.NAME, new String[] {DBDefinitions.InformationResources.COLUMN_ID}, null, null, null, null, null);
		
		if (!cursor.moveToFirst()) {
			cursor.close();
			return -1;
		}
		
		int id = cursor.getInt(cursor.getColumnIndex(DBDefinitions.InformationResources.COLUMN_ID));
		cursor.close();
				
		return id;
	}

	public byte[] findWienParkzoneData() {
		Cursor cursor = db.query(DBDefinitions.InformationResources.NAME, new String[] {DBDefinitions.InformationResources.COLUMN_CONTENT}, null, null, null, null, null);
		
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		
		byte[] content = cursor.getBlob(cursor.getColumnIndex(DBDefinitions.InformationResources.COLUMN_CONTENT));
		cursor.close();
		
		return content;
	}
	
	public java.util.Date findLastUpdate() {
		Cursor cursor = db.query(DBDefinitions.InformationResources.NAME, new String[] {DBDefinitions.InformationResources.COLUMN_UPDATED_AT}, null, null, null, null, null);
		
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		
		String updated_at = cursor.getString(cursor.getColumnIndex(DBDefinitions.InformationResources.COLUMN_UPDATED_AT));
		Log.d(TAG, updated_at);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			java.util.Date date = sdf.parse(updated_at);
			return date;
		} catch (ParseException e) {
			Log.e(TAG, "Failure during parsing date.");
			return null;
		}
	}

	public void saveWienParkzoneData(final int id, final String type, final byte[] content) {
		ContentValues values = new ContentValues();
		if (id != -1) {
			values.put(DBDefinitions.InformationResources.COLUMN_ID, id);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		values.put(DBDefinitions.InformationResources.COLUMN_TYPE, type);
		values.put(DBDefinitions.InformationResources.COLUMN_CONTENT, content);
		values.put(DBDefinitions.InformationResources.COLUMN_UPDATED_AT, sdf.format(new java.util.Date()));
		
		db.replace(DBDefinitions.InformationResources.NAME, null, values);
	}
}
