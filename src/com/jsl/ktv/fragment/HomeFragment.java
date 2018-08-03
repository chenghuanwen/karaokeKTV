package com.jsl.ktv.fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName; 
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.fragment.SingerFragment.MyReceiver;
import com.jsl.ktv.karaok.MainActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast; 
import android.view.KeyEvent;
import android.media.AudioManager;
import com.jsl.ktv.R;
import com.jsl.ktv.R.style;
import android.widget.Toast;

 import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;

import com.jsl.ktv.util.DepthPageTransformer;
import com.jsl.ktv.util.ZoomOutPageTransformer;
import com.jsl.ktv.view.AnimationButton;
import com.jsl.ktv.view.CustomViewPager;

import com.jsl.ktv.karaok.SongsLibaryDialog;
import com.jsl.ktv.view.MyApplication;
public class HomeFragment extends CommonFragment implements OnClickListener{
    private final int WHAT_UPDATE_LIST = 1;
 	private final int DEFAULT_DRAWABLE = R.drawable.browser ;
	private HorizontalScrollView scrollview;
	private AnimationButton button_1;
	private AnimationButton button_2;
	private AnimationButton button_3;
	private AnimationButton button_4;
	private AnimationButton button_5;
	private AnimationButton button_7;

	private static int focusButton=-1;
	private String[] textTitle;
	private List<View> views;
	private List<Bitmap> bitmaps;
	private boolean setDefaultView = true;
	private static Thread flashThread=null;
	private static boolean paused = false;
	private int curPosition = 0;
	private View myview=null;

	private Handler mHandler;

	private ArrayList<View> pagerViews;
	//private MyPagerAdapter pagerAdapter;
	//private RecommendPagerAdapter pagerAdapter;
	
	 //轮播控制
    private Handler vpHandler;
    private Timer timer;
    private TimerTask task;
    private int mPosition = -1;
	
    private ConnectivityManager connectivityManager;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case WHAT_UPDATE_LIST:
				/*adapter.notifyDataSetChanged();
				if(button_1.getAnimationStatus()==false)
				{
					button_1.startBkAnimation(views.size());
				}*/
				break;

