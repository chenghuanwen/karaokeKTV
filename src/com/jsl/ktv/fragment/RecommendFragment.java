package com.jsl.ktv.fragment;

import java.util.ArrayList;

import com.jsl.ktv.R;
import com.jsl.ktv.adapter.RecommendOrRankAdapter;
import com.jsl.ktv.bean.RecommendOrRankBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.listener.RecommendOrRankLoadListener;
import com.jsl.ktv.util.NetDataParseUtil;
import com.jsl.ktv.view.CustomeGridView;
import com.jsl.ktv.view.MyApplication;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RecommendFragment extends CommonFragment {
	private CustomeGridView gvRecommend;
	private RecommendOrRankAdapter adapter;
	private ArrayList<RecommendOrRankBean> datas;
	private Handler mHandler;
	private Handler uihHandler = new Handler(Looper.getMainLooper());
	private View rootView;
	public RecommendFragment(){};
	public RecommendFragment(Handler handler){
		this.mHandler = handler;
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	/*	if(rootView==null){
			rootView = inflater.inflate(R.layout.laucher_fragment_recommend,null,false);
			initViews(rootView);
		}
		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
				ViewGroup parent = (ViewGroup) rootView.getParent();  
				if (parent != null) {  
					parent.removeView(rootView);  
				}   */
		rootView = inflater.inflate(R.layout.laucher_fragment_recommend,null,false);
		initViews(rootView);
		return rootView;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW);
		gvRecommend.requestFocusFromTouch(); 
		gvRecommend.setSelection(0);
	}
	
	private void initViews(View view) {
		// TODO Auto-generated method stub
		gvRecommend = (CustomeGridView) view.findViewById(R.id.gv_recommend);
		datas = new ArrayList<RecommendOrRankBean>();
	
		adapter = new RecommendOrRankAdapter(1, getActivity(), datas);
		gvRecommend.setAdapter(adapter);
		
		NetDataParseUtil.getIntance().getRecommendList(1, new RecommendOrRankLoadListener() {
			
			@Override
			public void onLoadFinish(final ArrayList<RecommendOrRankBean> list) {
				// TODO Auto-generated method stub
				if(list==null){
					//TODO
				}else{			
					uihHandler.post( new Runnable() {
						public void run() {
							datas.clear();
							datas.addAll(list);
							adapter.notifyDataSetChanged();		
							gvRecommend.requestFocus();
							gvRecommend.requestFocusFromTouch();
							gvRecommend.setSelection(0);
						}
					});
	
				}
			}
		});
		
		
		gvRecommend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int id = datas.get(arg2).getId();
				MyApplication.currentSinger = getResources().getString(R.string.home_4)+"/"+datas.get(arg2).getTitel();
				MyApplication.isRecommendFragment = true;
				replaceFragment(R.id.center_fragment, new RecommendSongNameFragment(mHandler,id));
			}
		});
		
		
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

}
