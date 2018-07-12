package com.jsl.ktv.karaok;

import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.karaok.VideoString;
import com.jsl.ktv.R;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
public class SongsManagerDialog extends Dialog implements android.view.View.OnClickListener{
	private Context context;
	private TextView tvBatchAdd,tvBatchDelete,tvUpdate;
	private TextView pbBar;
	private ProgressBar pbBar2;
	private Timer addTimer,deleteTimer,updateTimer;
	private Handler uiHandler;
	private int jLanguage = 0;
     private LinearLayout btn1,btn2,btn3;
	public SongsManagerDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		uiHandler = new Handler(Looper.getMainLooper());
	}
	
	public SongsManagerDialog(Context context,int them){
    	super(context, R.style.MyDialog);
    	this.context = context;
		uiHandler = new Handler(Looper.getMainLooper());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.songs_manager);
		
		jLanguage=JNILib.getGlobalLanguage();
		
		tvBatchAdd = (TextView) findViewById(R.id.add_button);
		tvBatchDelete = (TextView) findViewById(R.id.delete_button);
		tvUpdate = (TextView) findViewById(R.id.update_button);
		pbBar = (TextView)findViewById(R.id.add_progressBar);
		pbBar2 = (ProgressBar)findViewById(R.id.add_progressBar1);
		
		tvBatchAdd.setText(VideoString.batch_add_songs[jLanguage]);
		tvBatchDelete.setText(VideoString.batch_delete_songs[jLanguage]);
		tvUpdate.setText(VideoString.update_data[jLanguage]);
		
			
		btn1 = (LinearLayout)findViewById(R.id.ll1);
		btn2 = (LinearLayout)findViewById(R.id.ll2);
		btn3 = (LinearLayout)findViewById(R.id.ll3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		  
		switch (v.getId()) {
		case R.id.ll1:
			int addState = JNILib.BatchAddSongDetect();
			Log.i("CHW", "批量加歌状�?==="+addState);
			if(addState == -2){
				Toast.makeText(context,VideoString.batch_songs1[jLanguage],Toast.LENGTH_LONG).show();
			}else if(addState == -3){
				Toast.makeText(context,VideoString.batch_songs2[jLanguage], Toast.LENGTH_LONG).show();
			}else if(addState == -1){
				Toast.makeText(context,VideoString.batch_songs3[jLanguage], Toast.LENGTH_LONG).show();
			}else if(addState == 0){
				Toast.makeText(context,VideoString.batch_songs4[jLanguage], Toast.LENGTH_LONG).show();
			}else if(addState>0){
				AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(VideoString.batch_songs5[jLanguage])
				.setPositiveButton(VideoString.batch_songs5[jLanguage], new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					JNILib.BatchAddSongSel(1);
					addTimer = new Timer();
					addTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
						final String progress = JNILib.GetBatchAddsongStatus();
							Log.i("CHW", "批量加歌进程==="+progress);
							uiHandler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
											// TODO Auto-generated method stub
									if(TextUtils.isEmpty(progress)){
										addTimer.cancel();
										pbBar.setVisibility(View.GONE);
										pbBar2.setVisibility(View.GONE);
										Toast.makeText(context,VideoString.song_handle4[jLanguage], Toast.LENGTH_SHORT).show();
									}else{
									pbBar.setVisibility(View.VISIBLE);
									pbBar2.setVisibility(View.VISIBLE);
									pbBar.setText(progress);	
									}
									
								}
							});
						}
					}, 1000, 1000);
					
					
					
					}
				})
				.setNegativeButton(VideoString.batch_songs7[jLanguage], new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						JNILib.BatchAddSongSel(0);
					}
				});
				
				builder.create().show();
			}
			break;
        case R.id.ll2:
			        	int deleteState = JNILib.BatchDeleteDetect();
						Log.i("CHW", "批量删歌状�?==="+deleteState);
        	if(deleteState==-2){
				Toast.makeText(context,VideoString.batch_songs1[jLanguage], Toast.LENGTH_LONG).show();
			}else if(deleteState==-1){
				Toast.makeText(context,VideoString.batch_songs2[jLanguage], Toast.LENGTH_LONG).show();
			}else if(deleteState==-3){
				Toast.makeText(context,VideoString.batch_songs3[jLanguage], Toast.LENGTH_LONG).show();
			}else if(deleteState == 0){
				Toast.makeText(context,VideoString.batch_songs11[jLanguage], Toast.LENGTH_LONG).show();
			}else if(deleteState>0){
				AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(VideoString.batch_songs9[jLanguage])
				.setPositiveButton(VideoString.batch_songs6[jLanguage], new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						JNILib.BatchDeleteSel(1);
						deleteTimer = new Timer();
						deleteTimer.schedule(new TimerTask() {
							
							@Override
							public void run() {
							final String progress = JNILib.GetBatchDelsongStatus();
								Log.i("CHW", "批量删歌进程==="+progress);
								uiHandler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
											// TODO Auto-generated method stub
									if(TextUtils.isEmpty(progress)){
										deleteTimer.cancel();
										pbBar.setVisibility(View.GONE);
										pbBar2.setVisibility(View.GONE);
										Toast.makeText(context, VideoString.song_handle4[jLanguage], Toast.LENGTH_SHORT).show();
									}else{
									pbBar.setVisibility(View.VISIBLE);
									pbBar2.setVisibility(View.VISIBLE);
									pbBar.setText(progress);	
									}
									
								}
							});
							}
						}, 1000, 1000);
						
						
					}
				})
				.setNegativeButton(VideoString.batch_songs7[jLanguage], new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						JNILib.BatchDeleteSel(0);
					}
				});
				
				builder.create().show();
			}
			break;
         case R.id.ll3:
	 
        	 int updateState = JNILib.HddUpdateDetect();
			 Log.i("CHW", "资料升级状�?==="+updateState);
         	if(updateState==0){
 				Toast.makeText(context,VideoString.batch_songs10[jLanguage], Toast.LENGTH_LONG).show();
 			}else if(updateState==1){
 				AlertDialog.Builder builder = new AlertDialog.Builder(context)
 				.setTitle(VideoString.batch_songs8[jLanguage])
 				.setPositiveButton(VideoString.batch_songs6[jLanguage], new OnClickListener() {
 					
 					@Override
 					public void onClick(DialogInterface arg0, int arg1) {
 						// TODO Auto-generated method stub
 						JNILib.HddUpdateSel(1);
 						updateTimer = new Timer();
 						updateTimer.schedule(new TimerTask() {
 							
 							@Override
 							public void run() {
 								final String progress = JNILib.GetHddUpdatestatus();
								 Log.i("CHW", "资料升级进程==="+progress);
 									uiHandler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
											// TODO Auto-generated method stub
									if(TextUtils.isEmpty(progress)){
										updateTimer.cancel();
										pbBar.setVisibility(View.GONE);
										pbBar2.setVisibility(View.GONE);
										Toast.makeText(context,VideoString.song_handle4[jLanguage], Toast.LENGTH_SHORT).show();
									}else{
										pbBar.setVisibility(View.VISIBLE);
									pbBar2.setVisibility(View.VISIBLE);
									pbBar.setText(progress);	
									}
									
								}
							});
 							}
 						}, 1000, 1000);
 						
 						
 					}
 				})
 				.setNegativeButton(VideoString.batch_songs7[jLanguage], new OnClickListener() {
 					
 					@Override
 					public void onClick(DialogInterface arg0, int arg1) {
 						// TODO Auto-generated method stub
 						JNILib.BatchDeleteSel(0);
 					}
 				});
 				
 				builder.create().show();
 			}
        	 
	     break;
        

		default:
			break;
		}
		
	}

}
