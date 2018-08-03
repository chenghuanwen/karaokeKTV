package com.jsl.ktv.karaok;

import com.jsl.ktv.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateSongTxtDialog extends Dialog {
	private Context mContext;
	private TextView tvTitel;
	private TextView tvsure,tvcancel;
	private String titel;
	public UpdateSongTxtDialog(Context context, int theme,String titel) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.titel = titel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.update_song_txt_dialog,null);
		setContentView(view);
		tvTitel = (TextView) view.findViewById(R.id.tv_tip);
		tvcancel = (TextView) view.findViewById(R.id.tv_cancel);
		tvsure = (TextView) view.findViewById(R.id.tv_sure);
		
		tvsure.setFocusable(true);
		tvsure.setFocusableInTouchMode(true);
		tvsure.requestFocus();
		tvTitel.setText(mContext.getString(R.string.Detected_update)+titel);
		tvcancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		tvsure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
			}
		});
		
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		
	}
}
