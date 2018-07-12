package com.jsl.ktv.karaok;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;

public class Text {

    private final int STEP = 1;// 移动步长

    private Paint paint;
	private Paint mpaint;

    private float tempX;// x坐标

    private float tempY;// x坐标

    private float viewWidth;

    private String content;// 文字内容

    private float contentWidth;// 文字宽度

    public Text(MyTextView textView, String content) {
		
		mpaint = new Paint();
        mpaint.setColor(0xee0000ff);
        mpaint.setTextSize(40);
		mpaint.setStrokeWidth(4);// 描边宽度
		mpaint.setStyle(Style.FILL_AND_STROKE); // 描边种类 */
		
        paint = new Paint();
        paint.setColor(0xffffffff);
        paint.setTextSize(40);
		//paint.setStrokeWidth(2);// 描边宽度
		paint.setStyle(Style.FILL_AND_STROKE); // 描边种类 */
		//paint.setAntiAlias(true); 
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();

        this.tempX = textView.getWidth();
        this.tempY = (textView.getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top)
                / 2 - fontMetrics.top+20;
        this.viewWidth = textView.getWidth();
        this.content = content;
        this.contentWidth = paint.measureText(content);
        this.contentWidth = mpaint.measureText(content);
    }

    public void move() {
        tempX -= STEP;
        if (tempX < -contentWidth)// 移出屏幕�?从右侧进�?
            tempX = 1920;
    }

    public void SetMoveString(String str)
    {
        this.content = str;
        this.contentWidth = paint.measureText(str);
        this.contentWidth = mpaint.measureText(str);
    }
    
    public void SeekTo0()
    {
        tempX = viewWidth;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(content, tempX, tempY, mpaint);
        canvas.drawText(content, tempX, tempY, paint);
    }

}
