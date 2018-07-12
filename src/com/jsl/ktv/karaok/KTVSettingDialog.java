package com.jsl.ktv.karaok;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.os.Handler;
import org.apache.http.util.EncodingUtils;

import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.karaok.VideoString;
import com.player.boxplayer.karaok.JNILib;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioButton;
import android.content.Context;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.graphics.Color;
import com.jsl.ktv.R;
import com.jsl.ktv.view.MyApplication;
public class KTVSettingDialog extends Dialog
{
    // 定义回调事件，用于dialog的点击事�?   
	private String name;
    private TextView open_start,audio_num,tvPass,tvDel,tvScore,tvVol,tvScroll,tvTitel,tvPMD,tvLogo;
	private RadioButton passOn,passOff,sgOn,sgOff,pfOn,pfOff,pmdOn,pmdOff,logoOn,logoOff;
    private EditText scoll_input;
	private String password = "";
	private String ps_length = "";
	private String acoll_ms = "";
	private String setting_ms = "";
    private String startup ="";
    private String exitapp ="";
    private String deleteSong = "";
	private String pmdState = "1";
	private String logoState = "1";
    private int volume;
    private Button audio_down,audio_up,open_start_l,open_start_r,setting_save;
    private String pw_info,read_audio,start_info;
    private String SCOLL_FILE = "/mnt/sdcard/jlink/sys_file/etc/message_scoll.txt";
    private String VOD_CONNFIG="/mnt/sdcard/jlink/sys_file/etc/vod_config.txt";
	private Context mContext;
	private RadioGroup rgPW,rgSG,rgPF,rgPMD,rgLogo;
	private boolean isFirst = true;
	private int jLanguage = 0;
	private Handler mHandler;
	private StringBuffer stringbuffer;
    public KTVSettingDialog(Context context, String name,Handler handle)
    {
        super(context);
        this.name = name;
		  mContext = context;
		  mHandler = handle;
    }
    
    
    public KTVSettingDialog(Context context,String name,Handler handle,int them){
    	super(context, R.style.MyDialog);
    	mContext = context;
		mHandler = handle;
        this.name = name;
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ktv_setting);
        //WindowManager.LayoutParams wmParams = this.getWindow().getAttributes();  
        //wmParams.format = PixelFormat.TRANSPARENT;  内容全�?�? 
        //wmParams.format = PixelFormat.TRANSLUCENT; 
       /* pw_switch = (Button) findViewById(R.id.pw_switch);
        sg_switch = (Button) findViewById(R.id.sg_switch);
		pf_switch = (Button) findViewById(R.id.pf_switch);*/
		
		jLanguage=JNILib.getGlobalLanguage();
		
		  passOn = (RadioButton)findViewById(R.id.pw_switch_on);
        passOff = (RadioButton)findViewById(R.id.pw_switch_off);
        sgOn = (RadioButton)findViewById(R.id.sg_switch_on);
        sgOff = (RadioButton)findViewById(R.id.sg_switch_off);
        pfOn = (RadioButton)findViewById(R.id.pf_switch_on);
        pfOff = (RadioButton)findViewById(R.id.pf_switch_off);
		pmdOn = (RadioButton)findViewById(R.id.pmd_switch_on);
		pmdOff = (RadioButton)findViewById(R.id.pmd_switch_off);
	    logoOff = (RadioButton)findViewById(R.id.logo_switch_off);
		logoOn = (RadioButton)findViewById(R.id.logo_switch_on);
		 rgPF = (RadioGroup)findViewById(R.id.rg_pf);
        rgSG = (RadioGroup)findViewById(R.id.rg_sg);
        rgPW = (RadioGroup)findViewById(R.id.rg_pw);
		rgPMD = (RadioGroup)findViewById(R.id.rg_pmd);
        rgLogo = (RadioGroup)findViewById(R.id.rg_logo);
		
