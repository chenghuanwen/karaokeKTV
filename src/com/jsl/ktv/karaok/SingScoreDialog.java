package com.jsl.ktv.karaok;

import com.jsl.ktv.karaok.ScoreWithPicView;
import com.jsl.ktv.karaok.VideoString;
import com.jsl.ktv.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.player.boxplayer.karaok.JNILib;
public class SingScoreDialog extends Dialog {
	private int score;
	private ScoreWithPicView tvScore;
	private TextView tvSongName,tvSystemTip,tvSinger,tvTitel,tv1,tv2;
	private String songName,systemTip,singer,picPath;
	private ImageView ivGequ,ivSay;
    private Context mContext;
	private int jLanguage = 0;
	public SingScoreDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public SingScoreDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sing_score_result2);
		
		jLanguage=JNILib.getGlobalLanguage();
		
		tvScore = (ScoreWithPicView)findViewById(R.id.show_sing_result);
		tvSystemTip = (TextView)findViewById(R.id.tv_system_tip);
		tvTitel = (TextView)findViewById(R.id.tv_titel);
		tvSongName = (TextView)findViewById(R.id.tv_song_name);
		tvSinger = (TextView)findViewById(R.id.tv_singer);
		tv1 = (TextView)findViewById(R.id.tv1);
		tv2 = (TextView)findViewById(R.id.tv2);
		
		tvTitel.setText(VideoString.score_titel[jLanguage]);
		tv1.setText(VideoString.score_sing[jLanguage]);
		tv2.setText(VideoString.score_tip[jLanguage]);
		
		
	
		ivGequ = (ImageView)findViewById(R.id.iv_gequ);
		ivSay = (ImageView)findViewById(R.id.iv_say);
		
	}
	
	public void setScoreInfo(int score,String songName,String systemTip,String singer,String picPath) {
		this.score = score;
		this.songName = songName;
		this.systemTip = systemTip;
		this.singer = singer;
		this.picPath = picPath;
	}
	
	
	
	public void showScore() {
		if(tvScore == null)
			return;
		tvScore.setScore(score, ScoreWithPicView.LEVEL);
		tvSongName.setText(VideoString.score_name[jLanguage]+": "+singer);
		tvSinger.setText(VideoString.score_singer[jLanguage]+": "+songName);
		tvSystemTip.setText(systemTip);
		if(!TextUtils.isEmpty(picPath)){
			ivGequ.setImageBitmap(BitmapFactory.decodeFile(picPath));
		}
		
			if(score>=80){
			ivSay.setImageResource(R.drawable.zhenbang);
		}else{
			ivSay.setImageResource(R.drawable.jiayou);
		}
		
		
	}

}
