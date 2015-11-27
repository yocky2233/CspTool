package com.zf.csptool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TanqiActivity extends Activity {

	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	Button bt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tanqi);
		et1 = (EditText)findViewById(R.id.csET);
		et2 = (EditText)findViewById(R.id.nowWDET);
		et3 = (EditText)findViewById(R.id.maxWDET);
		et4 = (EditText)findViewById(R.id.minWDET);
		bt = (Button)findViewById(R.id.bt);
		
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String str1 = et1.getText().toString().trim();
			    String str2 = et2.getText().toString().trim();
			    String str3 = et3.getText().toString().trim();
			    String str4 = et4.getText().toString().trim();
			    Intent i = new Intent();
			    i.putExtra("str1",str1);
			    i.putExtra("str2",str2);
			    i.putExtra("str3",str3);
			    i.putExtra("str4",str4);
			    setResult(0,i);
			    finish();
			}
			
		});
	}
	
//	@Override
//	 public void onBackPressed() {
//	     String str1 = et1.getText().toString().trim();
//	     String str2 = et2.getText().toString().trim();
//	     String str3 = et3.getText().toString().trim();
//	     String str = et4.getText().toString().trim();
//	     Intent i = new Intent();
//	     i.putExtra("str1",str);
//	     i.putExtra("str2",str);
//	     i.putExtra("str3",str);
//	     i.putExtra("str4",str);
//	     setResult(0,i);
//	     finish();
//	 }


}
