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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.R;
import com.jsl.ktv.util.SongJsonParseUtils;
import android.os.Handler;
public class DownloadOptionDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private Button btnDelete, btnYouxuan, btnPause;
	private TextView tvTishi;
	private int NoticePicPlayTime;
	private int stytle ;
	private String songNum;
	private String tip = "";
	private Handler mHandler;
	private JSONObject cmdjObject;
	private DownloadRefreshListener mRefreshListener;
	private String cmd="{\"head\":\"jlink\",\"cmd\":1,\"key\":0,\"page_max\":6}";
	public DownloadOptionDialog(Context context,TextView tvTishi,int NoticePicPlayTime,int style,Handler handler) {
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
		setContentView(R.layout.download_option_dialog);	
		
		
		btnPause = (Button) findViewById(R.id.view_btn3);
		btnYouxuan = (Button) findViewById(R.id.view_btn2);	
		btnDelete = (Button) findViewById(R.id.view_btn5);
		
		btnDelete.setOnClickListener(this);
		btnYouxuan.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnYouxuan.requestFocus();
	}
	

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.view_btn2://优先
			selectSong(1);
			mRefreshListener.refreshList();
			break;
		case R.id.view_btn3://暂停
			selectSong(2);
			break;
		
		case R.id.view_btn5://删除
			selectSong(0);			
			mRefreshListener.refreshList();
			break;
		default:
			break;
		}
	}
	

	
	public void setSongNum(String num){
		this.songNum = num;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		//this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		super.show();
		btnYouxuan.requestFocus();
	}
	
	public void selectSong(int mode){
		int state = SongJsonParseUtils.selectSong("cloud_order", songNum, mode,"");
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
	
	
	public interface DownloadRefreshListener{
		void refreshList();
	}
	
	public void setRefreshListener(DownloadRefreshListener listener){
		this.mRefreshListener = listener;
	}
	
}
