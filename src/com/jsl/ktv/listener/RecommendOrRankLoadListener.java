package com.jsl.ktv.listener;

import java.util.ArrayList;

import com.jsl.ktv.bean.RecommendOrRankBean;

public interface RecommendOrRankLoadListener {
	void onLoadFinish(ArrayList<RecommendOrRankBean> list);
}
