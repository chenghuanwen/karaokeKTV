package com.jsl.ktv.fragment;

import java.util.ArrayList;
import java.util.List;
import com.jsl.ktv.util.SongJsonParseUtils;
import com.jsl.ktv.bean.SongSearchBean;
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
import com.jsl.ktv.view.PHAnimationButton;
import com.jsl.ktv.karaok.MainActivity;
import com.jsl.ktv.view.MyApplication;
import com.jsl.ktv.constant.FragmentMessageConstant;
public class PH_Fragment extends CommonFragment implements OnClickListener{

	private final int WHAT_UPDATE_LIST = 1;
	private final int DEFAULT_DRAWABLE = R.drawable.browser;

	private PHAnimationButton button_1;
	private PHAnimationButton button_2;
	private PHAnimationButton button_3;
	private PHAnimationButton button_4;
	private PHAnimationButton button_5;
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
	
	public PH_Fragment(){};
	public PH_Fragment(Handler handler){
		this.mHandler = handler;
		
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.laucher_fragment_ph, null);
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
		
	}

	
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_1:
		main.setFragmentPosition(1);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_5)+getResources().getString(R.string.ranking_1);
		   setData(3,0);
			break;

		case R.id.button_2:
		main.setFragmentPosition(2);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_5)+getResources().getString(R.string.ranking_2);
			setData(3,1);
			break;
        case R.id.button_3:
		main.setFragmentPosition(3);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_5)+getResources().getString(R.string.ranking_3);
			setData(3,2);
			break;
        case R.id.button_4:
		main.setFragmentPosition(4);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_5)+getResources().getString(R.string.ranking_4);
	       setData(3,3);
	        break;
        case R.id.button_5:
		main.setFragmentPosition(5);
		MyApplication.currentSinger = "/"+getResources().getString(R.string.home_5)+getResources().getString(R.string.ranking_5);
		    setData(3,4);
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

		button_1 = (PHAnimationButton) view.findViewById(R.id.button_1);
		button_2 = (PHAnimationButton) view.findViewById(R.id.button_2);
		button_3 = (PHAnimationButton) view.findViewById(R.id.button_3);
		button_4 = (PHAnimationButton) view.findViewById(R.id.button_4);
		button_5 = (PHAnimationButton) view.findViewById(R.id.button_5);

		button_1.setImage(R.drawable.lbendi);
		button_2.setImage(R.drawable.lnetpaihang);
        button_3.setImage(R.drawable.lyuefen1);
		button_4.setImage(R.drawable.lyuefen2);
		button_5.setImage(R.drawable.lyuefen3);
		
			ArrayList<SongSearchBean> songs1 = SongJsonParseUtils.getSongDatas2(0, "", 0, 3, 0, 0, 3, "");
		button_1.setText(getResources().getString(R.string.ranking_1));
		if(songs1==null || songs1.size()<3){
			button_1.Song1setText("1.喜欢你");
			button_1.Singer1setText("邓紫棋");
			button_1.Song2setText("2.夜空中最亮的星");
			button_1.Singer2setText("逃跑计划");
			button_1.Song3setText("3.十年");
			button_1.Singer3setText("陈奕迅");
		}else{
			button_1.Song1setText("1."+songs1.get(0).getSong());
			button_1.Singer1setText(songs1.get(0).getSinger());
			button_1.Song2setText("2."+songs1.get(1).getSong());
			button_1.Singer2setText(songs1.get(1).getSinger());
			button_1.Song3setText("3."+songs1.get(2).getSong());
			button_1.Singer3setText(songs1.get(2).getSinger());	
		}
		
		
		
		ArrayList<SongSearchBean> songs2 = SongJsonParseUtils.getSongDatas2(0, "", 0, 3, 1, 0, 3, "");
		button_2.setText(getResources().getString(R.string.ranking_2));
		if(songs2==null || songs2.size()<3){
			button_2.Song1setText("1.朋友");
			button_2.Singer1setText("周华健");
			button_2.Song2setText("2.朋友的酒");
			button_2.Singer2setText("李晓杰");
			button_2.Song3setText("3.小幸运");
			button_2.Singer3setText("田馥甄 ");
		}else{
			button_2.Song1setText("1."+songs2.get(0).getSong());
			button_2.Singer1setText(songs2.get(0).getSinger());
			button_2.Song2setText("2."+songs2.get(1).getSong());
			button_2.Singer2setText(songs2.get(1).getSinger());
			button_2.Song3setText("3."+songs2.get(2).getSong());
			button_2.Singer3setText(songs2.get(2).getSinger());
		}
		
		
		ArrayList<SongSearchBean> songs3 = SongJsonParseUtils.getSongDatas2(0, "", 0, 3, 2, 0, 3, "");
		button_3.setText(getResources().getString(R.string.ranking_3));
		if(songs3==null || songs3.size()<3){
			button_3.Song1setText("1.喜欢你");
			button_3.Singer1setText("邓紫棋");
			button_3.Song2setText("2.不良满罪名");
			button_3.Singer2setText("王杰");
			button_3.Song3setText("3.月半小夜曲");
			button_3.Singer3setText("李克勤");
		}else{
			button_3.Song1setText("1."+songs3.get(0).getSong());
			button_3.Singer1setText(songs3.get(0).getSinger());
			button_3.Song2setText("2."+songs3.get(1).getSong());
			button_3.Singer2setText(songs3.get(1).getSinger());
			button_3.Song3setText("3."+songs3.get(2).getSong());
			button_3.Singer3setText(songs3.get(2).getSinger());
		}
		
		
		ArrayList<SongSearchBean> songs4 = SongJsonParseUtils.getSongDatas2(0, "", 0, 3, 3, 0, 3, "");
		button_4.setText(getResources().getString(R.string.ranking_4));
		if(songs4==null || songs4.size()<3){
			button_4.Song1setText("1.车站");
			button_4.Singer1setText("李茂山");
			button_4.Song2setText("2.爱拼才会赢");
			button_4.Singer2setText("叶启田");
			button_4.Song3setText("3.半边月");
			button_4.Singer3setText("黄思婷");
		}else{
			button_4.Song1setText("1."+songs4.get(0).getSong());
			button_4.Singer1setText(songs4.get(0).getSinger());
			button_4.Song2setText("2."+songs4.get(1).getSong());
			button_4.Singer2setText(songs4.get(1).getSinger());
			button_4.Song3setText("3."+songs4.get(2).getSong());
			button_4.Singer3setText(songs4.get(2).getSinger());
			
		}
		
		ArrayList<SongSearchBean> songs5 = SongJsonParseUtils.getSongDatas2(0, "", 0, 3, 4, 0, 3, "");
		button_5.setText(getResources().getString(R.string.ranking_5));
		if(songs5==null || songs5.size()<3){
			button_5.Song1setText("1.Imagine");
			button_5.Singer1setText("杜丽莎");
			button_5.Song2setText("2.BAD");
			button_5.Singer2setText("Michael Jackson");
			button_5.Song3setText("3.yesterday once more");
			button_5.Singer3setText("Carpenters");
		}else{
			button_5.Song1setText("1."+songs5.get(0).getSong());
			button_5.Singer1setText(songs5.get(0).getSinger());
			button_5.Song2setText("2."+songs5.get(1).getSong());
			button_5.Singer2setText(songs5.get(1).getSinger());
			button_5.Song3setText("3."+songs5.get(2).getSong());
			button_5.Singer3setText(songs5.get(2).getSinger());
		}
		
		button_1.setTextSize(72);
		button_2.setTextSize(72);
		button_3.setTextSize(72);
		button_4.setTextSize(72);
		button_5.setTextSize(72);
		//button_1.setPadding(200,0,0,50);
		

		button_1.setOnClickListener(this);
		button_2.setOnClickListener(this);
		button_3.setOnClickListener(this);
		button_4.setOnClickListener(this);
		button_5.setOnClickListener(this);
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
		transaction.addToBackStack("ph_fragment");
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
		case 5:
		 button_5.requestFocus();
		break;
		
		default:
			break;
		}
		
	}
	

}
