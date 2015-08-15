package com.thickman.passbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SecurityQuestionActivity extends MasterActivity {

	private EditText etQuestion;
	private EditText etAnswer;
	private Button btnConfirm;
	private int backCount = 0;

	private boolean bAllowBack = false;
	public static final String ARG_ALLOW_BACK = "ARG_ALLOW_BACK";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.security_question);
		getActionBar().setTitle(
				getResources().getString(R.string.security_question));

		etQuestion = (EditText) findViewById(R.id.etQuestion);
		etAnswer = (EditText) findViewById(R.id.etAnswer);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);

		bAllowBack = getIntent().getBooleanExtra(ARG_ALLOW_BACK, false);

		getActionBar().setDisplayHomeAsUpEnabled(bAllowBack);

		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etQuestion.getText().toString().isEmpty()) {
					etQuestion.setError(SecurityQuestionActivity.this
							.getResources().getString(R.string.required_field));
					return;
				} else if (etAnswer.getText().toString().isEmpty()) {
					etAnswer.setError(SecurityQuestionActivity.this
							.getResources().getString(R.string.required_field));
					return;
				} else {
					Functions.setStringPreference(
							SecurityQuestionActivity.this,
							Functions.PREF_SECURITY_QUESTION, etQuestion
									.getText().toString());
					Functions.setStringPreference(
							SecurityQuestionActivity.this,
							Functions.PREF_SECURITY_ANSWER, etAnswer.getText()
									.toString());
					startActivity(new Intent(SecurityQuestionActivity.this,
							ItemActivity.class));
					Functions.makeAToast(SecurityQuestionActivity.this,
							"Security question set.");
					finish();
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (android.R.id.home): {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		if (bAllowBack) {
			Intent i = new Intent(this, ItemActivity.class);
			startActivity(i);
			finish();
		} else if (backCount == 1) {
			finish();
		} else {
			Functions
					.makeAToast(this,
							"Please enter a security question and answer. Press back again to exit.");
			backCount++;
		}
	}
}
