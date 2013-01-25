package org.saabo.parkingzone.db;

/**
 * Define all database tables in this class.
 * 
 * @author guersel
 * 
 */
public class DBDefinitions {

	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "wien_parkingzone";

	public static final String ID_DEF = "integer primary key autoincrement";

	public static class InformationResources {
		// Table name
		public static final String NAME = "information_resources";
		// Table columns
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_TYPE = "type";
		public static final String COLUMN_CONTENT = "content";
		public static final String COLUMN_UPDATED_AT = "updated_at";
		// SQL statements
		public static final String SQL_CREATE_TABLE = "create table " + NAME
				+ " (" + COLUMN_ID + " " + ID_DEF + ", " + COLUMN_TYPE
				+ " text, " + COLUMN_CONTENT + " blob, " + COLUMN_UPDATED_AT + " text );";
		
		public static final String SQL_DROP_TABLE = "drop table " + NAME;
		public static final String SQL_STMT_INSERT = "insert into " + NAME + " (" + COLUMN_TYPE + ", " + COLUMN_CONTENT + 
											", " + COLUMN_UPDATED_AT + ") " + "values (?, ?, ?);";
		
	}
}
