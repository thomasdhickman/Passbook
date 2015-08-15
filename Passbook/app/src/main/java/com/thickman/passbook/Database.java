package com.thickman.passbook;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

@SuppressWarnings("static-access")
public class Database {

	private Context ctx;
	private SQLiteDatabase database;
	private DatabaseHelper DatabaseHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
			DatabaseHelper.KEY, DatabaseHelper.VALUE1, DatabaseHelper.VALUE2 };

	private static byte[] KEY = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41,
			0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79 };

	public Database(Context context) {
		this.ctx = context;
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
			return ctx.getResources().getString(R.string.database_error);

		else
			return ctx.getResources().getString(R.string.database_update_success);

	}

	public String addItem(Item item) {
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.KEY, /*encrypt(*/item.getKey());//);
		values.put(DatabaseHelper.VALUE1, /*encrypt(*/item.getValue1());//);
		values.put(DatabaseHelper.VALUE2, /*encrypt(*/item.getValue2());//);

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

			return ctx.getResources().getString(R.string.database_item_added);
		}
		close();
		return ctx.getResources().getString(R.string.database_error);
	}

	public String removeItem(int index) {
		String where = "_id='" + index + "'";
		String whereArgs[] = null;

		int success = database.delete(DatabaseHelper.THICKMANPASSBOOK, where,
				whereArgs);
		if (success == 1)
			return ctx.getResources().getString(R.string.database_item_deleted);
		return ctx.getResources().getString(R.string.database_error);
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
		return ctx.getResources().getString(R.string.database_list_cleared);
	}

	private Item cursorToItem(Cursor cursor) {
		Item Item = new Item();

		Item.setId(cursor.getInt(0));
		Item.setKey(/*decrypt(*/cursor.getString(1));//);
		Item.setValue1(/*decrypt(*/cursor.getString(2));//);
		Item.setValue2(/*decrypt(*/cursor.getString(3));//);

		return Item;
	}

	public static String encrypt(String strToEncrypt)  {
		try {
			byte[] array = strToEncrypt.getBytes(Charset.forName("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedByte = Base64.encode(cipher.doFinal(array), 0);
			String encryptedString = new String(encryptedByte, "UTF-8");
			return encryptedString;
		} catch (Exception ex) {
			ex.printStackTrace();
			return strToEncrypt + "ee";
		}
	}

	public static String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedByte = cipher.doFinal(Base64
					.decode(strToDecrypt, 0));
			return new String(decryptedByte, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			return strToDecrypt + "de";
		}
	}
}
