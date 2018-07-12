package com.jsl.ktv.audio;

import com.jsl.ktv.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MyEqualizerSeekBar extends RelativeLayout implements
        OnTouchListener {

    private MyVerticalSeekBar seekBar;
    private TextView textView;

    private int textID;
    private boolean canBeSet;

    public void setCanBeSet(boolean canBeSet) {
        this.canBeSet = canBeSet;
        if (isFocused()) {
            if (canBeSet)
                setThumbImage(R.drawable.thumb_focus);
            else
                setThumbImage(R.drawable.thumb_normal);
        }
    }

    public MyEqualizerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Sound);
        textID = a.getResourceId(R.styleable.Sound_text, -1);
        a.recycle();

        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.view_focus_bg);

        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_equalizer_seek_bar, this);

        seekBar = (MyVerticalSeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnTouchListener(this);
        setThumbImage(R.drawable.thumb_normal);

        textView = (TextView) findViewById(R.id.text_title);
        textView.setText(getContext().getString(textID));
    }

    public void setCurProgress(int progress) {
        seekBar.setProgress(progress);
        seekBar.onSizeChanged();
    }

    public int getCurProgress() {
        return seekBar.getProgress();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        if (null != seekBar && null != listener)
            seekBar.setOnSeekBarChangeListener(listener);
    }

    private void setThumbImage(int id) {
        seekBar.setThumb(getResources().getDrawable(id));
        seekBar.setProgress(getCurProgress());
        seekBar.onSizeChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (canBeSet) {
            if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
                int progress = seekBar.getProgress();
                int max = seekBar.getMax();
                progress = ++progress > max ? max : progress;
                seekBar.setProgress(progress);
                seekBar.onSizeChanged();
                return true;
            } else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
                int progress = seekBar.getProgress();
                progress = --progress < 0 ? 0 : progress;
                seekBar.setProgress(progress);
                seekBar.onSizeChanged();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestFocus();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        requestFocus();
        return false;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction,
            Rect previouslyFocusedRect) {
        if (gainFocus) {
            if (!canBeSet) {
                setThumbImage(R.drawable.thumb_normal);
            } else {
                setThumbImage(R.drawable.thumb_focus);
            }
        } else {
            setThumbImage(R.drawable.thumb_normal);
        }
        seekBar.onSizeChanged();
    }

}