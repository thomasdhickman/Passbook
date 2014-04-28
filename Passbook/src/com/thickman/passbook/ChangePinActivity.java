package com.thickman.passbook;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePinActivity extends Activity {

	private EditText etOldPin;
	private EditText etNewPin;
	private EditText etConfirmNew;
	private Button btnConfirm;
	private SharedPreferences preferences;
	private String REQ = "Required field";

	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.change_pin);

		preferences = getApplicationContext().getSharedPreferences("pwd",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.commit();
		
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		etOldPin = (EditText) findViewById(R.id.etOldPin);
		etNewPin = (EditText) findViewById(R.id.etNewPin);
		etConfirmNew = (EditText) findViewById(R.id.etConfirmNew);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etOldPin.getText().length() != 0) {
					if (etNewPin.getText().toString().length() > 3) {
						if (etConfirmNew.getText().length() != 0) {
							if (check(etOldPin.getText().toString())) {
								if (etNewPin
										.getText()
										.toString()
										.equals(etConfirmNew.getText()
												.toString())) {
									setPreference("pwd", etNewPin.getText()
											.toString());
									Intent i = new Intent(
											ChangePinActivity.this,
											ItemActivity.class);
									startActivity(i);
									finish();
									Toast.makeText(getApplicationContext(), "Your PIN has been changed.", Toast.LENGTH_SHORT).show();
								} else {
									etConfirmNew.setError("PINs do not match.");
								}
							} else {
								etOldPin.setError("Incorrect PIN.");
							}
						} else {
							etConfirmNew.setError(REQ);
						}
					} else {
						etNewPin.setError("New PIN must be at least 4 numbers long.");
					}
				} else {
					etOldPin.setError(REQ);
				}
			}
		});
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (android.R.id.home): {
			Intent i = new Intent(this, ItemActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
