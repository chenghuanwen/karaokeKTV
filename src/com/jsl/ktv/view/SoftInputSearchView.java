package com.jsl.ktv.view;

import java.util.ArrayList;
import android.content.Intent;

import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.R;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
public class SoftInputSearchView extends LinearLayout implements OnItemClickListener,TextWatcher{
	private GridView gvkey;
	private TextView etInput;
	private Context mContext;
	private ArrayList<Integer> resIds;
	private StringBuffer inputResust;
	private Handler mHandler;
	private KeyAdapter mAdapter;
    private int enterType,layerType;
	private Intent mIntent;
	private int currentKey;
	public SoftInputSearchView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
	public SoftInputSearchView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("NewApi")
	public SoftInputSearchView(Context context, AttributeSet attrs,int style) {
		super(context, attrs,style);
		// TODO Auto-generated constructor stub
		mIntent = new Intent("justlink.action.intent.songlist_focus");
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.soft_input_search_view, this);
		
		gvkey = (GridView)findViewById(R.id.gv_input);
		etInput = (TextView)findViewById(R.id.et_input);
		
		inputResust = new StringBuffer();
		
		resIds = new ArrayList<Integer>();
		resIds.add(R.drawable.a);
		resIds.add(R.drawable.b2);
		resIds.add(R.drawable.c);
		resIds.add(R.drawable.d);
		resIds.add(R.drawable.e);
		resIds.add(R.drawable.f);
		resIds.add(R.drawable.g);
		resIds.add(R.drawable.h);
		resIds.add(R.drawable.i);
		resIds.add(R.drawable.j);
		resIds.add(R.drawable.k);
		resIds.add(R.drawable.l);
		resIds.add(R.drawable.m);
		resIds.add(R.drawable.n);
		resIds.add(R.drawable.o);
		resIds.add(R.drawable.p);
		resIds.add(R.drawable.q);
		resIds.add(R.drawable.r);
		resIds.add(R.drawable.s);
		resIds.add(R.drawable.t);
		resIds.add(R.drawable.u);
		resIds.add(R.drawable.v);
		resIds.add(R.drawable.w);
		resIds.add(R.drawable.x);
		resIds.add(R.drawable.y);
		resIds.add(R.drawable.z);
		resIds.add(R.drawable.input_back);
		resIds.add(R.drawable.input_delete);
		
		mAdapter = new KeyAdapter(resIds);
		gvkey.setAdapter(mAdapter);
		gvkey.setOnItemClickListener(this);
		etInput.addTextChangedListener(this);
		gvkey.requestFocusFromTouch();  
        gvkey.setSelection(10); 
        
