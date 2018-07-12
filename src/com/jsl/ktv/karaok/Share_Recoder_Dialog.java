package com.jsl.ktv.karaok;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.jsl.ktv.R.style;
import com.jsl.ktv.R;
import org.apache.http.util.EncodingUtils;
import android.util.Log;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.SystemClock;
import android.widget.ProgressBar;
import com.player.boxplayer.karaok.JNILib;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;
import com.google.zxing.WriterException;

import android.os.Handler;
import android.os.Looper;

public class Share_Recoder_Dialog extends Dialog
{
    // 定义回调事件，用于dialog的点击事�?   
    private Context mcontext;
    private TextView tvSong,tvSinger,tvDuration;
    private ImageView ivQR;
    private String linkPath;
    private String songInfo;
    private Bitmap qrCodeBitmap = null;
	private Button back;
	private ProgressBar pb;
	private String songNum,recordId;
	private Timer uploadTimer;
	private TextView tvProgress;
	private Handler mHandler;
	private int isUpload;

    public Share_Recoder_Dialog(Context context,String name,String songInfo,int them,int upload)
    {
        super(context,style.MyDialog);
        mcontext = context;
		Log.i("jlink","接收分享信息==="+name+"==="+songInfo);
        String[] split = name.split(";");
        songNum = split[0];
        recordId = split[1];
        this.songInfo = songInfo;
		this.isUpload = upload;
		mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weixin_share_dialog);
        //WindowManager.LayoutParams wmParams = this.getWindow().getAttributes();  
        //wmParams.format = PixelFormat.TRANSPARENT;  内容全�?�? 
        //wmParams.format = PixelFormat.TRANSLUCENT; 
        tvSong = (TextView)findViewById(R.id.tv_song);
        tvSinger = (TextView)findViewById(R.id.tv_singer);
        tvDuration = (TextView)findViewById(R.id.tv_duration);
		tvProgress = (TextView)findViewById(R.id.tv_progress);
		tvProgress.setVisibility(View.VISIBLE);
		
        ivQR = (ImageView)findViewById(R.id.iv_qrcode);
		back = (Button)findViewById(R.id.btn_back);
		back.requestFocus();
        
		 pb = (ProgressBar)findViewById(R.id.pb_1);
		setData();
		
		   back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		
		  upload();
		
    }
	
	
	  public void setSongInfo(String name,String songInfo){
    	Log.i("jlink","接收分享信息==="+name+"==="+songInfo);
        String[] split = name.split(";");
        songNum = split[0];
        recordId = split[1];
        this.songInfo = songInfo;
		
		//setData();
    }
    
	
	public void setData(){
		  if(songInfo != null){
            String[] split = songInfo.split(";");
            tvSong.setText(split[0]);
            tvSinger.setText(split[1]);
			 String string = split[2];
            int time = Integer.parseInt(string);
            int min = time/60;
			int second = time%60;
            if(min<10){
				if(second<10){
				tvDuration.setText("0"+min+":0"+second);			
				}else{
				tvDuration.setText("0"+min+":"+second);		
				}
            	
            }else{
            	if(second<10){
				tvDuration.setText("0"+min+":0"+second);			
				}else{
				tvDuration.setText("0"+min+":"+second);		
				}
            }
        }
	}
	
	
	public void dismiss(){
		super.dismiss();
	}
	
	
		public void upload(){
			//if(isUpload!=1){
				
			 pb.setVisibility(View.VISIBLE);
		int status = JNILib.SetRecordUpload(songNum, recordId);
		Log.i("jlink","执行上传=="+status);
			//}
		uploadTimer = new Timer();
		uploadTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = JNILib.GetRecordShareUrl(songNum, recordId);
				try {
					JSONObject jsonObject = new JSONObject(result);
					Log.i("jlink","获取分享URL=="+result);
					int record = jsonObject.getInt("rec_flag");
				/*	if(isUpload!=1)
						record==2;*/
					if(record == 1){//已录
						int upload = jsonObject.getInt("upload");
						switch (upload) {
						case 0://等待上传
							
							break;
                        case 1://正在上传
							final int percent = jsonObject.getInt("percent");//上传进度
							 mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									tvProgress.setText(mcontext.getResources().getString(R.string.share_method_1)+percent+"/100");
								}
							});
							
							break;
                        case 2://上传完成
                        	String url = jsonObject.getString("url");
                        	 qrCodeBitmap = EncodingHandler.createQRCode(url, 300); 
                        	 mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(qrCodeBitmap != null)
	                             	ivQR.setImageBitmap(qrCodeBitmap);
	                        	 pb.setVisibility(View.INVISIBLE);
								 tvProgress.setVisibility(View.INVISIBLE);
	                        	 uploadTimer.cancel();
								}
							});
	                    break;
						default:
							break;
						}
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, 1000, 1000);
	}

}