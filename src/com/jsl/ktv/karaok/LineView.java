package com.jsl.ktv.karaok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jsl.ktv.R;

import android.content.Context;
import android.graphics.Canvas;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
/**
 * @author Jesse
 * write this view for draw line,use it easy.
 */
public class LineView extends View implements Runnable {
	
	private final static String T_KEY = "Tpos";
	private final static String X_KEY = "Xpos";
	private final static String L_KEY = "Lpos";
	private final static String Y_KEY = "Ypos";
	
	private final static String ST_KEY = "STpos";
	private final static String SX_KEY = "SXpos";
	private final static String SL_KEY = "SLpos";
	private final static String SY_KEY = "SYpos";
	private final static String S_CORE = "SCpos";
	
	private final static String NT_KEY = "NTpos";
	private final static String NX_KEY = "NXpos";
	private final static String NL_KEY = "NLpos";
	private final static String NY_KEY = "NYpos";
	private int mpaint_x = 0;
	private int mpaint_y = 0;
	private int npaint_y = 0;
	private int nkey_y = 0;
	private int skey_y = 0;
	private int spaint_y = 150;
	private int s_score    = 80;
	private int max_y = 66;
	private int min_y = 65;
	private long start = 0;
	private int time = 0;
	private boolean score = false;
	private boolean nscore = true;
	private long stop = 0;
	private long stopTime = 0;
	private Bitmap bmp;
	private int accuracy = 3;
	private int start_time = 0;
	
	private List<Map<String, Integer>> mListPoint = new ArrayList<Map<String,Integer>>();
	private List<Map<String, Integer>> sListPoint = new ArrayList<Map<String,Integer>>();
	private List<Map<String, Integer>> gListPoint = new ArrayList<Map<String,Integer>>();
	
	Paint mPaint = new Paint();
	Paint sPaint = new Paint();
	Paint lPaint = new Paint();
	Paint nPaint = new Paint();
	
	private static Handler mHandler = null;
	
	public LineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(0xeeaaaaaa);
		mPaint.setStrokeWidth(10f);
		mPaint.setAntiAlias(true);
		
		sPaint.setStyle(Paint.Style.FILL);
		sPaint.setColor(0xaa11aa11);
		sPaint.setStrokeWidth(10f);
		sPaint.setAntiAlias(true);
		
		lPaint.setStyle(Paint.Style.FILL);
		lPaint.setColor(0xaa11aa11);
		lPaint.setStrokeWidth(4f);
		lPaint.setTextSize(40);
		lPaint.setAntiAlias(true);
		