        gvkey.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.i("song","=====searchview select======");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.i("song","=====searchview nothing select======");
				//TODO 解决外部跳转时搜索键盘焦点消失，模拟遥控ok键输入，重新获取焦点
				new Thread() {
					public void run() {
						SystemClock.sleep(1000);
						try {
							Instrumentation inst = new Instrumentation();
							inst.sendKeyDownUpSync(23);
							Log.i("song","send keycode finish=======");
						} catch (Exception e) {
							e.printStackTrace();
							Log.i("song","send keycode error======="+e.toString());
						}
					
					}
				}.start();
			}
		});
	
	}

		

	
	public void setHandler(Handler handler){
		this.mHandler = handler;
	}
	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		
		
	}
	
	
	
	
	private class KeyAdapter extends BaseAdapter{
		private ArrayList<Integer> mResIds;
		
		public KeyAdapter(ArrayList<Integer> resIds){
			this.mResIds = resIds;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mResIds==null?0:mResIds.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(contentView == null){
				holder = new ViewHolder();
				contentView = LayoutInflater.from(mContext).inflate(R.layout.soft_input_view_key_item, null);
				holder.iv = (ImageView)contentView.findViewById(R.id.iv_key);
				contentView.setTag(holder);
			}else{
				holder = (ViewHolder) contentView.getTag();
			}
			holder.iv.setImageResource(mResIds.get(position));
			//Log.i("searchview","position==="+mResIds.size());
			return contentView;
		}
		
		
		class ViewHolder {
			ImageView iv;
		}
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			 input("A");
			break;
         case 1:
        	  input("B");
			break;
			
         case 2:
        	 input("C");
 			break;
         case 3:
        	  input("D");
 			break;
         case 4:
        	  input("E");
 			break;
         case 5:
        	  input("F");
 			break;
         case 6:
        	  input("G");
 			break;
         case 7:
        	  input("H");
 			break;
         case 8:
        	  input("I");
 			break;
         case 9:
        	  input("J");
 			break;
         case 10:
        	 input("K");
 			break;
         case 11:
        	  input("L");
 			break;
         case 12:
        	  input("M");
 			break;
         case 13:
        	  input("N");
 			break;
         case 14:
        	 input("O");
 			break;
         case 15:
        	  input("P");
 			break;
         case 16:
        	  input("Q");
 			break;
         case 17:
        	  input("R");
 			break;
         case 18:
        	 input("S");
 			break;
         case 19:
        	 input("T");
 			break;
         case 20:
        	 input("U");
 			break;
         case 21:
        	 input("V");
 			break;
         case 22:
        	  input("W");
 			break;
         case 23:
        	 input("X");
 			break;
         case 24:
        	  input("Y");
 			break;
         case 25:
        	 input("Z");
 			break;
         case 26://回�?
 			String result = inputResust.toString();
 		inputResust.setLength(0);
		if(result.length() > 0)
 		inputResust.append(result.substring(0, result.length()-1));
		MyApplication.isDeleteSearchKey = true;
		
		forceRequestFocus(26);
		
 			break;
         case 27://清空
 			inputResust.setLength(0);
			MyApplication.isDeleteSearchKey = true;
			forceRequestFocus(10);
 			break;
        
		default:
		
			break;
		}
		
		etInput.setText(inputResust.toString());
		
	} 
	
	
	public void input(String msg){
		inputResust.append(msg);
		MyApplication.isBackHome = false;
		MyApplication.isDeleteSearchKey = false;
	}
	
	public void clear(boolean isback){
		if(!isback)
		MyApplication.isBackHome = true;
		etInput.setText("");
		inputResust.setLength(0);
		if(MyApplication.isSearchViewShow && !MyApplication.isBackHome)
		forceRequestFocus(10);
		//Log.i("song","searchview clear=====");
	}
	
	public void back(){
		String result = inputResust.toString();
 		inputResust.setLength(0);
		if(result.length() > 0)
 		inputResust.append(result.substring(0, result.length()-1));
		etInput.setText(inputResust.toString());
		MyApplication.isDeleteSearchKey = true;
		forceRequestFocus(26);
	//Log.i("song","searchKey back====");
	}

	public void refresh(boolean flag){	
		Log.i("song","searchview refresh=");
				setGridviewSelection(10);	
						}
	
	public void setGridviewSelection(int pos){
			gvkey.requestFocus();
			gvkey.requestFocusFromTouch();  
            gvkey.setSelection(pos); 
			//forceRequestFocus(pos);
	}
	
	
	public void forceRequestFocus(final int pos){
		mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
		        setGridviewSelection(pos);			
				}
			}, 30);
	}
	
	
	public void SetEnterAndLayerType(int enterType,int layerType){
		
		Log.i("song","SetEnterAndLayerType enterType==="+enterType+"=="+layerType);
		this.enterType = enterType;
		this.layerType = layerType;
	}		
	
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		Log.i("song","afterTextChanged enterType==="+enterType);
		MyApplication.searchKey = arg0.toString();
		if(MyApplication.isBackHome || enterType==31)
			return;
		Message msg = Message.obtain();
		if(enterType!=2){//非歌星查�?
			msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_INPUT_FINISH;
			msg.arg1 = enterType;
			msg.arg2 = layerType;
			msg.obj = arg0.toString();
			mHandler.sendMessage(msg);
			
			if(TextUtils.isEmpty(arg0.toString())){
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SONG_CLASS);
			}else{
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SONG_CLASS);
			}
			
		}else{
			msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER;
			msg.arg1 = enterType;
			msg.arg2 = layerType;
			msg.obj = arg0.toString();
			mHandler.sendMessage(msg);
			
			if(TextUtils.isEmpty(arg0.toString())){
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SINGER_CLASS);
			}else{
				mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SINGER_CLASS);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
