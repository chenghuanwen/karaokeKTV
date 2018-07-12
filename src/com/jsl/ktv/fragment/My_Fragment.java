package com.jsl.ktv.fragment;

import java.util.ArrayList;
import java.util.List;
import com.jsl.ktv.view.MyApplication;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName; 
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.media.AudioManager;
import com.jsl.ktv.R;
import android.widget.Toast;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import com.jsl.ktv.view.AnimationButton;
import com.jsl.ktv.karaok.MainActivity;
import com.jsl.ktv.constant.FragmentMessageConstant;
public class My_Fragment extends CommonFragment implements OnClickListener{

	private final int WHAT_UPDATE_LIST = 1;
	private final int DEFAULT_DRAWABLE = R.drawable.browser;

	private AnimationButton button_1;
	private AnimationButton button_2;
	private AnimationButton button_3;
	private AnimationButton button_4;
	private static int focusButton=-1;
	private String[] textTitle;
	private List<View> views;
	private List<Bitmap> bitmaps;
	private boolean setDefaultView = true;
	private static Thread flashThread=null;
	private static boolean paused = false;
	private int curPosition = 0;
	private View myview=null;
    private final String PROPERTY_TYPE = "ro.product.machinetype";
    private String MACHINE_TYPE = "changhe_2.1.1";
    private String SOUND_PATH = "/system/media/audio/ui/Effect_Tick.ogg";
	private Handler mHandler;
	private MainActivity main;
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

	public My_Fragment (){};
	public My_Fragment (Handler handler){
		this.mHandler = handler;
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.laucher_fragment_my, null);
		initView(myview);
		return myview;
	}

	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		paused = false;
		super.onResume();
		main = (MainActivity) getActivity();
		button_1.requestFocus();
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_YIDIAN_LIST);
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		paused = true;
		super.onPause();
	}
	@Override
	public void onStop() {
		super.onStop();
	//	mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_YIDIAN_LIST);
	}

	
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_1:
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_13)+"/"+getResources().getString(R.string.mine_1);
		setData(30,0);
			break;

		case R.id.button_2:
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_13)+"/"+getResources().getString(R.string.mine_2);
			setData(32,2);
			break;

		case R.id.button_3:

			replaceFragment(R.id.center_fragment, new CheckVersionFragment());
			break;
			
		case R.id.button_4:

			
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

		button_1 = (AnimationButton) view.findViewById(R.id.button_1);
		button_2 = (AnimationButton) view.findViewById(R.id.button_2);
		button_3 = (AnimationButton) view.findViewById(R.id.button_3);
		button_4 = (AnimationButton) view.findViewById(R.id.button_4);

		button_1.setImage(R.drawable.lcollection);
		button_2.setImage(R.drawable.lhistory);
        button_3.setImage(R.drawable.version_info);
        button_4.setImage(R.drawable.my_record);
		
		button_1.setText(getResources().getString(R.string.mine_1));
		button_2.setText(getResources().getString(R.string.mine_2));
		button_3.setText(getResources().getString(R.string.mine_3));
		button_4.setText(getResources().getString(R.string.mine_4));

		//button_1.setPadding(270,0,0,0);

		button_1.setOnClickListener(this);
		button_2.setOnClickListener(this);
		button_3.setOnClickListener(this);
		button_4.setOnClickListener(this);
		button_1.requestFocus();
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

	
	public void setData(int enter,int layer){
		main.SetSearchEnterAndLayerType(enter, layer);
		replaceFragment(R.id.center_fragment, new SongNameFragment(mHandler, enter, layer));
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
	

}
