package com.jsl.ktv.view;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	public static boolean isPipFocus;
	public static boolean isGridviewFocus;
	public static boolean isSingerListFocus;
	public static boolean isLanguageChange;
	public static boolean isSongNameFragment;
	public static boolean isSingerFragment;
	public static boolean isRecommendFragment;
    public static boolean isTopButtonFocus;
	public static int currentItemPosition = 0;
	public static boolean isNeedRefresh ;
	public static boolean isOnkeyupRefresh;
	public static boolean isBackHome;
	public static String currentSinger="";
	public static String currentSingerNum = "";
	public static String searchKey = "";
	public static String scrolling_switch = "1";
	public static String logo_switch = "1";
	public static boolean isInHomeFragment;
	public static boolean isOnSelectSong;
	public static boolean isSelectAll;
	public static int searchStartCount;
	public static int currentLayer = 0;
	public static int currentEnterType = 0;
	public static boolean isDeleteSearchKey ;
	public static boolean isSearchViewShow;
	public static boolean isShowDownloadProgress;
	public static String MAC = "";
	public static boolean isDataInit;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isPipFocus = false;
		isGridviewFocus = true;
		isSingerListFocus = true;
		isLanguageChange = false;
		isSongNameFragment = false;
		isSingerFragment = false;
		isRecommendFragment = false;
		isTopButtonFocus = false;
		isSelectAll = false;
		isNeedRefresh = true;
		isOnkeyupRefresh = false;
		isBackHome = false;
		isInHomeFragment = true;
		isOnSelectSong = false;
		isDeleteSearchKey = false;
		isSearchViewShow = false;
		isShowDownloadProgress = false;
		currentSinger = "";
		searchKey = "";
		isDataInit= false;
	}
	
	

}