        audio_down = (Button) findViewById(R.id.audio_down);
        audio_up = (Button) findViewById(R.id.audio_up);
        open_start_l = (Button) findViewById(R.id.open_start_l);
        open_start_r = (Button) findViewById(R.id.open_start_r);
        setting_save = (Button) findViewById(R.id.setting_save);
        scoll_input = (EditText) findViewById(R.id.scoll_input);
		audio_num = (TextView)findViewById(R.id.audio_num);
		open_start = (TextView)findViewById(R.id.open_start);
		
		tvTitel = (TextView)findViewById(R.id.setting_tittle);
		tvPass = (TextView)findViewById(R.id.pw_switch_tx);
		tvDel = (TextView)findViewById(R.id.sg_switch_tx);
		tvScore = (TextView)findViewById(R.id.pf_switch_tx);
		tvScroll = (TextView)findViewById(R.id.message_scoll);
		tvVol = (TextView)findViewById(R.id.boot_audio_tx);
		tvPMD = (TextView)findViewById(R.id.pmd_switch_tx);
		tvLogo = (TextView)findViewById(R.id.logo_switch_tx);
		
		tvTitel.setText(VideoString.setting_tittle[jLanguage]);
		tvPass.setText(VideoString.pw_switch[jLanguage]);
		tvDel.setText(VideoString.sg_switch[jLanguage]);
		tvScore.setText(VideoString.pf_switch[jLanguage]);
		tvScroll.setText(VideoString.message_scoll[jLanguage]);
		tvPMD.setText(VideoString.pmd_switch[jLanguage]);
		tvLogo.setText(VideoString.logo_switch[jLanguage]);
		tvVol.setText(VideoString.boot_audio[jLanguage]);
		setting_save.setText(VideoString.setting_save[jLanguage]);
		
		passOn.setText(VideoString.pw_on[jLanguage]);
		passOff.setText(VideoString.pw_off[jLanguage]);
		sgOn.setText(VideoString.pw_on[jLanguage]);
		sgOff.setText(VideoString.pw_off[jLanguage]);
		pfOn.setText(VideoString.pw_on[jLanguage]);
		pfOff.setText(VideoString.pw_off[jLanguage]);
		
		
		setting_save.requestFocus();
			
		passOn.setOnFocusChangeListener(mFocusListener);
		passOff.setOnFocusChangeListener(mFocusListener);
		sgOn.setOnFocusChangeListener(mFocusListener);
		sgOff.setOnFocusChangeListener(mFocusListener);
		pfOff.setOnFocusChangeListener(mFocusListener);
		pfOn.setOnFocusChangeListener(mFocusListener);
		pmdOn.setOnFocusChangeListener(mFocusListener);
		pmdOff.setOnFocusChangeListener(mFocusListener);
		logoOff.setOnFocusChangeListener(mFocusListener);
		logoOn.setOnFocusChangeListener(mFocusListener);
		
		rgPF.setOnCheckedChangeListener(mCheckListener);
		rgPW.setOnCheckedChangeListener(mCheckListener);
		rgSG.setOnCheckedChangeListener(mCheckListener);
		rgPMD.setOnCheckedChangeListener(mCheckListener);
		rgLogo.setOnCheckedChangeListener(mCheckListener);
		
        audio_down.setOnClickListener(clickListener);
        audio_up.setOnClickListener(clickListener);
        open_start_l.setOnClickListener(clickListener);
        open_start_r.setOnClickListener(clickListener);
        setting_save.setOnClickListener(clickListener);
		
		 passOn.setOnClickListener(clickListener);
        passOff.setOnClickListener(clickListener);
        sgOn.setOnClickListener(clickListener);
        sgOff.setOnClickListener(clickListener);
        pfOff.setOnClickListener(clickListener);
        pfOn.setOnClickListener(clickListener);
		pmdOn.setOnClickListener(clickListener);
		pmdOff.setOnClickListener(clickListener);
		logoOn.setOnClickListener(clickListener);
		logoOff.setOnClickListener(clickListener);
		
        //etName.addTextChangedListener(textWatcher);
		
