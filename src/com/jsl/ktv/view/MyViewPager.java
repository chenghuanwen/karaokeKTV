package com.jsl.ktv.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager{
	public MyViewPager(Context context)
	{
		super(context);
	}
	public MyViewPager(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		return false;
	}

}