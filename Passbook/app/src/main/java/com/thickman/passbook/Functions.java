package com.thickman.passbook;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Functions {

	public static SharedPreferences preferences = null;
	public static final String PREF_PASSWORD = "pwd";
	public static final String PREF_NULL = "PREF_NULL";
	public static final String PREF_FIRST_LOGIN = "PREF_FIRST_LOGIN";
	public static final String PREF_SECURITY_QUESTION = "PREF_SECURITY_QUESTION";
	public static final String PREF_SECURITY_ANSWER = "PREF_SECURITY_ANSWER";
	public static final String PREF_IMPORT_DISCLAIMER = "PREF_IMPORT_DISCLAIMER";
	
	private final static String HEX = "0123456789ABCDEF";
	private final static String ALGORITHM = "AES";
	private final static String KEY = "l0C0m0T1V3";
	private final static String INSTANCE = "PBKDF2WithHmacSHA1";

	public static void setPreferences(Context ctx) {
		if (preferences == null) {
			preferences = ctx.getSharedPreferences(PREF_PASSWORD,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.commit();
		}
	}

	public static void setStringPreference(Context ctx, String key, String value) {
		try {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
			Log.d(key, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String getStringPreference(Context ctx, String key) {
		return preferences.getString(key, PREF_NULL);
	}

	public static boolean isFirstLogin() {
		if (preferences.getString(PREF_PASSWORD, PREF_FIRST_LOGIN).equals(
				PREF_FIRST_LOGIN)) {
			return true;
		}
		return false;
	}

	public static void makeAToast(Context ctx, String msg) {
		Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
		toast.show();
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public static String encrypt(String data) throws Exception {

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance(INSTANCE);
		KeySpec spec = new PBEKeySpec(KEY.toCharArray(),
				KEY.getBytes(), 128, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		return toHex(cipher.doFinal(data.getBytes()));
	}

	public static String decrypt(String data)
			throws Exception {

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance(INSTANCE);
		KeySpec spec = new PBEKeySpec(KEY.toCharArray(),
				KEY.getBytes(), 128, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

		Cipher cipher = Cipher.getInstance(ALGORITHM);

		cipher.init(Cipher.DECRYPT_MODE, key);

		return new String(cipher.doFinal(toByte(data)));
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;

		byte[] result = new byte[len];

		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] stringBytes) {
		StringBuffer result = new StringBuffer(2 * stringBytes.length);

		for (int i = 0; i < stringBytes.length; i++) {
			result.append(HEX.charAt((stringBytes[i] >> 4) & 0x0f)).append(
					HEX.charAt(stringBytes[i] & 0x0f));
		}

		return result.toString();
	}
}
