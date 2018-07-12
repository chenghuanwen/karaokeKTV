package com.jsl.ktv.karaok;

import org.json.JSONObject;

import com.jsl.ktv.R;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.util.SongJsonParseUtils;
import com.jsl.ktv.view.MyApplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MoreOptionDialogFragment extends DialogFragment implements OnClickListener{
	
	
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
	private Dialog myDialog;
	private int Xoffset = 540, Yoffest = 200;
	private int Xbase = 150, Ybase = -300;
	public MoreOptionDialogFragment(Context context,TextView tvTishi,int NoticePicPlayTime,int style,Handler handler,boolean isFullScreen) {	
		// TODO Auto-generated constructor stub
		this.context = context;
		this.tvTishi = tvTishi;
		this.NoticePicPlayTime = NoticePicPlayTime;
		this.stytle = style;
		this.mHandler = handler;
		this.isFullScreen = isFullScreen;
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
	    // getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
		View v = inflater.inflate(R.layout.new_option_dialog, container,false);
		initViews(v);
		return v;
	}
	
	
	private void initViews(View v) {
		// TODO Auto-generated method stub
		btnGongfang = (Button) v.findViewById(R.id.view_btn1);
		btnDiange = (Button) v.findViewById(R.id.view_btn2);
		btnYouxuan = (Button) v.findViewById(R.id.view_btn3);
		btnShoucang = (Button) v.findViewById(R.id.view_btn4);
		btnChabo = (Button) v.findViewById(R.id.view_btn5);
		btnShoucang.setOnClickListener(this);
		btnDiange.setOnClickListener(this);
		btnYouxuan.setOnClickListener(this);
		btnGongfang.setOnClickListener(this);
		btnChabo.setOnClickListener(this);
		btnChabo.requestFocus();
	}



	@Override
	public void show(FragmentManager manager, String tag) {
		// TODO Auto-generated method stub
		btnChabo.requestFocus();
		super.show(manager, tag);
	}

	
	public void setSongNum(String num){
		this.songNum = num;
	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		myDialog = getDialog();
		//setAttributes(myDialog.getWindow());
		super.onStart();
	}
	
	public Dialog getMyDialog(){
		return myDialog;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_btn1://公播
			selectSong(3);
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
			selectSong(4);
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_IMMEDIATE_PLAY);
		/*	selectSong(1);			
			
				try{
					cmdjObject = new JSONObject(cmd);	
					cmdjObject.put("key" , 0);
					cmdjObject.put("cmd" , 6);
					String res=JNILib.getTvData(cmdjObject.toString());
					//Log.i(TAG, "====JSL==== cmdjObject = "+cmdjObject.toString() );
					//Log.i(TAG, "====JSL==== cmdjObject res= "+res );
					}catch (Exception e) {
						e.printStackTrace();
					}*/			
			
			break;
		default:
			break;
		}
	}
	
	
	public void selectSong(int mode){
		int state = SongJsonParseUtils.selectSong("song_select", songNum, mode, "");
	//	Log.i("song","歌号=="+songNum+"操作结果=="+state);
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
	

	public void setAttributes(Window window) {
		LayoutParams params = new LayoutParams();
		// params.gravity = Gravity.RIGHT | Gravity.TOP;
		params.width = 420;
		params.height = 130;
		params.alpha = 0.1f;
		switch (MyApplication.currentItemPosition) {
		case 0:
			params.x = Xbase;
			params.y = Ybase;
			break;
		case 1:
			params.x = Xbase + Xoffset;
			params.y = Ybase;
			break;
		case 2:
			params.x = Xbase;
			params.y = Ybase + Yoffest - 10;
			break;
		case 3:
			params.x = Xbase + Xoffset;
			params.y = Ybase + Yoffest - 10;
			break;
		case 4:
			params.x = Xbase;
			params.y = Ybase + Yoffest * 2 - 30;
			break;
		case 5:
			params.x = Xbase + Xoffset;
			params.y = Ybase + Yoffest * 2 - 30;
			break;
		case 6:
			params.x = Xbase;
			params.y = Ybase + Yoffest * 3 - 50;
			break;
		case 7:
			params.x = Xbase + Xoffset;
			params.y = Ybase + Yoffest * 3 - 50;
			break;
		default:
			break;
		}

		window.addFlags(LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(params);
	}
}
