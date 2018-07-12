package com.jsl.ktv.fragment;

import java.util.ArrayList;

import com.jsl.ktv.R;
import com.jsl.ktv.adapter.RecommendOrRankAdapter;
import com.jsl.ktv.bean.RecommendOrRankBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.listener.RecommendOrRankLoadListener;
import com.jsl.ktv.util.NetDataParseUtil;
import com.jsl.ktv.view.RankingAnimationButton;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RankingFragment extends CommonFragment {
	private GridView lvRecommend;
	private RecommendOrRankAdapter adapter;
	private ArrayList<RecommendOrRankBean> datas;
	private Handler mHandler;
	private LinearLayout llContainer;
	
	public RankingFragment(){};
	public RankingFragment(Handler handler){
		mHandler = handler;
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.launcher_fragment_new_ranking,null,false);
		initViews(view);
		displayItem();
		return view;
	}
	
	
	
	
    private void displayItem() {
		// TODO Auto-generated method stub
    	NetDataParseUtil.getIntance().getRecommendList(2,new RecommendOrRankLoadListener() {
		
		@Override
		public void onLoadFinish(ArrayList<RecommendOrRankBean> list) {
			// TODO Auto-generated method stub
			if(list==null){
				//TODO
			}else{
				datas=list;
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for ( int i = 0; i < datas.size(); i++) {
				    		final RecommendOrRankBean bean = datas.get(i);
				    		RankingAnimationButton button = new RankingAnimationButton(getActivity());
				    		button.setText(bean.getTitel());
				    		button.setImage(bean.getUrl());
				    		button.setClickable(true);
				    		button.setFocusable(true);
				    		button.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									//Toast.makeText(getActivity(), "click ===", Toast.LENGTH_SHORT).show();
									replaceFragment(R.id.center_fragment, new RecommendSongNameFragment(mHandler,bean.getId()));
								}
							});
				    		llContainer.addView(button);
				    		llContainer.getChildAt(0).requestFocus();
							
						}
					}
				});
	
			}
		}
	});
    	
		
	}

	private void initViews(View view) {
		// TODO Auto-generated method stub
		//lvRecommend = (GridView) view.findViewById(R.id.gv_search);
		llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
		datas = new ArrayList<RecommendOrRankBean>();
		
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW);
		RankingAnimationButton btn1 = (RankingAnimationButton) llContainer.getChildAt(0);
		if(btn1 != null)
			btn1.requestFocus();
		super.onResume();
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
