package com.jsl.ktv.fragment;

import java.util.ArrayList;

import com.jsl.ktv.R;
import com.jsl.ktv.adapter.RecommendOrRankAdapter;
import com.jsl.ktv.bean.RecommendOrRankBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.listener.RecommendOrRankLoadListener;
import com.jsl.ktv.util.NetDataParseUtil;
import com.jsl.ktv.view.MyApplication;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RecommendFragment extends CommonFragment {
	private GridView gvRecommend;
	private RecommendOrRankAdapter adapter;
	private ArrayList<RecommendOrRankBean> datas;
	private Handler mHandler;
	private String titels[]= new String[]{"最美的期待","望海潮","经典粤语","朝花夕拾","Jay经典系列","毕业季","最美的期待","望海潮","经典粤语","朝花夕拾","Jay经典系列","毕业季"};
	private String urls[] = new String[]{"http://imgsrc.baidu.com/forum/w%3D580/sign=65810d48fd03738dde4a0c2a831ab073/f8ca84cb39dbb6fd8ee697ce0224ab18962b37ad.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=6c7ae9538cd6277fe912323018391f63/ebb7dc33c895d143463099cb78f082025baf07a0.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=2e641336366d55fbc5c6762e5d204f40/00673c12b31bb051f0eaccc03d7adab44bede018.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=65810d48fd03738dde4a0c2a831ab073/f8ca84cb39dbb6fd8ee697ce0224ab18962b37ad.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=6c7ae9538cd6277fe912323018391f63/ebb7dc33c895d143463099cb78f082025baf07a0.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=2e641336366d55fbc5c6762e5d204f40/00673c12b31bb051f0eaccc03d7adab44bede018.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg",
			"http://imgsrc.baidu.com/forum/w%3D580/sign=758f9c9ba64bd11304cdb73a6aaea488/d9df3ec79f3df8dc6c1af779c611728b461028c7.jpg"};
	public RecommendFragment(){};
	public RecommendFragment(Handler handler){
		this.mHandler = handler;
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.laucher_fragment_recommend,null,false);
		initViews(view);
		return view;
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
		gvRecommend = (GridView) view.findViewById(R.id.gv_recommend);
		datas = new ArrayList<RecommendOrRankBean>();
	/*	for (int i = 0; i < titels.length; i++) {
			RecommendOrRankBean bean = new RecommendOrRankBean();
			bean.setTitel(titels[i]);
			bean.setUrl(urls[i]);
			bean.setId(i);
			datas.add(bean);
		}*/
		
		adapter = new RecommendOrRankAdapter(1, getActivity(), datas);
		gvRecommend.setAdapter(adapter);
		
		NetDataParseUtil.getIntance().getRecommendList(1, new RecommendOrRankLoadListener() {
			
			@Override
			public void onLoadFinish(final ArrayList<RecommendOrRankBean> list) {
				// TODO Auto-generated method stub
				if(list==null){
					//TODO
				}else{					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
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
				MyApplication.isSongNameFragment = true;
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
