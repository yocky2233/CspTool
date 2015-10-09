package com.zf.csptool;

import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

public class ContactsActivity extends Activity {
	String []topnumbers = { "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "150", "180", "186", "189"};
	EditText contacts;
	EditText nameLen;
	Button btn;
	Random rand;
	ContactsData cd;
	RadioGroup sex;
	int run;
	String Name;
	int topnumber;
	int C;
	int NL;
	StringBuffer sb;
	ProgressDialog progressDialog;
	boolean stop = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contacts);
		rand =new Random();
		topnumber = rand.nextInt(topnumbers.length);
		cd = new ContactsData();
		TextView topName = (TextView)findViewById(R.id.topNme);
		contacts = (EditText)findViewById(R.id.contacts);
		nameLen = (EditText)findViewById(R.id.nameLen);
		sex = (RadioGroup)findViewById(R.id.radio);
		btn = (Button)findViewById(R.id.generate2);
		topName.setText("生成联系人");
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if((contacts.getText().toString()).equals("")) {
					C = 100;
				}else{
					C = Integer.parseInt(contacts.getText().toString().trim());
				}
				if((nameLen.getText().toString()).equals("")) {
					NL = 3;
				}else{
					NL = Integer.parseInt(nameLen.getText().toString().trim());
				}
				
//				String c = contacts.getText().toString().trim();
//				int C = Integer.parseInt(c);
//				String NL = nameLen.getText().toString().trim();
//				int nl = Integer.parseInt(NL);
				if(NL==0){
					Toast.makeText(ContactsActivity.this, "联系人无姓名", Toast.LENGTH_SHORT).show();
				}
//				getData(cd.Ming);
				Progress(C);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						add();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(0);
					}
				}).start();
			}
		});
	}
	
	public void add() {
		int jumpTime = 0;
		for(int i=0; i<C; i++) {
			if(stop==true) {
				break;
			}
			getname();
			String phoneNumber = topnumbers[topnumber];
			for(int ii=1;ii<9;ii++){
				int n = rand.nextInt(10);
				phoneNumber = phoneNumber+n;
			}
			addContacts(Name,phoneNumber);
			jumpTime++;
			progressDialog.setProgress(jumpTime);
		}
	}
	
	public String getData(String str) { 
		int xing = rand.nextInt((str).length());
//		System.out.println("字符串个数:"+(str).length());
		String[] list = (str).split("");
//		System.out.println("随机取得姓:"+list[xing]);
		return list[xing];
	}
	
	public String getSex() {
		String a = null;
		for(int i=0; i<sex.getChildCount(); i++) {
			RadioButton rb = (RadioButton)sex.getChildAt(i);
			if(rb.isChecked()) {
				a = rb.getText().toString().trim();
			    break;
			}
		}
		return a;
	}
	
	public void getname() {
		sb = new StringBuffer();
		switch (getCONDITION()) {
		case 1:
			for(int i=0; i<NL; i++) {
				if(i==0){
					sb.append(getData(cd.Xing));
				}else {
					sb.append(getData(cd.Ming));
				}
			}
			break;
		case 2:
			for(int i=0; i<NL; i++) {
				if(i==0){
					sb.append(getData(cd.EngName));
				}else {
					sb.append(getData(cd.engname));
				}
			}
			break;
        case 3:
        	for(int i=0; i<NL; i++) {
        		sb.append(getData(cd.ZiFu));
        	}
			break;	
        case 4:	
        	String[] list ={getData(cd.Xing),getData(cd.Ming),getData(cd.EngName),getData(cd.engname),getData(cd.ZiFu)};
        	for(int i=0; i<NL; i++) {
        		sb.append(list[rand.nextInt(5)]);
        	}
        	break;
		}
		Name = sb.toString();
	}
	
	public int getCONDITION() {
		if(getSex().equals("中文")) {
			run = 1;
		}
		if(getSex().equals("英文")) {
			run = 2;
		}
		if(getSex().equals("特殊字符")) {
			run = 3;
		}
		if(getSex().equals("混合")) {
			run = 4;
		}
		if(getSex().equals("随机类型")) {
			run = rand.nextInt(4)+1;
		}
		return run;
	}
//	public String getName() {
//		int ming = rand.nextInt((cd.Ming).length());
//		String[] list = (cd.Xing).split("");
//		return list[ming];
//	}
//	
//	public String getZifu() {
//		int zifu = rand.nextInt((cd.ZiFu).length());
//		String[] list = (cd.Xing).split("");
//		return list[zifu];
//	}
//	
//	public String getZifu() {
//		int zifu = rand.nextInt((cd.ZiFu).length());
//		String[] list = (cd.Xing).split("");
//		return list[zifu];
//	}
	
	
	public void addContacts(String name,String number) {
        ContentValues values = new ContentValues ();     
        Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI,values);     
        long rawContactsId = ContentUris.parseId(rawContactUri);     
        values.clear();     
        values.put(StructuredName.RAW_CONTACT_ID,rawContactsId);     
        values.put(Data.MIMETYPE,StructuredName.CONTENT_ITEM_TYPE);     
        values.put(StructuredName.DISPLAY_NAME,name);     
        getContentResolver().insert(Data.CONTENT_URI,values);     
        values.clear();     
        values.put(Phone.RAW_CONTACT_ID,rawContactsId);     
        values.put(Data.MIMETYPE,Phone.CONTENT_ITEM_TYPE);     
        values.put(Phone.NUMBER,number);     
        getContentResolver().insert(Data.CONTENT_URI,values);
	}
	
	public void Progress(int time) {
    	progressDialog = new ProgressDialog(ContactsActivity.this);
        progressDialog.setMax(time);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		progressDialog.setTitle("正在生成联系人...");
		progressDialog.setCancelable(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",  
	            new DialogInterface.OnClickListener() {  
	                @Override  
	                public void onClick(DialogInterface dialog, int which) {  
	                    // TODO Auto-generated method stub  
	                	stop = true;
	                }  
	            });  
		progressDialog.show();
		stop = false;
    }
	
	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            Toast.makeText(ContactsActivity.this, "程序结束", Toast.LENGTH_LONG).show();
        }  
    };  
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
//	    for(String ss:name) {
//	    	addContacts(ss,"10086");
//	    }
		   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

}
