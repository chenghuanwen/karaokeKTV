package com.jsl.ktv.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jsl.ktv.bean.SingerSearchBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.util.SongJsonParseUtils;
import com.jsl.ktv.view.AnimationImageView;
import com.jsl.ktv.view.MyApplication;

import android.text.TextUtils;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.KeyEvent;
import com.jsl.ktv.R;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import android.graphics.Matrix;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.Button;
import android.widget.HorizontalScrollView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.player.boxplayer.karaok.JNILib;
import android.os.Message;
import com.jsl.ktv.karaok.MainActivity;
import com.jsl.ktv.karaok.VideoString;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jsl.ktv.view.CustomeGridView;

public class SingerFragment extends CommonFragment implements OnClickListener,
		OnFocusChangeListener {

	private static final int WHAT_UPDATE_LIST = 1;
	private final int DEFAULT_DRAWABLE = R.drawable.browser;
	private static JSONObject cmdObject;
	private static JSONObject resultObject;

	private static int focusButton = -1;
	private String[] textTitle;
	private List<View> views;
	private List<Bitmap> bitmaps;
	private boolean setDefaultView = true;
	private static Thread flashThread = null;
	private static boolean paused = false;
	private int curPosition = 0;
	private View myview = null;
	private TextView tvTitel1, tvTitel2, tvTitelCount;
	private final String PROPERTY_TYPE = "ro.product.machinetype";
	private String MACHINE_TYPE = "changhe_2.1.1";
	private String SOUND_PATH = "/system/media/audio/ui/Effect_Tick.ogg";
	// private static GridView grid_main;
	private static CustomeGridView grid_main;
	private static MyGridAdapter adapter;
	private LayoutInflater minflater;
	private static ArrayList<SingerSearchBean> Datas;
	private static Handler mHandler;
	private static int currentPosition = 0,currentPage = 1;
	private static String searchKey = "";
	private static int list_start = 0;
	// private GridView item_pick;
	private ArrayList<Bitmap> singerIcons;
	private boolean isDownPage = true;
	private int recoverCount = 0;
	private long firstKeyTime = 0L, offestTime = 0L;
	private int jLanguage = 0;
	private static int totalCount;
	// private boolean isNeedRefresh = true;
	int layType = 0;
	//private static final String baseNetPath = "http://www2.jiashilian.com/index.php?s=/Home/Index/imgPath";
	private static final String baseNetPath = "http://api.jiashilian.com:8888/Api/download/picture";
	private static final String baseSavePath = "/mnt/sdcard/jlink/picture/";
	private OkHttpClient okHttpClient;
	private Handler uiHandler;
	private HorizontalScrollView classContainer;
	private Button btnSingerAll, btnSinger1, btnSinger2, btnSinger3,
			btnSinger4, btnSinger5, btnSinger6, btnSinger7, btnSinger8,
			btnSinger9, btnSinger10, btnSinger11;
	private MyReceiver myReciver;
	private ProgressBar pb;
	private static int currentCount=0;
	private static int startCount =0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case WHAT_UPDATE_LIST:
				// Log.i("song","handle============WHAT_UPDATE_LIST========layer======="+layType);
				Datas = parseJSONObect(getSingerDatas(0, searchKey, 0, 2,
						layType, list_start, 12));
				refresh(Datas);

				break;

			default:
				break;
			}
		}
	};


	public SingerFragment() {

	};

	public SingerFragment(Handler handler) {
		this.mHandler = handler;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		minflater = inflater;
		myview = minflater.inflate(R.layout.singer_fragment_view, null);
		initView(myview);
		return myview;
	}
	

	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		registReciver();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		super.onResume();
		MyApplication.isNeedRefresh = true;
		MyApplication.isSingerFragment = true;
		list_start = 0;
		recoverCount = 0;
		currentPage = 1;
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SEARCHVIEW);
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
		singerIcons.clear();
		MainActivity main = (MainActivity) getActivity();
		main.SetSearchEnterAndLayerType(2, 0);
		// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_CLEAR_SEARCHVIEW);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		paused = true;
		MyApplication.isSingerListFocus = false;
		MyApplication.isSingerFragment = false;
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_CLEAR_SEARCHVIEW);
		MyApplication.isSingerListFocus = false;
		MyApplication.isSingerFragment = false;
		getActivity().unregisterReceiver(myReciver);
	}

	
	@SuppressLint("NewApi")
	private void registReciver() {
		// TODO Auto-generated method stub
		myReciver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("justlink.action.intent.data_inited");
		getActivity().registerReceiver(myReciver, filter);
		
	}
	
	
	@SuppressLint("NewApi")
	private void initView(View view) {
		okHttpClient = new OkHttpClient.Builder().connectTimeout(5,
				TimeUnit.SECONDS).build();

		uiHandler = new Handler(Looper.getMainLooper());

		jLanguage = JNILib.getGlobalLanguage();

		tvTitel1 = (TextView) view.findViewById(R.id.tv_search_1);
		tvTitel2 = (TextView) view.findViewById(R.id.tv_search_2);
		tvTitelCount = (TextView) view.findViewById(R.id.tv_search_count);
		pb = (ProgressBar) view.findViewById(R.id.pb);

		tvTitel1.setText(VideoString.song_search_tip2[jLanguage]);
		tvTitel2.setText(VideoString.song_search_tip2[jLanguage]);
		tvTitelCount.setText("0");

		classContainer = (HorizontalScrollView) view
				.findViewById(R.id.ll_singer_class);

		btnSingerAll = (Button) view.findViewById(R.id.btn_singer_class_all);
		btnSinger1 = (Button) view.findViewById(R.id.btn_singer_class_1);
		btnSinger2 = (Button) view.findViewById(R.id.btn_singer_class_2);
		btnSinger3 = (Button) view.findViewById(R.id.btn_singer_class_3);
		btnSinger4 = (Button) view.findViewById(R.id.btn_singer_class_4);
		btnSinger5 = (Button) view.findViewById(R.id.btn_singer_class_5);
		btnSinger6 = (Button) view.findViewById(R.id.btn_singer_class_6);
		btnSinger7 = (Button) view.findViewById(R.id.btn_singer_class_7);
		btnSinger8 = (Button) view.findViewById(R.id.btn_singer_class_8);
		btnSinger9 = (Button) view.findViewById(R.id.btn_singer_class_9);
		btnSinger10 = (Button) view.findViewById(R.id.btn_singer_class_10);
		btnSinger11 = (Button) view.findViewById(R.id.btn_singer_class_11);
		btnSingerAll.setOnFocusChangeListener(this);
		btnSinger1.setOnFocusChangeListener(this);
		btnSinger2.setOnFocusChangeListener(this);
		btnSinger3.setOnFocusChangeListener(this);
		btnSinger4.setOnFocusChangeListener(this);
		btnSinger5.setOnFocusChangeListener(this);
		btnSinger6.setOnFocusChangeListener(this);
		btnSinger7.setOnFocusChangeListener(this);
		btnSinger8.setOnFocusChangeListener(this);
		btnSinger9.setOnFocusChangeListener(this);
		btnSinger10.setOnFocusChangeListener(this);
		btnSinger11.setOnFocusChangeListener(this);

		singerIcons = new ArrayList<Bitmap>();
		grid_main = (CustomeGridView) view.findViewById(R.id.grid_main);
		// grid_main.setItemsCanFocus(true);
		// grid_main.setFocusable(false);
		//grid_main.setOverScrollMode(View.OVER_SCROLL_NEVER);
		String res = getSingerDatas(0, searchKey, 0, 2, 0, 0, 12);
		Datas = parseJSONObect(res);
		
		if(Datas.size() == 0)
		pb.setVisibility(View.VISIBLE);
		
		adapter = new MyGridAdapter(getActivity(), Datas);
		grid_main.setAdapter(adapter);
		grid_main
				.setOnItemSelectedListener(new GridView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						Log.v("song", "onItemSelected====" + position);
						// adapter.update_sel(position);
						currentPosition = position;
						adapter.setNotifyDataChange(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

						Log.v("song", "onNothingSelected*******");
						// adapter.update_unsel(position);
					//	mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);

					}

				});
		grid_main.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				replaceFragment(R.id.center_fragment, new SongNameFragment(
						mHandler, 0, 0));
				SingerSearchBean searchBean = Datas.get(pos);
				// Log.v("song","song_no===="+Datas.get(pos));
				if (searchBean.getSinger() != null)
					MyApplication.currentSinger = searchBean.getSinger();
				Message msg = Message.obtain();
				msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER_DATA;
				msg.obj = searchBean.getSingerNumber();
				mHandler.sendMessage(msg);

			}

		});

		grid_main.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.i("song", "grid_main focus change=======" + arg1);
				if (arg1){
					MyApplication.isSingerListFocus = true;
					/* try {
				            Method fireOnSelected = AdapterView.class.getDeclaredMethod("fireOnSelected", null);
				            fireOnSelected.setAccessible(true);
				            fireOnSelected.invoke(grid_main); //运行该方法
				        } catch (Exception e) {
				            e.printStackTrace();
				        }*/
				}
				else
					MyApplication.isSingerListFocus = false;
			}
		});

		tvTitelCount.setText("/" + MyApplication.searchKey +getPageInfo());

	}

	public static boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// Log.i("song", "====JSL==== fragment onKeyup= "+keyCode);
		if (keyCode == 20 || keyCode == 19) {
			MyApplication.isNeedRefresh = true;
			if (adapter != null && MyApplication.isSingerFragment
					&& MyApplication.isOnkeyupRefresh)
				adapter.notifyDataSetChanged();
			// Log.i("song","松开刷新==========");
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (grid_main != null)
			Log.i("song", "fragment onkeydown==" + keyCode + "=="
					+ currentPosition + "Datas.size()=" + Datas.size());
		if (keyCode == event.KEYCODE_DPAD_DOWN) {

			offestTime = System.currentTimeMillis() - firstKeyTime;
			if (offestTime > 1000) {
				MyApplication.isNeedRefresh = true;
			} else {
				MyApplication.isNeedRefresh = false;
			}
			firstKeyTime = System.currentTimeMillis();
			MyApplication.isOnkeyupRefresh = false;
			// Log.i("song","onkeydown=======+offestTime"+offestTime+"isNeedRefresh====="+MyApplication.isNeedRefresh);
			isDownPage = true;

			if (grid_main != null && currentPosition < Datas.size() - 2
					&& MyApplication.isSingerListFocus)
				grid_main.setSelection(currentPosition);

			if (grid_main != null
					&& (currentPosition >= 8 && currentPosition <= 11)
					&& Datas.size() == 12 && MyApplication.isSingerListFocus) {
				MyApplication.isOnkeyupRefresh = true;
				list_start += 4;
				currentPage += 1;
				Message message1 = new Message();
				message1.what = WHAT_UPDATE_LIST;
				handler.sendMessage(message1);
				banClassFocus();
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS);
				// refresh(Datas);
			}

		} else if (keyCode == event.KEYCODE_DPAD_UP) {

			offestTime = System.currentTimeMillis() - firstKeyTime;
			if (offestTime > 1000) {
				MyApplication.isNeedRefresh = true;
			} else {
				MyApplication.isNeedRefresh = false;
			}
			firstKeyTime = System.currentTimeMillis();
			MyApplication.isOnkeyupRefresh = false;
			Log.i("song", "onkeyup=======offestTime==" + offestTime + "=="
					+ currentPosition + "==" + list_start + "=="
					+ MyApplication.isSingerListFocus);
			isDownPage = false;
			if (grid_main != null && currentPosition > 1
					&& MyApplication.isSingerListFocus)
				grid_main.setSelection(currentPosition);

			if (grid_main != null
					&& (currentPosition == 0 || currentPosition == 1
							|| currentPosition == 2 || currentPosition == 3)
					&& list_start > 0 && MyApplication.isSingerListFocus) {
				MyApplication.isOnkeyupRefresh = true;
				list_start -= 4;
				currentPage -= 1;
				Message message1 = new Message();
				message1.what = WHAT_UPDATE_LIST;
				handler.sendMessage(message1);
				// refresh(Datas);
				// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS);
				return false;
			} else if (list_start == 0 && mHandler != null
					&& MyApplication.isSingerListFocus) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
				resetClassFocus();
				Log.i("song", "singer reset top focus==========");
			}

		} else if (keyCode == event.KEYCODE_DPAD_RIGHT) {

		/*	if (mHandler != null)
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);*/

		} else if (keyCode == event.KEYCODE_DPAD_LEFT) {
		/*	if (!MyApplication.isGridviewFocus && mHandler != null
					&& !MyApplication.isSearchViewShow)
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);*/
			if (MyApplication.isSearchViewShow
					&& (currentPosition == 4 || currentPosition == 8)
					&& MyApplication.isSingerFragment) {
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
				currentPosition = 0;
				MyApplication.isOnkeyupRefresh = false;
			}

		}
		return false;
	}

	/**
	 * int search_type; 搜索类型(参数和之前TV对接�?��) char search_key[20];
	 * 搜索字段(参数和之前TV对接�?��) int cloud_flag; 云端标签(暂时无用) int enter_type; 大分类ep:
	 * 歌名,歌星,分类,排行�? int layer_type; 子分�? ep:歌名下面的语别分�? 分类下面的子分类�? int
	 * list_start; �?��获取数据的序列号 int list_count; 获取数据数目
	 * 
	 * @return
	 */
	public static String getSingerDatas(int search_type, String search_key,
			int cloud_flag, int enter_type, int layer_type, int list_start,
			int list_count) {

		try {
			cmdObject = new JSONObject();
			cmdObject.put("search_type", search_type);
			cmdObject.put("search_key", search_key);// searchType 搜索字段(首字�?
			cmdObject.put("cloud_flag", cloud_flag);
			cmdObject.put("enter_type", enter_type);
			cmdObject.put("layer_type", layer_type);
			cmdObject.put("list_start", list_start);
			cmdObject.put("list_count", list_count);

			String result = JNILib.getSongData(cmdObject.toString());
			// Log.i("song","singer搜索命令==="+cmdObject.toString());
			Log.i("song", "singer搜索结果===" + result);

			return result;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("song", "JSONException===" + e.toString());
		}

		return null;
	}

	private static ArrayList<SingerSearchBean> parseJSONObect(String result) {
		// TODO Auto-generated method stub
		ArrayList<SingerSearchBean> list = new ArrayList<SingerSearchBean>();
		try {
			resultObject = new JSONObject(result);
			totalCount = resultObject.getInt("total_count");// 当前类别总数�?
			startCount = resultObject.getInt("list_start");// 本次起始序列�?
			currentCount = resultObject.getInt("list_count");// 当次返回数量
			if (currentCount == 0)
				return list;

			for (int i = 0; i < currentCount; i++) {
				JSONObject listObject = resultObject.getJSONObject("list" + i);
				SingerSearchBean singer = new SingerSearchBean();
				singer.setSinger(listObject.getString("singer_name"));
				singer.setSingerNumber(listObject.getString("singer_number"));
				list.add(singer);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Log.i("song","JSONException==="+e.toString());
		}

		return list;

	}

	// 判断文件夹是否存�? 存在 true, 不存�?false
	private boolean isFileExist(String strFolder) {
		File file = new File(strFolder);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void getLockBitmapFromFile(String path,
			onGetPictureFinisherListener listener) {
		// TODO Auto-generated method stub
		File file = new File(path);
		Bitmap bitmap = null;
		try {
			FileInputStream in = new FileInputStream(file);
			int available = in.available();
			byte[] buffer = new byte[available];
			int read = in.read(buffer);
			if (read != -1) {
				char data = (char) (available & 0xFF);
				for (int i = 0; i < available; i++) {
					buffer[i] ^= ((data + i) ^ i) & 0xFF;
				}

			}
			bitmap = Bytes2Bitmap(buffer);
			if (singerIcons.size() == 12 && isDownPage) {
				singerIcons.remove(0);
				singerIcons.add(bitmap);
			} else if (singerIcons.size() == 12 && !isDownPage) {
				if (recoverCount == 3)
					recoverCount = 0;
				singerIcons.remove(recoverCount);
				singerIcons.add(recoverCount, bitmap);
				recoverCount += 1;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		// Log.v("bitmap","======Width()====="+bitmap.getWidth());
		// Log.v("bitmap","======Height()====="+bitmap.getHeight());
		// return zoomBitmap(bitmap,175,175);
		// return bitmap;
		listener.onGetPictureFinish(bitmap);
	}

	public Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newBmp;
	}

	/*
	 * public static Bitmap byteToBitmap(byte[] imgByte) { InputStream input =
	 * null; Bitmap bitmap = null; BitmapFactory.Options options = new
	 * BitmapFactory.Options(); options.inSampleSize = 8; input = new
	 * ByteArrayInputStream(imgByte); SoftReference<Bitmap> softRef = new
	 * SoftReference<Bitmap>(BitmapFactory.decodeStream( input, null, options));
	 * bitmap = (Bitmap) softRef.get(); if (imgByte != null) { imgByte = null; }
	 * 
	 * try { if (input != null) { input.close(); } } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } return bitmap; }
	 */

	protected class MyGridAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<SingerSearchBean> mDatas;
		private Request okRequest;
		private FormBody body;
		private DisplayImageOptions options;
		private ImageLoader imageLoader;
		// private FileOutputStream outputStream;
		private ImageLoaderConfiguration configuration;
		public MyGridAdapter(Context context, ArrayList<SingerSearchBean> mDatas) {
			this.context = context;
			this.mDatas = mDatas;
			initOptions();
			imageLoader = ImageLoader.getInstance();
			configuration = new ImageLoaderConfiguration.Builder(context)
			.defaultDisplayImageOptions(options).tasksProcessingOrder(QueueProcessingType.FIFO).build();
			imageLoader.init(configuration);									
		}

						
		private void initOptions() {

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.b)
					// resource or drawable
					.showImageForEmptyUri(R.drawable.b)
					// resource or drawable
					.showImageOnFail(R.drawable.b)
					// resource or drawable
					.resetViewBeforeLoading(false)
					// default
					
					.delayBeforeLoading(1000).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(false) // default
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
					.bitmapConfig(Bitmap.Config.ARGB_4444) // default
					.displayer(new SimpleBitmapDisplayer()) // default
					.handler(new Handler()) // default
					.build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas == null ? 0 : mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// final VIewHolder holder = null;

			SingerSearchBean searchBean = mDatas.get(position);// isFileExist
			String path = baseSavePath + searchBean.getSingerNumber() + ".jpg";
			// Log.i("song","path====="+path);
			// Log.i("song","isNeedRefresh===="+MyApplication.isNeedRefresh+"===isFileExist==="+isFileExist(path));
			final VIewHolder holder;
			if (convertView == null) {
				holder = new VIewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.list_singer_main, null);
			/*	holder.animation = (AnimationImageView) convertView
						.findViewById(R.id.iv_image);*/
				holder.inageView = (ImageView) convertView
						.findViewById(R.id.iv_image);
			//	holder.focusButton = (ImageView) convertView.findViewById(R.id.singer_bk_image);
				holder.textView = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.inageView.setTag(searchBean.getSingerNumber());
				convertView.setTag(holder);
			} else {
				holder = (VIewHolder) convertView.getTag();
				holder.inageView.setTag(searchBean.getSingerNumber());
			}

			/*if (selectPic == position) {
				Log.i("song","selectpic=="+selectPic+"==islastitem=="+isLastItem(selectPic));
				Animation testAnim = AnimationUtils.loadAnimation(
						getActivity(), R.anim.singerselect_animation);
				convertView.startAnimation(testAnim);
				//if(selectPic>=8 || isLastItem(selectPic)){
					LinearLayout.LayoutParams param = (LayoutParams) holder.textView.getLayoutParams();
					param.setMargins(0, -50, 0, 0);
					holder.textView.setLayoutParams(param);	
				//}
				
			} else {
				Animation testAnim = AnimationUtils.loadAnimation(
						getActivity(), R.anim.singerselect_animation_reset);
				convertView.startAnimation(testAnim);	
				LinearLayout.LayoutParams param = (LayoutParams) holder.textView.getLayoutParams();
				param.setMargins(0, 0, 0, 0);
				holder.textView.setLayoutParams(param);
				//Log.i("song","no selectpic=="+position);
			}*/

			// SystemClock.sleep(100);
			if (isFileExist(path) && MyApplication.isNeedRefresh) {
				imageLoader.displayImage("file:/" + path,
						holder.inageView, options);
			//	Log.i("song","加载本地歌星图========");
			} else if (!isFileExist(path) && MyApplication.isNeedRefresh) {

				try {
					final File saveFile = new File(path);
					saveFile.createNewFile();
					final FileOutputStream outputStream = new FileOutputStream(
							saveFile);
					LoadPicturePath(searchBean.getSingerNumber(),
							new GetPictureListener() {

								@Override
								public void onGetPathFinish(final String path) {
									// Log.i("song","返回图片地址======"+path);
									if (TextUtils.isEmpty(path)) {
										defaultPiture(saveFile,
												holder.inageView);
									} else {
										imageLoader.loadImage(
												path, options,
												new ImageLoadingListener() {

													@Override
													public void onLoadingStarted(
															String arg0,
															View arg1) {
														// TODO Auto-generated
														// method stub
														// Log.i("song","ImageLoader 开始下载======");
													}

													@Override
													public void onLoadingFailed(
															String arg0,
															View arg1,
															FailReason arg2) {
														// TODO Auto-generated
														// method stub
														 Log.i("song","ImageLoader 下载失败======");
														defaultPiture(
																saveFile,
																holder.inageView);
													}

													@Override
													public void onLoadingComplete(
															String arg0,
															View arg1,
															final Bitmap bitmap) {
														// TODO Auto-generated
														// method stub
														// Log.i("song","ImageLoader 下载完成======path=="+path+"===singernum=="+(String)holder.inageView.getTag());
														if (bitmap == null) {
															defaultPiture(
																	saveFile,
																	holder.inageView);
															// Log.i("song","返回图片bitmap======null");
														} else {
															uiHandler
																	.post(new Runnable() {

																		@Override
																		public void run() {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			if (path.contains((String) holder.inageView
																					.getTag()))
																				holder.inageView
																						.setImageBitmap(bitmap);
																		}
																	});
															boolean isFinish = bitmap
																	.compress(
																			CompressFormat.JPEG,
																			100,
																			outputStream);
															if (!isFinish)
																saveFile.delete();
														}
													}

													@Override
													public void onLoadingCancelled(
															String arg0,
															View arg1) {
														// TODO Auto-generated
														// method stub
														// Log.i("song","ImageLoader 下载取消======");
														defaultPiture(
																saveFile,
																holder.inageView);

													}
												});

									}
									// TODO Auto-generated method stub

								}
							});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("song", "download error ====" + e.toString());
				}

			} else {
				holder.inageView.setImageResource(R.drawable.b);
			}

			
			holder.textView.setText(searchBean.getSinger());
			// Log.i("song","searchBean.getSinger()====="+searchBean.getSinger());

			return convertView;
		}

		// 刷新显示

		private String path;
		private JSONObject object;
		private int selectPic = -1;
		private HashMap<String,String> map = new HashMap<String, String>();
		private void LoadPicturePath(String singerNumber,
				final GetPictureListener listener) {
			// TODO Auto-generated method stub
			//Log.i("song","开始下载歌星图========");

			// realseCacheFile();
			map.clear();
			map.put("mac", MyApplication.MAC);
			map.put("type","singer");
			map.put("ID", singerNumber);
			String json = "" ;
			ObjectMapper mapper = new ObjectMapper();
			try {
				json = mapper.writeValueAsString(map);
			} catch (JsonGenerationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);						
			
			okRequest = new Request.Builder().url(baseNetPath).post(body)
					.build();
			okHttpClient.newCall(okRequest).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response arg1)
						throws IOException {
					// TODO Auto-generated method stub								                                                                       							
					String result = arg1.body().string();
				//	Log.i("song","歌星========"+result);
					try {
						object = new JSONObject(result);
						String state = object.getString("result");
						if("0".equals(state)){
							path = object.getString("url");
							listener.onGetPathFinish(path);
						}else{
							listener.onGetPathFinish("");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						listener.onGetPathFinish("");
						e.printStackTrace();
					}
					
					
					
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					listener.onGetPathFinish("");
				//	Log.i("song", "歌星 error ========" + arg1.toString());
				}
			});
		}

		public void refresh(ArrayList<SingerSearchBean> datas) {
			// Log.i("song","refresh.refresh()====="+datas.size());
			mDatas.clear();
			mDatas.addAll(datas);
			// mDatas.addAll(datas.subList(datas.size()-12, datas.size()));
			notifyDataSetChanged();
			// grid_main.setSelection(currentPosition);
			handler.postDelayed(new Runnable() {

				@SuppressLint("NewApi")
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tvTitelCount.setText("/" + MyApplication.searchKey +getPageInfo());
				}
			}, 500);

		}

		private class VIewHolder {
			AnimationImageView animation;
			ImageView inageView;
			TextView textView;
			ImageView focusButton;
		}

		public void defaultPiture(final File file, final ImageView iv) {
			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					file.delete();
					iv.setBackgroundResource(R.drawable.b);
				}
			});

		}

		public void setNotifyDataChange(int id) {
			selectPic = id;
			super.notifyDataSetChanged();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}

	}

	public void refresh(ArrayList<SingerSearchBean> singer) {
		/*
		 * tvTitelCount =
		 * (TextView)songNameView.findViewById(R.id.tv_search_count);
		 * Log.i("song","refresh song =="+songs+"tvTitel1==="+tvTitelCount);
		 * tvTitelCount.setText(Integer.toString(songs.size()));
		 */
		if (adapter != null) {
			adapter.refresh(singer);
			handler.postDelayed(new Runnable() {

				@SuppressLint("NewApi")
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tvTitelCount.setText("/" + MyApplication.searchKey + getPageInfo());
					
				}
			}, 1000);
		} else {
			Log.i("song", "refresh song ==adapter != null");
		}
	}

	public void refresh_search(String search_key) {
		searchKey = search_key;
		Message message1 = new Message();
		message1.what = WHAT_UPDATE_LIST;
		handler.sendMessage(message1);
	}

	@SuppressLint("NewApi")
	private void replaceFragment(int id, Fragment fragment) {
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out);
		transaction.replace(id, fragment);
		transaction.addToBackStack("home_fragment");
		transaction.commit();
		fManager.executePendingTransactions();
	}

	private void updateViewList() {
		/*
		 * if (null == views) views = new ArrayList<View>(); views.clear(); for
		 * (int i = 0; i < bitmaps.size(); i++) { ImageView imageView = new
		 * ImageView(getActivity()); imageView.setBackground(new
		 * BitmapDrawable(getResources(), bitmaps .get(i)));
		 * views.add(imageView); } setDefaultView = false;
		 */

	}

	@SuppressLint("NewApi")
	private View getDefaultView() {
		ImageView imageView = new ImageView(getActivity());
		imageView.setBackgroundResource(DEFAULT_DRAWABLE);
		return imageView;
	}

	@SuppressLint("NewApi")
	private Bitmap getDefaultBitmap() {
		return BitmapFactory.decodeResource(getResources(), DEFAULT_DRAWABLE);
	}

	private boolean isEmptyList(List<?> list) {
		return null == list || list.size() == 0;
	}

	/**
	 * 通过包名打开app
	 * 
	 */
	@SuppressLint("NewApi")
	private void openAPPByPackage(String pg) {
		//  通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等  

		Log.i("TAG", "==包名==" + pg);
		PackageInfo packageInfo = null;
		try {
			packageInfo = getActivity().getPackageManager().getPackageInfo(pg,
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (packageInfo == null) {
			return;
		}

		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(pg);
		List<ResolveInfo> infoList = getActivity().getPackageManager()
				.queryIntentActivities(intent, 0);
		ResolveInfo resolveInfo = infoList.iterator().next();

		if (resolveInfo != null) {

			String packageName = resolveInfo.activityInfo.packageName;
			String activityName = resolveInfo.activityInfo.name;
			Log.i("TAG", "==类名==" + activityName);
			Intent appIntent = new Intent(Intent.ACTION_MAIN, null);
			appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cp = new ComponentName(packageName, activityName);
			appIntent.setComponent(cp);
			startActivity(appIntent);
		}
	}

	public interface onGetPictureFinisherListener {
		void onGetPictureFinish(Bitmap bitmap);
	}

	public interface GetPictureListener {
		void onGetPathFinish(String path);
	}

	@SuppressLint("NewApi")
	public void realseCacheFile() {
		File cacheFile = new File(baseSavePath);
		if (cacheFile.exists()) {
			long free = cacheFile.getFreeSpace();
			Log.i("song", "cache free==" + free / 1024 / 1024);
			if (free / 1024 / 1024 < 1) {
				File[] files = cacheFile.listFiles();
				for (int i = files.length; i > files.length - 20; i--) {
					File file = files[i];
					file.delete();
				}
			}

		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (!arg1)
			return;
		switch (arg0.getId()) {
		case R.id.btn_singer_class_all:
			setData(0);
			MyApplication.searchKey = getResources().getString(
					R.string.class_all);
			break;
		case R.id.btn_singer_class_1:
			setData(1);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class1);
			break;
		case R.id.btn_singer_class_2:
			setData(2);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class2);
			break;
		case R.id.btn_singer_class_3:
			setData(3);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class3);
			break;
		case R.id.btn_singer_class_4:
			setData(4);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class4);
			break;
		case R.id.btn_singer_class_5:
			setData(5);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class5);
			break;
		case R.id.btn_singer_class_6:
			setData(6);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class6);
			break;
		case R.id.btn_singer_class_7:
			setData(7);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class7);
			break;
		case R.id.btn_singer_class_8:
			setData(8);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class8);
			break;
		case R.id.btn_singer_class_9:
			setData(9);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class9);

			break;
		case R.id.btn_singer_class_10:
			setData(10);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class10);
			break;
		case R.id.btn_singer_class_11:
			setData(11);
			MyApplication.searchKey = getResources().getString(
					R.string.singer_class11);
			break;

		default:
			break;

		}
		// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
		// mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SEARCHVIEW);
	}

	private void setData(int layer) {
		Message msg = Message.obtain();
		msg.arg1 = 2;
		msg.arg2 = layer;
		msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_SET_SEARCHVIEW_ENTER_LAYER;
		mHandler.sendMessage(msg);

		layType = layer;
		handler.sendEmptyMessage(WHAT_UPDATE_LIST);

	}

	public void hideClass() {
		classContainer.setVisibility(View.INVISIBLE);
	}

	public void showClass() {
		classContainer.setVisibility(View.VISIBLE);
	}

	public void banClassFocus() {
		btnSingerAll.setFocusable(false);
		btnSinger1.setFocusable(false);
		btnSinger2.setFocusable(false);
		btnSinger3.setFocusable(false);
		btnSinger4.setFocusable(false);
		btnSinger5.setFocusable(false);
		btnSinger6.setFocusable(false);
		btnSinger7.setFocusable(false);
		btnSinger8.setFocusable(false);
		btnSinger9.setFocusable(false);
		btnSinger10.setFocusable(false);
		btnSinger11.setFocusable(false);
		classContainer.setFocusable(false);
	}

	public void resetClassFocus() {
		btnSingerAll.setFocusable(true);
		btnSinger1.setFocusable(true);
		btnSinger2.setFocusable(true);
		btnSinger3.setFocusable(true);
		btnSinger4.setFocusable(true);
		btnSinger5.setFocusable(true);
		btnSinger6.setFocusable(true);
		btnSinger7.setFocusable(true);
		btnSinger8.setFocusable(true);
		btnSinger9.setFocusable(true);
		btnSinger10.setFocusable(true);
		btnSinger11.setFocusable(true);
	}
	
	/**
	 * 模拟系统按键。
	 * @param keyCode
	 */
	public static void onKeyEvent(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
					Log.i("song","send keycode finish=======");
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("song","send keycode error======="+e.toString());
				}
			}
		}.start();
	}
	
	
	
	private StringBuffer sb = new StringBuffer();
	public String getPageInfo() {
		sb.setLength(0);
		int total = totalCount;
		if (total > 12) {
			if ((total - 12) % 4 == 0)
				sb.append("(").append(currentPage).append("/").append((total - 12) / 4 + 1).append(")");
			else
				sb.append("(").append(currentPage).append("/").append((total - 12) / 4 + 2).append(")");
		} else {
			sb.append("(1/1)");
		}
		return sb.toString();
	}
	
	
	public class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if("justlink.action.intent.data_inited".equals(arg1.getAction())){
				pb.setVisibility(View.GONE);
			/*	//外部跳转刷新数据
				Datas = parseJSONObect(getSingerDatas(0, searchKey, 0, 2,
						layType, list_start, 12));
				refresh(Datas);*/
			}
		}
		
	}
	
	
	public boolean isLastItem(int postion){
		Log.i("song","current count==="+currentCount);
		if(currentCount <= 4){
			return true;
		}else if(currentCount>4 && currentCount<=8 && postion>3 && postion<8 ){
			return true;
		}
		return false;
	}
}
