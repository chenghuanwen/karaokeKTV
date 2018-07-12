package com.jsl.ktv.karaok;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

public class MyTextView extends TextView implements Runnable {

    private Text text=null;
    private String string_move="";

    public MyTextView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        text = new Text(MyTextView.this, string_move);
                    }
                });
    }

    public void SetMoveString(String str)
    {
        string_move = str;
		if(text==null)
		{
			text = new Text(MyTextView.this, string_move);
		}
        text.SetMoveString(string_move);
    }

    public void SeekTo0()
    {
		if(text==null)
		{
			text = new Text(MyTextView.this, string_move);
		}
        text.SeekTo0();
    }

    public void startMove() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 1.刷新
                postInvalidate();
                // 2.睡眠
                Thread.sleep(20L);
                // 3.移動
                if (null != text) {
                    text.move();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 背景�?
        //canvas.drawColor(R.color.transparent);
        // 绘制文字
		if(text==null)
		{
			text = new Text(MyTextView.this, string_move);
		}
        text.draw(canvas);
    }

}