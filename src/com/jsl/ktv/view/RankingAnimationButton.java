package com.jsl.ktv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.TypedValue;
import android.text.TextPaint;
import com.jsl.ktv.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RankingAnimationButton extends RelativeLayout implements
		OnFocusChangeListener {

	private ImageView animationBkImage;
	private ImageView animationImage;
	private TextView tvRanking;

	public RankingAnimationButton(Context context) {
		super(context);
		initView();
	}

	public RankingAnimationButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public RankingAnimationButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ranking_item, this);
		animationBkImage = (ImageView) findViewById(R.id.singer_bk_image);
		animationImage = (ImageView) findViewById(R.id.iv_image);
		tvRanking = (TextView) findViewById(R.id.tv_title);
		setOnFocusChangeListener(this);
	}

	public void setText(String text) {
		tvRanking.setText(text);
		TextPaint paint = tvRanking.getPaint();
		paint.setFakeBoldText(true); 
	}
	
	public void setTextSize(int size){
		tvRanking.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
	}


	public void setImage(String url) {
		if(animationImage != null)
		ImageLoader.getInstance().displayImage(url, animationImage);
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if(hasFocus){
			animationBkImage.setVisibility(View.VISIBLE);
		}else{
			animationBkImage.setVisibility(View.INVISIBLE);
		}

	}



}