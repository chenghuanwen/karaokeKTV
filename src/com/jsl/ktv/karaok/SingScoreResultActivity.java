package com.jsl.ktv.karaok;



import com.jsl.ktv.karaok.ScoreWithPicView;
import com.jsl.ktv.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SingScoreResultActivity extends Activity {
	private ScoreWithPicView ScorePicView;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sing_score_result);
		int score = getIntent().getIntExtra("score", 0);
		ScorePicShow(score);
	}

	public void ScorePicShow(int score){
	
		ScorePicView = (ScoreWithPicView)findViewById(R.id.show_sing_result);
		ScorePicView.setVisibility(View.VISIBLE);
		ScorePicView.setScore(score, ScoreWithPicView.INCREMENT);
	}
}
