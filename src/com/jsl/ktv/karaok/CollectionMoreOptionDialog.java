package com.jsl.ktv.karaok;

import org.json.JSONException;
import org.json.JSONObject;

import com.jsl.ktv.bean.SongSearchBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.util.SongJsonParseUtils;
import java.util.ArrayList;
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
import com.jsl.ktv.R;
import com.jsl.ktv.util.SongJsonParseUtils;
import android.os.Handler;
import android.os.SystemClock;
import com.jsl.ktv.view.MyApplication;
import android.app.Dialog;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
public class CollectionMoreOptionDialog extends Dialog implements android.view.View.OnClickListener {
	private Context context;
	private Button btnDiange, btnYouxuan, btnGongfang,btnShoucang,btnAllAdd;
	private int stytle ;
	private String songNum;
	private String tip = "";
	private Handler mHandler;
	private CollectRefreshListener mRefreshListener;
	private Dialog deleteDialog;
	public CollectionMoreOptionDialog(Context context,int style,Handler handler) {
		super(context,style);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.stytle = style;
		this.mHandler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.collection_option_dialog);	
		
		btnGongfang = (Button) findViewById(R.id.view_btn1);
		btnDiange = (Button) findViewById(R.id.view_btn2);
		btnYouxuan = (Button) findViewById(R.id.view_btn3);
		btnShoucang = (Button) findViewById(R.id.view_btn4);
		btnAllAdd = (Button) findViewById(R.id.view_btn5);
		btnShoucang.setOnClickListener(this);
		btnDiange.setOnClickListener(this);
		btnGongfang.setOnClickListener(this);
		btnYouxuan.setOnClickListener(this);
		btnAllAdd.setOnClickListener(this);
		btnDiange.requestFocus();
	}

	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		//btnDiange.requestFocus();
	//	this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		super.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_btn1://公播
			//selectSong("song_select",3);
			break;
		case R.id.view_btn2://点歌
			selectSong("song_select",0);
            mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY);
            mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
            
			break;
		case R.id.view_btn3://优先
			selectSong("song_select",1);
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY);
            mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
			break;
		case R.id.view_btn4://删除
			dismiss();
			showDeleteDialog();
			break;
		case R.id.view_btn5://�?��全点
			addAll();
			MyApplication.isSelectAll = true;
			break;
		default:
			break;
		}
	}
	

	
	public void setSongNum(String num){
		this.songNum = num;
	}
	
	
	
	public void selectSong(String cmd,int mode){
		int state = SongJsonParseUtils.selectSong(cmd, songNum, mode, "");
	//	Log.i("song","歌号=="+songNum+"操作结果=="+state);
		dismiss();
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
		Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
		
		 
	}
	
	
	public interface CollectRefreshListener{
		void refreshCollectList();
	}
	
	public void setRefreshListener(CollectRefreshListener listener){
		this.mRefreshListener = listener;
	}
	
	public void addAll(){
		ArrayList<SongSearchBean> songs = SongJsonParseUtils.getSongDatas2(0, "", 0, 30, 0, 0, 100,"");
		for(int i=0;i<songs.size();i++){
			int state = SongJsonParseUtils.selectSong("song_select", songs.get(i).getSongNumber(), 0, "");
			//SystemClock.sleep(50);
	//	Log.i("song","歌号=="+songs.get(i).getSongNumber()+"操作结果=="+state);
		}
		dismiss();
		Toast.makeText(getContext(),context.getResources().getString(R.string.song_all_add_ok), Toast.LENGTH_SHORT).show();
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
				selectSong("fav_order",-1);
				mRefreshListener.refreshCollectList();
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
