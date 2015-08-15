package com.thickman.passbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePinActivity extends MasterActivity {

	public static final String ARG_RESET = "ARG_RESET";

	private boolean bReset = false;

	private EditText etOldPin;
	private EditText etNewPin;
	private EditText etConfirmNew;
	private Button btnConfirm;

	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.change_pin);
		getActionBar().setTitle(getResources().getString(R.string.change_pin));

		bReset = getIntent().getBooleanExtra(ARG_RESET, false);

		etOldPin = (EditText) findViewById(R.id.etOldPin);
		etNewPin = (EditText) findViewById(R.id.etNewPin);
		etConfirmNew = (EditText) findViewById(R.id.etConfirmNew);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);

		if (bReset) {
			etOldPin.setText(Functions.getStringPreference(this,
					Functions.PREF_PASSWORD));
			etOldPin.setEnabled(false);
		}

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
									Toast.makeText(
											getApplicationContext(),
											getResources()
													.getString(
															R.string.your_pin_has_been_changed),
											Toast.LENGTH_SHORT).show();
								} else {
									etConfirmNew
											.setError(getResources().getString(
													R.string.pins_do_not_match));
								}
							} else {
								etOldPin.setError(getResources().getString(
										R.string.incorrect_pin));
							}
						} else {
							etConfirmNew.setError(getResources().getString(
									R.string.required_field));
						}
					} else {
						etNewPin.setError(getResources().getString(
								R.string.new_pin_four_digits));
					}
				} else {
					etOldPin.setError(getResources().getString(
							R.string.required_field));
				}
			}
		});
	}

	public void setPreference(String key, String val) {
		Functions.setStringPreference(this, key, val);
	}

	public boolean check(String key) {
		return Functions.getStringPreference(this, Functions.PREF_PASSWORD)
				.equals(key);
	}

	@Override
	public void onBackPressed() {
		if (!bReset) {
			Intent i = new Intent(this, ItemActivity.class);
			startActivity(i);
			finish();
		}
		else
			finish();
	}
}
