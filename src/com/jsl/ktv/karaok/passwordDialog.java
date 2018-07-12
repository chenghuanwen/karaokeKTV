package com.jsl.ktv.karaok;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.util.EncodingUtils;
import android.os.Handler;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.text.InputType;
import com.jsl.ktv.R;
import android.view.inputmethod.InputMethodManager;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.KeyEvent;

public class passwordDialog extends Dialog
{
    // 定义回调事件，用于dialog的点击事�?   
	private String name;
    private TextView pass_info;
	private EditText etName;
	private String password = "";
	private String ps_length = "";
    private Button key_0,key_1,key_2,key_3,key_4,key_5,key_6,key_7,key_8,key_9,key_del,key_ok,key_back;
    private String read_password = "";
    private String PASSWOER_FILE = "/mnt/sdcard/jlink/sys_file/etc/passwd.txt";
	private Context mcontext;
	private Handler mHandler;

    public passwordDialog(Context context, String name)
    {
        super(context);
		mcontext = context;
        this.name = name;
    }
    
    public passwordDialog(Context context,String name,Handler handle,int them){
    	super(context, R.style.MyDialog);
    	mcontext = context;
		mHandler = handle;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.password);
        etName = (EditText) findViewById(R.id.edit_input);
	//	pass_info = (TextView) findViewById(R.id.pass_info);
        key_0 = (Button) findViewById(R.id.key_0);
        key_1 = (Button) findViewById(R.id.key_1);
        key_2 = (Button) findViewById(R.id.key_2);
        key_3 = (Button) findViewById(R.id.key_3);
        key_4 = (Button) findViewById(R.id.key_4);
        key_5 = (Button) findViewById(R.id.key_5);
        key_6 = (Button) findViewById(R.id.key_6);
        key_7 = (Button) findViewById(R.id.key_7);
        key_8 = (Button) findViewById(R.id.key_8);
        key_9 = (Button) findViewById(R.id.key_9);
        key_del = (Button) findViewById(R.id.key_del);
        key_ok = (Button) findViewById(R.id.key_ok);
        key_back = (Button) findViewById(R.id.key_back);
		key_0.setOnClickListener(clickListener);
		key_1.setOnClickListener(clickListener);
		key_2.setOnClickListener(clickListener);
		key_3.setOnClickListener(clickListener);
		key_4.setOnClickListener(clickListener);
		key_5.setOnClickListener(clickListener);
		key_6.setOnClickListener(clickListener);
		key_7.setOnClickListener(clickListener);
		key_8.setOnClickListener(clickListener);
		key_9.setOnClickListener(clickListener);
        key_del.setOnClickListener(clickListener);
        key_ok.setOnClickListener(clickListener);
        key_back.setOnClickListener(clickListener);
        //etName.addTextChangedListener(textWatcher);
		   etName.setInputType(InputType.TYPE_NULL);
		   key_ok.requestFocus();
		 //  InputMethodManager imm = (InputMethodManager)mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
          // imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }

    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
			
