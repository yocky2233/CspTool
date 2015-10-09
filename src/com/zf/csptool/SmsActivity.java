package com.zf.csptool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import com.zf.csptool.R;
import com.zf.csptool.Telephony.Mms;
import com.zf.csptool.Telephony.Mms.Addr;
import com.zf.csptool.Telephony.Mms.Part;
import com.zf.csptool.Telephony.Threads;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SmsActivity extends Activity 
{
	private EditText etNumber;
	private Button btnSend;
	private Button btnSend1;
	private Button btnSend2;
	private Button btnSend3;
	private Button btnCancel;
	private Button handoverBtn;
	private EditText etinput;
	private EditText etTime;
	private EditText etText;
	private EditText etmms;
	private EditText etHHS;
	private ProgressDialog pd;
	ProgressDialog progressDialog;
	int time;
	boolean stop = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sms);
        etNumber = (EditText)findViewById(R.id.et_number);
        btnSend = (Button)findViewById(R.id.btn_send);
        etinput = (EditText)findViewById(R.id.not_default_app);
        etTime = (EditText)findViewById(R.id.smstime);
        etText = (EditText)findViewById(R.id.smsText);
        etHHS = (EditText)findViewById(R.id.smsHHS);
        btnSend1 = (Button)findViewById(R.id.btn_send1);
        etmms = (EditText)findViewById(R.id.mmsNumber);
        btnSend2 = (Button)findViewById(R.id.btn_send2);
        btnSend3 = (Button)findViewById(R.id.btn_send3);
        handoverBtn = (Button)findViewById(R.id.Handover);
        TextView topName = (TextView)findViewById(R.id.topNme);
        etText.setEnabled(true);
        etinput.requestFocus();
        topName.setText("生成信息");
        handoverBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int version = Integer.valueOf(android.os.Build.VERSION.SDK);
				if(version>=19){
					Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);  
					intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.android.mms");  
					startActivity(intent);
				}
				finish();
			}
        	
        });
        btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String Number = etNumber.getText().toString().trim();
				try {
					time = Integer.parseInt(Number);
				}catch(Exception e) {
					e.printStackTrace();
				}
				if(Number.equals("")){
					Toast.makeText(SmsActivity.this,"生成短信条数不可以为空", 0).show();
	            }
				Progress(time);
                new Thread(new Runnable() {  
                    @Override  
                    public void run() {  
                    	inSms(); 
                    	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        handler.sendEmptyMessage(0); 
                    }  
                }).start();
			}
		});
        
        btnSend1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String SmsHHS = etHHS.getText().toString().trim();
				String SmsNumber = etTime.getText().toString().trim();
				try {
					time = Integer.parseInt(SmsHHS);
				}catch(Exception e) {
					e.printStackTrace();
				}
				if(SmsHHS.equals("")){
					Toast.makeText(SmsActivity.this, "生成会话数不可以为空", 0).show();
        		}else if(SmsNumber.equals("")){
        			Toast.makeText(SmsActivity.this,"生成短信条数不可以为空", 0).show();
        		}
                Progress(time);
                new Thread(new Runnable() {  
                    @Override  
                    public void run() {  
                    	zdySms(); 
                    	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        handler.sendEmptyMessage(0); 
                    }  
                }).start();
        	} 	
        });

        
        btnSend2.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		String smsNumber2 = etmms.getText().toString().trim();
        		try {
					time = Integer.parseInt(smsNumber2);
				}catch(Exception e) {
					e.printStackTrace();
				}
        		if(smsNumber2.equals("")){
        			Toast.makeText(SmsActivity.this, "生成彩信条数不可以为空", 0).show();
        		}
                Progress(time);
                new Thread(new Runnable() {  
                    @Override  
                    public void run() {  
                    	huihuaMms();
                    	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        handler.sendEmptyMessage(0);
                    }  
                }).start();	
			}
        });
        
        
        btnSend3.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		String smsNumber3 = etmms.getText().toString().trim();
        		try {
					time = Integer.parseInt(smsNumber3);
				}catch(Exception e) {
					e.printStackTrace();
				}
        		if(smsNumber3.equals("")){
        			Toast.makeText(SmsActivity.this, "生成彩信条数不可以为空", 0).show();
        		}
                Progress(time);
                new Thread(new Runnable() {  
                    @Override  
                    public void run() {  
                    	huihuapaoMms();
                    	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        handler.sendEmptyMessage(0); 
                    }  
                }).start();	
			}
        });
    }
    
    public void Progress(int time) {
    	progressDialog = new ProgressDialog(SmsActivity.this);
        progressDialog.setMax(time);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		progressDialog.setTitle("正在生成信息...");
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
    
    private void inSms() {
    	try {
    		String Number = etNumber.getText().toString().trim();
    		int time = Integer.parseInt(Number);
    		int i = 0;
    		int jumpTime = 0;
    		while(i<time){
    			if(stop==true) {
    				break;
    			}
    			Random rand =new Random();
    			String []topnumbers = { "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "150", "180", "186", "189"};
    			int topnumber = rand.nextInt(topnumbers.length);
    			String phoneNumber = topnumbers[topnumber];
    			String []types = {"1","2"};
    			int typetime = rand.nextInt(types.length);
    			String type = types[typetime];
    			for(int ii=1;ii<9;ii++){
    				int n = rand.nextInt(10);
    				phoneNumber = phoneNumber+n;
    			}
    		    String smsContent = "随机短信test";
    		    writeToDataBase(phoneNumber, smsContent, type);
    		    i++;
    		    jumpTime++;
    		    progressDialog.setProgress(jumpTime);
    		}
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } 
    }
    
	private void zdySms() {
		try {
			String Number1 = etinput.getText().toString().trim();
			String SmsNumber = etTime.getText().toString().trim();
			String SmsText = etText.getText().toString().trim();
			String SmsHHS = etHHS.getText().toString().trim();
			Random rand = new Random();
			int intHHS = Integer.parseInt(SmsHHS);
			int jumpTime = 0;
			for (int hhs = 0; hhs < intHHS; hhs++) {
				if(stop==true) {
    				break;
    			}
				if (hhs > 0) {
					String[] topnumbers = { "130", "131", "132", "133", "134",
							"135", "136", "137", "138", "139", "150", "180",
							"186", "189" };
					int topnumber = rand.nextInt(topnumbers.length);
					String phoneNumber = topnumbers[topnumber];
					for (int ii = 1; ii < 9; ii++) {
						int n = rand.nextInt(10);
						phoneNumber = phoneNumber + n;
					}
					int intNumber = Integer.parseInt(SmsNumber);
					for (int i = 0; i < intNumber; i++) {
						if(stop==true) {
		    				break;
		    			}
						String[] types2 = { "1", "2" };
						int typetime2 = rand.nextInt(types2.length);
						String type = types2[typetime2];
						writeToDataBase(phoneNumber, SmsText, type);
					}
				} else {
					int intNumber = Integer.parseInt(SmsNumber);
					for (int i = 0; i < intNumber; i++) {
						if(stop==true) {
		    				break;
		    			}
						String[] types = { "1", "2" };
						int typetime = rand.nextInt(types.length);
						String type = types[typetime];
						writeToDataBase(Number1, SmsText, type);
					}
				}
				jumpTime++;
				progressDialog.setProgress(jumpTime);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	private void huihuaMms() {
		try {
			Random rand = new Random();
			String smsNumber2 = etmms.getText().toString().trim();
			int intNumber = Integer.parseInt(smsNumber2);
			int jumpTime = 0;
			for (int times = 0; times < intNumber; times++) {
				if(stop==true) {
    				break;
    			}
				String[] topnumbers = { "130", "131", "132", "133", "134",
						"135", "136", "137", "138", "139", "150", "180", "186",
						"189" };
				int topnumber = rand.nextInt(topnumbers.length);
				String phoneNumber = topnumbers[topnumber];
				for (int ii = 1; ii < 9; ii++) {
					int n = rand.nextInt(10);
					phoneNumber = phoneNumber + n;
					System.out.println(phoneNumber);
				}
				long longNumr = Long.parseLong(phoneNumber);
				insert(Mms.MESSAGE_BOX_INBOX, AttachmentType.IMAGE, longNumr);
				jumpTime++;
				progressDialog.setProgress(jumpTime);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	private void huihuapaoMms() {
		try {
			Random rand = new Random();
			String smsNumber3 = etmms.getText().toString().trim();
			String[] topnumbers = { "130", "131", "132", "133", "134", "135",
					"136", "137", "138", "139", "150", "180", "186", "189" };
			int topnumber = rand.nextInt(topnumbers.length);
			String phoneNumber = topnumbers[topnumber];
			for (int ii = 1; ii < 9; ii++) {
				int n = rand.nextInt(10);
				phoneNumber = phoneNumber + n;
			}
			long longNumr = Long.parseLong(phoneNumber);

			int intNumber = Integer.parseInt(smsNumber3);
			int jumpTime = 0;
			for (int time = 0; time < intNumber; time++) {
				if(stop==true) {
    				break;
    			}
				insert(Mms.MESSAGE_BOX_INBOX, AttachmentType.IMAGE, longNumr);
				jumpTime++;
				progressDialog.setProgress(jumpTime);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
    
    
    
    
    Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            Toast.makeText(SmsActivity.this, "程序结束", Toast.LENGTH_LONG).show();
        }  
    };  
    
    

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
    	super.onResume();
    	int version = Integer.valueOf(android.os.Build.VERSION.SDK);
    	if(version>=19){
    		final String myPackageName = getPackageName(); 
        	String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this); 
        	if (!defaultSmsApp.equals(myPackageName)) {
        		handoverBtn.setVisibility(View.GONE);
        		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("更改默认SMS").setMessage("android4.4以上需设置为默认SMS才能正常使用")
        		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        		Intent intent =  
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        		intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,   
                        myPackageName);  
        		startActivity(intent); 
        		}})
        		.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int which) {
                finish();
                }})
                .create();
        		alertDialog.setCancelable(false);
        		alertDialog.show();
        		//.show();
        	}else {
        		handoverBtn.setVisibility(View.VISIBLE);
        	}
    	}else {
    		handoverBtn.setVisibility(View.GONE);
    	}
    }
    
    
    
    private void writeToDataBase(String phoneNumber, String smsContent, String type)
    {
    	ContentValues values = new ContentValues();
        values.put("address",  phoneNumber);
        values.put("body", smsContent);
        values.put("type", type);  
        values.put("read", "1");
        getContentResolver().insert(Uri.parse("content://sms/sent"), values);//inbox
    }
    
    
    private static final String IMAGE_NAME_1 = "image_1.jpeg";
    private static final String IMAGE_NAME_2 = "image_2.jpeg";
    private static final String FROM_NUM= "";
    private static final String IMAGE_CID = "<img_cid>";
    private static final String AUDIO_NAME = "audio_1.ogg";
    private static final String AUDIO_CID = "<300k>";
    private static final String VIDEO_NAME = "video_1.mp4";
    private static final String VIDEO_CID = "<300k>";
    private static final String SMIL_TEXT_VIDEO = "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/></layout></head><body><par dur=\"21500ms\"><text src=\"text_1.txt\" region=\"Text\"/><VIDEO src=\""
            + VIDEO_NAME + "\" dur=\"21500\" /></par></body></smil>";
    private static final String SMIL_TEXT_AUDIO = "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/></layout></head><body><par dur=\"21500ms\"><text src=\"text_1.txt\" region=\"Text\"/><audio src=\""
            + AUDIO_NAME + "\" dur=\"21500\" /></par></body></smil>";
    private static final String SMIL_TEXT_IMAGE = "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/></layout></head><body><par dur=\"2000ms\"><text src=\"text_1.txt\" region=\"Text\"/><img src=\"%s\" region=\"Image\"/></par><par dur=\"2000ms\"><text src=\"text_2.txt\" region=\"Text\"/><img src=\"%s\" region=\"Image\"/></par></body></smil>";
    
    private void insert(int msgBoxType, AttachmentType type, long idx) {
        long threadId = Threads.getOrCreateThreadId(SmsActivity.this, FROM_NUM + idx);
        Log.e("", "threadId = " + threadId);

        String name_1 = null;
        String name_2 = null;
        String name_3 = null;
        String smil_text = null;
        ContentValues cv_part_1 = null;
        ContentValues cv_part_2 = null;
        ContentValues cv_part_3 = null;

        switch (type) {
            case IMAGE:
                name_1 = IMAGE_NAME_1;
//                name_2 = IMAGE_NAME_2;
                name_2 = AUDIO_NAME;
                name_3 = VIDEO_NAME;
                smil_text = String.format(SMIL_TEXT_IMAGE,SMIL_TEXT_VIDEO,SMIL_TEXT_AUDIO, name_1, name_2,name_3);
//                smil_text = String.format(SMIL_TEXT_IMAGE, name_1, name_2);
                cv_part_1 = createPartRecord(0, "image/jpeg", name_1, IMAGE_CID, name_1, null, null);
//                cv_part_2 = createPartRecord(0, "image/jpeg", name_2, IMAGE_CID.replace("cid", "cid_2"), name_2, null, null);
                cv_part_2 = createPartRecord(0, "audio/ogg", name_2, AUDIO_CID, name_2, null, null);
                cv_part_3 = createPartRecord(0, "video/mp4", name_3, VIDEO_CID, name_3, null, null);//video/3gpp
                break;
            case AUDIO:
                name_1 = AUDIO_NAME;
                smil_text = String.format(SMIL_TEXT_AUDIO, name_1);
                cv_part_1 = createPartRecord(0, "audio/ogg", AUDIO_NAME, AUDIO_CID, AUDIO_NAME, null, null);
                break;
            case VIDEO:
                name_1 = VIDEO_NAME;
                smil_text = String.format(SMIL_TEXT_VIDEO, name_1);
                cv_part_1 = createPartRecord(0, "video/mp4", VIDEO_NAME, VIDEO_CID, VIDEO_NAME, null, null);//video/3gpp
                break;
        }

        // make MMS record
        ContentValues cvMain = new ContentValues();
        cvMain.put(Mms.THREAD_ID, threadId);

        cvMain.put(Mms.MESSAGE_BOX, msgBoxType);
        cvMain.put(Mms.READ, 1);
        cvMain.put(Mms.DATE, System.currentTimeMillis() / 1000);
        cvMain.put(Mms.SUBJECT, "my subject ");
        
        cvMain.put(Mms.CONTENT_TYPE, "application/vnd.wap.multipart.related");
        cvMain.put(Mms.MESSAGE_CLASS, "personal");
        cvMain.put(Mms.MESSAGE_TYPE, 132); // Retrive-Conf Mms
//        cvMain.put(Mms.MESSAGE_SIZE, getSize(name_1) + getSize(name_2) + 512);  // suppose have 512 bytes extra text size
        cvMain.put(Mms.PRIORITY, String.valueOf(129));
        cvMain.put(Mms.READ_REPORT, String.valueOf(129));
        cvMain.put(Mms.DELIVERY_REPORT, String.valueOf(129));
        Random random = new Random();
        cvMain.put(Mms.MESSAGE_ID, String.valueOf(random.nextInt(100000)));
        cvMain.put(Mms.TRANSACTION_ID, String.valueOf(random.nextInt(120000)));

        long msgId = 0;
        try {
            msgId = ContentUris.parseId(getContentResolver().insert(Mms.CONTENT_URI, cvMain));
        } catch (Exception e) {
            Log.e("", "insert pdu record failed", e);
            return;
        }

        // make parts
        ContentValues cvSmil = createPartRecord(-1, "application/smil", "smil.xml", "<siml>", "smil.xml", null, smil_text);
        cvSmil.put(Part.MSG_ID, msgId);

        cv_part_1.put(Part.MSG_ID, msgId);
//        cv_part_2.put(Part.MSG_ID, msgId);

        ContentValues cv_text_1 = createPartRecord(0, "text/plain", "text_1.txt", "<text_1>", "text_1.txt", null, null);
        cv_text_1.put(Part.MSG_ID, msgId);
        cv_text_1.remove(Part.TEXT);
        cv_text_1.put(Part.TEXT, "slide 1 text");
        cv_text_1.put(Part.CHARSET, "106");

        ContentValues cv_text_2 = createPartRecord(0, "text/plain", "text_2.txt", "<text_2>", "text_2.txt", null, null);
        cv_text_2.put(Part.MSG_ID, msgId);
        cv_text_2.remove(Part.TEXT);
        cv_text_2.put(Part.TEXT, "slide 2 audio");
        cv_text_2.put(Part.CHARSET, "106");
        
        ContentValues cv_text_3 = createPartRecord(0, "text/plain", "text_3.txt", "<text_3>", "text_3.txt", null, null);
        cv_text_3.put(Part.MSG_ID, msgId);
        cv_text_3.remove(Part.TEXT);
        cv_text_3.put(Part.TEXT, "slide 3 video");
        cv_text_3.put(Part.CHARSET, "106");

        // insert parts
        Uri partUri = Uri.parse("content://mms/" + msgId + "/part");
        try {
            getContentResolver().insert(partUri, cvSmil);

            Uri dataUri_1 = getContentResolver().insert(partUri, cv_part_1);
            if (!copyData(dataUri_1, name_1)) {
                Log.e("", "write " + name_1 + " failed");
                return;
            }
            getContentResolver().insert(partUri, cv_text_1);

            Uri dataUri_2 = getContentResolver().insert(partUri, cv_part_2);
            if (!copyData(dataUri_2, name_2)) {
                Log.e("", "write " + name_2 + " failed");
                return;
            }
            getContentResolver().insert(partUri, cv_text_2);
            
            Uri dataUri_3 = getContentResolver().insert(partUri, cv_part_3);
            if (!copyData(dataUri_3, name_3)) {
                Log.e("", "write " + name_3 + " failed");
                return;
            }
            getContentResolver().insert(partUri, cv_text_3);
        } catch (Exception e) {
            Log.e("", "insert part failed", e);
            return;
        }

        // to address
        ContentValues cvAddr = new ContentValues();
        cvAddr.put(Addr.MSG_ID, msgId);
        cvAddr.put(Addr.ADDRESS, "703");
        cvAddr.put(Addr.TYPE, "151");
        cvAddr.put(Addr.CHARSET, 106);
        getContentResolver().insert(Uri.parse("content://mms/" + msgId + "/addr"), cvAddr);

        // from address
        cvAddr.clear();
        cvAddr.put(Addr.MSG_ID, msgId);
        cvAddr.put(Addr.ADDRESS, FROM_NUM + idx);
        cvAddr.put(Addr.TYPE, "137");
        cvAddr.put(Addr.CHARSET, 106);
        getContentResolver().insert(Uri.parse("content://mms/" + msgId + "/addr"), cvAddr);
    }

    private int getSize(final String name) {
        InputStream is = null;
        int size = 0;

        try {
            is = getAssets().open(name);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = is.read(buffer)) != -1;)
                size += len;
            return size;
        } catch (FileNotFoundException e) {
            Log.e("", "failed to found file?", e);
            return 0;
        } catch (IOException e) {
            Log.e("", "write failed..", e);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Log.e("", "close failed...");
            }
        }
        return 0;
    }

    private ContentValues createPartRecord(int seq, String ct, String name, String cid, String cl, String data,
            String text) {
        ContentValues cv = new ContentValues(8);
        cv.put(Part.SEQ, seq);
        cv.put(Part.CONTENT_TYPE, ct);
        cv.put(Part.NAME, name);
        cv.put(Part.CONTENT_ID, cid);
        cv.put(Part.CONTENT_LOCATION, cl);
        if (data != null)
            cv.put(Part._DATA, data);
        if (text != null)
            cv.put(Part.TEXT, text);
        return cv;
    }

    private boolean copyData(final Uri dataUri, final String name) {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = getAssets().open(name);
            output = getContentResolver().openOutputStream(dataUri);

            byte[] buffer = new byte[1024];
            for (int len = 0; (len = input.read(buffer)) != -1;) 
                output.write(buffer, 0, len);
        } catch (FileNotFoundException e) {
            Log.e("", "failed to found file?", e);
            return false;
        } catch (IOException e) {
            Log.e("", "write failed..", e);
            return false;
        } finally {
            try {
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {
                Log.e("", "close failed...");
                return false;
            }
        }
        return true;
    }
    
    
    enum AttachmentType {
        IMAGE, AUDIO, VIDEO;
    }
    

    }

