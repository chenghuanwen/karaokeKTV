package com.jsl.ktv.util;

import com.jsl.ktv.view.MyApplication;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyLanguageChangeListener extends BroadcastReceiver {
	private ActivityManager mAm;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		mAm = (ActivityManager) arg0.getSystemService(Context.ACTIVITY_SERVICE);
		if(arg1.getAction().equals(Intent.ACTION_LOCALE_CHANGED)){
			MyApplication.isLanguageChange = true;
			 
		//	mAm.forceStopPackage("com.player.boxplayer");
			
		}
	}

}