		Resources res=getResources(); 
		BitmapDrawable bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.point);
		bmp=bmpDraw.getBitmap();
	
		Log.i("score","new lineview=====");
		// TODO Auto-generated constructor stub
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(0xeeaaaaaa);
		mPaint.setStrokeWidth(10f);
		mPaint.setAntiAlias(true);
		
		sPaint.setStyle(Paint.Style.FILL);
		sPaint.setColor(0xaa11aa11);
		sPaint.setStrokeWidth(10f);
		sPaint.setAntiAlias(true);
		
		lPaint.setStyle(Paint.Style.FILL);
		lPaint.setColor(0xaa11aa11);
		lPaint.setStrokeWidth(4f);
		lPaint.setTextSize(40);
		lPaint.setAntiAlias(true);
		
		Resources res=getResources(); 
		BitmapDrawable bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.point);
		bmp=bmpDraw.getBitmap();
		
		// TODO Auto-generated constructor stub
	}
	
	public LineView(Context context) {
		super(context);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(0xeeaaaaaa);
		mPaint.setStrokeWidth(10f);
		mPaint.setAntiAlias(true);
		
		sPaint.setStyle(Paint.Style.FILL);
		sPaint.setColor(0xaa11aa11);
		sPaint.setStrokeWidth(10f);
		sPaint.setAntiAlias(true);
		
		lPaint.setStyle(Paint.Style.FILL);
		lPaint.setColor(0xaa11aa11);
		lPaint.setStrokeWidth(4f);
		lPaint.setTextSize(40);
		lPaint.setAntiAlias(true);
		
		Resources res=getResources(); 
		BitmapDrawable bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.point);
		bmp=bmpDraw.getBitmap();
		
		//mpaint_x = getWidth()/5*50;
		//TODO Auto-generated constructor stub
	}
	
  public void startMove() {
		nscore = true;
		score = true;
		Thread thread = new Thread(this);
		thread.start();
		start = SystemClock.uptimeMillis();
		Log.v("score","startMove");
	}
	
	
	
	  public void stopSing(){
	  
	nscore = false;
	stop =  SystemClock.uptimeMillis();
	}
  	
	
	  public void startSing(){
	  
	nscore = true;
	stopTime =stopTime + SystemClock.uptimeMillis() - stop;
	}	
	
	
 public void setHandler(Handler handler)
    {
        mHandler = handler;
    }

 public int getScorea(){
	 
	 return s_score;
 }	
	
 public void Destroy(){
		    score = false;
			if(mListPoint!=null)
			mListPoint.clear();
			if(sListPoint!=null)
			sListPoint.clear();
			if(gListPoint!=null)
			gListPoint.clear();
			spaint_y = 150;
			npaint_y = -1;
			nkey_y = 0;
			skey_y = 0;
			s_score =0;
			Log.v("score","Destroy");
	  }	

	    public void run() {
	        try {
	            while (score) {
					if(nscore){
	                Thread.sleep(50L);
					Message message = new Message();    
                    message.what = 1;    
                    handler.sendMessage(message);
					}
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		
		final Handler handler = new Handler(){          // handle    
        public void handleMessage(Message msg){    
            switch (msg.what) {    
            case 1:
				//刷新
	            postInvalidate();
                move(); 
            }    
            super.handleMessage(msg);    
        }    
    }; 
	
	public void move(){
	    //time = time +50;
		time = (int)(SystemClock.uptimeMillis() - start -stopTime);
	//	Log.v("time", "==========="+time);
		//移动标准�?
		for (int index=0; index<mListPoint.size(); index++)
		{
			if(mListPoint.get(index).get(T_KEY)-12800<time){
				Map<String, Integer> temp = new HashMap<String, Integer>();
				temp.put(T_KEY, mListPoint.get(index).get(T_KEY));
				temp.put(X_KEY, mListPoint.get(index).get(X_KEY)-5);
				temp.put(L_KEY, mListPoint.get(index).get(L_KEY));
				temp.put(Y_KEY, mListPoint.get(index).get(Y_KEY));
				mListPoint.set(index,temp);
				//Log.i("CHW","MOVE==="+mListPoint.get(index).get(X_KEY));
				//invalidate();
			}
			//if(time+7800-mListPoint.get(index).get(T_KEY)-10*mListPoint.get(index).get(L_KEY)<0){
			if(time>mListPoint.get(index).get(T_KEY)&&time<mListPoint.get(index).get(T_KEY)+10*mListPoint.get(index).get(L_KEY)){	
				//Log.v("mListPointy","1="+mListPoint.get(index).get(Y_KEY));
				nkey_y = mListPoint.get(index).get(Y_KEY);
				npaint_y = 155-((mListPoint.get(index).get(Y_KEY)-min_y)*150/(max_y-min_y));
				//Log.v("mListPointy","1="+mListPoint.get(index).get(Y_KEY));
				Log.v("key","nn="+mListPoint.get(index).get(Y_KEY));
				
			}
			
			//评分结束
			if(time>10000){
				if(500>mListPoint.get(mListPoint.size()-1).get(X_KEY)+mListPoint.get(mListPoint.size()-1).get(L_KEY)){
				if(npaint_y!=-1 && mHandler!=null)
					mHandler.sendEmptyMessage(111111);	
				npaint_y = -1;
				spaint_y =150;
				nkey_y = 0;
				skey_y = 0;
				//ScorePicShow(100);
				}
			}
			if(index<mListPoint.size()-1){
				//线段间隔忽略
				if(time>mListPoint.get(index).get(T_KEY)+10*mListPoint.get(index).get(L_KEY)&&mListPoint.get(index+1).get(T_KEY)>time){
					npaint_y = -1;
					nkey_y = 0;
					//Log.v("mListPointy","npaint_y = 0");
				}
				//区分线段重叠
				if(mListPoint.get(index).get(T_KEY)+10*mListPoint.get(index).get(L_KEY)>mListPoint.get(index+1).get(T_KEY)){
					Map<String, Integer> temp = new HashMap<String, Integer>();
					temp.put(T_KEY, mListPoint.get(index).get(T_KEY));
					temp.put(X_KEY, mListPoint.get(index).get(X_KEY));
					temp.put(L_KEY, (mListPoint.get(index+1).get(T_KEY)-mListPoint.get(index).get(T_KEY))/10);
					temp.put(Y_KEY, mListPoint.get(index).get(Y_KEY));
					mListPoint.set(index,temp);
				}
			}
			if((mListPoint.get(index).get(X_KEY)+mListPoint.get(index).get(L_KEY))<-100){
				mListPoint.remove(index);
			}
		}
		
		//计算mic声音显示高度
		for (int i=0; i<sListPoint.size(); i++)
		{	
	
			Map<String, Integer> sPoint = sListPoint.get(i);
			if(sPoint==null)
				continue;
			if(-50<(sPoint.get(ST_KEY)+10*sPoint.get(SL_KEY)-time)&&(sPoint.get(ST_KEY)+10*sPoint.get(SL_KEY)-time)<50){
				
				skey_y = sPoint.get(SY_KEY);
				if(skey_y<min_y&&skey_y!=0)
					skey_y = min_y;	
				s_score = sPoint.get(S_CORE);
			}
			if(sPoint.get(SY_KEY)<min_y){
				spaint_y =150;
			}else{
				spaint_y=155-((sPoint.get(SY_KEY)-min_y)*150/(max_y-min_y));
				if(spaint_y<7)
					spaint_y = 7;
			}
			if((sPoint.get(ST_KEY)-time)<-600){
				sListPoint.remove(i);
			}
		}
			
		
		/*if(spaint_y-npaint_y>-16&&spaint_y-npaint_y<16&&spaint_y!=150&&npaint_y!=150){
				Map<String, Integer> n_temp = new HashMap<String, Integer>();
				n_temp.put(NT_KEY, time);
				n_temp.put(NX_KEY, 500);
				n_temp.put(NL_KEY, 5);
				n_temp.put(NY_KEY, npaint_y);
				gListPoint.add(n_temp);
				
			}*/
		//判断mic声音和标准声音是否接�?
		//if(skey_y-nkey_y>-((max_y-min_y)/accuracy)&&skey_y-nkey_y<(max_y-min_y)/accuracy&&skey_y!=0&&nkey_y!=min_y&&npaint_y!=-1)
		if(skey_y-nkey_y>-16&&skey_y-nkey_y<16&&skey_y!=0&&nkey_y!=min_y&&npaint_y!=-1){
				Map<String, Integer> n_temp = new HashMap<String, Integer>();
				n_temp.put(NT_KEY, time);
				n_temp.put(NX_KEY, 645);
				n_temp.put(NL_KEY, 5);
				n_temp.put(NY_KEY, npaint_y);
				gListPoint.add(n_temp);
				spaint_y = npaint_y;
			}	
			//Log.v("gListPoint","spaint_y="+spaint_y);
			//Log.v("gListPoint","mpaint_y="+mpaint_y);
		//移动绿线	
		if(gListPoint.size()>0){
			for(int index=0; index<gListPoint.size(); index++){
				Map<String, Integer> temp = new HashMap<String, Integer>();
				temp.put(NT_KEY, gListPoint.get(index).get(NT_KEY));
				temp.put(NX_KEY, gListPoint.get(index).get(NX_KEY)-5);
				temp.put(NL_KEY, gListPoint.get(index).get(NL_KEY));
				temp.put(NY_KEY, gListPoint.get(index).get(NY_KEY));
				gListPoint.set(index,temp);
				
				if(gListPoint.get(index).get(NX_KEY)<-10){
					gListPoint.remove(index);
				}
					
			}
		}
		//mpaint_x = mpaint_x - 1;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		for (int index=0; index<mListPoint.size(); index++)
		{
			if (mListPoint.get(index).get(T_KEY)-12800<time)
			{
			//	Log.v("line1", "max_y==="+max_y);
			//	Log.v("line1", "min_y==="+min_y);
				if(max_y-min_y>0){
					mpaint_y=155-((mListPoint.get(index).get(Y_KEY)-min_y)*150/(max_y-min_y));
				}else{
					mpaint_y = 155-(mListPoint.get(index).get(Y_KEY)*150/max_y);
				}
				/*if(max_y-min_y>0){
					int sh = (mListPoint.get(index).get(Y_KEY)-min_y)/((max_y-min_y)/10);
					if(sh>0)
					mpaint_y = 155-(sh*(max_y-min_y)/10);
					else
					mpaint_y = 150;	
					if(mpaint_y<7)
						mpaint_y=7;
					}else{
					mpaint_y = 155-(mListPoint.get(index).get(Y_KEY)*150/max_y);
					}*/
				//Log.v("line", "mpaint_y==="+mpaint_y);
				//canvas.drawCircle(0, 0,5f, mPaint);
				//canvas.drawCircle(mListPoint.get(index).get(X_KEY), mpaint_y,5f, mPaint);
				canvas.drawLine(mListPoint.get(index).get(X_KEY), mpaint_y,
						mListPoint.get(index).get(X_KEY)+mListPoint.get(index).get(L_KEY), mpaint_y, mPaint);
			//	canvas.drawCircle(mListPoint.get(index).get(X_KEY)+mListPoint.get(index).get(L_KEY)-5, mpaint_y,5f, mPaint);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
			}
		}
		
		if(gListPoint.size()>0){
			for(int index=0; index<gListPoint.size(); index++){
				canvas.drawLine(gListPoint.get(index).get(NX_KEY), gListPoint.get(index).get(NY_KEY),
						gListPoint.get(index).get(NX_KEY)+gListPoint.get(index).get(NL_KEY), gListPoint.get(index).get(NY_KEY), sPaint);
			}
		}
		//if(sListPoint.size()>0){
		//	canvas.drawText(""+s_score, 440, 120, lPaint);
		//}
		canvas.drawBitmap(bmp, 645, spaint_y-7, null);
		//canvas.drawLine(500,0,500,150,lPaint);
	}
	/**
	 * @param curX  which x position you want to draw.
	 * @param curY	which y position you want to draw.
	 * @see	all you put x-y position will connect to a line.
	 */
	public void setLinePoint(int time,int curL, int curY)
	{
		start_time = 1920;
		if(time<12800){//标准文件12.8秒前�?��从屏幕中间开始画
			start_time = start_time -(12800-time)/10;
		}
		//	Log.i("CHW","st"+start_time);
		Map<String, Integer> temp = new HashMap<String, Integer>();
		temp.put(T_KEY, time);
		temp.put(X_KEY, start_time);
		temp.put(L_KEY, curL/10);
		temp.put(Y_KEY, curY);
	//	Log.i("CHW","x_key"+temp.get(X_KEY));
		mListPoint.add(temp);
		if(max_y<curY){
			max_y = curY;
		}
		if(min_y>curY){
			min_y = curY;
		}	
		
	}
	
	public void setSingPoint(int time,int curL, int curY,int score)
	{
		
		Map<String, Integer> temp = new HashMap<String, Integer>();
		temp.put(ST_KEY, time);
		temp.put(SX_KEY, 645);
		temp.put(SL_KEY, curL/10);
		temp.put(SY_KEY, curY);
		temp.put(S_CORE, score);
		sListPoint.add(temp);
		//Log.v("line", "getWidth()="+getWidth());
		//invalidate();
		/*mpaint_x = curX;
		mpaint_y = curY;*/
	}
	
}
