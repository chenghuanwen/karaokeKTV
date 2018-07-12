package com.jsl.ktv.karaok;
import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class MarqueeText extends TextView implements Runnable {        
    private int currentScrollX;// 当前滚动的位�?       
    private boolean isStop = false;        
    private int textWidth;        
    private int start_cnt = 0;        
    private int time_cnt = 0;        
    private boolean isMeasure = false;
    private final String TAG = "queetext";	
    public MarqueeText(Context context) 
    {                
        super(context);                // TODO Auto-generated constructor stub        
    }        
    public MarqueeText(Context context, AttributeSet attrs) 
    {                
        super(context, attrs);        
    }        
    public MarqueeText(Context context, AttributeSet attrs, int defStyle) 
    {                
        super(context, attrs, defStyle);        
    }  

    @Override        
    protected void onDraw(Canvas canvas) 
    {                // TODO Auto-generated method stub                
        super.onDraw(canvas);                
        if (!isMeasure) 
        {// 文字宽度只需获取�?��就可以了                        
            getTextWidth();                        
            isMeasure = true;                
        }        
    }

    /**         * 获取文字宽度         */        
    private void getTextWidth() {                
        Paint paint = this.getPaint();                
        String str = this.getText().toString();                
        textWidth = (int) paint.measureText(str);    
        Log.v(TAG,"getTextWidth text is:"+str);
        Log.v(TAG,"getTextWidth textWidth:"+textWidth);
    }

    /*@Override        
    public void run() {                
        currentScrollX -= 2;// 滚动速度                
        scrollTo(currentScrollX, 0);                
        if (isStop) {                        
            return;                
        }                
        if (getScrollX() <= -(this.getWidth())) {                        
            scrollTo(textWidth, 0);                        
            currentScrollX = textWidth;           
        }                
        postDelayed(this, 10);        
    }  */

    @Override        
    public void run() {                
        if (isStop) {                        
            return;                
        }

        // Log.v(TAG,"time_cnt:"+time_cnt);
        if (time_cnt == 0)
        {
            Log.v(TAG,"setVisibility GONE time_cnt:"+time_cnt);
            this.setVisibility(View.GONE);
        }

        if (time_cnt == 10)
        {
            Log.v(TAG,"setVisibility VISIBLE, time_cnt:"+time_cnt);
            this.setVisibility(View.VISIBLE);
        }

        if (time_cnt >= 100)
        {
            currentScrollX += 2;// 滚动速度
            if (currentScrollX >= textWidth) {	
                currentScrollX = 0-(this.getWidth());
    			Log.v(TAG,"reword currentScrollX:"+currentScrollX);
            }
            
            //Log.v(TAG,"1currentScrollX:"+currentScrollX);
            scrollTo(currentScrollX, 0);
        }
            

        time_cnt++;
        postDelayed(this, 10);        
          
    } 

    // �?��滚动        
    public void startScroll() {
        Log.v(TAG,"startScroll now:"+start_cnt);
        isStop = false;                
        if (start_cnt == 0)
        {
            this.removeCallbacks(this);                
            post(this);  
            start_cnt++;
        }
    } 

    // 停止滚动        
    public void stopScroll() {                
        Log.v(TAG,"stopScroll now:");
        isStop = true;        
    }  

    // 从头�?��滚动        
    public void startFrom0() {  
        Log.v(TAG,"startFrom0 now:");
        time_cnt = 0;
        getTextWidth();
        currentScrollX = 0;// - (this.getWidth()); 
        Log.v(TAG,"startFrom0 currentScrollX:"+currentScrollX);
        scrollTo(currentScrollX, 0); 
        startScroll(); 
    }
    
}