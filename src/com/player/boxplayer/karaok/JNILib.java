package com.player.boxplayer.karaok;

import android.content.res.AssetManager;

public class JNILib 
{

    static 
    {
        System.loadLibrary("jlinkjni");
    }
    public static native void init(int width, int height);
    public static native int step(int x,int y,int z,int p);
    public static native int searchKey(String key);
    public static native int toAssetManager(AssetManager assetManager);
    public static native int setBoxSide();
    public static native int keyBack();
    public static native String qrGet();
    public static native int qrIsDisp();
    public static native String playNextSong(int play);
    public static native String getbgVideoPath();
    public static native String getHttpUrl();
    public static native int NoticeVolume(int volume);
    public static native int NoticeMute(int IsMute);
    public static native int NoticeVoice(int VoiceMode);
    public static native int NoticePause(int isPause);
    public static native int NoticeRefresh(int flag);
	public static native int setGlobalLanguage(int lag);
    public static native void PreviewSet(int flag);
    public static native String getScollMessage();
	public static native int getCurrentPlayVoice();
	public static native int getCurrentPIPMode();
	public static native int Array(int[] paramInt, int para);
    public static native int  PreviewGetPlayBar();
    public static native void PreviewSetPlayBar(int percentage, int flag);
    public static native int  PIPGetPlayBar();
    public static native void PIPSetPlayBar(int Duration, int CurrentPos);
    public static native void PIPEnableUi();
	public static native int getGlobalLanguage();
	public static native void CloseUart();
	public static native int setSN(String deviceinfo);
	public static native void setGlobalVersion(String GlobalVersion);
	public static native String getTvData(String key);
    public static native int getDefaultVolume();
    public static native int getDefaultStart();
	public static native int setCurrentNetstat(int lag);
    public static native String getBarrage();
    public static native String getCurrentInput();
    public static native int getPublicStatus();
    
    public static native int setScollMessage(String scroll);

	 public static native int BatchDeleteDetect();
    public static native int BatchDeleteSel(int mode);
    public static native String GetBatchDelsongStatus();
    
    public static native int BatchAddSongDetect();
    public static native int BatchAddSongSel(int mode);
    public static native String GetBatchAddsongStatus();
    
	public static native int HddUpdateDetect();
	public static native int HddUpdateSel(int mode);
	public static native String GetHddUpdatestatus();
	
	public static native int SetSmartDelFlag(int mode);
	
	
	public static native int SetScoreFlag(int mode);
	public static native String getScorePath();
	public static native String GetWechatTip(int mode);
	
	public static native int SetRecordUpload(String num,String id);
	public static native String GetRecordShareUrl(String num,String id); 
	
	public static native String getSongData(String cmd); 
	public static native int SelectSong(String cmd); 
	
	public static native String GetMountSMB();
	public static native String GetSystemCmd();            
	public static native String GetAppDownloadUrl(); 
	public static native String getSongDownPercent();
	public static native int GetBootStep();
    public static native String getSongListData(String ids); 
	
	
	
    public final static int JLINK_SPEECH_SEARCH = 1;
    public final static int JLINK_PREVIEW_MEDIA = 2;
    public final static int JLINK_PUSH_MEDIA = 3;
    public final static int JLINK_PLAY_MEDIA = 4;
    public final static int JLINK_QR_SCAN = 5;
    public final static int JLINK_KEY_BACK = 6;
    public final static int JLINK_DIALOG_INPUT = 102;
	public final static int JLINK_DIALOG_INPUT2 = 103;
	public final static int JLINK_DIALOG_INPUT3 = 104;
    public final static int JLINK_TIMER = 8;
    
    public final static int JLINK_VIDEO_SELECT = 100;
    public final static int JLINK_REFRESH_SUBTITLE = 101;
    
    
    public final static int JLINK_PLAY_VOICE  = 1000;
    public final static int JLINK_PLAY_PAUSE  = 1001;
    public final static int JLINK_PLAY_MUTE   = 1002;
    public final static int JLINK_PLAY_NEXT   = 1003;
    public final static int JLINK_PLAY_RPEATE = 1004;
    public final static int JLINK_PLAY_VOL_UP = 1005;
    public final static int JLINK_PLAY_VOL_DN = 1006;
    public final static int JLINK_PLAY_VOICE_YUANCHANG  = 1007;
    public final static int JLINK_PLAY_VOICE_BANCHANG   = 1008;
    public final static int JLINK_PLAY_UNMUTE   = 1009;

