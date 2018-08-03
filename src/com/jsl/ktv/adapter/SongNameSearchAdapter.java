package com.jsl.ktv.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.RelativeLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.R;
import com.jsl.ktv.bean.SongSearchBean;
import android.view.View.OnFocusChangeListener;
import android.util.Log;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jsl.ktv.util.SongJsonParseUtils;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.view.MyApplication;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
public class SongNameSearchAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<SongSearchBean> mDatas;
	private OnMoreClickListener mClickListener;
	private RecordShareClickListener mShareClickListener;
	private int currentPosition;
	private Handler mHandler;
	private int itemType;
	private int downloadProgress,downloadState;
	private View firstRefreshView;
	public DownloadThread downloadThread;
	private Handler uiHandler;
	private static final int ENTER_TYPE_SONG_NAME = 1;//点歌
	private static final int ENTER_TYPE_GONGBO = 2;//公播
	private static final int ENTER_TYPE_RECORD = 3;//录音
	private static final int ENTER_TYPE_CLOUD = 4;//云端
	private static final int ENTER_TYPE_COLLECTION = 5;//收藏
	private static final int ENTER_TYPE_HAS_SELECTED = 6;//已点
	
	private Uri uri;
	
	
	public SongNameSearchAdapter(Context mContext,ArrayList<SongSearchBean> mDatas,Handler handler) {
		super();
		this.mContext = mContext;
		this.mDatas = mDatas;
		this.mHandler = handler;
		downloadThread = new DownloadThread();
		//downloadThread.start();
		uiHandler = new Handler(Looper.getMainLooper());
		 initOptions();
	}
	
	
	
	  DisplayImageOptions options;
	    
	    private void initOptions() {
		
			options = new DisplayImageOptions.Builder()  
	        .showImageOnLoading(R.drawable.song_default) // resource or drawable  
	        .showImageForEmptyUri(R.drawable.song_default) // resource or drawable  
	        .showImageOnFail(R.drawable.song_default) // resource or drawable  
	        .resetViewBeforeLoading(false)  // default  
	        .delayBeforeLoading(1000)  
	        .cacheInMemory(true)   
	        .cacheOnDisk(true)  
	        .considerExifParams(false) // default  
	        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default  
	        .bitmapConfig(Bitmap.Config.ARGB_4444) // default  
	        .displayer(new SimpleBitmapDisplayer()) // default  
	        .handler(new Handler()) // default  
	        .build();  
	    }

	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas==null?0:mDatas.size();
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		switch (mDatas.get(position).getEnterType()) {
		case 30:
			return ENTER_TYPE_COLLECTION;
		
		case 21:
			return ENTER_TYPE_CLOUD;
		
		case 40:
			return ENTER_TYPE_RECORD;
		
		case 43:
			return ENTER_TYPE_GONGBO;
		case 31:
			return ENTER_TYPE_HAS_SELECTED;
		default:
			return ENTER_TYPE_SONG_NAME;
		
		}
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//Log.i("song","song adapter getview======");
		switch (getItemViewType(position)) {
		
		 case ENTER_TYPE_GONGBO:
		     itemType = 1;
			 Viewholder_Gongbo holder1;
			 if(contentView == null){
					holder1 = new Viewholder_Gongbo();
					contentView = LayoutInflater.from(mContext).inflate(R.layout.gongbo_select_item, null);
					holder1.ivSinger = (ImageView)contentView.findViewById(R.id.iv_singer);
					holder1.ivDelete = (ImageView)contentView.findViewById(R.id.iv_delete);
					holder1.ivScore = (ImageView)contentView.findViewById(R.id.iv_score);
					holder1.tvSinger = (TextView)contentView.findViewById(R.id.tv_singer);
					holder1.tvSong = (TextView)contentView.findViewById(R.id.tv_song);
					holder1.tvLanguage = (TextView)contentView.findViewById(R.id.tv_language);
					holder1.bg = (RelativeLayout)contentView.findViewById(R.id.rl_bg);
					holder1.ivDelete.setOnFocusChangeListener(new onButtonFocusChangeListener(holder1.bg,position));
					contentView.setTag(holder1);
				}else{
					holder1 = (Viewholder_Gongbo) contentView.getTag();
				}
				
				final SongSearchBean searchBean1 = mDatas.get(position);
				
				String path1 = "/mnt/link2src/mv/"+searchBean1.getSongNumber().substring(0, 3)+"/"+searchBean1.getSongNumber()+".jpg";
				//Log.i("song","path====="+path1);
				if(isFileExist(path1) && MyApplication.isNeedRefresh){
					ImageLoader.getInstance().displayImage("file:/" + path1, holder1.ivSinger, options);	
					//holder1.ivSinger.setImageBitmap(getLockBitmapFromFile(path1));
					//Log.i("song","path====="+path1);
				}else if(MyApplication.isNeedRefresh && !isFileExist(path1)){
				holder1.ivSinger.setImageResource(R.drawable.song_default);
			   }
				
				
				holder1.tvSong.setText(searchBean1.getSong());
				holder1.tvSinger.setText(searchBean1.getSinger());
				holder1.tvLanguage.setText(searchBean1.getLanguage());
				holder1.ivDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int status1 = SongJsonParseUtils.selectSong("public_order", searchBean1.getSongNumber(), -1, "");
						if(status1==0){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
						}else if(status1==1){
						//	Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
							refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 43, 0, 0, 8, ""));
						}
						//TODO 刷新列表
					}
				});
				
				
				
		        break;
	     case ENTER_TYPE_RECORD:
		     itemType = 3;
	    	 Viewholder_Record holder2;
				if(contentView == null){
					holder2 = new Viewholder_Record();
					contentView = LayoutInflater.from(mContext).inflate(R.layout.record_select_item, null);
					holder2.ivSinger = (ImageView)contentView.findViewById(R.id.iv_singer);
					holder2.ivPlay = (ImageView)contentView.findViewById(R.id.iv_play);
					holder2.ivDelete = (ImageView)contentView.findViewById(R.id.iv_delete);
					holder2.ivShare = (ImageView)contentView.findViewById(R.id.iv_share);
					holder2.tvSinger = (TextView)contentView.findViewById(R.id.tv_singer);
					holder2.tvSong = (TextView)contentView.findViewById(R.id.tv_song);
					holder2.tvLanguage = (TextView)contentView.findViewById(R.id.tv_language);
					holder2.bg = (RelativeLayout)contentView.findViewById(R.id.rl_bg);
				
					holder2.ivPlay.setOnFocusChangeListener(new onButtonFocusChangeListener(holder2.bg,position));
					holder2.ivDelete.setOnFocusChangeListener(new onButtonFocusChangeListener(holder2.bg,position));
					holder2.ivShare.setOnFocusChangeListener(new onButtonFocusChangeListener(holder2.bg,position));
					contentView.setTag(holder2);
				}else{
					holder2 = (Viewholder_Record) contentView.getTag();
				}
				
				final SongSearchBean searchBean2 = mDatas.get(position);
				
			String path2 = "/mnt/link2src/mv/"+searchBean2.getSongNumber().substring(0, 3)+"/"+searchBean2.getSongNumber()+".jpg";
				//Log.i("song","path====="+path2);
				if(isFileExist(path2) && MyApplication.isNeedRefresh){
					ImageLoader.getInstance().displayImage("file:/" + path2, holder2.ivSinger, options);	
					//holder2.ivSinger.setImageBitmap(getLockBitmapFromFile(path2));
					//Log.i("song","path====="+path2);
				}else if(MyApplication.isNeedRefresh && !isFileExist(path2)){
				holder2.ivSinger.setImageResource(R.drawable.song_default);
			   }
			 
				holder2.tvSong.setText(searchBean2.getSong());
				holder2.tvSinger.setText(searchBean2.getSinger());
				holder2.tvLanguage.setText(searchBean2.getLanguage());
				
				holder2.ivPlay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int status1 = SongJsonParseUtils.selectSong("rec_order", searchBean2.getSongNumber(), 0,searchBean2.getOrderId());
						if(status1==0){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
						}else if(status1==1){
							//Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				
				holder2.ivPlay.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if(arg2.getAction()==KeyEvent.ACTION_DOWN){
						if(arg2.getKeyCode()==82){
							mClickListener.onRecordOption(searchBean2);
							return true;
						}
					}
					return false;
				}
			});
				
			/*	holder2.ivDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int status1 = SongJsonParseUtils.selectSong("rec_order", searchBean2.getSongNumber(), 2, searchBean2.getOrderId());
						if(status1==0){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
						}else if(status1==1){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
							refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 40, 0, 0, 8, ""));
						}
						//TODO 刷新列表
					}
				});
				
				holder2.ivShare.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mShareClickListener.onShareClickListener(searchBean2);
					}
				});*/
				
				
		        break;
         // case ENTER_TYPE_COLLECTION:
         case ENTER_TYPE_HAS_SELECTED:
				itemType = 2;
        	  Viewholder holder3;
  			if(contentView == null){
  				holder3 = new Viewholder();
  				contentView = LayoutInflater.from(mContext).inflate(R.layout.song_yidian_item, null);
  				holder3.ivSinger = (ImageView)contentView.findViewById(R.id.iv_singer);
  				holder3.ivadd = (ImageView)contentView.findViewById(R.id.iv_youxian);
  				holder3.ivmore = (ImageView)contentView.findViewById(R.id.iv_delete);
  				holder3.ivScore = (ImageView)contentView.findViewById(R.id.iv_score);
  				holder3.tvSinger = (TextView)contentView.findViewById(R.id.tv_singer);
  				holder3.tvSong = (TextView)contentView.findViewById(R.id.tv_song);
  				holder3.tvLanguage = (TextView)contentView.findViewById(R.id.tv_language);
				holder3.bg = (RelativeLayout)contentView.findViewById(R.id.rl_bg);
				holder3.tvProgress = (TextView) contentView.findViewById(R.id.tv_progress);
				holder3.ivadd.setOnFocusChangeListener(new onButtonFocusChangeListener(holder3.bg,position));
				holder3.ivmore.setOnFocusChangeListener(new onButtonFocusChangeListener(holder3.bg,position));
  				contentView.setTag(holder3);
  			}else{
  				holder3 = (Viewholder) contentView.getTag();
  			}
  			
  			final SongSearchBean searchBean3 = mDatas.get(position);
  			
  			String path3 = "/mnt/link2src/mv/"+searchBean3.getSongNumber().substring(0, 3)+"/"+searchBean3.getSongNumber()+".jpg";
  			//Log.i("song","path====="+path3);
  			if(isFileExist(path3) && MyApplication.isNeedRefresh){
  				
  				ImageLoader.getInstance().displayImage("file:/" + path3, holder3.ivSinger, options);	
  				//holder.ivSinger.setImageBitmap(getLockBitmapFromFile(path));
  				//Log.i("song","path====="+path3);
  			}else if(MyApplication.isNeedRefresh && !isFileExist(path3)){
				holder3.ivSinger.setImageResource(R.drawable.song_default);
			}
  			
  			
			if(searchBean3.isCloud){
				holder3.tvProgress.setVisibility(View.VISIBLE);
			}else{
				holder3.tvProgress.setVisibility(View.GONE);
			}
  			
  			holder3.tvSong.setText(searchBean3.getSong());
  			holder3.tvSinger.setText(searchBean3.getSinger());
  			holder3.tvLanguage.setText(searchBean3.getLanguage());
  			
  			holder3.ivadd.setImageResource(R.drawable.new_youxian_button);
  			
  			
  			holder3.ivadd.setOnClickListener(new OnClickListener() {//优先
  				
  				@Override
  				public void onClick(View arg0) {
  					// TODO Auto-generated method stub
  					int status = SongJsonParseUtils.selectSong("song_order", searchBean3.getSongNumber(), 1,searchBean3.getOrderId());
  					
  					if(status==0){
  						Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
  					}else if(status ==1){
  						//Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
						refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 31, 0, 0, 8, ""));
  					}
  					
  					//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
  				}
  			});
  			
			holder3.ivadd.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if(arg2.getAction()==KeyEvent.ACTION_DOWN){
						if(arg2.getKeyCode()==82){
							mClickListener.onMoreClickListener(searchBean3.getSongNumber(),searchBean3.getOrderId(),0);
							return true;
						}
					}
					return false;
				}
			});
			
  		/*	holder3.ivmore.setOnClickListener(new OnClickListener() {//删除
  				
  				@Override
  				public void onClick(View arg0) {
  					// TODO Auto-generated method stub
                  int status = SongJsonParseUtils.selectSong("song_order", searchBean3.getSongNumber(), 0,searchBean3.getOrderId());
  					
  					if(status==0){
  						Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
  					}else if(status ==1){
  						Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
						refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 31, 0, 0, 8, ""));
  					}
  					
  				}
  			});*/
        	   break;
       
         case ENTER_TYPE_CLOUD:
		    itemType = 3; 
			Viewholder_Record holder4;
				if(contentView == null){
					holder4 = new Viewholder_Record();
					contentView = LayoutInflater.from(mContext).inflate(R.layout.cloud_select_item, null);
					holder4.ivSinger = (ImageView)contentView.findViewById(R.id.iv_singer);
					holder4.ivPlay = (ImageView)contentView.findViewById(R.id.iv_youxian);
					holder4.ivDelete = (ImageView)contentView.findViewById(R.id.iv_delete);
					holder4.ivShare = (ImageView)contentView.findViewById(R.id.iv_pause);
					holder4.tvSinger = (TextView)contentView.findViewById(R.id.tv_singer);
					holder4.tvSong = (TextView)contentView.findViewById(R.id.tv_song);
					holder4.tvLanguage = (TextView)contentView.findViewById(R.id.tv_language);
					holder4.tvProgress = (TextView)contentView.findViewById(R.id.tv_progress);
					holder4.bg = (RelativeLayout)contentView.findViewById(R.id.rl_bg);
					holder4.ivPlay.setOnFocusChangeListener(new onButtonFocusChangeListener(holder4.bg,position));
					holder4.ivDelete.setOnFocusChangeListener(new onButtonFocusChangeListener(holder4.bg,position));
					holder4.ivShare.setOnFocusChangeListener(new onButtonFocusChangeListener(holder4.bg,position));
					contentView.setTag(holder4);
				}else{
					holder4 = (Viewholder_Record) contentView.getTag();
				}
				
				final SongSearchBean searchBean4 = mDatas.get(position);
				
				String path4 = "/mnt/link2src/mv/"+searchBean4.getSongNumber().substring(0, 3)+"/"+searchBean4.getSongNumber()+".jpg";
			//	Log.i("song","path====="+path4);
				if(isFileExist(path4) && MyApplication.isNeedRefresh){
					ImageLoader.getInstance().displayImage("file:/" + path4, holder4.ivSinger, options);	
					//holder2.ivSinger.setImageBitmap(getLockBitmapFromFile(path2));
				//	Log.i("song","path====="+path4);
				}else if(MyApplication.isNeedRefresh && !isFileExist(path4)){
				   holder4.ivSinger.setImageResource(R.drawable.song_default);	
				}
			
				holder4.tvSong.setText(searchBean4.getSong());
				holder4.tvSinger.setText(searchBean4.getSinger());
				holder4.tvLanguage.setText(searchBean4.getLanguage());
				
				holder4.ivPlay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int status4 = SongJsonParseUtils.selectSong("cloud_order", searchBean4.getSongNumber(), 1,"");
						if(status4==0){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
						}else if(status4==1){
							//Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
							refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 21, 0, 0, 8, ""));
						}
						
					}
				});
				
				holder4.ivPlay.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if(arg2.getAction()==KeyEvent.ACTION_DOWN){
						if(arg2.getKeyCode()==82){
							mClickListener.onMoreClickListener(searchBean4.getSongNumber(),searchBean4.getOrderId(),searchBean4.getDownSta());
							return true;
						}
					}
					return false;
				}
			});
				
				
			/*	holder4.ivDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int status5 = SongJsonParseUtils.selectSong("cloud_order", searchBean4.getSongNumber(), 0, "");
						if(status5==0){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
						}else if(status5==1){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
							refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 21, 0, 0, 8, ""));
						}
						//TODO 刷新列表
					}
				});
				
				holder4.ivShare.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int status5 = SongJsonParseUtils.selectSong("cloud_order", searchBean4.getSongNumber(), 2, "");
						if(status5==0){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
						}else if(status5==1){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_success), Toast.LENGTH_SHORT).show();
							refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 21, 0, 0, 8, ""));
						}
					}
				});*/
				
	        break;

		 default:
		  itemType = 2;
		 final Viewholder holder;
			if(contentView == null){
				holder = new Viewholder();
				contentView = LayoutInflater.from(mContext).inflate(R.layout.song_select_item, null);
				holder.ivSinger = (ImageView)contentView.findViewById(R.id.iv_singer);
				holder.ivadd = (ImageView)contentView.findViewById(R.id.iv_add);
				holder.ivmore = (ImageView)contentView.findViewById(R.id.iv_more);
				holder.ivScore = (ImageView)contentView.findViewById(R.id.iv_score);
				holder.ivCloud = (ImageView)contentView.findViewById(R.id.iv_cloud);
				holder.tvSinger = (TextView)contentView.findViewById(R.id.tv_singer);
				holder.tvSong = (TextView)contentView.findViewById(R.id.tv_song);
				holder.tvLanguage = (TextView)contentView.findViewById(R.id.tv_language);
				holder.bg = (RelativeLayout)contentView.findViewById(R.id.rl_bg);
				holder.ivadd.setOnFocusChangeListener(new onButtonFocusChangeListener(holder.bg,position));
				holder.ivmore.setOnFocusChangeListener(new onButtonFocusChangeListener(holder.bg,position));
				contentView.setTag(holder);
			}else{
				holder = (Viewholder) contentView.getTag();
			}
			
			final SongSearchBean searchBean = mDatas.get(position);
			
			String path = "/mnt/link2src/mv/"+searchBean.getSongNumber().substring(0, 3)+"/"+searchBean.getSongNumber()+".jpg";
			//Log.i("song","path====="+path+"===isFileExist=="+isFileExist(path));
			if(isFileExist(path) && MyApplication.isNeedRefresh){
				
				ImageLoader.getInstance().displayImage("file:/" + path, holder.ivSinger, options);	
				//holder.ivSinger.setImageBitmap(getLockBitmapFromFile(path));
				//Log.i("song","path====="+path);
			}else if(MyApplication.isNeedRefresh && !isFileExist(path)){
				   holder.ivSinger.setImageResource(R.drawable.song_default);	
				}
			holder.tvSong.setText(searchBean.getSong());
			holder.tvSinger.setText(searchBean.getSinger());
			holder.tvLanguage.setText(searchBean.getLanguage());
			
			
			if(searchBean.isHasAdd()){
				holder.ivadd.setImageResource(R.drawable.new_has_add_selector);
			}else{
				holder.ivadd.setImageResource(R.drawable.new_addsong_button);
				
			}
			
			if(searchBean.isScore()){
				holder.ivScore.setImageResource(R.drawable.socre_tag);
			}else{
				holder.ivScore.setImageResource(R.drawable.touming);
			}
			
			if(searchBean.isCloud()){
				holder.ivCloud.setImageResource(R.drawable.cloud);
			}else{
				holder.ivCloud.setImageResource(R.drawable.touming);
			}
			
			holder.ivadd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int status = SongJsonParseUtils.selectSong("song_select", searchBean.getSongNumber(), 0, "");
					
					if(status==0){
						Toast.makeText(mContext, mContext.getResources().getString(R.string.opration_fail), Toast.LENGTH_SHORT).show();	
					}else if(status ==1){
						if(searchBean.isCloud()){
							Toast.makeText(mContext,"add to download list！", Toast.LENGTH_SHORT).show();
							mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY);
							mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
						}else{
						//Toast.makeText(mContext, mContext.getResources().getString(R.string.select_success), Toast.LENGTH_SHORT).show();
						if(searchBean.getEnterType()!=32)//历史
						MyApplication.isOnSelectSong = true;
						//mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW);
						mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY);
						mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
					//	Log.i("song","refresh =====enter=="+MyApplication.currentEnterType+"====layer==="+MyApplication.currentLayer);
						if("".equals(MyApplication.currentSingerNum))
						refresh(SongJsonParseUtils.getSongDatas2(0, MyApplication.searchKey, 0, MyApplication.currentEnterType, MyApplication.currentLayer, MyApplication.searchStartCount, 8, ""));
						else
						refresh(SongJsonParseUtils.getSongDatas2(0, MyApplication.searchKey, 0, 11, 0, MyApplication.searchStartCount, 8,MyApplication.currentSingerNum));	
						}
					}										
				}
			});
			
			holder.ivadd.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if(arg2.getAction()==KeyEvent.ACTION_DOWN){
						if(arg2.getKeyCode()==82){					
							mClickListener.onMoreClickListener(searchBean.getSongNumber(),searchBean.getOrderId(),0);
							return true;
						}
					}
					return false;
				}
			});
			
			break;
		}		
			
		return contentView;
	}
	
	
	
    class Viewholder{
    	ImageView ivSinger,ivadd,ivmore,ivScore,ivCloud;
    	TextView tvSong,tvSinger,tvLanguage,tvProgress;
		RelativeLayout bg;
    }
    
    class Viewholder_Record{
    	ImageView ivSinger,ivPlay,ivDelete,ivShare,ivScore;
    	TextView tvSong,tvSinger,tvLanguage,tvProgress;
		RelativeLayout bg;
    }
    
    class Viewholder_Gongbo{
    	ImageView ivSinger,ivDelete,ivScore;
    	TextView tvSong,tvSinger,tvLanguage;
		RelativeLayout bg;
    }
    
    
	public int getItemType(){
    	return itemType;
    }
	
    
	 //判断文件夹是否存在,  存在 true,  不存在 false
  	private boolean isFileExist(String strFolder) {
  		File file = new File(strFolder);
  		if (file.exists()) {
  			return true;
  		}
  		return false;
  	}


	
	
	private Bitmap getLockBitmapFromFile(String path) {
		// TODO Auto-generated method stub
		File file = new File(path);
		Bitmap bitmap = null;
		try {					
			FileInputStream in = new FileInputStream(file);
			int available = in.available();
			byte[] buffer = new byte[available];
			int read = in.read(buffer);
			if(read != -1){
				char data = (char) (available&0xFF);
				for (int i = 0; i <available ; i++) {
					buffer[i] ^= ((data+i)^i)&0xFF; 
				}
				
			}			
			bitmap=Bytes2Bitmap(buffer);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		//Log.v("bitmap","======Width()====="+bitmap.getWidth());
		//Log.v("bitmap","======Height()====="+bitmap.getHeight());
		return zoomBitmap(bitmap,175,175);
	}
	
	public Bitmap Bytes2Bitmap(byte[] b) {
         if (b.length != 0) {
             return BitmapFactory.decodeByteArray(b, 0, b.length);
         } else {
             return null;
         }
     }
	 
	  public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h){  
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
	
  
    public void refresh(ArrayList<SongSearchBean> datas){
		//Log.i("song","refresh === "+datas.size());
    	mDatas.clear();
    	mDatas.addAll(datas);
    	notifyDataSetChanged();
    }
	
	
    
    public interface OnMoreClickListener{
    	void onMoreClickListener(String number,String orderId,int downstat);
		void onRecordOption(SongSearchBean song);
    }
    
    
    public void setMoreClickListener(OnMoreClickListener listener){
    	this.mClickListener = listener;
    }
    
    public interface RecordShareClickListener{
    	void onShareClickListener(SongSearchBean song);
    }
    
    public void setShareClickListener(RecordShareClickListener listener){
    	this.mShareClickListener = listener;
    }
	
	   class onButtonFocusChangeListener implements OnFocusChangeListener{
    	private View view;
		private int position;
    	public onButtonFocusChangeListener(View v,int pos){
    		this.view = v;
			this.position = pos;
    	}
    	
    	@Override
    	public void onFocusChange(View arg0, boolean arg1) {
    		// TODO Auto-generated method stub
    		//RelativeLayout bg = (RelativeLayout) view.findViewById(R.id.rl_bg);
			//Log.i("song","button focus change ====="+arg1+"bg ====="+view);
			if(view == null)
				return;
    		if(arg1){
    			view.setBackgroundResource(R.drawable.song_selected_bg);
				MyApplication.currentItemPosition = position;
				MyApplication.isGridviewFocus = true;
    		}else{
    			view.setBackgroundResource(R.drawable.song_unselect_bg);
			}
    	}
    } 
	   
	   
	   
	   
	   public class DownloadThread extends Thread{
		   private int temProgress;
		   @Override
		public void run() {
			// TODO Auto-generated method stub
			
			while(MyApplication.isShowDownloadProgress && MyApplication.isSongNameFragment){
				String json = JNILib.getSongDownPercent();
				Log.i("song","download percent==="+json);
				try {
					if(json!=null && json.contains("down_percent")){
						JSONObject object = new JSONObject(json);
						downloadProgress = object.getInt("down_percent");
						downloadState = object.getInt("cloud_status");
						if(downloadProgress==0){
							temProgress = downloadProgress;
							refreshItemView(downloadProgress,downloadState);
						}else{
							refreshItemView(downloadProgress,temProgress);
						}
						
					}else{
						MyApplication.isShowDownloadProgress = false;
						//MyApplication.isShowDownloadProgress = false;
						uiHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								refresh(SongJsonParseUtils.getSongDatas2(0, "", 0, 21, 0, 0, 8, ""));
								mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);			
							}
						});
					}
					SystemClock.sleep(1000);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("song","downloadProgress error======="+e.toString());
					e.printStackTrace();
				}
			
				
			}
			super.run();
		}
	   }
	   
	   
	   
	   //刷新某一条item
		private void refreshItemView(final int downloadProgress,final int status) {
			// TODO Auto-generated method stub
			uiHandler.post(new Runnable() {
				
				@Override
				public void run() {
					Log.i("song","refresh down progress==="+downloadProgress+"=="+firstRefreshView);
					// TODO Auto-generated method stub
					if(firstRefreshView != null){
						Viewholder_Record holder = (Viewholder_Record) firstRefreshView.getTag();
						if(status==0){
							if(downloadProgress>1)
							holder.tvProgress.setText(mContext.getResources().getString(R.string.on_loading)+downloadProgress+"%");	
						}else if(status == -10){//文件校验失败
							holder.tvProgress.setText(mContext.getResources().getString(R.string.Inspection_failed));
							MyApplication.isShowDownloadProgress = false;
						}else if(status == -2){//文件不存在
							holder.tvProgress.setText(mContext.getResources().getString(R.string.File_does_not_exist));
							MyApplication.isShowDownloadProgress = false;
							
						}else if(status == -8){//暂停
							holder.tvProgress.setText(mContext.getResources().getString(R.string.song_handle10));
							MyApplication.isShowDownloadProgress = false;
							
						}else if(status == -3){//空间不足
							holder.tvProgress.setText(mContext.getResources().getString(R.string.The_disk_is_full));
							MyApplication.isShowDownloadProgress = false;
							
						}else if(status == -5){//MAC 地址校验失败
							holder.tvProgress.setText(mContext.getResources().getString(R.string.MAC_is_not_opened));
							MyApplication.isShowDownloadProgress = false;
							
						}else {
							holder.tvProgress.setText(mContext.getResources().getString(R.string.download_failed));
							MyApplication.isShowDownloadProgress = false;
						}						
						}
				}
			});
		
		}
		
		
		public void setRefreshView(View view){
			firstRefreshView = view;
		}
}
