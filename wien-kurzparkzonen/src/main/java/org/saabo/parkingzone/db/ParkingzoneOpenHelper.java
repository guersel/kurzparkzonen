package org.saabo.parkingzone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.DropBoxManager;

public class ParkingzoneOpenHelper extends SQLiteOpenHelper {

	
	public ParkingzoneOpenHelper(Context context) {
		super(context, DBDefinitions.DB_NAME, null, DBDefinitions.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBDefinitions.InformationResources.SQL_CREATE_TABLE);
		
		importData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		exportData(db);
		
		db.execSQL(DBDefinitions.InformationResources.SQL_DROP_TABLE);
		onCreate(db);
	}
	
	/**
	 * Export necessary data before deleting tables.
	 * @param db
	 */
	private void exportData(SQLiteDatabase db) {
		// nop
	}

	/**
	 * Import data into tables after upgrading or creating.
	 * @param db
	 */
	private void importData(SQLiteDatabase db) {
		// nop
	}
}
