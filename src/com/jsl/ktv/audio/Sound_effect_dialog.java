package com.jsl.ktv.audio;

//import com.player.boxplayer.String;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.cmcc.media.RTSoundEffects;
import com.jsl.ktv.R;
import android.media.AudioManager;

public class Sound_effect_dialog extends Dialog implements
		OnFocusChangeListener {
	String TAG = "EQSetting";
	public final String PROPERTY_NAME = "Equalizer";
	public final String PRO_MODE = "mode";
	public final String PRO_ARG_0 = "arg0";
	public final String PRO_ARG_1 = "arg1";
	public final String PRO_ARG_2 = "arg2";
	public final String PRO_ARG_3 = "arg3";

	public final int EQUALIZER_USER = 5;
	public int DEF_VOLUME = 2;
	private Context mContext;
	private Toast toast;
	private MyEqualizerSeekBar[] seekBars = new MyEqualizerSeekBar[4];
	private boolean isUserMode = true;
	private boolean canBeSet;
	private int curMode = -1;
	private OnsetSoundListener mSetSoundListener;
	private OnSetMicListener mSetMicListener;
	private SharedPreferencesHelper sPreferencesHelper;
	private Button btnMode1, btnMode2, btnMode3;

	public Sound_effect_dialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sound_effect);
		init();
		android.view.WindowManager.LayoutParams lp = this.getWindow()
				.getAttributes();
		this.getWindow().setGravity(
				Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		lp.width = 1000; // 宽度
		lp.height = 900; // 高度
		lp.alpha = 1.0f; // 透明�?

		// this.getWindow().setType(android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		this.getWindow().setAttributes(lp);

		/*
		 * Micphone micphone = (Micphone) getSystemService("Micphone");
		 * micphone.initial(); micphone.start();
		 */
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		seekBars[3].setCurProgress(getVolume() / 2);
	}

	private void init() {
		// TODO Auto-generated method stub

		seekBars[0] = (MyEqualizerSeekBar) findViewById(R.id.seek_bar_0);
		seekBars[1] = (MyEqualizerSeekBar) findViewById(R.id.seek_bar_1);
		seekBars[2] = (MyEqualizerSeekBar) findViewById(R.id.seek_bar_2);
		seekBars[3] = (MyEqualizerSeekBar) findViewById(R.id.seek_bar_3);

		// 音调值：-5~5（seek max 11�?
		seekBars[0].setCurProgress(getIntProperty(PRO_ARG_0));
		seekBars[1].setCurProgress(getIntProperty(PRO_ARG_1));
		seekBars[2].setCurProgress(getIntProperty(PRO_ARG_2));
		// seekBars[3].setCurProgress(getVolume()/2);

		seekBars[0].setOnSeekBarChangeListener(new MyListener(seekBars[0]));
		seekBars[1].setOnSeekBarChangeListener(new MyListener(seekBars[1]));
		seekBars[2].setOnSeekBarChangeListener(new MyListener(seekBars[2]));
		seekBars[3].setOnSeekBarChangeListener(new MyListener(seekBars[3]));

		btnMode1 = (Button) findViewById(R.id.btn_sound_1);
		btnMode2 = (Button) findViewById(R.id.btn_sound_2);
		btnMode3 = (Button) findViewById(R.id.btn_sound_3);
		btnMode1.setOnFocusChangeListener(this);
		btnMode2.setOnFocusChangeListener(this);
		btnMode3.setOnFocusChangeListener(this);

	}

	private void setCanBeSet(boolean flag) {
		for (int i = 0; i < seekBars.length; i++) {
			seekBars[i].setCanBeSet(flag);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!canBeSet && KeyEvent.KEYCODE_DPAD_CENTER == keyCode) {
			canBeSet = true;
			setCanBeSet(canBeSet);
			return true;
		} else if (canBeSet && KeyEvent.KEYCODE_BACK == keyCode) {
			canBeSet = false;
			setCanBeSet(canBeSet);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class MyListener implements OnSeekBarChangeListener {

		private MyEqualizerSeekBar mySeek;

		public MyListener(MyEqualizerSeekBar mySeek) {
			this.mySeek = mySeek;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int curProgress = mySeek.getCurProgress();
			// curMode = EQUALIZER_USER;
			// isUserMode = true;
			Log.i(TAG, "soundListener current seekBar progress==="
					+ curProgress);
			isUserMode = true;
			switch (mySeek.getId()) {
			case R.id.seek_bar_0:

				break;

			case R.id.seek_bar_1:
				mSetMicListener.onSetMic(curProgress * 10);
				seekBars[1].setCurProgress(curProgress);
				break;

			case R.id.seek_bar_2:
				mSetSoundListener.onSetSound(getSoundValue(curProgress));
				seekBars[2].setCurProgress(curProgress);
				break;

			case R.id.seek_bar_3:
				if (curProgress > 0)
					setVolume(curProgress * 2);
				else
					setVolume(1);
				seekBars[3].setCurProgress(curProgress);
				break;

			default:
				break;
			}

			setUserProperty();

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.i(TAG, "onStartTrackingTouch");
			curMode = EQUALIZER_USER;
			// buttons[EQUALIZER_USER].requestFocus();
			// isUserMode = true;
			// refreshView();
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.i(TAG, "onStopTrackingTouch");
			curMode = EQUALIZER_USER;
			// refreshView();

		}
	}

	private void setUserProperty() {
		// if (curMode == EQUALIZER_USER) {
		// setIntProperty(PRO_MODE, EQUALIZER_USER);
		setIntProperty(PRO_ARG_0, seekBars[0].getCurProgress());
		setIntProperty(PRO_ARG_1, seekBars[1].getCurProgress());
		setIntProperty(PRO_ARG_2, seekBars[2].getCurProgress());
		setIntProperty(PRO_ARG_3, seekBars[3].getCurProgress());
		// }
	}

	public void showToast(String msg) {
		if (null == toast) {
			toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	public void setIntProperty(String key, int value) {
		sPreferencesHelper.getInstance(mContext).putInt(key, value);
	}

	public int getIntProperty(String key) {

		return sPreferencesHelper.getInstance(mContext).getInt(key, 6);
	}

	public interface OnsetSoundListener {
		void onSetSound(int progress);
	}

	public void setSoundChangerListener(OnsetSoundListener soundListener) {
		this.mSetSoundListener = soundListener;
	}

	public interface OnSetMicListener {
		void onSetMic(int progress);
	}

	public void setMicVolumeListener(OnSetMicListener listener) {
		this.mSetMicListener = listener;
	}

	private int getSoundValue(int curProgress) {
		// TODO Auto-generated method stub
		int value = 0;
		switch (curProgress) {
		case 1:
			value = -1;
			break;
		case 2:
			value = -5;
			break;
		case 3:
			value = -2;
			break;
		case 4:
			value = -3;
			break;
		case 5:
			value = -4;
			break;
		case 6:
			value = 0;
			break;
		case 7:
			value = 1;
			break;
		case 8:
			value = 2;
			break;
		case 9:
			value = 3;
			break;
		case 10:
			value = 4;
			break;
		case 11:
			value = 5;
			break;
		default:
			break;
		}
		return value;
	}

	// TODO 设置当前Volume�?
	private void setVolume(int value) {
		long time = System.currentTimeMillis();
		Log.i(TAG, "TIME 1:===============" + time);

		// set
		AudioManager am = (AudioManager) mContext
				.getSystemService(mContext.AUDIO_SERVICE);
		int audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		if ((value != audioCurrentVolumn) && (value <= audioMaxVolumn)) {
			am.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
		}

		time = System.currentTimeMillis();
		Log.i(TAG, "TIME 2:===============" + time);

		DEF_VOLUME = value;
		// showToast(mContext.getString(R.string.sound_volume) + ":" + value);
	}

	// TODO 获取当前Volume�?
	private int getVolume() {
		AudioManager am = (AudioManager) mContext
				.getSystemService(mContext.AUDIO_SERVICE);
		int audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		DEF_VOLUME = audioCurrentVolumn * 20 / audioMaxVolumn;
		Log.i(TAG, "MAX VOLUME=====" + audioMaxVolumn + "==CURRENT VOLUME=="
				+ audioCurrentVolumn + "==DEF_VOLUME===" + DEF_VOLUME);
		return DEF_VOLUME;
	}

	@Override
	public void onFocusChange(View v, boolean arg1) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_sound_1://录音棚
			RTSoundEffects rtSoundEffects1 = (RTSoundEffects)
			mContext.getSystemService("RTSoundEffects");
			rtSoundEffects1.setReverbMode(RTSoundEffects.REVERB_MODE_STUDIO);
			Log.i("song","sound mode studio======");
			break;
		case R.id.btn_sound_2://KTV
			RTSoundEffects rtSoundEffects2 = (RTSoundEffects)
			mContext.getSystemService("RTSoundEffects");
			rtSoundEffects2.setReverbMode(RTSoundEffects.REVERB_MODE_KTV);
			Log.i("song","sound mode ktv======");
			break;
		case R.id.btn_sound_3://演唱会
			RTSoundEffects rtSoundEffects3 = (RTSoundEffects)
			mContext.getSystemService("RTSoundEffects");
			rtSoundEffects3.setReverbMode(RTSoundEffects.REVERB_MODE_CONCERT);
			Log.i("song","sound mode concert======");
			break;

		default:
			break;
		}
	}
}
