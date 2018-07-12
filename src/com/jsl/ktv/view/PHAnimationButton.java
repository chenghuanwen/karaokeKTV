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

public class PHAnimationButton extends RelativeLayout implements
		OnFocusChangeListener {

	private float animationX = 1.0f;
	private float animationY = 1.0f;

	private ImageView animationBkImage;
	private ImageView animationImage;
	private TextView topText,song1,song2,song3,singer1,singer2,singer3;

	public PHAnimationButton(Context context) {
		super(context);
		initView();
	}

	public PHAnimationButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PHAnimationButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ph_animation_button, this);
		animationBkImage = (ImageView) findViewById(R.id.animation_bk_image);
		animationImage = (ImageView) findViewById(R.id.animation_image);
		topText = (TextView) findViewById(R.id.tp_animation_text);
		song1   = (TextView) findViewById(R.id.song1_text);
		singer1 = (TextView) findViewById(R.id.singer1_text);
		song2   = (TextView) findViewById(R.id.song2_text);
		singer2 = (TextView) findViewById(R.id.singer2_text);
		song3   = (TextView) findViewById(R.id.song3_text);
		singer3 = (TextView) findViewById(R.id.singer3_text);
        topText.setVisibility(View.VISIBLE);
		setOnFocusChangeListener(this);
	}

	public void setText(String text) {
		topText.setText(text);
		TextPaint paint = topText.getPaint();
		paint.setFakeBoldText(true); 
	}
	
	public void setTextSize(int size){
		topText.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
	}
	
	public void Song1setText(String text) {
		song1.setText(text);
	}
	public void Singer1setText(String text) {
		singer1.setText(text);
	}
	
	public void Song2setText(String text) {
		song2.setText(text);
	}
	public void Singer2setText(String text) {
		singer2.setText(text);
	}
	
	public void Song3setText(String text) {
		song3.setText(text);
	}
	public void Singer3setText(String text) {
		singer3.setText(text);
	}
	
	public void setPadding(int l,int t,int r,int b){
		topText.setPadding(l,t,r,b);
	}

	public void setImage(int id) {
		animationImage.setImageResource(id);
	}

	public void setAnimationScale(float animationX, float animationY) {
		this.animationX = animationX;
		this.animationY = animationY;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if(hasFocus){
			animationBkImage.setVisibility(View.VISIBLE);
		}else{
			animationBkImage.setVisibility(View.GONE);
		}
		/*caleAnimation animation = null;
		if (hasFocus) {
			animation = new ScaleAnimation(1.0f, animationX, 1.0f, animationY,
					Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f);

		} else {
			animation = new ScaleAnimation(animationX, 1.0f, animationY, 1.0f,
					Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f);
		}
		bringToFront();
		animation.setDuration(200);
		animation.setFillAfter(true);
		animation.setAnimationListener(new MyAnimationListener(hasFocus));
		this.startAnimation(animation);*/
	}

	private class MyAnimationListener implements AnimationListener {
		private boolean hasFocus;

		public MyAnimationListener(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (!hasFocus) {
				animationBkImage.setVisibility(View.GONE);
				//topText.setVisibility(View.GONE);
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (hasFocus) {
				animationBkImage.setVisibility(View.VISIBLE);
				//topText.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}

}