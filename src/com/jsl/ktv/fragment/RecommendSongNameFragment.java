package com.jsl.ktv.fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.util.ArrayList;
import java.util.List;

import com.jsl.ktv.R;

import com.jsl.ktv.adapter.SongNameSearchAdapter;
import com.jsl.ktv.adapter.SongNameSearchAdapter.OnMoreClickListener;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.karaok.MainActivity;
import com.jsl.ktv.karaok.VideoString;
import com.jsl.ktv.karaok.MoreOptionDialog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.os.Handler;

import android.os.Message;

import org.json.JSONObject;

import com.jsl.ktv.util.NetDataParseUtil;
import com.jsl.ktv.util.SongJsonParseUtils;

import com.jsl.ktv.listener.RecommendOrRankSongListLoadListener;
import com.jsl.ktv.view.CustomeGridView;
import com.jsl.ktv.view.MyApplication;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Dialog;

import android.widget.LinearLayout;

import com.jsl.ktv.bean.SongSearchBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class RecommendSongNameFragment extends CommonFragment implements
		OnClickListener, OnItemSelectedListener {
	private View songNameView;
	private static CustomeGridView  gvSong;
	private static ArrayList<SongSearchBean> mSongs;
	private SongSearchBean recordSong;
	private static SongNameSearchAdapter mAdapter;
	private TextView tvTitel1, tvTitel2, tvTitelCount;
	private ImageView ivPageup, ivPagedown;
	private int jLanguage = 0;
	private MoreOptionDialog mOptionDialog;

	private static int currentPosition;
	private static int left_right_count = 1;
	private Handler handler;
	private static Handler mHandler;
	private JSONObject cmdObject;
	private static int list_start = 0, currentPage = 1;
	private static int enterType, layerType;
	private MyReciver myReciver;
	private TextView tvEmpty;
	private LinearLayout tvOprationTip;
	private int Xoffset = 540, Yoffest = 200;
	private int Xbase = 150, Ybase = -300;
	private static long firstKeyTime = 0L, offestTime = 0L;

	private int orderId ;
	private static List<String> songIds;
	private static StringBuffer sbArg;
	private ProgressBar pb;
	private static MainActivity activity;
	
	public RecommendSongNameFragment() {
	};

	public RecommendSongNameFragment(Handler handler, int orderId) {
		this.mHandler = handler;
		this.orderId = orderId;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		songNameView = inflater.inflate(R.layout.laucher_fragment_recommend_song, null);
		// registReciver();//fragment接收广播�?��在onstart（）中注册，onstop()
		initView(songNameView);
		return songNameView;
	}

	@SuppressLint("NewApi")
	private void registReciver() {
		// TODO Auto-generated method stub
		myReciver = new MyReciver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("justlink.action.intent.data_inited_outside_order");
		filter.addAction("justlink.action.intent.refresh_searchcount");
		getActivity().registerReceiver(myReciver, filter);
		
	}

	public void setEnterAndLayerType(int enterType, int layerType) {
		this.enterType = enterType;
		this.layerType = layerType;
		// Log.i("song","设置刷新参数===entertype=="+enterType+"===layertype=="+layerType);
	};

	@SuppressLint("NewApi")
	private void initView(View view) {
		// TODO Auto-generated method stub

		jLanguage = JNILib.getGlobalLanguage();
		sbArg = new StringBuffer();
		gvSong = (CustomeGridView) view.findViewById(R.id.gv_search);
		tvTitel1 = (TextView) view.findViewById(R.id.tv_search_1);
		tvTitel2 = (TextView) view.findViewById(R.id.tv_search_2);
		tvTitelCount = (TextView) view.findViewById(R.id.tv_search_count);
		ivPagedown = (ImageView) view.findViewById(R.id.iv_pagedown);
		ivPageup = (ImageView) view.findViewById(R.id.iv_pageup);
		tvEmpty = (TextView) view.findViewById(R.id.tv_emptyview);
		tvOprationTip = (LinearLayout) view.findViewById(R.id.tv_opration_tip);
		pb = (ProgressBar) view.findViewById(R.id.pb);
		pb.setVisibility(View.VISIBLE);

		if (enterType == 43)
			tvOprationTip.setVisibility(View.GONE);
		else
			tvOprationTip.setVisibility(View.VISIBLE);

		tvTitel1.setText(VideoString.song_search_tip1[jLanguage]);
		tvTitel2.setText(VideoString.song_search_tip2[jLanguage]);
		tvTitelCount.setText("0");

		
		mSongs = new ArrayList<SongSearchBean>();

		mAdapter = new SongNameSearchAdapter(getActivity(), mSongs, mHandler);
		gvSong.setEmptyView(tvEmpty);
		gvSong.setAdapter(mAdapter);
		gvSong.setOnItemSelectedListener(this);
		
		left_right_count = 1;

		gvSong.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				// Log.i("song","gridview focus change ==="+arg1+"==MyApplication.isDeleteSearchKey=="+MyApplication.isDeleteSearchKey);
				if (arg1) {
					MyApplication.isGridviewFocus = true;
					// gvSong.setSelection(currentPosition);
					if (SongJsonParseUtils.getListCount() < 8) {
						currentPosition = 0;
						gvSong.setSelection(0);
					} else {
						gvSong.setSelection(currentPosition);
					}
					View childAt = gvSong.getChildAt(currentPosition);
					Log.i("song", "childAt=====" + childAt);
					if (childAt == null)
						childAt = gvSong.getChildAt(gvSong
								.getFirstVisiblePosition());
					ImageView iv = null;
					switch (enterType) {

					case 21:// 云端（下载）
					case 31:// 已点
						iv = (ImageView) childAt.findViewById(R.id.iv_youxian);
						break;
					case 40:// 录音
						iv = (ImageView) childAt.findViewById(R.id.iv_play);
						break;
					case 43:// 公播
						iv = (ImageView) childAt.findViewById(R.id.iv_delete);
						break;
					default:
						iv = (ImageView) childAt.findViewById(R.id.iv_add);
						break;
					}

					iv.requestFocus();

				}

			}
		});
	

		NetDataParseUtil.getIntance().getRecommendOrRankSongList(orderId, new RecommendOrRankSongListLoadListener() {
			
			@Override
			public void onLoadFinish(final List<String> ids) {
				// TODO Auto-generated method stub
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						songIds = ids;
						if(ids!=null && MyApplication.isDataInit){
						pb.setVisibility(View.GONE);
						mAdapter.refresh(SongJsonParseUtils.getRecommendOrRankSongs(createRequstString(subArray(songIds, 0))));		
						tvTitelCount.setText(MyApplication.currentSinger + "/"
								+ SongJsonParseUtils.getSearchKey() + getPageInfo());
						
						}
						
					}
				});
				
			}
		});

		
	}

	public String getPageInfo() {
		String result = "";
		int total = 1;
		if(songIds!=null)
		total = songIds.size();
		if (total > 8) {
			if ((total - 8) % 2 == 0)
				result = "(" + currentPage + "/" + ((total - 8) / 2 + 1) + ")";
			else
				result = "(" + currentPage + "/" + ((total - 8) / 2 + 2) + ")";
		} else {
			result = "(1/1)";
		}
		return result;
	}

	@SuppressLint("NewApi")
	private void showMoreOptionDialog(String num, String orderId) {
		// TODO Auto-generated method stub
		// Log.i("song","MyApplication.currentItemPosition=="+MyApplication.currentItemPosition);
	
			if (mOptionDialog == null)
				mOptionDialog = new MoreOptionDialog(getActivity(), null, 20,
						R.style.MyDialog, mHandler, false);			
				//	window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				mOptionDialog.setSongNum(num);
				setAttributes(mOptionDialog);         		

	}

	public void setAttributes(Dialog dialog) {
		Window window = dialog.getWindow();
		LayoutParams params = new LayoutParams();
		// params.gravity = Gravity.RIGHT | Gravity.TOP;
		params.width = 420;
		params.height = 130;
		//params.alpha = 0.1f;
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
		window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		window.setAttributes(params);
		dialog.show();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		registReciver();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		activity = (MainActivity) getActivity();
		activity.recommendSongNameFragment = this;
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW);
		currentPage = 1;
		list_start = 0;
		MyApplication.isRecommendFragment = true;
		MyApplication.currentSinger = getResources().getString(R.string.home_4);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
	}



	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Log.i("song", "select position==" + position);
		
		
	//	setClassState();
		currentPosition = position;
		MyApplication.currentItemPosition = position;
		MyApplication.isGridviewFocus = true;
		left_right_count = 1;
		// mAdapter.setCurrentPostion(position);
		// TODO Auto-generated method stub
	
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_add);			
			iv.requestFocus();		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		MyApplication.isGridviewFocus = false;
		super.onPause();
	};

	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Log.i("song","onstop =======");
		MyApplication.isGridviewFocus = false;
		MyApplication.isRecommendFragment = false;
		MyApplication.currentSinger = "";
		MyApplication.currentSingerNum = "";
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW);
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
		getActivity().unregisterReceiver(myReciver);
	}

	public void refresh(ArrayList<SongSearchBean> songs) {
		// Log.i("song","refresh data 1111111");
		if (mAdapter != null)
			mAdapter.refresh(songs);
		// list_start = 0;
		// currentPage = 1;
	}

	private class MyReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			if ("justlink.action.intent.refresh_searchcount".equals(arg1
					.getAction())) {
				// Log.i("song","receive boardcast for refresh count=====");
				tvTitelCount.setText(MyApplication.currentSinger + "/"
						+ SongJsonParseUtils.getSearchKey() + getPageInfo());
			} else if ("justlink.action.intent.refresh_listpage".equals(arg1
					.getAction())) {
				list_start = 0;
				currentPage = 1;
			}else if("justlink.action.intent.data_inited_outside_order".equals(arg1.getAction())){//数据初始化完成后刷新网络推荐歌单
				 Log.i("song","receive boardcast for refresh netdata=====");
				pb.setVisibility(View.GONE);
				if(songIds != null)
				mAdapter.refresh(SongJsonParseUtils.getRecommendOrRankSongs(createRequstString(subArray(songIds, 0))));		
				tvTitelCount.setText(MyApplication.currentSinger + "/"
						+ SongJsonParseUtils.getSearchKey() + getPageInfo());
			}
		}

	}

	public static boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// Log.i("song", "====JSL==== fragment onKeyup= "+keyCode);
		if (keyCode == 20 || keyCode == 19) {
			MyApplication.isNeedRefresh = true;
			if (mAdapter != null && MyApplication.isRecommendFragment
					&& MyApplication.isOnkeyupRefresh)
				mAdapter.notifyDataSetChanged();
			// Log.i("song","松开刷新==========");
		}
		return false;
	}

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("song", "recommend fragment onkeydown==" + keyCode + "==" + currentPosition);

		if (MyApplication.isPipFocus && mHandler != null) {
			left_right_count = 0;
			// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS);
		}
		if (keyCode == event.KEYCODE_DPAD_DOWN
				&& MyApplication.isRecommendFragment) {
			offestTime = System.currentTimeMillis() - firstKeyTime;
			if (offestTime > 1000) {
				MyApplication.isNeedRefresh = true;
			//	Log.i("song", "set need refresh true");
			} else {
				MyApplication.isNeedRefresh = false;
				//Log.i("song", "set need refresh false");
			}
			firstKeyTime = System.currentTimeMillis();
			MyApplication.isOnkeyupRefresh = false;
			if (mHandler != null && MyApplication.isGridviewFocus) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS);
				// Log.i("song","song ban top focus===========");				
			}

			 Log.i("song","=="+NetDataParseUtil.getIntance().getTotalCount()+"=="+MyApplication.currentItemPosition+"=="+list_start+"=="+MyApplication.isSelectAll);
			if (gvSong != null
					&& (MyApplication.currentItemPosition == 6 || MyApplication.currentItemPosition == 7)
					&& MyApplication.isGridviewFocus) {
				if (( list_start+8 >= NetDataParseUtil.getIntance()
								.getTotalCount() && !MyApplication.isSelectAll)
						&& MyApplication.isRecommendFragment) {
					//Log.i("song", "last page tip ========");
					Message msg = Message.obtain();
					msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_TOAST_TIP;
					mHandler.sendMessage(msg);
				} else {
					MyApplication.isSelectAll = false;
					MyApplication.isOnkeyupRefresh = true;
					list_start += 2;
					currentPage += 1;
					mAdapter.refresh(SongJsonParseUtils.getRecommendOrRankSongs(createRequstString(subArray(songIds, list_start))));
					activity.sendBroadcast(new Intent("justlink.action.intent.refresh_searchcount"));
				}

			}

		} else if (keyCode == event.KEYCODE_DPAD_UP) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);
				if (!MyApplication.isSearchViewShow)
					mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_UNFOCUS_SEARCHVIEW);
			}
			offestTime = System.currentTimeMillis() - firstKeyTime;
			if (offestTime > 600) {
				MyApplication.isNeedRefresh = true;
			} else {
				MyApplication.isNeedRefresh = false;
			}
			firstKeyTime = System.currentTimeMillis();
			MyApplication.isOnkeyupRefresh = false;
			if (gvSong != null
					&& (MyApplication.currentItemPosition == 0 || MyApplication.currentItemPosition == 1)
					&& list_start > 0 && MyApplication.isGridviewFocus) {
				MyApplication.isOnkeyupRefresh = true;
				list_start -= 2;
				currentPage -= 1;
				MyApplication.searchStartCount = list_start;
				mAdapter.refresh(SongJsonParseUtils.getRecommendOrRankSongs(createRequstString(subArray(songIds, list_start))));
				activity.sendBroadcast(new Intent("justlink.action.intent.refresh_searchcount"));
				// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS);
			} else if (list_start == 0 && mHandler != null
					&& MyApplication.isRecommendFragment) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
				currentPosition = 0;
				 Log.i("song","song name reset top focus==========");
			}

		} else if (keyCode == event.KEYCODE_DPAD_RIGHT) {

			if (mAdapter == null || MyApplication.isTopButtonFocus)
				return false;
			if (mHandler != null)
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);

		} else if (keyCode == event.KEYCODE_DPAD_LEFT) {
			if (!MyApplication.isGridviewFocus && mHandler != null
					&& !MyApplication.isSearchViewShow)
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);
			if (MyApplication.isSearchViewShow
					&& (MyApplication.currentItemPosition == 4 || MyApplication.currentItemPosition == 6)
					&& MyApplication.isRecommendFragment) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
				MyApplication.currentItemPosition = 0;
				MyApplication.isOnkeyupRefresh = false;
			}
			if (mAdapter == null || MyApplication.isTopButtonFocus)
				return false;

		}

		if (enterType == 21 && list_start == 0)
			MyApplication.isShowDownloadProgress = true;
		else
			MyApplication.isShowDownloadProgress = false;

		// Log.i("song","left-right-count==="+left_right_count);
		return false;
	}


	public static List<String> subArray(List<String> src,int start){
		if(start <= src.size()-8){
			
			return src.subList(start, start+8);	
		}else{
			
			return src.subList(start, src.size());
		}
		
	}
	
	
	public static String createRequstString(List<String> list){
		sbArg.setLength(0);
		for (int i = 0; i < list.size()-1; i++) {
			sbArg.append(list.get(i)).append("|");
		}
		sbArg.append(list.get(list.size()-1));
		return sbArg.toString();
	}
	

	
}
