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
public class Quz_Fragment extends CommonFragment implements OnClickListener{

	private final int WHAT_UPDATE_LIST = 1;
	private final int DEFAULT_DRAWABLE = R.drawable.browser;

	private AnimationButton button_1;
	private AnimationButton button_2;
	private AnimationButton button_3;
	private AnimationButton button_4;
	private AnimationButton button_5;
	private AnimationButton button_6;
	private AnimationButton button_7;
	private AnimationButton button_8;
	private AnimationButton button_9;
	private AnimationButton button_10;
	private AnimationButton button_11;
	private AnimationButton button_12;
	private AnimationButton button_13;
	private AnimationButton button_14;
	private AnimationButton button_15;
	private AnimationButton button_16;
	private AnimationButton button_17;
	private AnimationButton button_18;
	private AnimationButton button_19;
	private AnimationButton button_20;
	private AnimationButton button_21;
	private AnimationButton button_22;
	private AnimationButton button_23;
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

	public Quz_Fragment(){};
	public Quz_Fragment(Handler handler){
		this.mHandler = handler;
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.laucher_fragment_quzhong, null);
		initView(myview);
		return myview;
	}

	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		paused = false;
		super.onResume();
		 main = (MainActivity) getActivity();
		initFocus(main.getFragmentPositon());
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
		main.setFragmentPosition(0);
	}

	
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_1:
		main.setFragmentPosition(1);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_1);
			setData(8,1);
			break;

		case R.id.button_2:
		main.setFragmentPosition(2);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_2);
			setData(8,3);
			break;
        case R.id.button_3:
		main.setFragmentPosition(3);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_3);
			setData(8,4);
			break;
        case R.id.button_4:
		main.setFragmentPosition(4);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_4);
			setData(8,0);
			break;
        case R.id.button_5:
		main.setFragmentPosition(5);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_5);
	        setData(8,2);
	          break;
        case R.id.button_6:
		main.setFragmentPosition(6);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_6);
	        setData(8,7);
	           break;
        case R.id.button_7:
		main.setFragmentPosition(7);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_7);
	        setData(8,16);
	             break;
        case R.id.button_8:
		main.setFragmentPosition(8);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_8);
	        setData(8,6);
	        break;
        case R.id.button_9:
		main.setFragmentPosition(9);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_9);
			setData(8,5);
			break;
		case R.id.button_10:
		main.setFragmentPosition(10);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_10);
			setData(8,21);
			break;

		case R.id.button_11:
		main.setFragmentPosition(11);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_11);
			setData(8,8);
			break;
		case R.id.button_12:
		main.setFragmentPosition(12);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_12);
			setData(8,9);
			break;
        case R.id.button_13:
		main.setFragmentPosition(13);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_13);
			setData(8,10);
			break;
        case R.id.button_14:
		main.setFragmentPosition(14);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_14);
			setData(8,20);
			break;
        case R.id.button_15:
		main.setFragmentPosition(15);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_15);
	        setData(8,11);
	          break;
        case R.id.button_16:
		main.setFragmentPosition(16);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_16);
	        setData(8,12);
	           break;
        case R.id.button_17:
		main.setFragmentPosition(17);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_17);
	        setData(8,19);
	             break;
        case R.id.button_18:
		main.setFragmentPosition(18);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_18);
	        setData(8,14);
	        break;
			case R.id.button_19:
			main.setFragmentPosition(19);
			MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_19);
	        setData(8,13);
	        break;
			case R.id.button_20:
			main.setFragmentPosition(20);
			MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_20);
	        setData(8,15);
	        break;
			case R.id.button_21:
			main.setFragmentPosition(21);
			MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_21);
	        setData(8,18);
	        break;
			case R.id.button_22:
			main.setFragmentPosition(22);
			MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_22);
	        setData(8,17);
	        break;
			case R.id.button_23:
			main.setFragmentPosition(23);
			MyApplication.currentSinger = "/"+getResources().getString(R.string.class_3)+"/"+getResources().getString(R.string.theme_23);
	        setData(8,22);
	        break;

		

		default:
			break;
		}

	}
	
	
	public void setData(int enter,int layer){
		main.SetSearchEnterAndLayerType(enter, layer);
		replaceFragment(R.id.center_fragment, new SongNameFragment(mHandler, enter, layer));
	}
	
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (paused==false && keyCode == event.KEYCODE_BACK) {
            return true;
        }
        
		return false;
    }
	/*
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			switch (v.getId()) {
	
			case R.id.button_1:
			case R.id.button_2:
			case R.id.button_3:
			case R.id.button_4:
				return true;
			default:
				break;
			}
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			switch (v.getId()) {
			case R.id.button_4:
				button_5.requestFocus();
				return true;
			default:
				break;
			}
		}
		return false;
	}*/
	@SuppressLint("NewApi")
	private void initView(View view) {
		textTitle = getResources().getStringArray(R.array.home_fragment_title);

		button_1 = (AnimationButton) view.findViewById(R.id.button_1);
		button_2 = (AnimationButton) view.findViewById(R.id.button_2);
		button_3 = (AnimationButton) view.findViewById(R.id.button_3);
		button_4 = (AnimationButton) view.findViewById(R.id.button_4);
		button_5 = (AnimationButton) view.findViewById(R.id.button_5);
		button_6 = (AnimationButton) view.findViewById(R.id.button_6);
		button_7 = (AnimationButton) view.findViewById(R.id.button_7);
		button_8 = (AnimationButton) view.findViewById(R.id.button_8);
		button_9 = (AnimationButton) view.findViewById(R.id.button_9);
		button_10 = (AnimationButton) view.findViewById(R.id.button_10);
		button_11 = (AnimationButton) view.findViewById(R.id.button_11);
		button_12 = (AnimationButton) view.findViewById(R.id.button_12);
		button_13 = (AnimationButton) view.findViewById(R.id.button_13);
		button_14 = (AnimationButton) view.findViewById(R.id.button_14);
		button_15 = (AnimationButton) view.findViewById(R.id.button_15);
		button_16 = (AnimationButton) view.findViewById(R.id.button_16);
		button_17 = (AnimationButton) view.findViewById(R.id.button_17);
		button_18 = (AnimationButton) view.findViewById(R.id.button_18);
		button_19 = (AnimationButton) view.findViewById(R.id.button_19);
		button_20 = (AnimationButton) view.findViewById(R.id.button_20);
		button_21 = (AnimationButton) view.findViewById(R.id.button_21);
		button_22 = (AnimationButton) view.findViewById(R.id.button_22);
		button_23 = (AnimationButton) view.findViewById(R.id.button_23);
		
		button_1.setImage(R.drawable.lgeming);
		button_2.setImage(R.drawable.lhuajiu);
        button_3.setImage(R.drawable.lxiqing);
        button_4.setImage(R.drawable.lhechang);
        button_5.setImage(R.drawable.lerge);
        button_6.setImage(R.drawable.lshenri);
        button_7.setImage(R.drawable.lwangluoge);
        button_8.setImage(R.drawable.ljingju);
        button_9.setImage(R.drawable.leju);
        button_10.setImage(R.drawable.lchaoju);
        button_11.setImage(R.drawable.lyueju);
        button_12.setImage(R.drawable.lhuangmeixi);
        button_13.setImage(R.drawable.lqitaxq);
        button_14.setImage(R.drawable.lzongjiao);
        button_15.setImage(R.drawable.lpub);
        button_16.setImage(R.drawable.lxiangsheng);
        button_17.setImage(R.drawable.lyinshige);
        button_18.setImage(R.drawable.lmingzhu);
        button_19.setImage(R.drawable.lcaoyuan);
        button_20.setImage(R.drawable.lmingyao);
        button_21.setImage(R.drawable.lxiaoyuan);
        button_22.setImage(R.drawable.lyinyuexs);
        button_23.setImage(R.drawable.lqinyingyue);
		
		button_1.setText(getResources().getString(R.string.theme_1));
		button_2.setText(getResources().getString(R.string.theme_2));
		button_3.setText(getResources().getString(R.string.theme_3));
		button_4.setText(getResources().getString(R.string.theme_4));
		button_5.setText(getResources().getString(R.string.theme_5));
		button_6.setText(getResources().getString(R.string.theme_6));
		button_7.setText(getResources().getString(R.string.theme_7));
		button_8.setText(getResources().getString(R.string.theme_8));
		button_9.setText(getResources().getString(R.string.theme_9));
		button_10.setText(getResources().getString(R.string.theme_10));
		button_11.setText(getResources().getString(R.string.theme_11));
		button_12.setText(getResources().getString(R.string.theme_12));
		button_13.setText(getResources().getString(R.string.theme_13));
		button_14.setText(getResources().getString(R.string.theme_14));
		button_15.setText(getResources().getString(R.string.theme_15));
		button_16.setText(getResources().getString(R.string.theme_16));
		button_17.setText(getResources().getString(R.string.theme_17));
		button_18.setText(getResources().getString(R.string.theme_18));
		button_19.setText(getResources().getString(R.string.theme_19));
		button_20.setText(getResources().getString(R.string.theme_20));
		button_21.setText(getResources().getString(R.string.theme_21));
		button_22.setText(getResources().getString(R.string.theme_22));
		button_23.setText(getResources().getString(R.string.theme_23));
	
		
		
		
		//button_1.setPadding(200,0,0,50);
		

		button_1.setOnClickListener(this);
		button_2.setOnClickListener(this);
		button_3.setOnClickListener(this);
		button_4.setOnClickListener(this);
		button_5.setOnClickListener(this);
		button_6.setOnClickListener(this);
		button_7.setOnClickListener(this);
		button_8.setOnClickListener(this);
		button_9.setOnClickListener(this);
		button_10.setOnClickListener(this);
		button_11.setOnClickListener(this);
		button_12.setOnClickListener(this);
		button_13.setOnClickListener(this);
		button_14.setOnClickListener(this);
		button_15.setOnClickListener(this);
		button_16.setOnClickListener(this);
		button_17.setOnClickListener(this);
		button_18.setOnClickListener(this);
		button_19.setOnClickListener(this);
		button_20.setOnClickListener(this);
		button_21.setOnClickListener(this);
		button_22.setOnClickListener(this);
		button_23.setOnClickListener(this);

		button_2.requestFocus();
	}

	@SuppressLint("NewApi")
	private void replaceFragment(int id, Fragment fragment) {
		/*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                //fragmentTransaction.replace(R.id.fragment_layout,secondFragment);
                //此处是隐藏了SecondFragment，所以SecondFragment的UI视图不会销毁，只是不可见了，回退时不会重走onCreateView方法
                fragmentTransaction.hide(ClassFragment.this);

                //
                fragmentTransaction.add(id,fragment,"three");

                //将SecondFragment放入回退栈
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
		
		
		
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out);
		transaction.replace(id, fragment);
		transaction.addToBackStack("Quz_Fragment");
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
	
	
	/**
	 * 通过包名打开app
	 * 
	 */
	@SuppressLint("NewApi")
	private void openAPPByPackage(String pg) {
		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等  
		
		Log.i("TAG", "==包名=="+pg);
		PackageInfo packageInfo = null;
		try {
			packageInfo = getActivity().getPackageManager().getPackageInfo(pg, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(packageInfo == null){return;}
		
		Intent intent = new Intent(Intent.ACTION_MAIN,null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(pg);
		List<ResolveInfo> infoList = getActivity().getPackageManager().queryIntentActivities(intent, 0);
		ResolveInfo resolveInfo = infoList.iterator().next();
		
		if(resolveInfo != null){
			
		String packageName = resolveInfo.activityInfo.packageName;
		String activityName = resolveInfo.activityInfo.name;
		Log.i("TAG", "==类名=="+activityName);
		Intent appIntent = new Intent(Intent.ACTION_MAIN,null);
		appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cp = new ComponentName(packageName,activityName);
		appIntent.setComponent(cp);
		startActivity(appIntent);
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
         case 6:
        	 button_6.requestFocus();
 			break;
         case 7:
        	 button_7.requestFocus();
 			break;
         case 8:
        	 button_8.requestFocus();
 			break;
         case 9:
        	 button_9.requestFocus();
 			break;
         case 10:
        	 button_10.requestFocus();
 			break;
         case 11:
        	 button_11.requestFocus();
 			break;
		case 12:
			button_12.requestFocus();
			break;
         case 13:
        	 button_13.requestFocus();
			break;
         case 14:
        	 button_14.requestFocus();
 			break;			
         case 15:
        	 button_15.requestFocus();
 			break;
		case 16:
			button_16.requestFocus();
			break;
         case 17:
        	 button_17.requestFocus();
			break;
         case 18:
        	 button_18.requestFocus();
 			break;			
		 case 19:
        	 button_19.requestFocus();
			break;
         case 20:
        	 button_20.requestFocus();
 			break;			
         case 21:
        	 button_21.requestFocus();
 			break;
		case 22:
			button_22.requestFocus();
			break;
         case 23:
        	 button_23.requestFocus();
			break;	
		default:
			break;
		}
		
	}
}
