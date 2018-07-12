package com.jsl.ktv.view;
import android.util.Log;
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
import com.jsl.ktv.R;

public class AnimationButton extends RelativeLayout implements
		OnFocusChangeListener {

	private float animationX = 1.0f;
	private float animationY = 1.0f;

	private ImageView animationBkImage;
	private ImageView animationImage;
	private TextView animationText;

	public AnimationButton(Context context) {
		super(context);
		initView();
	}

	public AnimationButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AnimationButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.animation_button, this);
		animationBkImage = (ImageView) findViewById(R.id.animation_bk_image);
		animationImage = (ImageView) findViewById(R.id.animation_image);
		animationText = (TextView) findViewById(R.id.animation_text);
        animationText.setVisibility(View.VISIBLE);
		setOnFocusChangeListener(this);
	}

	public void setText(String text) {
		animationText.setText(text);
	}
	
	public void setTextSize(int size){
		animationText.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
	}
	
	public void setPadding(int l,int t,int r,int b){
		animationText.setPadding(l,t,r,b);
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
		//Log.i("song","pip button focus change=="+hasFocus);
		ScaleAnimation animation = null;
		if (hasFocus) {
			/*animation = new ScaleAnimation(1.0f, animationX, 1.0f, animationY,
					Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f);*/
				/*	if(v.getId()==R.id.pip_botton){
					MyApplication.isPipFocus = true;
					MyApplication.isGridviewFocus = false;
					MyApplication.isSingerListFocus = false;

					Log.i("song","pip focus==========");
				}*/
					animationBkImage.setVisibility(View.VISIBLE);
		} else {
		/*	animation = new ScaleAnimation(animationX, 1.0f, animationY, 1.0f,
					Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f);*/
					/*if(v.getId()==R.id.pip_botton){
					MyApplication.isPipFocus = false;
					Log.i("song","pip focus==========");
				}*/
					animationBkImage.setVisibility(View.GONE);
		}
	/*	bringToFront();
		animation.setDuration(100);
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
				//animationText.setVisibility(View.GONE);
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (hasFocus) {
				animationBkImage.setVisibility(View.VISIBLE);
				//animationText.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}

}