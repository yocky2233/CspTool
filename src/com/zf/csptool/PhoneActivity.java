package com.zf.csptool;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PhoneActivity extends Activity {
	String []topnumbers = { "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "150", "180", "186", "189"};
	Button btu;
	Button btu2;
	EditText phoneNumber;
	EditText addNumber;
	RadioGroup sex;
	int judge = 0;
	String NUMBER;
	int addTime;
	int CONDITION;
	int topnumber;
	Random rand;
	boolean stop = false;
	ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rand =new Random();
		topnumber = rand.nextInt(topnumbers.length);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phone);
		btu = (Button)findViewById(R.id.btu);
		btu2 = (Button)findViewById(R.id.generate);
		phoneNumber = (EditText)findViewById(R.id.phoneNumber);
		addNumber = (EditText)findViewById(R.id.addNumber);
		TextView topName = (TextView)findViewById(R.id.topNme);
		sex = (RadioGroup)findViewById(R.id.radiogroup);
		topName.setText("生成通话记录");
		
		btu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btu.getText().toString().equals(" ")) {
					btu.setBackgroundResource(R.drawable.toggle_on);
					judge = 1;
//					System.out.println("选中");
				}else {
					btu.setBackgroundResource(R.drawable.toggle_off);
					judge = 0;
//					System.out.println("取消");
				}
			}
		});
		
		btu2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if((phoneNumber.getText().toString()).equals("")) {
					NUMBER = "10086";
				}else{
					NUMBER = phoneNumber.getText().toString();
				}
				if((addNumber.getText().toString()).equals("")) {
					addTime = 100;
				}else{
					String str = addNumber.getText().toString();
					addTime = Integer.parseInt(str);
				}
				Progress(addTime);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						addRecords();
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
	
	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            Toast.makeText(PhoneActivity.this, "程序结束", Toast.LENGTH_LONG).show();
        }  
    };  
	
	public int getCONDITION() {
		if(getSex().equals("已接电话")) {
			CONDITION = 1;
		}
		if(getSex().equals("已拨电话")) {
			CONDITION = 2;
		}
		if(getSex().equals("未接电话")) {
			CONDITION = 3;
		}
		if(getSex().equals("随机类型")) {
			CONDITION = rand.nextInt(3)+1;
		}
		return CONDITION;
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
	
	private void insertCallLog(String number,int condition)
	{
	    // TODO Auto-generated method stub
		Random rand =new Random();
		int i,ii;
		i = rand.nextInt(6000)+1;
		ii = rand.nextInt(2);
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("number", number);
		localContentValues.put("type", condition);
		localContentValues.put("date", Long.valueOf(System.currentTimeMillis()));
		localContentValues.put("duration", i);
		localContentValues.put("new", ii);
		getContentResolver().insert(CallLog.Calls.CONTENT_URI, localContentValues);
	}
	
	public void Progress(int time) {
    	progressDialog = new ProgressDialog(PhoneActivity.this);
        progressDialog.setMax(time);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		progressDialog.setTitle("正在生成通话记录...");
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
	
	public void addRecords() {
		int jumpTime = 0;
		if (judge==1) {
			for(int i=0; i<addTime; i++) {
				if(stop==true) {
    				break;
    			}
				getCONDITION();
				insertCallLog(NUMBER,CONDITION);
				jumpTime++;
				progressDialog.setProgress(jumpTime);
			}
		}else {
			for(int i=0; i<addTime; i++) {
				if(stop==true) {
    				break;
    			}
				if(i==0) {
					getCONDITION();
					insertCallLog(NUMBER,CONDITION);
				}else {
					String phoneNumber = topnumbers[topnumber];
					for(int ii=1;ii<9;ii++){
	    				int n = rand.nextInt(10);
	    				phoneNumber = phoneNumber+n;
	    			}
					getCONDITION();
					insertCallLog(phoneNumber,CONDITION);
				}
				jumpTime++;
				progressDialog.setProgress(jumpTime);
			}
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		insertCallLog("1382730358",2);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phone, menu);
		return true;
	}

}
