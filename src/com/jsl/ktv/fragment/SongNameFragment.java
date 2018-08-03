package com.jsl.ktv.fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import android.os.SystemClock;
import com.jsl.ktv.R;
import com.jsl.ktv.R.style;
import com.jsl.ktv.adapter.SongNameSearchAdapter;
import com.jsl.ktv.adapter.SongNameSearchAdapter.OnMoreClickListener;
import com.jsl.ktv.adapter.SongNameSearchAdapter.RecordShareClickListener;
import com.jsl.ktv.karaok.CollectionMoreOptionDialog.CollectRefreshListener;
import android.view.Gravity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.karaok.UpdateSongTxtDialog;
import com.jsl.ktv.karaok.VideoString;
import com.jsl.ktv.karaok.MoreOptionDialog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.os.Handler;
import com.jsl.ktv.karaok.MainActivity;
import android.os.Message;
import org.json.JSONException;
import org.json.JSONObject;
import com.jsl.ktv.util.SongJsonParseUtils;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import com.jsl.ktv.karaok.CollectionMoreOptionDialog;
import com.jsl.ktv.karaok.CollectionMoreOptionDialog.CollectRefreshListener;
import com.jsl.ktv.karaok.LocalMoreOptionDialog;
import com.jsl.ktv.karaok.LocalMoreOptionDialog.LocalRefreshListener;
import com.jsl.ktv.karaok.MoreOptionDialogFragment;
import com.jsl.ktv.karaok.RecordOptionDialog;
import com.jsl.ktv.karaok.RecordOptionDialog.RecordRefreshListener;
import com.jsl.ktv.karaok.YidianOptionDialog;
import com.jsl.ktv.karaok.YidianOptionDialog.YiDianRefreshListener;
import com.jsl.ktv.karaok.DownloadOptionDialog;
import com.jsl.ktv.karaok.DownloadOptionDialog.DownloadRefreshListener;
import com.jsl.ktv.view.CustomeGridView;
import com.jsl.ktv.view.MyApplication;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.jsl.ktv.bean.SongSearchBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.fragment.SingerFragment.MyGridAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class SongNameFragment extends CommonFragment implements
		OnClickListener, OnItemSelectedListener, OnFocusChangeListener {
	private View songNameView;
	private static CustomeGridView  gvSong;
	private static ArrayList<SongSearchBean> mSongs;
	private SongSearchBean recordSong;
	private static SongNameSearchAdapter mAdapter;
	private TextView tvTitel1, tvTitel2, tvTitelCount;
	private ImageView ivPageup, ivPagedown;
	private int jLanguage = 0;
	private MoreOptionDialog mOptionDialog;
	//private MoreOptionDialogFragment mOptionDialog;
	private CollectionMoreOptionDialog mCollectionMoreOptionDialog;
	private YidianOptionDialog mYidianOptionDialog;
	private RecordOptionDialog mRecordOptionDialog;
	private DownloadOptionDialog mDownloadOptionDialog;
	private LocalMoreOptionDialog mlLocalMoreOptionDialog;
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
	private LinearLayout languageClass;
	private static Button tvAll, tvCN, tvEN, tvMN, tvKo, tvGD, tvJap;// 全部、国语、英语、闽南、韩语、粤语、日语
	private int class_select_index;
	private static boolean isBanClassFocus = false;
	private ProgressBar pb;
	public SongNameFragment() {
	};

	public SongNameFragment(Handler handler, int enterType, int layerType) {
		this.mHandler = handler;
		this.enterType = enterType;
		this.layerType = layerType;
		MyApplication.currentEnterType = enterType;
		MyApplication.currentLayer = layerType;
		// Log.i("song","初始化刷新参�?==entertype=="+enterType+"===layertype=="+layerType);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		songNameView = inflater.inflate(R.layout.laucher_fragment_gq, null);
		// registReciver();//fragment接收广播�?��在onstart（）中注册，onstop()
		initView(songNameView);
		return songNameView;
	}

	@SuppressLint("NewApi")
	private void registReciver() {
		// TODO Auto-generated method stub
		myReciver = new MyReciver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("justlink.action.intent.songlist_focus");
		filter.addAction("justlink.action.intent.refresh_searchcount");
		filter.addAction("justlink.action.intent.refresh_listpage");
		filter.addAction("justlink.action.intent.data_inited");
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
		handler = new Handler();
		jLanguage = JNILib.getGlobalLanguage();

		gvSong = (CustomeGridView) view.findViewById(R.id.gv_search);
		tvTitel1 = (TextView) view.findViewById(R.id.tv_search_1);
		tvTitel2 = (TextView) view.findViewById(R.id.tv_search_2);
		tvTitelCount = (TextView) view.findViewById(R.id.tv_search_count);
		ivPagedown = (ImageView) view.findViewById(R.id.iv_pagedown);
		ivPageup = (ImageView) view.findViewById(R.id.iv_pageup);
		tvEmpty = (TextView) view.findViewById(R.id.tv_emptyview);
		tvOprationTip = (LinearLayout) view.findViewById(R.id.tv_opration_tip);
		pb = (ProgressBar) view.findViewById(R.id.pb);
		
		
		languageClass = (LinearLayout) view.findViewById(R.id.ll_language);
		tvAll = (Button) view.findViewById(R.id.btn_language_all);
		tvCN = (Button) view.findViewById(R.id.btn_language_1);
		tvEN = (Button) view.findViewById(R.id.btn_language_2);
		tvMN = (Button) view.findViewById(R.id.btn_language_3);
		tvKo = (Button) view.findViewById(R.id.btn_language_4);
		tvGD = (Button) view.findViewById(R.id.btn_language_5);
		tvJap = (Button) view.findViewById(R.id.btn_language_6);
		tvAll.setOnFocusChangeListener(this);
		tvCN.setOnFocusChangeListener(this);
		tvEN.setOnFocusChangeListener(this);
		tvMN.setOnFocusChangeListener(this);
		tvKo.setOnFocusChangeListener(this);
		tvGD.setOnFocusChangeListener(this);
		
		tvJap.setOnFocusChangeListener(this);

		if (enterType == 43)
			tvOprationTip.setVisibility(View.GONE);
		else
			tvOprationTip.setVisibility(View.VISIBLE);

		tvTitel1.setText(VideoString.song_search_tip1[jLanguage]);
		tvTitel2.setText(VideoString.song_search_tip2[jLanguage]);
		tvTitelCount.setText("0");


		ivPagedown.setOnClickListener(this);
		ivPageup.setOnClickListener(this);

		mSongs = new ArrayList<SongSearchBean>();

		mAdapter = new SongNameSearchAdapter(getActivity(), mSongs, mHandler);
		gvSong.setEmptyView(tvEmpty);
		gvSong.setAdapter(mAdapter);
		
		left_right_count = 1;

		gvSong.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				 Log.i("song","gridview focus change ==="+arg1+"==MyApplication.isDeleteSearchKey=="+MyApplication.isDeleteSearchKey);
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
								.getFirstVisiblePosition());//null
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

	
		mAdapter.setMoreClickListener(new OnMoreClickListener() {

			@Override
			public void onMoreClickListener(String number, String orderId,int downstat) {
				// TODO Auto-generated method stub
				showMoreOptionDialog(number, orderId,downstat);
			}

			@Override
			public void onRecordOption(SongSearchBean song) {
				recordSong = song;
				showMoreOptionDialog(song.getSongNumber(), song.getOrderId(),0);
			}
		});

		mAdapter.setShareClickListener(new RecordShareClickListener() {

			public void onShareClickListener(SongSearchBean song) {
				// TODO Auto-generated method stub
				// Log.i("song","share song ==="+song);
				Message msg = Message.obtain();
				msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_RECORD_SHARE_DIALOG;
				msg.obj = song;
				mHandler.sendMessage(msg);
			}
		});

		// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SEARCHVIEW);

		ArrayList<SongSearchBean> songs = SongJsonParseUtils.getSongDatas2(0,
				"", 0, enterType, layerType, 0, 8, "");
		if(songs.size()==0)
			pb.setVisibility(View.VISIBLE);
		mAdapter.refresh(songs);
		tvTitelCount.setText(MyApplication.currentSinger + "/"
				+ SongJsonParseUtils.getSearchKey() + getPageInfo());
	}

	
	
	private StringBuffer sb = new StringBuffer();
	public String getPageInfo() {
		sb.setLength(0);
		int total = SongJsonParseUtils.getTotal();
		if (total > 8) {
			
			if ((total - 8) % 2 == 0)
				sb.append("(").append(currentPage).append("/").append((total - 8) / 2 + 1).append(")");
			else
				sb.append("(").append(currentPage).append("/").append((total - 8) / 2 + 2).append(")");
		} else {
			sb.append("(1/1)");
		}
		return sb.toString();
	}

	@SuppressLint("NewApi")
	private void showMoreOptionDialog(String num, String orderId,int downstat) {
		// TODO Auto-generated method stub
		// Log.i("song","MyApplication.currentItemPosition=="+MyApplication.currentItemPosition);
		if (enterType == 30) {// 收藏
			if (mCollectionMoreOptionDialog == null)
				mCollectionMoreOptionDialog = new CollectionMoreOptionDialog(
						getActivity(), style.MyDialog, mHandler);
			mCollectionMoreOptionDialog.setSongNum(num);
		
			mCollectionMoreOptionDialog
					.setRefreshListener(new CollectRefreshListener() {

						@Override
						public void refreshCollectList() {
							// TODO Auto-generated method stub
							ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas2(
									0, "", 0, 30, 0,MyApplication.searchStartCount, 8, "");
							mAdapter.refresh(list);
	
							tvTitelCount.setText(MyApplication.currentSinger
									+ "/" + SongJsonParseUtils.getSearchKey()
									+ getPageInfo());
							
							if(list!=null && list.size()>0){
								gvSong.requestFocus();
								gvSong.requestFocusFromTouch();
								gvSong.setSelection(0);
							}	
						}

					});
			setAttributes(mCollectionMoreOptionDialog);

		} else if (enterType == 21) {// 下载
			if (mDownloadOptionDialog == null)
				mDownloadOptionDialog = new DownloadOptionDialog(getActivity(),
						null, 20, style.MyDialog, mHandler);
			mDownloadOptionDialog.setSongNum(num);
			mDownloadOptionDialog.setDownstat(downstat);
			Window window2 = mDownloadOptionDialog.getWindow();
			setAttributes(mDownloadOptionDialog);

			mDownloadOptionDialog
					.setRefreshListener(new DownloadRefreshListener() {

						@Override
						public void refreshList() {
							// TODO Auto-generated method stub
							ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas2(
									0, "", 0, 21, 0,
									MyApplication.searchStartCount, 8, "");
							mAdapter.refresh(list);

							tvTitelCount.setText(MyApplication.currentSinger
									+ "/" + SongJsonParseUtils.getSearchKey()
									+ getPageInfo());
							
							if(list!=null && list.size()>0){
								gvSong.requestFocus();
								gvSong.requestFocusFromTouch();
								gvSong.setSelection(0);
							}															
						}

					});
			//mDownloadOptionDialog.show();

		} else if (enterType == 31) {// 已点

			if (mYidianOptionDialog == null)
				mYidianOptionDialog = new YidianOptionDialog(getActivity(),
						null, 20, style.MyDialog, mHandler);
			mYidianOptionDialog.setSongNum(num);
			mYidianOptionDialog.setOrderId(orderId);
			Window window4 = mYidianOptionDialog.getWindow();
			setAttributes(mYidianOptionDialog);

			mYidianOptionDialog.setRefreshListener(new YiDianRefreshListener() {

				@Override
				public void refreshList() {
					// TODO Auto-generated method stub
					ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas2(0, "", 0,
							31, 0, MyApplication.searchStartCount, 8, "");
					
					mAdapter.refresh(list);
					if(list!=null && list.size()>0){
						gvSong.requestFocus();
						gvSong.requestFocusFromTouch();
						gvSong.setSelection(0);
					}
				}

			});
			//mYidianOptionDialog.show();
		} else if (enterType == 40) {// 录音
			if (mRecordOptionDialog == null)
				mRecordOptionDialog = new RecordOptionDialog(getActivity(),
						null, 20, style.MyDialog, mHandler);
			mRecordOptionDialog.setSong(recordSong);
			Window window3 = mRecordOptionDialog.getWindow();
			setAttributes(mRecordOptionDialog);

			mRecordOptionDialog.setRefreshListener(new RecordRefreshListener() {

				@Override
				public void refreshList() {
					// TODO Auto-generated method stub
					mAdapter.refresh(SongJsonParseUtils.getSongDatas2(0, "", 0,
							40, 0, MyApplication.searchStartCount, 8, ""));
				}

			});
			//mRecordOptionDialog.show();

		} else if (enterType == 22) {
			if (mlLocalMoreOptionDialog == null)
				mlLocalMoreOptionDialog = new LocalMoreOptionDialog(
						getActivity(), null, 20, style.MyDialog, mHandler,
						false);
			Window window = mlLocalMoreOptionDialog.getWindow();
			setAttributes(mlLocalMoreOptionDialog);
			mlLocalMoreOptionDialog.setSongNum(num);
			mlLocalMoreOptionDialog
					.setRefreshListener(new LocalRefreshListener() {

						@Override
						public void refreshLocalList() {
							// TODO Auto-generated method stub
							ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas2(
									0, "", 0, 22, 0,MyApplication.searchStartCount, 8, "");
							
							mAdapter.refresh(list);
							if(list!=null && list.size()>0){
								gvSong.requestFocus();
								gvSong.requestFocusFromTouch();
								gvSong.setSelection(0);
							}
							
						}
					});
			//mlLocalMoreOptionDialog.show();
		} else {
			if (mOptionDialog == null)
				mOptionDialog = new MoreOptionDialog(getActivity(), null, 20,
						R.style.MyDialog, mHandler, false);			
				//	window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				mOptionDialog.setSongNum(num);
				//mOptionDialog.show();
				setAttributes(mOptionDialog);
			/*	FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				mOptionDialog.show(ft, "df");*/
          
		}

	}

	public void setAttributes(Dialog dialog) {
	
		Window window = dialog.getWindow();
	//	window.addFlags(LayoutParams.FLAG_DIM_BEHIND);
		window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		LayoutParams params = window.getAttributes();
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

		params.dimAmount=0f;
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
		// Log.i("song","onresume =======");
		MyApplication.isNeedRefresh = true;
		if (enterType != 43 && enterType != 40 && enterType != 31
				&& enterType != 21 && enterType != 32 && enterType != 22) {
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_YIDIAN_LIST);
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SEARCHVIEW);
			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
			//解决默认选择时不触发onitemselectlistener
			try {
	            Method fireOnSelected = AdapterView.class.getDeclaredMethod("fireOnSelected", null);
	            fireOnSelected.setAccessible(true);
	            fireOnSelected.invoke(gvSong); //运行该方法
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
		}else{
			handler.postDelayed(new Runnable() {				
			@Override
			public void run() {
				// TODO Auto-generated method stub
				gvSong.requestFocus();
				gvSong.requestFocusFromTouch();
				gvSong.setSelection(0);
			}
		}, 500);
			
		}
		
		if(enterType != 1)
			languageClass.setVisibility(View.INVISIBLE);
		MyApplication.isInHomeFragment = false;
		MyApplication.isSongNameFragment = true;
		MyApplication.isSingerListFocus = false;
		MyApplication.searchStartCount = 0;
		// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_CLEAR_SEARCHVIEW);

		list_start = 0;
		currentPage = 1;
		handler.postDelayed(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				tvTitelCount.setText(MyApplication.currentSinger + "/"
						+ SongJsonParseUtils.getSearchKey() + getPageInfo());
				if (enterType == 21) {
					View view = gvSong.getChildAt(0);
					if(view != null){
					mAdapter.setRefreshView(view);
					MyApplication.isShowDownloadProgress = true;
					mAdapter.downloadThread.start();
					Log.i("song","refresh download progress===========");
					}
				}
			}

		}, 500);

		
	//	new UpdateSongTxtDialog(getActivity(), R.style.MyDialog, "歌单").show();
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.iv_pagedown:
			getpageDown();
			break;
		case R.id.iv_pageup:
			getpageUp();
			break;

		default:
			break;
		}
	}

	private void getpageDown() {
		// TODO Auto-generated method stub

	}

	private void getpageUp() {
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

		if (enterType == 43 || enterType == 40 || enterType == 31
				|| enterType == 21 || enterType == 3 || enterType==22) {// 默认、录音�?已点、下载�?历史无搜索功�?

			mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS);
			ImageView iv = null;
			switch (enterType) {

			case 21:// 云端（下载）
			case 31:// 已点
				iv = (ImageView) view.findViewById(R.id.iv_youxian);
				break;
			case 40:// 录音
				iv = (ImageView) view.findViewById(R.id.iv_play);
				break;
			case 43:// 公播
				iv = (ImageView) view.findViewById(R.id.iv_delete);
				break;
			default:
				iv = (ImageView) view.findViewById(R.id.iv_add);
				break;
			}
			iv.requestFocus();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
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
		MyApplication.isSongNameFragment = false;
		MyApplication.currentSinger = "";
		MyApplication.currentSingerNum = "";
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW);
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
		getActivity().unregisterReceiver(myReciver);
	}

	public void refresh(ArrayList<SongSearchBean> songs) {
		 Log.i("song","refresh data size 111===="+songs.size());
		if (mAdapter != null)
			mAdapter.refresh(songs);
			if(songs.size()<8 && MyApplication.isGridviewFocus){//最后一页，焦点回到第一条
			/*	try {
		            Method fireOnSelected = AdapterView.class.getDeclaredMethod("fireOnSelected", null);
		            fireOnSelected.setAccessible(true);
		            fireOnSelected.invoke(gvSong); //运行该方法
		        } catch (Exception e) {
		            e.printStackTrace();
		        }*/
				gvSong.requestFocus();
				gvSong.requestFocusFromTouch();
				gvSong.setSelection(0);
			
			}
	}

	private class MyReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
		
			if ("justlink.action.intent.refresh_searchcount".equals(arg1
					.getAction())) {
				// Log.i("song","receive boardcast for refresh count=====");
				tvTitelCount.setText(MyApplication.currentSinger + "/"
						+ SongJsonParseUtils.getSearchKey() + getPageInfo());
			} else if ("justlink.action.intent.refresh_listpage".equals(arg1
					.getAction())) {
				list_start = 0;
				currentPage = 1;
			}else if("justlink.action.intent.data_inited".equals(arg1.getAction())){//数据初始化完成后刷新网络推荐歌单
				 Log.i("song","receive boardcast for refresh netdata=====");
				 pb.setVisibility(View.GONE);
					ArrayList<SongSearchBean> songs = SongJsonParseUtils.getSongDatas2(0,
							"", 0, enterType, layerType, 0, 8, "");
					mAdapter.refresh(songs);
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
			if (mAdapter != null && MyApplication.isSongNameFragment
					&& MyApplication.isOnkeyupRefresh)
				mAdapter.notifyDataSetChanged();
			// Log.i("song","松开刷新==========");
		}
		return false;
	}

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("song", "fragment onkeydown==" + keyCode + "==" + currentPosition);
		if(!MyApplication.isGridviewFocus)
			return false;

		if (MyApplication.isPipFocus && mHandler != null) {
			left_right_count = 0;
			// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS);
		}
		if (keyCode == event.KEYCODE_DPAD_DOWN
				&& MyApplication.isSongNameFragment) {
			offestTime = System.currentTimeMillis() - firstKeyTime;
			if (offestTime > 1000) {
				MyApplication.isNeedRefresh = true;
				Log.i("song", "set need refresh true");
			} else {
				MyApplication.isNeedRefresh = false;
				Log.i("song", "set need refresh false");
			}
			firstKeyTime = System.currentTimeMillis();
			MyApplication.isOnkeyupRefresh = false;
			if (mHandler != null && MyApplication.isGridviewFocus) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS);
				 Log.i("song","song ban top focus===========");				
			}

			 Log.i("song","====="+SongJsonParseUtils.getListCount()+"=="+MyApplication.currentItemPosition+"=="+list_start+"=="+MyApplication.isSelectAll+"=="+MyApplication.isGridviewFocus);
			if (gvSong != null
					&& (MyApplication.currentItemPosition == 6 || MyApplication.currentItemPosition == 7)
					&& MyApplication.isGridviewFocus) {
				if ((SongJsonParseUtils.getListCount() < 8 && !MyApplication.isSelectAll)
						|| (SongJsonParseUtils.getListCount() + list_start == SongJsonParseUtils
								.getTotal() && !MyApplication.isSelectAll)
						&& MyApplication.isSongNameFragment) {
					Log.i("song", "last page tip ========");
					Message msg = Message.obtain();
					msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_TOAST_TIP;
					mHandler.sendMessage(msg);
				} else {
					MyApplication.isSelectAll = false;
					MyApplication.isOnkeyupRefresh = true;
					list_start += 2;
					currentPage += 1;
					MyApplication.searchStartCount = list_start;
					Message msg = Message.obtain();
					msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_DATA;
					msg.arg1 = enterType;
					msg.arg2 = layerType;
					msg.obj = list_start;
					// Log.i("song","发�?刷新参数===entertype=="+msg.arg1+"===layertype=="+msg.arg2+"==start count=="+msg.obj);
					mHandler.sendMessage(msg);
				}

			}

		} else if (keyCode == event.KEYCODE_DPAD_UP) {
			if (mHandler != null) {
				//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);
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
				Message msg = Message.obtain();
				msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_DATA;
				msg.arg1 = enterType;
				msg.arg2 = layerType;
				msg.obj = list_start;
				mHandler.sendMessage(msg);
				 mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS);
			} else if (list_start == 0 && mHandler != null
					&& MyApplication.isSongNameFragment) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
				currentPosition = 0;