		scoll_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1)
					scoll_input.setSelection(scoll_input.getText().toString().length());
			}
		});
		
		 int state = JNILib.SetSmartDelFlag(0);
		  if(state==0){
			//sg_switch.setText(R.string.pw_off);
			 sgOff.setChecked(true);
			}else{
			//sg_switch.setText(R.string.pw_on);
				sgOn.setChecked(true);
			}
		//Log.v("setting_ms","sg_switch======查询=="+"state=="+state);
			
			
			 int state1 = JNILib.SetScoreFlag(0);
			 
		 if(state1==0){
		//	pf_switch.setText(R.string.pw_off);
			 pfOff.setChecked(true);
			}else{
		//	pf_switch.setText(R.string.pw_on);
				pfOn.setChecked(true);
			}
		//	Log.v("setting_ms","pf_switch======查询=="+"state=="+state1);
		
        if(isFileExist(SCOLL_FILE)){
            acoll_ms = readFileSdcard(SCOLL_FILE);
            }else{
				
            acoll_ms = VideoString.scroll_default[jLanguage];
            }
        scoll_input.setText(acoll_ms);
		
        if(isFileExist(VOD_CONNFIG)){
            setting_ms = readFileSdcard(VOD_CONNFIG);
			stringbuffer = new StringBuffer(setting_ms);
            startup = setting_ms.substring(setting_ms.indexOf("startup = ")+10,setting_ms.indexOf("startup = ")+11);
			exitapp = setting_ms.substring(setting_ms.indexOf("exitapp = ")+10,setting_ms.indexOf("exitapp = ")+11);
			if(setting_ms.contains("scrolling"))
			pmdState = setting_ms.substring(setting_ms.indexOf("scrolling = ")+12,setting_ms.indexOf("scrolling = ")+13);
			if(setting_ms.contains("tv_logo"))
			logoState = setting_ms.substring(setting_ms.indexOf("tv_logo = ")+10,setting_ms.indexOf("tv_logo = ")+11);	
		//	deleteSong = setting_ms.substring(setting_ms.indexOf("delete_song = ")+14,setting_ms.indexOf("delete_song = ")+15);
			String volume_s = setting_ms.substring(setting_ms.indexOf("volume = ")+9,setting_ms.indexOf("startup = ")-1);
			try {
				volume  = Integer.parseInt(volume_s);
			} catch (Exception e) {
				// TODO: handle exception
				volume = -1;
			}
			//Log.v("setting_ms","startup======"+startup);
			Log.v("setting_ms","exitapp======"+exitapp);
			//Log.v("setting_ms","volume======"+volume_s);
			if(exitapp.equals("0")){
				//pw_switch.setText(R.string.pw_off);
				passOff.setChecked(true);
				passOff.requestFocus();
				Log.v("setting_ms","exitapp======");
				}else{
				//pw_switch.setText(R.string.pw_on);
					passOn.setChecked(true);
					passOn.requestFocus();
				}
				
				
			if(pmdState.equals("1")){
				pmdOn.setChecked(true);
				pmdOn.requestFocus();
			}else{
				pmdOff.setChecked(true);
				pmdOff.requestFocus();
			}
				
			if(logoState.equals("1")){
				logoOn.setChecked(true);
				logoOn.requestFocus();
			}else{
				logoOff.setChecked(true);
				logoOff.requestFocus();
			}
				//	Log.v("setting_ms","pw_switch======查询=="+"state=="+exitapp);
			if(volume!=-1)	{
				audio_num.setText("("+volume+")");
			}else{
				audio_num.setText("("+VideoString.vul_mr[jLanguage]+")");
			}
			if(startup.equals("2")){
				open_start.setText(R.string.move);
			}else if(startup.equals("1")){
				open_start.setText(R.string.android);
			}else{
				open_start.setText(R.string.ktv);
			}
		}else{
		setting_ms = "null";
		}
		Log.v("setting_ms","======"+setting_ms);	
    }

    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
			
			switch(v.getId()){
		/*		case R.id.pw_switch_on:
			//	Log.v("pw_switch","pw_switch======"+pw_switch.getText());
					/*if(exitapp.equals("0")){
						pw_switch.setText(R.string.pw_on);	
						exitapp = "1";
					}else{
						pw_switch.setText(R.string.pw_off);	
						exitapp = "0";
					}*/
			/*		exitapp = "1";
					break;
				case R.id.pw_switch_off:
				    exitapp = "0";
				    break;
				case R.id.sg_switch_on:
				case R.id.sg_switch_off:
					
					 int state = JNILib.SetSmartDelFlag(1);
						if(state==0){
							//sg_switch.setText(R.string.pw_off);	
							sgOff.requestFocus();
						}else{
							//sg_switch.setText(R.string.pw_on);	
							sgOn.requestFocus();
						}
					//	Log.v("setting_ms","sg_switch======"+pw_switch.getText()+"state=="+state);
						break;
						case R.id.pf_switch_on:
						case R.id.pf_switch_off:
					
					 int state1 = JNILib.SetScoreFlag(1);
						if(state1==0){
						//	pf_switch.setText(R.string.pw_off);	
						pfOff.requestFocus();
						}else{
						///	pf_switch.setText(R.string.pw_on);	
						pfOn.requestFocus();
						}
					//	Log.v("setting_ms","pf_switch======"+pf_switch.getText()+"state=="+state1);
						break;*/
				 case R.id.pf_switch_off:
					switchFocusByClick(pfOff,pfOn);
			    	break;
			    case R.id.pf_switch_on:
			    	switchFocusByClick(pfOn,pfOff);
			    	break;
			    case R.id.sg_switch_off:
			    	switchFocusByClick(sgOff,sgOn);
			    	break;
			    case R.id.sg_switch_on:
			    	switchFocusByClick(sgOn,sgOff);
			    	break;
			    case R.id.pw_switch_off:
			    	switchFocusByClick(passOff,passOn);
			    	break;
			    case R.id.pw_switch_on:
			    	switchFocusByClick(pfOn,pfOff);
			    	break;
				case R.id.pmd_switch_off:
				    switchFocusByClick(pmdOff,pmdOn);
					break;
				case R.id.pmd_switch_on:
				    switchFocusByClick(pmdOn,pmdOff);
					break;
				case R.id.logo_switch_off:
				    switchFocusByClick(logoOff,logoOn);
					break;
				case R.id.logo_switch_on:
				    switchFocusByClick(logoOn,logoOff);
					break;
			    	
				case R.id.audio_down:
					if(volume == -1){
						volume = 100;
						audio_num.setText("("+volume+")");
					}else if(volume == 0){
						volume = -1;
						audio_num.setText("("+VideoString.vul_mr[jLanguage]+")");
					}else{
						volume -= 5;
						audio_num.setText("("+volume+")");
					}
					break;

				case R.id.audio_up:
					if(volume == -1){
						volume = 0;
						audio_num.setText("("+volume+")");
					}else if(volume == 100){
						volume = -1;
						audio_num.setText("("+VideoString.vul_mr[jLanguage]+")");
					}else{
						volume += 5;
						audio_num.setText("("+volume+")");
					}
					break;
					
				case R.id.open_start_r:
			//	Log.v("pw_switch","pw_switch======"+pw_switch.getText());
					if(startup.equals("0")){
						open_start.setText(R.string.android);	
						startup = "1";
					}else if (startup.equals("1")){
						open_start.setText(R.string.move);	
						startup = "2";
					}else{
						open_start.setText(R.string.ktv);	
						startup = "0";
					}
					break;
					
				case R.id.open_start_l:
			//	Log.v("pw_switch","pw_switch======"+pw_switch.getText());
					if(startup.equals("0")){
						open_start.setText(R.string.move);	
						startup = "2";
					}else if (startup.equals("1")){
						open_start.setText(R.string.ktv);	
						startup = "0";
					}else{
						open_start.setText(R.string.android);	
						startup = "1";
					}
					break;
					
				case R.id.setting_save:
					save_ms();
				default:
					break;
				
			}
        }
    };
    
	
	
	private void switchFocusByClick(RadioButton rb1,RadioButton rb2){
		    rb1.setChecked(true);
		 	rb1.setBackgroundResource(R.drawable.set_check_focus);
			rb1.setTextColor(Color.parseColor("#ffffff"));
		   	rb2.setBackgroundResource(R.drawable.guan);
     		rb2.setTextColor(Color.parseColor("#666666"));
	}
	  
    private RadioGroup.OnCheckedChangeListener mCheckListener = new RadioGroup.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
		
			// TODO Auto-generated method stub
		
			switch (arg1) {
			case R.id.pw_switch_on:
			//	Log.v("pw_switch","pw_switch======"+pw_switch.getText());
					/*if(exitapp.equals("0")){
						pw_switch.setText(R.string.pw_on);	
						exitapp = "1";
					}else{
						pw_switch.setText(R.string.pw_off);	
						exitapp = "0";
					}*/
					exitapp = "1";
					///	Log.v("setting_ms","pw_switch===setting===state=="+exitapp);
					break;
				case R.id.pw_switch_off:
				    exitapp = "0";
					//	Log.v("setting_ms","pw_switch===setting11111111===state=="+exitapp);
				    break;
				case R.id.sg_switch_on:
				case R.id.sg_switch_off:
					if(!isFirst){
					 int state = JNILib.SetSmartDelFlag(1);	
			//		 Log.v("setting_ms","sg_switch===setting===state=="+state);
					}
					
					/*	if(state==0){
							//sg_switch.setText(R.string.pw_off);	
							sgOff.setChecked(true);
						}else{
							//sg_switch.setText(R.string.pw_on);	
							sgOn.setChecked(true);
						}*/
						
						break;
				case R.id.pf_switch_on:
				case R.id.pf_switch_off:
					if(!isFirst){
					 int state1 = JNILib.SetScoreFlag(1);	
				//	 Log.v("setting_ms","pf_switch==setting====state=="+state1);
					}
					
					/*	if(state1==0){
						//	pf_switch.setText(R.string.pw_off);	
						pfOff.setChecked(true);
						}else{
						///	pf_switch.setText(R.string.pw_on);	
						pfOn.setChecked(true);
						}*/
						
					break;
				 case R.id.pmd_switch_off:
					pmdState = "0";
					MyApplication.scrolling_switch = "0";
					mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SCROLLING);
					if(setting_ms.contains("scrolling"))
				          stringbuffer.replace(setting_ms.indexOf("scrolling = ")+12,setting_ms.indexOf("scrolling = ")+13, pmdState);
			        else
			              stringbuffer.append("\n scrolling = "+pmdState);
    		            writeFileSdcard(VOD_CONNFIG,stringbuffer.toString());
				    break;
				 case R.id.pmd_switch_on:
					pmdState = "1";
					MyApplication.scrolling_switch = "1";
					mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SCROLLING);
					if(setting_ms.contains("scrolling"))
				          stringbuffer.replace(setting_ms.indexOf("scrolling = ")+12,setting_ms.indexOf("scrolling = ")+13, pmdState);
			        else
			              stringbuffer.append("\n scrolling = "+pmdState);
    		            writeFileSdcard(VOD_CONNFIG,stringbuffer.toString());
				    break;
				case R.id.logo_switch_off:
					logoState = "0";
					MyApplication.logo_switch = "0";
					mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_LOGO);
					if(setting_ms.contains("tv_logo"))
				          stringbuffer.replace(setting_ms.indexOf("tv_logo = ")+10,setting_ms.indexOf("tv_logo = ")+11, logoState);
			        else
			              stringbuffer.append("\n tv_logo = "+logoState);
    		            writeFileSdcard(VOD_CONNFIG,stringbuffer.toString());
				    break;
				 case R.id.logo_switch_on:
					logoState = "1";
					MyApplication.logo_switch = "1";
					mHandler.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_LOGO);
					if(setting_ms.contains("tv_logo "))
				          stringbuffer.replace(setting_ms.indexOf("tv_logo = ")+10,setting_ms.indexOf("tv_logo = ")+11, logoState);
			        else
			              stringbuffer.append("\n tv_logo = "+logoState);
    		            writeFileSdcard(VOD_CONNFIG,stringbuffer.toString());
				    break;


			default:
				break;
			}
		}
		
	};
    
    
		
	private View.OnFocusChangeListener mFocusListener  = new View.OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub
			RadioButton view = (RadioButton)arg0;
					if(arg1){
				isFirst = false;		
				if(view.isChecked()){
					view.setBackgroundResource(R.drawable.set_check_focus);
					view.setTextColor(Color.parseColor("#ffffff"));
				}else{
					view.setBackgroundResource(R.drawable.set_normal_focus);
					view.setTextColor(Color.parseColor("#666666"));
				}
				
			}else{
				if(view.isChecked()){
					view.setBackgroundResource(R.drawable.kai);
					view.setTextColor(Color.parseColor("#ffffff"));
				}else{
					view.setBackgroundResource(R.drawable.guan);
					view.setTextColor(Color.parseColor("#666666"));
				}
			}
			
			
		}
	};
	
	
	
	
    @Override
    public void dismiss() {
        super.dismiss();
    }
    
    private void save_ms(){
    	//writeFileSdcard(SCOLL_FILE,scoll_input.getText().toString());
		
		String scroll = scoll_input.getText().toString();
    	if(TextUtils.isEmpty(scroll)){
    		Toast.makeText(mContext, VideoString.scroll_tip[jLanguage],Toast.LENGTH_LONG).show();
    		JNILib.setScollMessage(VideoString.scroll_default[jLanguage]);	
    	}else{
    		JNILib.setScollMessage(scroll);	
    	}
    	
    	if(!TextUtils.isEmpty(setting_ms)){
    		stringbuffer.replace(setting_ms.indexOf("startup = ")+10,setting_ms.indexOf("startup = ")+11, startup);
    		stringbuffer.replace(setting_ms.indexOf("exitapp = ")+10,setting_ms.indexOf("exitapp = ")+11, exitapp);
			//	stringbuffer.replace(setting_ms.indexOf("delete_song = ")+14,setting_ms.indexOf("delete_song = ")+15, deleteSong);
    		stringbuffer.replace(setting_ms.indexOf("volume = ")+9,setting_ms.indexOf("startup = ")-1, Integer.toString(volume));
		/*	if(setting_ms.contains("scrolling"))
				stringbuffer.replace(setting_ms.indexOf("scrolling = ")+12,setting_ms.indexOf("scrolling = ")+13, pmdState);
			else
			    stringbuffer.append("\n scrolling = "+pmdState);*/
			
    		//Log.v("setting_ms","setting_ms======"+stringbuffer.toString());
    		writeFileSdcard(VOD_CONNFIG,stringbuffer.toString());
    	}
    	dismiss();
    }
    
  //判断文件夹是否存�?  存在 true,  不存�?false
  	private boolean isFileExist(String strFolder) {
  		File file = new File(strFolder);
  		if (file.exists()) {
  			return true;
  		}
  		return false;
  	}
    
    public String readFileSdcard(String fileName) {
        String res = ""; 
        try {  

            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available(); 
            byte[] buffer = new byte[length]; 
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close(); 
        }  

        catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return res;  

    } 
    

    public void writeFileSdcard(String fileName, String message) {  
  	  
        try { 
            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE); 
            FileOutputStream fout = new FileOutputStream(fileName); 
            byte[] bytes = message.getBytes(); 
            fout.write(bytes); 
            fout.close(); 
           
        }  
        catch (Exception e) { 
            e.printStackTrace();
           
        }  

    } 
    
    
    /*private TextWatcher textWatcher = new TextWatcher()
    {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count)
        {
            //System.out.println("-1-onTextChanged-->"
            //        + et_search.getText().toString() + "<--");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after)
        {
            //System.out.println("-2-beforeTextChanged-->"
            //        + et_search.getText().toString() + "<--");
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            //这种方式是每输入�?��字符就往下传，动态改�?            JNILib.searchKey(s.toString());
        }
    };*/
}