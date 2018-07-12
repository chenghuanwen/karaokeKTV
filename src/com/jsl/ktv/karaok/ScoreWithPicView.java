package com.jsl.ktv.karaok;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jsl.ktv.R;

/**
 * 显示分数
 * 
 * @author Administrator
 * 
 */
public class ScoreWithPicView extends RelativeLayout {
	public static final int COMMON = 0;// 正常形式�?
	public static final int INCREMENT = 1;// 增长形式�?
	public static final int LEVEL = 2;// 等级

	private RelativeLayout layout = null;
	private Context context;

	private ImageView first_num;
	private ImageView second_num;
	private ImageView score_line;
	private Map<Integer, Integer> drawMap = new HashMap<Integer, Integer>();
	private int delayTime;
	private Handler handler;

	public ScoreWithPicView(Context context) {
		super(context);
		initView(context);
		this.context = context;
	}

	public ScoreWithPicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);

		initView(context);
		this.context = context;
	}

	public ScoreWithPicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		this.context = context;
	}

	private void initView(Context context) {
		this.delayTime = 30;
		initDrawMap();
		initHandler();
		if (this.layout == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.layout = (RelativeLayout) inflater.inflate(
					R.layout.score_view_layout, this);
		}
		this.first_num = (ImageView) this.layout.findViewById(R.id.first_num);
		this.second_num = (ImageView) this.layout.findViewById(R.id.second_num);
		this.score_line = (ImageView) this.layout.findViewById(R.id.score_line);
	}

	private void initHandler() {
		this.handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					switch (msg.what) {
					case 1:
						showScoreWithPic(Integer.valueOf(msg.obj.toString()));
						break;
					}
				} catch (Exception e) {
				}
			}
		};
	}

	public void setScore(final int score, int type) {
		if (score > 100 || score < 0) {
			Toast.makeText(this.context, "动�?显示分数 - 数字应该�?-100之间",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (type == ScoreWithPicView.INCREMENT) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					int first = score / 3;
					int second = score * 2 / 3;
					for (int i = 0; i <= first; i++) {
						sendScoreToHandler(i, 1);
					}
					for (int i = first; i <= second; i++) {
						sendScoreToHandler(i, 2 * delayTime);
					}
					for (int i = second; i <= score; i++) {
						sendScoreToHandler(i, 6 * delayTime);
					}
				}

				private void sendScoreToHandler(int i, int times) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = i;
					handler.sendMessage(msg);
					try {
						Thread.sleep(times);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}else if (type == ScoreWithPicView.COMMON) {
			showScoreWithPic(score);
		}else if (type == ScoreWithPicView.LEVEL){
			showLevel(score);
		}
	}

	
	
		/**
		drawMap.put(10, R.drawable.level_a);
		drawMap.put(11, R.drawable.level_b);
		drawMap.put(12, R.drawable.level_c);
		drawMap.put(13, R.drawable.level_s);
		drawMap.put(14, R.drawable.level_add);
		<60  :C
		60   :C+
		70   :B
		75   :B+
		80   :A	
		85   :A+
		90   :S	
		95   :S+		
	*/
	private void showLevel(int score){
		if(score<60){
			this.first_num.setImageResource(drawMap.get(12));
			this.second_num.setVisibility(View.INVISIBLE);	
		}else if(score>=60&&score<70){
			this.first_num.setImageResource(drawMap.get(12));
			this.second_num.setImageResource(drawMap.get(14));
			
		}else if(score>=70&&score<80){
			this.first_num.setImageResource(drawMap.get(11));
			if(score>=75){
				this.second_num.setImageResource(drawMap.get(14));
			}else{
				this.second_num.setVisibility(View.INVISIBLE);	
			}
			
		}else if(score>=80&&score<90){
			this.first_num.setImageResource(drawMap.get(10));
			if(score>=85){
				this.second_num.setImageResource(drawMap.get(14));
			}else{
				this.second_num.setVisibility(View.INVISIBLE);	
			}
			
		}else if(score>=90){
			this.first_num.setImageResource(drawMap.get(13));
			if(score>=95){
				this.second_num.setImageResource(drawMap.get(14));
			}else{
				this.second_num.setVisibility(View.INVISIBLE);	
			}
			
		}
		
	}
	
	
	
	
	
	private void showScoreWithPic(int score) {

		if (score == 100) {
			this.first_num.setImageResource(drawMap.get(100));
			this.second_num.setVisibility(View.INVISIBLE);
			this.score_line.setVisibility(View.INVISIBLE);
		} else if (score >= 0 && score < 10) {
			this.first_num.setImageResource(drawMap.get(score));
			this.second_num.setVisibility(View.INVISIBLE);
			this.score_line.setVisibility(View.VISIBLE);
		} else {
			this.first_num.setImageResource(drawMap.get(score / 10));
			this.second_num.setImageResource(drawMap.get(score % 10));
			this.second_num.setVisibility(View.VISIBLE);
			this.score_line.setVisibility(View.VISIBLE);
		}
	}

	private void initDrawMap() {
		drawMap.put(0, R.drawable.num0);
		drawMap.put(1, R.drawable.num1);
		drawMap.put(2, R.drawable.num2);
		drawMap.put(3, R.drawable.num3);
		drawMap.put(4, R.drawable.num4);
		drawMap.put(5, R.drawable.num5);
		drawMap.put(6, R.drawable.num6);
		drawMap.put(7, R.drawable.num7);
		drawMap.put(8, R.drawable.num8);
		drawMap.put(9, R.drawable.num9);
		drawMap.put(100, R.drawable.num100);
		drawMap.put(10, R.drawable.level_a);
		drawMap.put(11, R.drawable.level_b);
		drawMap.put(12, R.drawable.level_c);
		drawMap.put(13, R.drawable.level_s);
		drawMap.put(14, R.drawable.level_add);
	}

	// 设置图片变动时�?的延�?
	public void setDelayTime(int time) {
		this.delayTime = time;
	}
}
