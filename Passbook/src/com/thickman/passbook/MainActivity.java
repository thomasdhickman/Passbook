package com.thickman.passbook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView txtBanner;
	private EditText etPIN;
	private EditText etConfirm;
	private Button btnConfirm;
	private Database database;
	public SharedPreferences preferences = null;
	private int ATTEMPT = 0;
	private String garbage = "&*&$#*(&$@23423423(**&*&*#$$&**SDFSDFSDFDFDS***#&$*@*$&@$@&&234234234&$&$&$&*@$!!@)*CHJSDFKHSD";
	private String locked  = "56456sd4f565as45DS6F4SDFSFDFSDFS62343434378978************##########544345634234234#$$#*&*(@&$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtBanner = (TextView) findViewById(R.id.txtBanner);
		etPIN = (EditText) findViewById(R.id.etPIN);
		etConfirm = (EditText) findViewById(R.id.etConfirm);
		etConfirm.setVisibility(View.GONE);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		database = new Database(this);
		database.open();

		setPreferences();

		if (isFirstLogin()) {
			txtBanner
					.setText("Thanks for installing Passbook.\n\nIt is highly recommended that you click the settings button up top and read the About section before using this app.\n\nPlease set your PIN. This is NOT recoverable, so don't forget it.\n\nYou will be able to change your PIN later if you'd like.");
			etConfirm.setVisibility(View.VISIBLE);
		} else if (isLocked())
			txtBanner
					.setText("App has been locked out. Please reinstall to reset.");

		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!isLocked()) {
					if (isFirstLogin()) {
						if (etPIN.getText().toString().length() > 3) {
							if (etPIN.getText().toString()

							.equals(etConfirm.getText().toString())) {
								database.addItem(new Item(
										"Tap the + button to add an entry.",
										" - Press and hold this entry to delete it.",
										" - Tap the settings button to learn more."));
								setPreference("pwd", etPIN.getText().toString());
								Intent i = new Intent(MainActivity.this,
										ItemActivity.class);
								startActivity(i);
								finish();
							} else {
								etConfirm.setError("PINs do not match.");
							}
						} else {
							etPIN.setError("PIN must be at least 4 numbers long.");
						}
					} else if (check(etPIN.getText().toString())) {
						Intent i = new Intent(MainActivity.this,
								ItemActivity.class);
						startActivity(i);
						finish();
					} else {
						txtBanner.setText("Incorrect PIN. Please try again.");
						ATTEMPT++;
					}
					if (ATTEMPT == 3)
						txtBanner.setText("Two tries remaining.");
					else if (ATTEMPT == 4)
						txtBanner
								.setText("One try remaining. Your private data will be deleted if your login fails.");
					else if (ATTEMPT == 5) {
						database.drop();
						setPreference("pwd", locked);
						txtBanner
								.setText("All private data has been deleted due to too many failed login attempts.");
					}
				} else
					txtBanner
							.setText("App has been locked out. Please reinstall to reset.");
			}
		});
	}

	private void setPreferences() {
		if (preferences == null) {
			preferences = getApplicationContext().getSharedPreferences("pwd",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.commit();
		}
	}

	public void setPreference(String key, String val) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, val);
		editor.commit();
	}

	public boolean check(String key) {
		if (key.equals(preferences.getString("pwd", null))) {
			return true;
		}
		return false;
	}

	public boolean isLocked() {
		if (preferences.getString("pwd", garbage).equals(locked)) {
			return true;
		}
		return false;
	}

	public boolean isFirstLogin() {
		if (preferences.getString("pwd", garbage).equals(garbage)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (R.id.action_about): {
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