    public final static int JLINK_PLAY_GET_VOICE  = 1010;
    public final static int JLINK_PLAY_GET_PAUSE  = 1011;
    public final static int JLINK_PLAY_GET_MUTE   = 1012;
    public final static int JLINK_PLAY_GET_VOL    = 1013;
    
    public final static int JLINK_RETURN      = 1100;
    public final static int JLINK_PIP         = 1101;
    public final static int JLINK_HANDWRITE   = 1102;
    public final static int JLINK_PIP_FULLSCREEN  = 1103;
    public final static int JLINK_PIP_SMALLWIN    = 1104;
    public final static int JLINK_PREVIEW_SEEK    = 1105;
    public final static int JLINK_GET_PREVIEW_POSION    = 1106;
    public final static int JLINK_PIP_SEEK        = 1107;
    public final static int JLINK_PIP_GET_CURRENT_POS   = 1108;
    public final static int JLINK_PIP_SEEK_RIGHT        = 1109;
    public final static int JLINK_PIP_SEEK_LEFT         = 1110;
	
	public final static int JLINK_CLOSE_QF    = 1200;
    public final static int JLINK_Atmosphere  = 1201;
	public final static int JLINK_Site        = 1202;
	public final static int JLINK_SOUND       = 1203;
	public final static int JLINK_GRAFFITI    = 1204;
	public final static int JLINK_BLESS       = 1205;
    
    public final static int JLINK_ATMOSPHERE_HECAI   = 1211;
    public final static int JLINK_ATMOSPHERE_DAOCAI  = 1212;
    public final static int JLINK_ATMOSPHERE_HUANHU  = 1213;

    public final static int JLINK_SCENE_STOP        = 1220;
    public final static int JLINK_SCENE_HUIYI       = 1221;
    public final static int JLINK_SCENE_CHEZHAN     = 1222;
    public final static int JLINK_SCENE_MALU        = 1223;
    public final static int JLINK_SCENE_MAJIANG     = 1224;
    public final static int JLINK_SCENE_GONGDI      = 1225;

    public final static int JLINK_REC_START             = 1301;
    public final static int JLINK_REC_STOP              = 1302;
    public final static int JLINK_REC_PLAYBACK_START    = 1303;
    public final static int JLINK_REC_PLAYBACK_STOP     = 1304;

	public final static int JLINK_START_PLAY		 = 9993;
	public final static int JLINK_START_SCAN         = 9994; 
	public final static int JLINK_CREAT_CODE         = 9995;
	public final static int JLINK_START_MOIVE		 = 9996;
    public final static int JLINK_ENTER_PREVIEW      = 9997;
    public final static int JLINK_EXIT_PREVIEW       = 9998;
    public final static int JLINK_START_ANDROID      = 9999;
    /* enter of system setting*/
    public final static int JLINK_SYSTEM_SETTING = 8888;
	public final static int JLINK_SHUTDOWN = 505;
	public final static int JLINK_LANGUAGE = 506;
	
	public final static int JLINK_RECORD_START = 507;
	public final static int JLINK_RECORD_STOP= 508;
	public final static int JLINK_BARRAGE= 510;
	public int JLINK_AP_Index;
	
	public final static int JLINK_MOUNT_SERVER = 511;
	
	public final static int JLINK_WIFI_OPEN = 512;
	public final static int JLINK_TV_LOGO_STATUS = 513;
	public final static int JLINK_TV_MARQUEE_STATUS = 514;
	public final static int JLINK_SCORE_START=515;
	public final static int JLINK_SCORE_STOP=516;
	
	public final static int JLINK_SAMBA_CMD=665;//SAMBA mount命令
	public final static int JLINK_SYSTEM_CHMOD=666;
	

   

}