//				 Log.i("song","song name reset top focus==========");
			}

		} else if (keyCode == event.KEYCODE_DPAD_RIGHT) {

			if (mAdapter == null || MyApplication.isTopButtonFocus)
				return false;
		/*	if (mHandler != null)
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);*/

		} else if (keyCode == event.KEYCODE_DPAD_LEFT) {
			/*if (!MyApplication.isGridviewFocus && mHandler != null
					&& !MyApplication.isSearchViewShow)
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);*/
			if (MyApplication.isSearchViewShow
					&& (MyApplication.currentItemPosition == 4 || MyApplication.currentItemPosition == 6)
					&& MyApplication.isSongNameFragment) {
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

		 Log.i("song","songname onkeydown isShowDownloadProgress==="+MyApplication.isShowDownloadProgress);
		return false;
	}

	@SuppressLint("NewApi")
	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		//setClassState();
	//	Log.i("song","class focus change=========="+arg1);
		if (arg1 && !isBanClassFocus){
		switch (arg0.getId()) {
		case R.id.btn_language_all://全部
			setData(1,0,0);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.class_all);
			break;
		case R.id.btn_language_1://国语
			setData(1,1,1);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.language_1);
			break;
		case R.id.btn_language_2://英语
			setData(1,4,2);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.language_2);
			break;
		case R.id.btn_language_3://闽南
			setData(1,3,3);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.language_3);
			break;
		case R.id.btn_language_4://韩语
			setData(1,6,4);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.language_4);
			break;
		case R.id.btn_language_5://粤语
			setData(1,2,5);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.language_5);
			break;
		case R.id.btn_language_6://日语
			setData(1,5,6);
			MyApplication.currentSinger="/song/"+getResources().getString(R.string.language_6); 
			break;

		default:
			break;
		}
		
		//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
		//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SEARCHVIEW);
		
		ArrayList<SongSearchBean> songs = SongJsonParseUtils.getSongDatas2(0,"", 0, enterType, layerType, 0, 8, "");
		mAdapter.refresh(songs);
		tvTitelCount.setText(MyApplication.currentSinger + "/"
				+ SongJsonParseUtils.getSearchKey() + getPageInfo());
		}
	}

	private void setData(int enterType,int layer,int index){
		setEnterAndLayerType(enterType, layer);
		Message msg = Message.obtain();
		msg.arg1=enterType;
		msg.arg2 = layer;
		msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_SET_SEARCHVIEW_ENTER_LAYER;
		mHandler.sendMessage(msg);
		class_select_index = index;
	}

	
}