			switch(v.getId()){
				case R.id.key_0:
				keydown(0);
					break;

				case R.id.key_1:
				keydown(1);
					break;

				case R.id.key_2:
				keydown(2);
					break;

				case R.id.key_3:
				keydown(3);
					break;

				case R.id.key_4:
				keydown(4);
					break;

				case R.id.key_5:
				keydown(5);
					break;
					
				case R.id.key_6:
				keydown(6);
					break;
					
				case R.id.key_7:
				keydown(7);
					break;

				case R.id.key_8:
				keydown(8);
					break;

				case R.id.key_9:
				keydown(9);
					break;

				case R.id.key_del:
					if(password == null){
						password = "";
						ps_length = "";
						etName.setText(ps_length);
					}else if(password.length()>0){	
						password = password.substring(0,password.length()-1);
						ps_length = ps_length.substring(0,ps_length.length()-1);
						etName.setText(ps_length);
						Log.v("password", "password===="+password);
						}else {
						 password = "";
						 ps_length = "";
						 etName.setText(ps_length);	
						}
						
					break;

				case R.id.key_ok:
				keyOk();
					break;

				case R.id.key_back:
					dismiss();
					break;

				default:
					break;
				
			}
        }
    };
	
	
	
	 public void keydown(int key) {
    	if(password == null){
			password = password + key+"".trim();
			ps_length = ps_length + "*";
			etName.setText(ps_length);
			//pass_info.setText("");
			Log.v("password", "password===="+password);
		}else if(password.length()<6){
			password = password + key+"".trim();
			ps_length = ps_length + "*";
			etName.setText(ps_length);
			//pass_info.setText("");
			Log.v("password", "password===="+password);
		}
	}
    
    
    public void keyOk() {
		if(password==null){
						
					}else{
						if(isFileExist(PASSWOER_FILE)){
							read_password = readFileSdcard(PASSWOER_FILE);
							}else{
							read_password = "123456";
							}
						if(password.equals(read_password)){
							
							if(name.equals("ktv_setting")){
								KTVSettingDialog password_dialog = new KTVSettingDialog(mcontext,"",mHandler,R.style.MyDialog);
								Window window = password_dialog.getWindow();
								/*LayoutParams params = new LayoutParams();
								params.gravity = Gravity.TOP;
								window.setAttributes(params);*/
								password_dialog.show();	
							}else if(name.equals("songs_setting")){
								SongsManagerDialog songsDialog = new SongsManagerDialog(mcontext,R.style.MyDialog);
								songsDialog.show();
								}else {
									Intent intent = new Intent();
									intent.setClassName("com.android.hisiliconsetting", "com.android.hisiliconsetting.MainActivity");
									mcontext.startActivity(intent);
								}
							dismiss();
						}else{
							Log.v("password", "password====false");
							password = "";
							ps_length = "";
							etName.setText(ps_length);	
							etName.setHint(mcontext.getResources().getString(R.string.key_info2));
							//pass_info.setText(R.string.key_info2);
						}
					}
	}
    
   @Override
   public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
	   switch (keyCode) {
	case KeyEvent.KEYCODE_0:
		keydown(0);
		break;
    case KeyEvent.KEYCODE_1:
    	keydown(1);
		break;
    case KeyEvent.KEYCODE_2:
    	keydown(2);
	     break;
      case KeyEvent.KEYCODE_3:
    	  keydown(3);
	   break;
     case KeyEvent.KEYCODE_4:
    	 keydown(4);
	   break;
    case KeyEvent.KEYCODE_5:
    	keydown(5);
	   break;
    case KeyEvent.KEYCODE_6:
    	keydown(6);
	    break;
    case KeyEvent.KEYCODE_7:
    	keydown(7);
	  break;
    case KeyEvent.KEYCODE_8:
    	keydown(8);
	  break;
    case KeyEvent.KEYCODE_9:
    	keydown(9);
	   break;
    case KeyEvent.KEYCODE_ENTER:
    		keyOk();
	   break;

	default:
		break;
	}
	return false;
   }
	
	
    @Override
    public void dismiss() {
        super.dismiss();
    }
    
  //判断文件夹是否存�?  存在 true,  不存�?false
  	private boolean isFileExist(String strFolder) {
  		File file = new File(strFolder);
  		if (file.exists()) {
  			return true;
  		}
  		return false;
  	}
    
    public String readFileSdcard(String fileName) {
        String res = ""; 
        try {  

            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available(); 
            byte[] buffer = new byte[length]; 
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close(); 
        }  

        catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return res;  

    } 
    
    
    /*private TextWatcher textWatcher = new TextWatcher()
    {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count)
        {
            //System.out.println("-1-onTextChanged-->"
            //        + et_search.getText().toString() + "<--");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after)
        {
            //System.out.println("-2-beforeTextChanged-->"
            //        + et_search.getText().toString() + "<--");
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            //这种方式是每输入�?��字符就往下传，动态改�?            JNILib.searchKey(s.toString());
        }
    };*/
}