			default:
				break;
			}
		}
	};

	private MainActivity maActivity;
	private SingerFragment singerFragment;
	private SongNameFragment songFragment;
	private MyReceiver myReciver;
	
	
	public HomeFragment(){};
	public HomeFragment(Handler handler){
		this.mHandler = handler;
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.laucher_fragment_view_bak, null);
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
		paused = false;
		super.onResume();
		maActivity = (MainActivity) getActivity();
		initFocus(maActivity.getHomePosition());
		MyApplication.isInHomeFragment = true;
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_YIDIAN_LIST);
		//Log.i("song","home onresume=======");
		connectivityManager = (ConnectivityManager) getActivity().getSystemService("connectivity");
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		paused = true;
		MyApplication.isInHomeFragment = false;
		super.onPause();
	}
	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(myReciver);
		//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_YIDIAN_LIST);
	}

	
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_1:
		    maActivity.setHomePosition(1);
		    singerFragment = new SingerFragment(mHandler);
		    maActivity.singerFragment = singerFragment;
			replaceFragment(R.id.center_fragment,singerFragment);
	
			break;

		case R.id.button_2:
		    MainActivity main = (MainActivity) getActivity();
			main.SetSearchEnterAndLayerType(1, 0);
			songFragment = new SongNameFragment(mHandler, 1, 0);
			main.songFragment = songFragment;
			replaceFragment(R.id.center_fragment, songFragment);
			 maActivity.setHomePosition(2);
			 MyApplication.currentSinger = "/"+getResources().getString(R.string.home_2);
			break;

		case R.id.button_3:
			replaceFragment(R.id.center_fragment, new Quz_Fragment(mHandler));
			 maActivity.setHomePosition(3);
			break;

		case R.id.button_4:
			
			if(isNetworkLink()){
				replaceFragment(R.id.center_fragment, new RecommendFragment(mHandler));
				 maActivity.setHomePosition(4);
			}else{
				Toast.makeText(getActivity(), getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
			}
			
			break;

		case R.id.button_5:
			if(isNetworkLink()){
				 maActivity.setHomePosition(5);
				replaceFragment(R.id.center_fragment, new RankingFragment(mHandler));
			}else{
				Toast.makeText(getActivity(), getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
			}
			
			break;

		case R.id.button_6://本地歌曲（已下载）
			 MainActivity main1 = (MainActivity) getActivity();
				main1.SetSearchEnterAndLayerType(22, 0);
				replaceFragment(R.id.center_fragment, new SongNameFragment(mHandler,22,0));
				 maActivity.setHomePosition(6);
				 MyApplication.currentSinger = "/"+getResources().getString(R.string.home_6);
			
			break;

		case R.id.button_7:
			maActivity.setHomePosition(7);
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_DESTORY_LINEVIEW);
		replaceFragment(R.id.center_fragment, new Wuqu_Fragment(mHandler));
			
			break;

		case R.id.button_13:
			 maActivity.setHomePosition(13);
			replaceFragment(R.id.center_fragment, new My_Fragment(mHandler));
			
			break;

		

		default:
			break;
		}

	}
	
		private void initFocus(int fragmentPositon) {
		// TODO Auto-generated method stub
		switch (fragmentPositon) {
		case 1:
			button_1.requestFocus();
			break;
         case 2:
        	 button_2.requestFocus();
			break;
         case 3:
        	 button_3.requestFocus();
 			break;			
         case 4:
        	 button_4.requestFocus();
 			break;
         case 5:
        	 button_5.requestFocus();
 			break;
     
         case 7:
        	 button_7.requestFocus();
 			break;
     
		default:
			break;
		}
		
	}
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (paused==false && keyCode == event.KEYCODE_BACK) {
            return true;
        }
        
		return false;
    }

	@SuppressLint("NewApi")
	private void initView(View view) {
		textTitle = getResources().getStringArray(R.array.home_fragment_title);
		scrollview = (HorizontalScrollView) view.findViewById(R.id.scrollview);
		button_1 = (AnimationButton) view.findViewById(R.id.button_1);
		button_2 = (AnimationButton) view.findViewById(R.id.button_2);
		button_3 = (AnimationButton) view.findViewById(R.id.button_3);
		button_4 = (AnimationButton) view.findViewById(R.id.button_4);
		button_5 = (AnimationButton) view.findViewById(R.id.button_5);
		//button_6 = (AnimationButton) view.findViewById(R.id.button_6);
		button_7 = (AnimationButton) view.findViewById(R.id.button_7);
	/*	button_8 = (AnimationButton) view.findViewById(R.id.button_8);
		button_9 = (AnimationButton) view.findViewById(R.id.button_9);
		button_10 = (AnimationButton) view.findViewById(R.id.button_10);
		button_11 = (AnimationButton) view.findViewById(R.id.button_11);
		button_12 = (AnimationButton) view.findViewById(R.id.button_12);
		button_13 = (AnimationButton) view.findViewById(R.id.button_13);*/

		button_1.setImage(R.drawable.singer);
		button_2.setImage(R.drawable.song_name);
        button_3.setImage(R.drawable.fquzhong);
		button_4.setImage(R.drawable.recommend);
        button_5.setImage(R.drawable.lpaihang);
      //  button_6.setImage(R.drawable.local_song);
        button_7.setImage(R.drawable.fwuqu);
       /* button_8.setImage(R.drawable.lfile_manager);
        button_9.setImage(R.drawable.lapp_store);
        button_10.setImage(R.drawable.lquku);
        button_11.setImage(R.drawable.lappmanager);
       // button_12.setImage(R.drawable.lsetting);
        button_13.setImage(R.drawable.long_mine);*/
		
		button_1.setText(getResources().getString(R.string.home_1));
		button_2.setText(getResources().getString(R.string.home_2));
		button_3.setText(getResources().getString(R.string.class_3));
		button_4.setText(getResources().getString(R.string.home_4));
		button_5.setText(getResources().getString(R.string.home_5));
		//button_6.setText(getResources().getString(R.string.home_6));
		button_7.setText(getResources().getString(R.string.class_2));
	/*	button_8.setText(getResources().getString(R.string.home_8));
		button_9.setText(getResources().getString(R.string.home_9));
		button_10.setText(getResources().getString(R.string.home_10));
		button_11.setText(getResources().getString(R.string.home_11));
		button_12.setText(getResources().getString(R.string.home_12));*/
		//button_13.setText(getResources().getString(R.string.home_13));


		button_1.setTextSize(40);
		button_2.setTextSize(40);
		button_3.setTextSize(40);
		button_5.setTextSize(40);
		//button_6.setTextSize(40);
		button_7.setTextSize(40);
		//button_13.setTextSize(40);
		
		//button_1.setPadding(170,0,0,0);
		//recommendPager.setPadding(410,0,0,0);
		//button_7.setPadding(200,0,0,50);
		//button_10.setPadding(410,0,0,0);
		//button_13.setPadding(180,0,0,0);

		button_1.setOnClickListener(this);
		button_2.setOnClickListener(this);
		button_3.setOnClickListener(this);
		button_4.setOnClickListener(this);
		button_5.setOnClickListener(this);
		//button_6.setOnClickListener(this);
		button_7.setOnClickListener(this);
		/*button_8.setOnClickListener(this);
		button_9.setOnClickListener(this);
		button_10.setOnClickListener(this);
		button_11.setOnClickListener(this);
		button_12.setOnClickListener(this);*/
		//button_13.setOnClickListener(this);
		
	}

	@SuppressLint("NewApi")
	private void replaceFragment(int id, Fragment fragment) {
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
		transaction.replace(id, fragment);
		transaction.addToBackStack("home_fragment");
		transaction.commit();
		fManager.executePendingTransactions();
	}

	private void updateViewList() {
		/*if (null == views)
			views = new ArrayList<View>();
		views.clear();
		for (int i = 0; i < bitmaps.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setBackground(new BitmapDrawable(getResources(), bitmaps
					.get(i)));
			views.add(imageView);
		}
		setDefaultView = false;*/
		
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
		
	
	@SuppressLint("NewApi")
	private View buildImageView(int id) { 
		LinearLayout ll = new LinearLayout(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.WRAP_CONTENT,
		LinearLayout.LayoutParams.WRAP_CONTENT);

		ll.setLayoutParams(params);
		//ll.setGravity(Gravity.CENTER);
		ImageView iv = new ImageView(getActivity());
		LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(690,375);		
		Log.i("width--height", "width = " + params.width + "---" + "height = " + params.height);
		iv.setLayoutParams(iv_params);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setBackgroundResource(id);
		//iv.setAdjustViewBounds(true);
		ll.addView(iv);

		return ll;
		}
	
	
	public boolean isNetworkLink(){
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info != null){
			return true;
		}
		return false;
	}
	
	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if("justlink.intent.action.homefragment_requst_focus".equals(arg1.getAction())){
				button_1.setFocusable(true);
				button_1.requestFocus();
			}else if("justlink.intent.action.ban_home_focus".equals(arg1.getAction())){
				button_1.setFocusable(false);
			}
		}
		
	}
	
	@SuppressLint("NewApi")
	private void registReciver() {
		// TODO Auto-generated method stub
		myReciver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("justlink.intent.action.homefragment_requst_focus");
		filter.addAction("justlink.intent.action.ban_home_focus");
		getActivity().registerReceiver(myReciver, filter);
		
	}
}
