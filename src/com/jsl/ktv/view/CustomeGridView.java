package com.jsl.ktv.view;
import android.content.Context;  
import android.graphics.Rect;
import android.util.AttributeSet;  
import android.util.Log;
import android.view.MotionEvent;  
import android.widget.GridView;  
  
/** 
 * 自定义上下不滚动的GridView 
 */  
public class CustomeGridView extends GridView {  
  
    public CustomeGridView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    
    
	 @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    }
	
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction,
            Rect previouslyFocusedRect) {
        int lastSelectItem = getSelectedItemPosition();
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            setSelection(lastSelectItem);
        }
        
    }
	
    
    /** 
     * 设置上下不滚�?
     */  
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
    	Log.i("song","gridview dispatchTouch========");
        if(ev.getAction() == MotionEvent.ACTION_MOVE){  
            return true;//true:禁止滚动  
        }  
  
        return super.dispatchTouchEvent(ev);  
    }  
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
    	Log.i("song","gridview TouchEvent========");
    	return super.onTouchEvent(ev);
    }
    
}
