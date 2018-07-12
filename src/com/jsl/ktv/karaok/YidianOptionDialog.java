package com.jsl.ktv.karaok;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.R;
import com.jsl.ktv.util.SongJsonParseUtils;
import android.os.Handler;
public class YidianOptionDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private Button btnDelete, btnYouxuan,btnChabo;
	private TextView tvTishi;
	private int NoticePicPlayTime;
	private int stytle ;
	private String songNum,orderId;
	private String tip = "";
	private Handler mHandler;
	private JSONObject cmdjObject;
	private YiDianRefreshListener mRefreshListener;
	private String cmd="{\"head\":\"jlink\",\"cmd\":1,\"key\":0,\"page_max\":6}";
	public YidianOptionDialog(Context context,TextView tvTishi,int NoticePicPlayTime,int style,Handler handler) {
		super(context,style);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.tvTishi = tvTishi;
		this.NoticePicPlayTime = NoticePicPlayTime;
		this.stytle = style;
		this.mHandler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.yidian_option_dialog);	
		
		
		btnDelete = (Button) findViewById(R.id.view_btn2);
		btnYouxuan = (Button) findViewById(R.id.view_btn3);		
		btnChabo = (Button) findViewById(R.id.view_btn5);
		
		btnDelete.setOnClickListener(this);
		btnYouxuan.setOnClickListener(this);		
		btnChabo.setOnClickListener(this);
		btnYouxuan.requestFocus();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.view_btn2://删除
			selectSong(0);
			mRefreshListener.refreshList();
			break;
		case R.id.view_btn3://优先
			selectSong(1);
			mRefreshListener.refreshList();
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
	
	public void setOrderId(String id){
		this.orderId = id;
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		btnYouxuan.requestFocus();
	}
	
	public void selectSong(int mode){
		int state = SongJsonParseUtils.selectSong("song_order", songNum, mode,orderId);
		Log.i("song","歌号=="+songNum+"操作结果=="+state);
		dismiss();
		NoticePicPlayTime = 3;
		switch (state) {
		case 0:
			tip = context.getResources().getString(R.string.opration_fail);
			break;
         case 1:
        	 tip = context.getResources().getString(R.string.opration_success);
			break;
         case 2:
        	 tip = context.getResources().getString(R.string.list_full);
	        break;
         case 3:
        	 tip = context.getResources().getString(R.string.song_existed);
	        break;
		default:
			break;
		}
		if(tvTishi==null){
			Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
		}else{
			 tvTishi.setVisibility(View.VISIBLE);
			  tvTishi.setText(tip);	
		}
		 
	}
	
	public interface YiDianRefreshListener{
		void refreshList();
	}
	
	public void setRefreshListener(YiDianRefreshListener listener){
		this.mRefreshListener = listener;
	}
	
}
