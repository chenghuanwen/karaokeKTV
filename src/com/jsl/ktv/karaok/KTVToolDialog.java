package com.jsl.ktv.karaok;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.os.SystemClock;
import android.os.Handler;
import org.apache.http.util.EncodingUtils;
import android.util.Log;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jsl.ktv.R;
import com.player.boxplayer.karaok.JNILib;
public class KTVToolDialog extends Dialog
{
    // 定义回调事件，用于dialog的点击事�?  
	private String name;
    private Button password_button,setting_button,songs_button;
    private Context mcontext;
	private String configRoot = "/mnt/sdcard/jlink/sys_file/etc/";
    private String VOD_CONNFIG="/mnt/sdcard/jlink/sys_file/etc/vod_config.txt";
	private String SCOLL_FILE = "/mnt/sdcard/jlink/sys_file/etc/message_scoll.txt";
    private boolean isNeedPass = true;
	private int jLanguage = 0;
	private LinearLayout btn1,btn2,btn3;
	private Handler mHandler;
    public KTVToolDialog(Context context, String name)
    {
        super(context);
        mcontext = context;
        this.name = name;
    }

    public KTVToolDialog(Context context,String name,Handler handle,int them){
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
        setContentView(R.layout.tool);
		jLanguage=JNILib.getGlobalLanguage();
		
        //WindowManager.LayoutParams wmParams = this.getWindow().getAttributes();  
        //wmParams.format = PixelFormat.TRANSPARENT;  内容全�?�? 
        //wmParams.format = PixelFormat.TRANSLUCENT; 
        password_button = (Button) findViewById(R.id.password_button);
		setting_button = (Button) findViewById(R.id.setting_button);
		songs_button = (Button) findViewById(R.id.geku_button);
		
		password_button.setText(VideoString.password_setting[jLanguage]);
		setting_button.setText(VideoString.setting_tittle[jLanguage]);
		songs_button.setText(VideoString.songs_manager[jLanguage]);
		
		
		btn1 = (LinearLayout)findViewById(R.id.ll1);
		btn2 = (LinearLayout)findViewById(R.id.ll2);
		btn3 = (LinearLayout)findViewById(R.id.ll3);
        btn1.setOnClickListener(clickListener);
        btn2.setOnClickListener(clickListener);
        btn3.setOnClickListener(clickListener);
        //etName.addTextChangedListener(textWatcher);
		//Log.v("setting_ms","======"+setting_ms);	
        
		
		copy2SD(configRoot, "vod_config.txt");
		copy2SD(configRoot, "message_scoll.txt");
		SystemClock.sleep(500);
        
        if(isFileExist(VOD_CONNFIG)){
        	String  setting_ms = readFileSdcard(VOD_CONNFIG);
        	String pw = setting_ms.substring(setting_ms.indexOf("exitapp = ")+10,setting_ms.indexOf("exitapp = ")+11);
        	if ("0".equals(pw)) {
				isNeedPass = false;
			} else {
				isNeedPass = true;
			}
            }
        
        
    }
	
	public void dismiss(){
		super.dismiss();
	}
	
    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
			
			switch(v.getId()){
				case R.id.ll1:
					dismiss();
					passwordSettingDialog passwordSetting_dialog = new passwordSettingDialog(mcontext,"",R.style.MyDialog);
					passwordSetting_dialog.show();
					break;

				case R.id.ll2:
					dismiss();
					if (isNeedPass) {						
						passwordDialog password_dialog = new passwordDialog(mcontext,"ktv_setting",mHandler,R.style.MyDialog);
						password_dialog.show();
					} else {
						KTVSettingDialog ktvSettingDialog = new KTVSettingDialog(mcontext, "",mHandler,R.style.MyDialog);
						ktvSettingDialog.show();
					}
					break;
				case R.id.ll3:
					dismiss();
					if (isNeedPass) {						
						passwordDialog songs_dialog = new passwordDialog(mcontext,"songs_setting",mHandler,R.style.MyDialog);
						songs_dialog.show();
					} else {
						SongsManagerDialog songsManagerDialog = new SongsManagerDialog(mcontext,R.style.MyDialog);
						songsManagerDialog.show();
					}
					break;
					
				default:
					break;
				
			}
        }
    };
    
    
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
	
public void copy2SD(String sdPath ,String fileName){
    	InputStream inputStream;
    	
    	try {
			
		
    	inputStream = mcontext.getResources().getAssets().open(fileName);
    	File file = new File(sdPath);
    	if(!file.exists()){
    	file.mkdirs();
    	File outfile = new File(sdPath+fileName);
		if(!outfile.exists())
    		outfile.createNewFile();
    	
    	FileOutputStream fileOutputStream = new FileOutputStream(outfile);
    	byte[] buffer = new byte[512];
    	int count = 0;
    	while((count = inputStream.read(buffer)) > 0){
    	fileOutputStream.write(buffer, 0 ,count);
    	}
    	fileOutputStream.flush();
    	fileOutputStream.close();
    	inputStream.close();
    	Log.i("CHW", "配置写入成功====");
		}
    	
    	} catch (Exception e) {
			// TODO: handle exception
			Log.i("CHW", "配置写入异常===="+e.toString());
		}
		
    	}
    
}