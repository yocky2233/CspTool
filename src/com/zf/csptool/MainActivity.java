package com.zf.csptool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button phone = (Button)findViewById(R.id.phoneButton);
		Button contact = (Button)findViewById(R.id.contactButton);
		Button sms = (Button)findViewById(R.id.smsButton);
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PhoneActivity.class);
				startActivity(intent);
			}
			
		});
		
		contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ContactsActivity.class);
				startActivity(intent);
			}
			
		});
		
		sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SmsActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		return super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(1, 1, 1, "¹ØÓÚ").setShowAsAction(1);
		return true;
	}
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			 Dialog dialog=new AlertDialog.Builder(MainActivity.this)
			    .setTitle("About")
			    .setMessage("Application Name:CspTool"+"\n"+"Author:ZF(471410616@QQ.com)"+"\n"+"Version:1.0")
			    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			     @Override
			     public void onClick(DialogInterface dialog, int which) {
			      dialog.cancel();
			     }
			    })
			    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			     @Override
			     public void onClick(DialogInterface dialog, int which) {
			      dialog.cancel();
			     }
			    })
			    .create();
			    dialog.show();
			break;
		default:
			break;
		}
		return true;
	}
}
