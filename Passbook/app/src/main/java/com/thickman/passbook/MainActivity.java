package com.thickman.passbook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView txtBanner;
	private TextView tvConfirm;
	private EditText etPIN;
	private EditText etConfirm;
	private Button btnConfirm;
	private Button btnForgot;
	private Database database;
	public SharedPreferences preferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.blue));

		txtBanner = (TextView) findViewById(R.id.txtBanner);
		etPIN = (EditText) findViewById(R.id.etPIN);
		etConfirm = (EditText) findViewById(R.id.etConfirm);
		etConfirm.setVisibility(View.GONE);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnForgot = (Button) findViewById(R.id.btnForgot);
		tvConfirm = (TextView) findViewById(R.id.tvConfirm);
		tvConfirm.setVisibility(View.GONE);
		txtBanner.setVisibility(View.GONE);
		database = new Database(this);
		database.open();

		Functions.setPreferences(this);

		if (Functions.isFirstLogin()) {
			txtBanner.setVisibility(View.VISIBLE);
			txtBanner.setText(getResources().getString(
					R.string.thanks_for_installing));
			etConfirm.setVisibility(View.VISIBLE);
			tvConfirm.setVisibility(View.VISIBLE);
		}
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (Functions.isFirstLogin()) {
					if (etPIN.getText().toString().length() > 3) {
						if (etPIN.getText().toString()
								.equals(etConfirm.getText().toString())) {
							database.addItem(new Item(
									"Tap the + button to add an entry.",
									" - Press and hold this entry for more options.",
									" - Tap to edit this entry."));

							Functions.setStringPreference(MainActivity.this,
									Functions.PREF_PASSWORD, etPIN.getText()
											.toString());
							
							chooseActivity();
							
						} else {
							etConfirm.setError(getResources().getString(
									R.string.pins_do_not_match));
						}
					} else {
						etPIN.setError(getResources().getString(
								R.string.new_pin_four_digits));
					}
				} else if (Functions.getStringPreference(MainActivity.this,
						Functions.PREF_PASSWORD).equals(
						etPIN.getText().toString())) {

					chooseActivity();
				} else {
					etPIN.setError(getResources().getString(
							R.string.incorrect_pin));
				}

			}
		});
		
		btnForgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, RecoverPINActivity.class));
			}
		});
		btnForgot.setVisibility(Functions.isFirstLogin() ? View.GONE : View.VISIBLE);

		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private void chooseActivity() {
		Intent i = new Intent();

		if (Functions.getStringPreference(MainActivity.this,
				Functions.PREF_SECURITY_QUESTION).equals(
				Functions.PREF_NULL))
			i = new Intent(MainActivity.this,
					SecurityQuestionActivity.class);
		else
			i = new Intent(MainActivity.this, ItemActivity.class);

		startActivity(i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_about) {
			Intent i = new Intent(this, AboutActivity.class);
			i.putExtra("intent", "home");
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
