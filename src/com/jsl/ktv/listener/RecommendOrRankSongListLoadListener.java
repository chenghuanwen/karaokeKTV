package com.jsl.ktv.listener;

import java.util.List;

public interface RecommendOrRankSongListLoadListener {
	void onLoadFinish(List<String> ids);
}
