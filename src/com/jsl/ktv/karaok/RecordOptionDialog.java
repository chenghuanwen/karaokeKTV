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

import com.jsl.ktv.bean.SongSearchBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.R;
import com.jsl.ktv.util.SongJsonParseUtils;
import android.os.Handler;
import android.os.Message;
public class RecordOptionDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private Button btnDelete,btnPlay,btnShare;
	private TextView tvTishi;
	private int NoticePicPlayTime;
	private int stytle ;
	private SongSearchBean song;
	private String tip = "";
	private Handler mHandler;
	private JSONObject cmdjObject;
	private RecordRefreshListener mRefreshListener;
	private String cmd="{\"head\":\"jlink\",\"cmd\":1,\"key\":0,\"page_max\":6}";
	public RecordOptionDialog(Context context,TextView tvTishi,int NoticePicPlayTime,int style,Handler handler) {
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
		setContentView(R.layout.record_option_dialog);	

		btnPlay = (Button) findViewById(R.id.view_btn2);
		btnDelete = (Button) findViewById(R.id.view_btn3);
		btnShare = (Button) findViewById(R.id.view_btn5);
		
		btnPlay.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		
		btnPlay.requestFocus();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.view_btn2://播放
			selectSong(0);
			break;
		case R.id.view_btn3://删除
			selectSong(2);
			mRefreshListener.refreshList();
			break;
		
		case R.id.view_btn5://分享
			Message msg = Message.obtain();
			msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_RECORD_SHARE_DIALOG;
			msg.obj = song;
			mHandler.sendMessage(msg);
			
			break;
		default:
			break;
		}
	}
	

	
	public void setSong(SongSearchBean song){
		this.song = song;
	}
	

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		btnPlay.requestFocus();
	}
	
	public void selectSong(int mode){
		int state = SongJsonParseUtils.selectSong("rec_order", song.getSongNumber(),mode,song.getOrderId());
		Log.i("song","歌号=="+song.getSongNumber()+"操作结果=="+state);
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
	
	public interface RecordRefreshListener{
		void refreshList();
	}
	
	public void setRefreshListener(RecordRefreshListener listener){
		this.mRefreshListener = listener;
	}
	
	
}
