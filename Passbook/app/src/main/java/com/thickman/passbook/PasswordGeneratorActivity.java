package com.thickman.passbook;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PasswordGeneratorActivity extends MasterActivity {

	private Button btnGenerate;
	private EditText etPassword;
	private Spinner spNumber;
	private Button btnCopy;
	private CheckBox cbCaps;
	private CheckBox cbSpecial;
	private CheckBox cbNumbers;

	private String[] num = { "7", "8", "9", "10", "11", "12", "13" };

	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.activity_generator);
		getActionBar().setTitle(getResources().getString(R.string.password_generator));

		btnGenerate = (Button) findViewById(R.id.btnGenerate);
		etPassword = (EditText) findViewById(R.id.etPassword);
		spNumber = (Spinner) findViewById(R.id.spNumber);
		btnCopy = (Button) findViewById(R.id.btnCopy);
		cbCaps = (CheckBox) findViewById(R.id.cbCaps);
		cbSpecial = (CheckBox) findViewById(R.id.cbSpecial);
		cbNumbers = (CheckBox) findViewById(R.id.cbNum);

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_dropdown_item, num);
		spNumber.setAdapter(adapter);

		btnGenerate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				etPassword.setText(generatePassword());
			}
		});

		btnCopy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Activity.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("PassbookPassword",
						etPassword.getText().toString());
				clipboard.setPrimaryClip(clip);
				Toast.makeText(getApplicationContext(),
						"Password copied to clipboard.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(PasswordGeneratorActivity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}

		etPassword.setText(generatePassword());
	}

	private String generatePassword() {
		String pwd = "";

		String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String num = "123456789";
		String spec = "!@#$%^&*()_+-=?.,><}{[]|";
		String keylist = "abcdefghijklmnopqrstuvwxyz";

		if (cbCaps.isChecked())
			keylist += caps;
		if (cbNumbers.isChecked())
			keylist += num;
		if (cbSpecial.isChecked())
			keylist += spec;

		int specCount = 0;
		for (int i = 0; i < spNumber.getSelectedItemPosition() + 7; i++) {
			char tmp;
			tmp = keylist.charAt((int) Math.floor(Math.random()
					* keylist.length()));
			if (spec.contains(String.valueOf(tmp))) {
				if (specCount < 3) {
					specCount++;
					pwd += tmp;
				}
			}
			else
				pwd += tmp;
		}
		return pwd;
	}
}
