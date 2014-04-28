package com.thickman.passbook;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {

	private SQLiteDatabase database;
	private DatabaseHelper DatabaseHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
			DatabaseHelper.KEY, DatabaseHelper.VALUE1, DatabaseHelper.VALUE2 };

	public Database(Context context) {
		DatabaseHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = DatabaseHelper.getWritableDatabase();
		
	}

	public void close() {
		DatabaseHelper.close();
	}

	public String updateItem(Item item) {
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.KEY, item.getKey());
		values.put(DatabaseHelper.VALUE1, item.getValue1());
		values.put(DatabaseHelper.VALUE2, item.getValue2());

		String where = "_id='" + item.getId() + "'";
		String whereArgs[] = null;

		int success = database.update(DatabaseHelper.THICKMANPASSBOOK, values,
				where, whereArgs);

		if (success == 0)
			return "Error: Item not updated.";

		else
			return "Item updated";

	}

	public String addItem(Item item) {
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.KEY, item.getKey());
		values.put(DatabaseHelper.VALUE1, item.getValue1());
		values.put(DatabaseHelper.VALUE2, item.getValue2());

		String where = "_id = ?";
		String whereArgs[] = null;

		int success = database.update(DatabaseHelper.THICKMANPASSBOOK, values,
				where, whereArgs);

		if (success == 0) {
			long insertId = database.insert(DatabaseHelper.THICKMANPASSBOOK,
					null, values);

			Cursor cursor = database.query(DatabaseHelper.THICKMANPASSBOOK,
					allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId,
					null, null, null, null);

			cursor.moveToFirst();
			cursor.close();

			return "Item added.";
		}
		close();
		return "There was a problem.";
	}

	public String removeItem(int index) {
		String where = "_id='" + index + "'";
		String whereArgs[] = null;

		int success = database.delete(DatabaseHelper.THICKMANPASSBOOK, where,
				whereArgs);
		if (success == 1)
			return "Item deleted.";
		return "Error deleting Item. " + Integer.toString(index);
	}

	public List<Item> getAllItems() {
		List<Item> Items = new ArrayList<Item>();
		Cursor cursor = database.query(DatabaseHelper.THICKMANPASSBOOK,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Item Item = cursorToItem(cursor);
			Items.add(Item);
			Log.d("Sending out Items", Integer.toString(Item.getId()));
			cursor.moveToNext();
		}
		cursor.close();
		return Items;
	}

	public boolean isEmpty() {
		return getAllItems().isEmpty();
	}
	
	public String drop() {
		database.execSQL("DROP TABLE IF EXISTS " + "THICKMANPASSBOOK");
		DatabaseHelper.onCreate(database);
		return "Item list cleared.";
	}

	private Item cursorToItem(Cursor cursor) {
		Item Item = new Item();

		Item.setId(cursor.getInt(0));
		Item.setKey(cursor.getString(1));
		Item.setValue1(cursor.getString(2));
		Item.setValue2(cursor.getString(3));

		return Item;
	}
	
	private String encrypt(String str) {
		
		return "";
	}
	
	private String decrypt(String str) {
		
		return "";
	}
}
