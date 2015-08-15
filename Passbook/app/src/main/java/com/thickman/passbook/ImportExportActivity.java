package com.thickman.passbook;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.thickman.passbook.ItemActivity.LongClickDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ImportExportActivity extends MasterActivity {

	private Context ctx;
	private static XmlPullParserFactory xmlFactoryObject;
	private TextView txtPath;
	private Button btnDisclaimer;

	@Override
	protected void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.activity_import_export);
		getActionBar().setTitle(
				getResources().getString(R.string.import_export));

		txtPath = (TextView) findViewById(R.id.txtPath);
		btnDisclaimer = (Button) findViewById(R.id.btnDisclaimer);
		btnDisclaimer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DisclaimerDialog();
				newFragment.show(getFragmentManager(), getResources()
						.getString(R.string.import_export));	
			}
		});

		ctx = getApplicationContext();

		Button btnImport = (Button) findViewById(R.id.btnImport);
		btnImport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				importXml();
			}
		});

		Button btnExport = (Button) findViewById(R.id.btnExport);
		btnExport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exportXml();
			}
		});

		Button btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exportPlainText();
			}
		});

		makeDirectory();
	}

	private void exportPlainText() {
		/*
		String export = "";
		Database db = new Database(ctx);
		db.open();
		List<Item> allItems = db.getAllItems();
		for (int i = 0; i < allItems.size(); i++) {
			export += "Key: " + allItems.get(i).getKey() + " / Value 1: "
					+ allItems.get(i).getValue1() + " / Value 2: "
					+ allItems.get(i).getValue2() + "\n";
		}
		*/
		String filename = "export.xml";
		String export = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<passbook>\n";

		Database db = new Database(ctx);
		db.open();
		List<Item> allItems = db.getAllItems();

		for (int i = 0; i < allItems.size(); i++) {
			try {
				export += "<item key=\"" + allItems.get(i).getKey()
						+ "\" value1=\""
						+ Functions.encrypt(allItems.get(i).getValue1())
						+ "\" value2=\""
						+ Functions.encrypt(allItems.get(i).getValue2())
						+ "\" />\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		export += "</passbook>";
		
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/xml");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, export);
		startActivity(Intent.createChooser(shareIntent, "Share"));
	}

	public void importXml() {
		try {
			String ret = "";
			File inputFile = new File(Environment.getExternalStorageDirectory()
					.toString() + "/passbook", "export.xml");

			InputStream fis = new FileInputStream(inputFile);

			if (fis != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(fis);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				fis.close();
				ret = stringBuilder.toString();
				InputStream is = new ByteArrayInputStream(ret.getBytes("UTF-8"));

				importPasswords(is);
			}
		} catch (FileNotFoundException e) {
			Toast.makeText(ctx,
					txtPath.getText().toString() + "/export.xml not found.",
					Toast.LENGTH_LONG).show();
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Toast.makeText(ctx, "Cannot read file.", Toast.LENGTH_LONG).show();
			Log.e("login activity", "Can not read file: " + e.toString());
		} catch (Exception e) {
			Toast.makeText(ctx, "An error occurred.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.e("Exception", e.toString());
		}
	}

	private void exportXml() {
		if (makeDirectory()) {

			String filename = "export.xml";
			String export = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<passbook>\n";

			Database db = new Database(ctx);
			db.open();
			List<Item> allItems = db.getAllItems();

			for (int i = 0; i < allItems.size(); i++) {
				try {
					export += "<item key=\"" + allItems.get(i).getKey()
							+ "\" value1=\""
							+ Functions.encrypt(allItems.get(i).getValue1())
							+ "\" value2=\""
							+ Functions.encrypt(allItems.get(i).getValue2())
							+ "\" />\n";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			export += "</passbook>";

			File file = new File(getPassbookDirectory(), filename);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				out.write(export.getBytes());
				out.flush();
				out.close();
				Toast.makeText(ctx, "Export complete!", Toast.LENGTH_LONG)
						.show();

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ctx, "Error. " + e.toString(), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	private void importPasswords(InputStream is) {
		Database db = new Database(this);
		db.open();
		try {
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			xmlFactoryObject.setNamespaceAware(true);

			XmlPullParser xmlParser = xmlFactoryObject.newPullParser();
			xmlParser.setInput(is, null);

			int eventType = xmlParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String key = xmlParser.getAttributeValue(null, "key");
					String value1 = xmlParser.getAttributeValue(null, "value1");
					String value2 = xmlParser.getAttributeValue(null, "value2");
					if (key != null && value1 != null && value2 != null) {
						String v1 = Functions.decrypt(value1);
						String v2 = Functions.decrypt(value2);
						Item item = new Item(key, v1, v2);
						db.addItem(item);
					}
				}
				eventType = xmlParser.next();
			}
			System.out.println("End document");
			Functions.makeAToast(this, "Import complete!");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean makeDirectory() {
		File file = getPassbookDirectory();
		if (file != null) {
			txtPath.setText(file.toString());
			return true;
		} else
			Toast.makeText(ctx, "Directory creation error.", Toast.LENGTH_LONG)
					.show();
		return false;
	}

	private File getPassbookDirectory() {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/passbook");
		myDir.mkdirs();
		return myDir;
	}

	public void openFolder() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		Uri uri = Uri
				.parse(Environment.getExternalStorageDirectory().getPath());
		intent.setDataAndType(uri, "text/xml");
		startActivity(Intent.createChooser(intent, "Open file"));
	}

	@SuppressLint("ValidFragment")
	public class DisclaimerDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle(R.string.import_export);
			builder.setMessage(
					getResources().getString(R.string.import_export_subtitle))
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});

			return builder.create();
		}
	}
}
