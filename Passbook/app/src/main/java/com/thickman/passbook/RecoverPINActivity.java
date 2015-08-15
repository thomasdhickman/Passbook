package com.thickman.passbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RecoverPINActivity extends MasterActivity {

	private TextView txtQuestion;
	private EditText etAnswer;
	private Button btnConfirm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recover_pin);
		getActionBar().setTitle(getResources().getString(R.string.recover_password));

		txtQuestion = (TextView) findViewById(R.id.txtQuestion);
		etAnswer = (EditText) findViewById(R.id.etAnswer);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);

		txtQuestion.setText(Functions.getStringPreference(this,
				Functions.PREF_SECURITY_QUESTION));
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etAnswer
						.getText()
						.toString()
						.equals(Functions.getStringPreference(
								RecoverPINActivity.this,
								Functions.PREF_SECURITY_ANSWER))) {
					Intent i = new Intent(RecoverPINActivity.this, ChangePinActivity.class);
					i.putExtra(ChangePinActivity.ARG_RESET, true);
					startActivity(i);
					finish();
				}
				else
					etAnswer.setError(RecoverPINActivity.this.getResources().getString(R.string.incorrect_answer));
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
