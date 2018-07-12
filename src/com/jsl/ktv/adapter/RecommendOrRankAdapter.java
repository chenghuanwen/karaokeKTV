package com.jsl.ktv.adapter;

import java.util.ArrayList;

import com.jsl.ktv.R;
import com.jsl.ktv.bean.RecommendOrRankBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendOrRankAdapter extends BaseAdapter {
	private int itemType = -1;
	private Context mContext;
	private ArrayList<RecommendOrRankBean> datas;
	

	public RecommendOrRankAdapter(int itemType, Context mContext,
			ArrayList<RecommendOrRankBean> datas) {
		super();
		this.itemType = itemType;
		this.mContext = mContext;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas==null?0:datas.size();
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
			if (itemType == 1) {
				contentView = LayoutInflater.from(mContext).inflate(R.layout.recommend_item, null);
			}else if(itemType == 2){
				contentView = LayoutInflater.from(mContext).inflate(R.layout.ranking_item, null);
			}
			holder.iv = (ImageView) contentView.findViewById(R.id.iv_image);
			holder.tv = (TextView) contentView.findViewById(R.id.tv_title);
			contentView.setTag(holder);
		}else{
			holder = (ViewHolder) contentView.getTag();
		}
		RecommendOrRankBean bean = datas.get(position);
		ImageLoader.getInstance().displayImage(bean.getUrl(),holder.iv);
		holder.tv.setText(bean.getTitel());
		
		return contentView;
	}
	
	
	class ViewHolder{
		ImageView iv;
		TextView tv;
	}

}
