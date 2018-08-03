package com.jsl.ktv.view;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.graphics.Rect;  
import android.util.AttributeSet;  
import android.util.Log;
import android.widget.TextView;  
  
public class MarqueeTextView extends TextView{  
    public static final String TAG = "MyTextView";  
   
  
  
    public MarqueeTextView(Context context) {  
        super(context);  
        setSingleLine(true);  
          
    }  
    public MarqueeTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        setSingleLine(true);  
      
    }  
    @Override
    public boolean isFocused() {
        return true;
    }
  
}  
