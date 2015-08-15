package com.thickman.passbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper{

	public static final String THICKMANPASSBOOK	= "thickmanpassbook";
	public static final String KEY 				= "KEY";
	public static final String VALUE1 			= "VALUE1";
	public static final String VALUE2 			= "VALUE2";
	public static final String COLUMN_ID 		= "_id";
	
	private static final String DATABASE_NAME   = "thickman.passbook.db";
	private static final int DATABASE_VERSION   = 1;
	private static final String DATABASE_CREATE = "CREATE TABLE " 
												+ THICKMANPASSBOOK 
												+ "(" 
												+ COLUMN_ID 	+ " integer primary key autoincrement not null, " 
												+ KEY			+ " string, "
												+ VALUE1 		+ " string, "
												+ VALUE2		+ " string);";
													
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + THICKMANPASSBOOK);
		onCreate(db);
	}
	
}
