package com.jsl.ktv.karaok;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.karaok.CollectionMoreOptionDialog.CollectRefreshListener;
import com.jsl.ktv.R;
import com.jsl.ktv.util.SongJsonParseUtils;
import android.os.Handler;
public class LocalMoreOptionDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private Button btnDiange, btnYouxuan, btnGongfang,btnShoucang,btnChabo;
	private TextView tvTishi;
	private int NoticePicPlayTime;
	private int stytle ;
	private String songNum;
	private String tip = "";
	private Handler mHandler;
	private JSONObject cmdjObject;
	private String cmd="{\"head\":\"jlink\",\"cmd\":1,\"key\":0,\"page_max\":6}";
	private boolean isFullScreen;
	private Dialog deleteDialog;
	private LocalRefreshListener mRefreshListener;
	public LocalMoreOptionDialog(Context context,TextView tvTishi,int NoticePicPlayTime,int style,Handler handler,boolean isFullScreen) {
		super(context,style);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.tvTishi = tvTishi;
		this.NoticePicPlayTime = NoticePicPlayTime;
		this.stytle = style;
		this.mHandler = handler;
		this.isFullScreen = isFullScreen;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.local_new_option_dialog);	
		
		btnGongfang = (Button) findViewById(R.id.view_btn1);
		btnDiange = (Button) findViewById(R.id.view_btn2);
		btnYouxuan = (Button) findViewById(R.id.view_btn3);
		btnShoucang = (Button) findViewById(R.id.view_btn4);
		btnChabo = (Button) findViewById(R.id.view_btn5);
		btnShoucang.setOnClickListener(this);
		btnDiange.setOnClickListener(this);
		btnYouxuan.setOnClickListener(this);
		btnGongfang.setOnClickListener(this);
		btnChabo.setOnClickListener(this);
		btnDiange.requestFocus();
	}

	
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_btn1://删除
			dismiss();
			showDeleteDialog();
			break;
		case R.id.view_btn2://点歌
			selectSong(0);
			if(!isFullScreen){
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY);
            mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);	
			}
			
			break;
		case R.id.view_btn3://优先
			selectSong(1);
			if(!isFullScreen){
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY);
            mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);	
			}
			
			break;
		case R.id.view_btn4://收藏
			selectSong(2);
			break;
		case R.id.view_btn5://插播
			selectSong(1);
			
				try{
					cmdjObject = new JSONObject(cmd);	
					cmdjObject.put("key" , 0);
					cmdjObject.put("cmd" , 6);
					String res=JNILib.getTvData(cmdjObject.toString());
					//Log.i(TAG, "====JSL==== cmdjObject = "+cmdjObject.toString() );
					//Log.i(TAG, "====JSL==== cmdjObject res= "+res );
					}catch (Exception e) {
						e.printStackTrace();
					}		
			
			break;
		default:
			break;
		}
	}
	

	
	public void setSongNum(String num){
		this.songNum = num;
	}
	
	/*@Override
	public void show() {
		// TODO Auto-generated method stub
		//this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  
		super.show();
		btnDiange.requestFocus();
	}*/
	
	public void selectSong(int mode){
		int state = SongJsonParseUtils.selectSong("song_select", songNum, mode, "");
		Log.i("song","歌号=="+songNum+"操作结果=="+state);
		dismiss();
		NoticePicPlayTime = 3;
		switch (state) {		
         case 1:
        	 tip = context.getResources().getString(R.string.opration_success);
			break;
        
		default:
			tip = context.getResources().getString(R.string.opration_fail);
			break;
		}
		if(tvTishi==null){
			Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
		}else{
			 tvTishi.setVisibility(View.VISIBLE);
			  tvTishi.setText(tip);	
		}
		 
	}
	
	public interface LocalRefreshListener{
		void refreshLocalList();
	}
	
	public void setRefreshListener(LocalRefreshListener listener){
		this.mRefreshListener = listener;
	}
	
	
public void showDeleteDialog(){
	
	View view = getLayoutInflater().inflate(R.layout.cover_child_path_dialog, null);
	TextView tvcancel = (TextView) view.findViewById(R.id.tv_cancel);
	TextView tvsure = (TextView) view.findViewById(R.id.tv_sure);
	tvcancel.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deleteDialog.dismiss();
		}
	});
	tvsure.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			selectSong(5);
			mRefreshListener.refreshLocalList();
			deleteDialog.dismiss();
			dismiss();
		}
	});
	
	if(deleteDialog==null){
		deleteDialog = new Dialog(context, R.style.MyDialog);
		deleteDialog.setContentView(view);
	}
	deleteDialog.getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);
	deleteDialog.show();
	
}
	
}
