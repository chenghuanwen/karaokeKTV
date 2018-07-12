package com.jsl.ktv.fragment;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.util.EncodingUtils;
import android.content.Context;
import android.graphics.Bitmap;
import com.google.zxing.WriterException;
import com.jsl.ktv.bean.SongSearchBean;
import com.jsl.ktv.R;
import com.jsl.ktv.R.style;
import com.jsl.ktv.karaok.EncodingHandler;
import com.player.boxplayer.karaok.JNILib;
import android.net.ConnectivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.ProgressBar;
import org.json.JSONException;
import org.json.JSONObject;
public class ShareRecordFragment extends Dialog{

	static final String TOUCH_FILE_NAME = "/mnt/link2src/sys_file/data/message.txt";
	private static final String TAG = "phone";
	private String appDownAddress = "http://www.magimore.com/soft/download";
	private TextView tvLinkCode;
	private ImageView ivLink,ivWeixin;
	private String qrCode;
	private String weixinCode;
	private boolean isZX;
	private Context mContext;
	private View view;
    private TextView tvSong,tvSinger,tvDuration;
	private ProgressBar pb;
	private TextView tvProgress;
    private Handler mHandler;
	private Timer uploadTimer;
	private SongSearchBean song;
	public ShareRecordFragment(Context context, int theme,SongSearchBean song) {
			super(context, style.MyDialog);
			// TODO Auto-generated constructor stub
			this.mContext = context;
			this.song = song;
			mHandler = new Handler(Looper.getMainLooper());
		}
		
		public ShareRecordFragment(Context context, boolean cancelable,
				OnCancelListener cancelListener) {
			super(context, cancelable, cancelListener);
			// TODO Auto-generated constructor stub
		}


		public ShareRecordFragment(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

	 
	 
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = getLayoutInflater().inflate(R.layout.share_record_song_fragment, null);
		initView(view);
		setContentView(view);
		getData();
		upload();
	}
	

	  @Override
	    public void show() {
	        super.show();
	        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
	        DisplayMetrics dm = new DisplayMetrics();
	        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	        manager.getDefaultDisplay().getMetrics(dm);
	        layoutParams.width = dm.widthPixels;
	        layoutParams.height = dm.heightPixels;
	        view.setLayoutParams(layoutParams);
	    }
	 
	
	
	private void getData() {
		// TODO Auto-generated method stub
		qrCode = JNILib.qrGet();
		weixinCode = JNILib.GetWechatTip(1);
		Log.i("song","weixinCode=="+weixinCode);
		try {
			if (!TextUtils.isEmpty(weixinCode) && !isZX) {
				Bitmap wxCodeBitmap = EncodingHandler.createQRCode(weixinCode, 300);                        
				ivWeixin.setImageBitmap(wxCodeBitmap);
			}
			
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void initView(View view) {
		// TODO Auto-generated method stub
		
		ivLink = (ImageView)view.findViewById(R.id.iv_link);
		ivWeixin = (ImageView)view.findViewById(R.id.iv_weixin);
		
		
		tvSong = (TextView)view.findViewById(R.id.tv_song);
        tvSinger = (TextView)view.findViewById(R.id.tv_singer);
        tvDuration = (TextView)view.findViewById(R.id.tv_duration);
		tvProgress = (TextView)view.findViewById(R.id.tv_progress);
		tvProgress.setVisibility(View.VISIBLE);
		 pb = (ProgressBar)view.findViewById(R.id.pb_1);
		
			
	       if(song != null){
	            tvSong.setText(song.getSong());
	            tvSinger.setText(song.getSinger());				
	            int time = song.getRecordTime();
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
	
	
	public void upload(){
		//if(!song.isUpload()){
			
    pb.setVisibility(View.VISIBLE);
	int status = JNILib.SetRecordUpload(song.getSongNumber(), song.getOrderId());
	Log.i("jlink","执行上传=="+status);
	//	}
	uploadTimer = new Timer();
	uploadTimer.schedule(new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String result = JNILib.GetRecordShareUrl(song.getSongNumber(), song.getOrderId());
			try {
				JSONObject jsonObject = new JSONObject(result);
				Log.i("jlink","获取分享URL=="+result);
				int record = jsonObject.getInt("rec_flag");
				/*if(!song.isUpload)
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
								tvProgress.setText(mContext.getResources().getString(R.string.share_method_1)+percent+"/100");
							}
						});
						
						break;
                    case 2://上传完成
                    	String url = jsonObject.getString("url");						
						Log.i("song","weixinCode=="+url);
                    	 final Bitmap qrCodeBitmap = EncodingHandler.createQRCode(url, 300); 
                    	 mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(qrCodeBitmap != null)
                             	ivLink.setImageBitmap(qrCodeBitmap);
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

}
