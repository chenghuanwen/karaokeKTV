package com.jsl.ktv.fragment;


import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
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
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.karaok.MainActivity;
public class ClassFragment extends CommonFragment implements OnClickListener{

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
	private Handler mhHandler;
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
	public ClassFragment(){};
	public ClassFragment(Handler handler){
		this.mhHandler = handler;
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.laucher_fragment_class, null);
		initView(myview);
		return myview;
	}
	
	
	
	

	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		paused = false;
		maActivity = (MainActivity) getActivity();
		initFocus(maActivity.getFragmentPositon());
		mhHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
		super.onResume();
		
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
		
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_1:
		 maActivity.setFragmentPosition(1);
			replaceFragment(R.id.center_fragment, new Language_Fragment(mhHandler));
		

			break;

		case R.id.button_2:
		 maActivity.setFragmentPosition(2);
			replaceFragment(R.id.center_fragment, new Wuqu_Fragment(mhHandler));
			
			break;

		case R.id.button_3:
		 maActivity.setFragmentPosition(3);
			replaceFragment(R.id.center_fragment, new Quz_Fragment(mhHandler));
			
			break;

		case R.id.button_4:
		/* maActivity.setFragmentPosition(4);
			replaceFragment(R.id.center_fragment, new Hot_Fragment(mhHandler));*/
			
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

		button_1.setImage(R.drawable.fyubie);
		button_2.setImage(R.drawable.fwuqu);
        button_3.setImage(R.drawable.fquzhong);
		button_4.setImage(R.drawable.fremeng);
		
		button_1.setText(getResources().getString(R.string.class_1));
		button_2.setText(getResources().getString(R.string.class_2));
		button_3.setText(getResources().getString(R.string.class_3));
		button_4.setText(getResources().getString(R.string.class_4));
		
		
		button_1.setTextSize(36);
		button_2.setTextSize(36);
		button_3.setTextSize(36);
		button_4.setTextSize(36);
		
		button_1.setPadding(200,0,0,50);
		button_2.setPadding(200,0,0,50);
		button_3.setPadding(200,0,0,50);
		button_4.setPadding(200,0,0,50);

		button_1.setOnClickListener(this);
		button_2.setOnClickListener(this);
		button_3.setOnClickListener(this);
		button_4.setOnClickListener(this);
	

		button_1.requestFocus();
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
		transaction.addToBackStack("class_fragment");
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
        
		default:
			break;
		}
		
	}

}
