package com.jsl.ktv.karaok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.player.boxplayer.karaok.JNILib;
import org.apache.http.util.EncodingUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jsl.ktv.R;
import com.jsl.ktv.R.style;
import com.jsl.ktv.audio.Sound_effect_dialog;
import com.jsl.ktv.audio.Sound_effect_dialog.OnSetMicListener;
import com.jsl.ktv.audio.Sound_effect_dialog.OnsetSoundListener;
import com.jsl.ktv.bean.SingerSearchBean;
import com.jsl.ktv.bean.SongSearchBean;
import com.jsl.ktv.constant.FragmentMessageConstant;
import com.jsl.ktv.fragment.HomeFragment;
import com.jsl.ktv.fragment.My_Fragment;
import com.jsl.ktv.fragment.PH_Fragment;
import com.jsl.ktv.fragment.Quz_Fragment;
import com.jsl.ktv.fragment.RankingFragment;
import com.jsl.ktv.fragment.RecommendFragment;
import com.jsl.ktv.fragment.RecommendSongNameFragment;
import com.jsl.ktv.fragment.ShareRecordFragment;
import com.jsl.ktv.fragment.SingerFragment;
import com.jsl.ktv.fragment.SongNameFragment;
import com.jsl.ktv.fragment.Wuqu_Fragment;
import com.jsl.ktv.gifview.GifView;
import com.jsl.ktv.karaok.CommonDialog.OnCallback;

import com.jsl.ktv.util.CreateSpecifiedSizeFileUtil;

import com.jsl.ktv.util.CreateSpecifiedSizeFileUtil.FileUnit;
import com.jsl.ktv.util.HiMediaPlayerInvoke;
import com.jsl.ktv.util.SongJsonParseUtils;
import com.jsl.ktv.view.AnimationButton;
import com.jsl.ktv.view.MarqueeTextView;
import com.jsl.ktv.view.MyApplication;
import com.jsl.ktv.view.SoftInputSearchView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
/*import com.hisilicon.android.hiaudiomanager.HiAudioManager;
 import com.hisilicon.android.hidisplaymanager.HiDisplayManager;
 import com.hisilicon.android.hidisplaymanager.DispFmt;
 import com.hisilicon.android.hisysmanager.HiSysManager;
 import android.os.SystemProperties;*/

import com.cmcc.media.*;

public class MainActivity extends Activity {
	// Jni jni;
	private boolean isStartAndroid = false;
	private String UInumber = "jlink_0015|";
	private boolean isFirstRun = true;
	private final String TAG = "jlink";
	// private HiAudioManager mAudioManager = null;
	// private HiDisplayManager mDisplayManager = null;
	private HisiVideoView PreviewVideo;
	// private VideoPresentation presentation;
	// private ImagePresentation imagePres;
	private Display hdmiDisplay;
	private Display cvbsDisplay;
	private String musicPath;
	private MediaPlayer mediaPlayerBackMusic = null;
	private boolean isBackMusicPrepare = true;
	// private GL2JNIView mGlView = null;
	private String CurrentPlayPath = "";
	private String vgaVideoPath = "";
	private String bgVideoPath = "";
	private String tmpVideoPath = "";
	private String startVideoPath = "";// "/mnt/sdcard/jlink/video/start.mpg";
	private String SongPathRoot = "/mnt/sdcard/jlink/link2src/";// "/mnt/sdcard/jlink/video/start.mpg";
	private int voice_mode = 0;
	private int vlume_now = 0;
	private int vlume_set = 0;
	private boolean isMute = false;
	private boolean isPause = false;
	private int isPlayScene = 0;
	private int isPlayPreview = 0;
	private int PIPWinMode = 1;
	private int mPreviewProgressThreadRun = 0;
	private int mPreviewPosCur = 0;
	private int misRecord = 0;
	// private String filepath;
	private String ScollMessage;
	private ProgressDialog dialog1;
	// private HWIMEInterface mIme;
	private int tem = 0;
	private MyVolumeReceiver mVolumeReceiver;
	private NetStatReceiver netStatReceiver;
	private BroadcastReceiver receiver = null;
	private boolean deviceinforead = true;
	private JSONObject barjObject;
	static final String PACKAGE_SKYPLAY = "com.abc.airplay.player";
	private final String FILE_NAME = "HiContraLauncher.apk";
	private final String LIB_NAME = "libjlinkjni.so";
	private String deviceinfo = null;
	private final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getPath();
	private int color[] = new int[] { R.color.white, R.color.red,
			R.color.orange, R.color.yellow, R.color.green, R.color.lan,
			R.color.blue, R.color.zise, R.color.black };

	public final static int JLINK_VOICE_MOD_YUANCHANG = 0;
	public final static int JLINK_VOICE_MOD_BANCHANG = 1;
	public final static int DISPLAY_REALTIME_SCORE = 2222;
	public static int g_Voice_Value = 0;
	public MediaPlayer mMediaPlayer = null;
	private MediaPlayer mediaPlayerMusic = null;
	private int sndType = 0;
	private int isRepeat = 0;
	private boolean isMusic = false;
	private boolean isPlay = false;
	private int mMusicDuration_s = 0;
	private int mPIPPlayPosCur_s = 0;
	private boolean isPrepared = false;
	private int mVoiceMode = 0;
	private int mTone = 0;
	private String mVideoPath;
	private String mAudioPath;
	private boolean isPrepared_music = false;
	private View mediaControllerLayout;
	private LinearLayout btnLinearLayout;
	private View frameLayout_gif;
	private GifView gf1, gf2, gf3, gf4, gf5, gf6, gf7, gf8, gf9, gf10, gf11,
			gf12, gf13, gf14, gf15, gf16, gf17, gf18, gf19;
	private DanmakuView mDanmakuView;
	private MySeekBar videoSeekBar;
	private Button app_button, chongchang_button, luyin_button, qiege_button,
			play_pause, banchang_button, yidian_button, diange_button,
			gongju_button, sound_button;
	private TextView timeText, timetotal;
	private TextView app_button_text, chongchang_button_text,
			qiege_button_text, play_pause_text, banchang_button_text,
			yidian_button_text, diange_button_text;
	private JSONArray myJsonArray;
	private JSONObject myjObject, cmdjObject, listjObject, samba_obj;
	private String cmd = "{\"head\":\"jlink\",\"cmd\":1,\"key\":0,\"page_max\":6}";
	private boolean SeekBarshow = false;
	private boolean isSeekBarSelected = false;
	private boolean OSDshow = false;
	private boolean OSDinit = false;
	private int jLanguage = 0;
	private int defaultFocus = 6;
	private int OSDPlayTime = 0;
	private MyTextView subtitleView;
	static final String TOUCH_FILE_NAME = "/mnt/link2src/sys_file/data/message.txt";
	private String appDownAddress = "http://www.magimore.com/soft/download";
	private String qrCode = "";
	private String weixinCode = "";
	private LinearLayout mMainNoticeLayout;

	private ImageView mNoticeImage = null;
	private TextView mNoticeText;
	private LinearLayout mLoadingtxtLayout;
	private TextView mLoading1Text;
	private TextView mLoading2Text;
	private ImageView mqrImage = null;
	private ImageView mappAddressImage = null;
	private ImageView mWinxinLinkImage = null;
	private TextView logoImage = null;
	private TextView mqrinfotext;
	private TextView mqrtext;
	private TextView mappAddrtext;
	private TextView mwxinfotext;

	private StrokeTextView recordText;
	// private StrokeTextView publictext;
	private StrokeTextView tishiText;

	private LineView mLineView;
	private TextView ScorePicView;
	private UdpMessageTool mUdpMessageTool;
	private double oldScore = 0.0;

	private ListView listView, listView_key, listView_type, listView_sort,
			listView_main, listView_songlist;
	private ViewHolder holder = null;
	private TopViewHolder holder2 = null;
	private LastViewHolder holder3 = null;
	private ViewHolder_main holder_main = null;
	private TopViewHolder_main holder_main2 = null;
	private LastViewHolder_main holder_main3 = null;
	private ViewHolder_type holder_type = null;
	private TopViewHolder_type holder_type2 = null;
	private LastViewHolder_type holder_type3 = null;
	private ViewHolder_sort holder_sort = null;
	private TopViewHolder_sort holder_sort2 = null;
	private LastViewHolder_sort holder_sort3 = null;
	private KeyViewHolder holder_key = null;
	private KeyViewHolder_Top holder_key2 = null;

	private MyAdapter_main adapter_main;
	private MyAdapter_sort adapter_sort;
	private MyAdapter_type adapter_type;
	private MyAdapter_key adapter_key;
	private MyAdapter adapter;
	private MyAdapter_songlist1 adapter_songlist1;
	private String TV_type = "";
	private String TV_page = "";
	private String KEY_type = "";
	private int button1_type = 20;
	private int button2_type = 0;
	private int button3_type = 10;
	private String title[] = new String[] { "", "", "", "", "", "" };
	private String title_name[] = new String[] { "", "", "", "", "", "" };
	private String title_num[] = new String[] { "", "", "", "", "", "" };
	private String record_id[] = new String[] { "", "", "", "", "", "" };
	private int record_upload[] = new int[6];
	private int record_time[] = new int[6];
	private String info[] = new String[] { "", "", "", "", "", "" };
	private String songNumber[] = new String[] { "", "", "", "", "", "" };
	private boolean isCloud[] = new boolean[]{false,false,false,false,false,false};
	private String main_info1[] = new String[4];
	private String main_info2[] = new String[4];
	private String key_slist1[] = new String[] { "A", "E", "I", "M", "Q", "U",
			"Y" };
	private String key_slist2[] = new String[] { "B", "F", "J", "N", "R", "V",
			"Z" };
	private String key_slist3[] = new String[] { "C", "G", "K", "O", "S", "W",
			"删" };
	private String key_slist4[] = new String[] { "D", "H", "L", "P", "T", "X",
			"清" };

	private int tipIndex, sayIndex;

	private int main_img1[] = new int[] { R.drawable.geming,
			R.drawable.paihang, R.drawable.gaoqing, R.drawable.yidian };

	private int main_img2[] = new int[] { R.drawable.gexing, R.drawable.wuqu,
			R.drawable.xinge, R.drawable.fenlei };
	private int key_list1[] = new int[] { R.drawable.key_a, R.drawable.key_e,
			R.drawable.key_i, R.drawable.key_m, R.drawable.key_q,
			R.drawable.key_u, R.drawable.key_y };
	private int key_list2[] = new int[] { R.drawable.key_b, R.drawable.key_f,
			R.drawable.key_j, R.drawable.key_n, R.drawable.key_r,
			R.drawable.key_v, R.drawable.key_z };
	private int key_list3[] = new int[] { R.drawable.key_c, R.drawable.key_g,
			R.drawable.key_k, R.drawable.key_o, R.drawable.key_s,
			R.drawable.key_w, R.drawable.key_tong };
	private int key_list4[] = new int[] { R.drawable.key_d, R.drawable.key_h,
			R.drawable.key_l, R.drawable.key_p, R.drawable.key_t,
			R.drawable.key_x, R.drawable.key_shan };
	private int NoticePicPlayTime = 0;
	public final static int JLINK_NOTICE_TYPE_PAUSE = 201;
	public final static int JLINK_NOTICE_TYPE_UNPAUSE = 202;
	public final static int JLINK_NOTICE_TYPE_MUTE = 301;
	public final static int JLINK_NOTICE_TYPE_UNMUTE = 302;
	public final static int JLINK_NOTICE_TYPE_VOLUME = 303;
	public final static int JLINK_NOTICE_TYPE_QIFEN = 101;
	public final static int JLINK_NOTICE_TYPE_YUANCHANG = 103;
	public final static int JLINK_NOTICE_TYPE_BANCHANG = 104;
	public final static int JLINK_NOTICE_TYPE_NEXT = 105;
	public final static int JLINK_NOTICE_TYPE_REPEAT = 106;
	public final static int JLINK_NOTICE_TYPE_RECORD_START = 107;
	public final static int JLINK_NOTICE_TYPE_RECORD_STOP = 108;
	public final static int MESSAGE_REFRESH_NOTICE_IMAGE = 1;
	public final static int MESSAGE_UPDATE_QR_IMAGE = 2;
	public final static int MESSAGE_GET_PLAYER_POSION = 3;
	public final static int MESSAGE_UPDATE_GIF_IMAGE = 4;
	public final static int MESSAGE_UPDATE_OSD = 5;
	public final static int MESSAGE_UPDATE_BAR = 6;
	private int NoticeThreadRun = 0;
	private int NoticeState_bPause = 0;
	private int NoticeState_bMute = 0;
	private int mQrPlayTime = 0;
	private int gifPlayTime = 0;
	private boolean gifShow = false;
	private boolean isPlayNext = true;
	private boolean isYidianListShow = true;
	private MoreOptionDialogOfVod mOptionDialog;
	private KTVToolDialog tool_dialog;
	private int realtimeScore;
	private SingScoreDialog scoreDialog;
	private String songName, singer, picPath;// 评分歌曲信息

	private Toast toast = null;
	private int enterType;
	private boolean isScore = false;
	private boolean isFirstScore = true;
	private boolean isRecordMusic = false;
	private boolean isOnScore = false;
	private int mHdmi_index = 0;
	private int mSpdif_index = 0;
	private boolean VideoViewFullScreen = false;
	private Fragment fg;

	private SoftInputSearchView searchView;
	public SongNameFragment songFragment;
	public SingerFragment singerFragment;
	public RecommendSongNameFragment recommendSongNameFragment;
	private Button btnPhone, wifiBotton, btnSearch, btnDownload;

	private Share_Recoder_Dialog shareDialog;
	private String searchKey = "";
	private String song_no = "";
	private AnimationButton button_pip;
	private float animationX = 1.0f;
	private float animationY = 1.0f;
	private ImageView animationBkImage, empty_view_wx;
	private TextView tvEmpty;
	private LinearLayout empty_layout;
	private ArrayList<SongSearchBean> songs;

	private boolean isSingerSong = false;

	private String smb_address, smb_workpath, smb_mountpoint, smb_user,
			smb_password;

	private int seekBarChangeProgress;
	private boolean isSeekbarTraking = false;
	private SataBroadcastReceiver sataReceiver;
	private MyMICplugReceiver mMICReceiver;
	private Sound_effect_dialog soundDialog;
	private String pictureCachePath = "/mnt/sdcard/jlink/picture";
	private int currentDownloadProgress,oldDownProgress;
	private FrameLayout downloadProgressBar;
	private TextView tvDownloadProgress;
	private ImageView ivAnimation;
	private String downloadJson = "";
	private TextView tvCurrentSinger, tvCurrentSong,tvCurrentSongNum;
	private boolean isUsbMediaConnect = false;
	private boolean isUsbMicConnect = false;
	private static final int KARAOKE_USB_MIC_CONNECT = 255;
	private static final int KARAOKE_USB_MIC_DISCONNECT = 256;
	private String[] order = { "input", "tap", "1300", "600" };
	private boolean isOnDownLoad = false;
	private ImageView ivMusicbg;
	private int[] page = new int[1];
	private int[] currentpage = new int[1];
	private boolean pipPageUp = false;
	private boolean pipPageDown = false;
	private FrameLayout llNextPlay;
	private TextView tvNowPlaying,tvNextPlay;
	private boolean isFromOutSideSong = false;
	private boolean isFromOutSideSinger = false;
	private boolean isFromOutSideOrder = false;
	private String outSideSinger,outSideSong;
	private FromOutsideInitReceiver outSideReceiver;
	private boolean isSelectFocus = false;
	private Handler fragmentHander = new Handler() {
		public void handleMessage(Message msg) {
			// Log.i("song","msg.what =="+msg.what);
			switch (msg.what) {
			case FragmentMessageConstant.FRAGMENT_MESSAGE_PLAY_DEFAULT_VIDEO:
				preLoading.setVisibility(View.VISIBLE);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_INPUT_FINISH:
				String input = (String) msg.obj;
				// Log.i("song","input result =="+input);
				Message msgs = Message.obtain();
				msgs.what = FragmentMessageConstant.FRAGMENT_MESSAGE_SEARCH_SONG;
				msgs.obj = input;
				msgs.arg1 = msg.arg1;
				msgs.arg2 = msg.arg2;
				fragmentHander.sendMessage(msgs);
				sendBroadcast(new Intent("justlink.action.intent.refresh_listpage"));
				// TODO
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESHDATA_FINISH:
				ArrayList<SongSearchBean> songs = (ArrayList<SongSearchBean>) msg.obj;
				// Log.i("song","song list transfer==="+songs);
				songFragment.setEnterAndLayerType(msg.arg1, msg.arg2);
				songFragment.refresh(songs);
				sendBroadcast(new Intent(
						"justlink.action.intent.refresh_searchcount"));
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER:
				searchKey = (String) msg.obj;
				// Log.i("song","singer list transfer==="+searchKey);
				singerFragment.refresh_search(searchKey);
				// searchKey = "";
				isSingerSong = false;
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SEARCH_SONG:

				searchKey = (String) msg.obj;
				if (msg.arg1 == 11) {
					Message msg2 = Message.obtain();
					msg2.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER_DATA;
					msg2.obj = song_no;
					fragmentHander.sendMessage(msg2);
				} else {
					ArrayList<SongSearchBean> list = SongJsonParseUtils
							.getSongDatas2(0, searchKey, 0, msg.arg1, msg.arg2,
									0, 8, "");
					// Log.i("song","song list ==="+list.size());
					Message msg1 = Message.obtain();
					msg1.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESHDATA_FINISH;
					msg1.arg1 = msg.arg1;
					msg1.arg2 = msg.arg2;
					msg1.obj = list;
					fragmentHander.sendMessage(msg1);
				}
				// searchKey = "";
				break;

			case FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_DATA:
				// Log.i("song","接收刷新参数===entertype=="+msg.arg1+"===layertype=="+msg.arg2+"==start count=="+msg.obj);
				ArrayList<SongSearchBean> list1 = SongJsonParseUtils
						.getSongDatas2(0, searchKey, 0, msg.arg1, msg.arg2,
								(Integer) msg.obj, 8, song_no);
				// Log.i("song","song list ==="+list1.size());
				Message msg2 = Message.obtain();
				msg2.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESHDATA_FINISH;
				msg2.obj = list1;
				msg2.arg1 = msg.arg1;
				msg2.arg2 = msg.arg2;
				fragmentHander.sendMessage(msg2);

				isSingerSong = false;
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_RECOMMEND_SONG_LIST://刷新推荐歌曲列表
				String[]  ids = (String[]) msg.obj;
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SEARCHVIEW:
				//Log.i("song", "hide searchview =======");
				hideSearchSoftInput();
				searchView.clear(false);
				// Log.i("song","clear333333");
				isYidianListShow = true;
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SEARCHVIEW:
				//Log.i("song", "show searchview =======");
				VideoViewInit();
				showSearchSoftInput();
				isYidianListShow = false;
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW:
				//Log.i("song", "收到恢复searchview焦点请求");
				isYidianListShow = false;
				searchView.setFocusable(true);
				searchView.findViewById(R.id.gv_input).setFocusable(true);
				searchView.refresh(false);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_UNFOCUS_SEARCHVIEW:
				searchView.setFocusable(false);
				searchView.findViewById(R.id.gv_input).setFocusable(false);
				//Log.i("song", "收到取消searchview焦点请求");
				// searchView.refresh(true);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_CLEAR_SEARCHVIEW:
				searchView.clear(false);
				searchKey = "";
				// Log.i("song","clear1111111111");
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SET_SEARCHVIEW_ENTER_LAYER:
				SetSearchEnterAndLayerType(msg.arg1, msg.arg2);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER_DATA:
				song_no = (String) msg.obj;
				MyApplication.currentSingerNum = song_no;
				// Log.i("song","song_no======="+song_no);
				ArrayList<SongSearchBean> list2 = null;
				list2 = SongJsonParseUtils.getSongDatas2(0, searchKey, 0, 11,
						0, 0, 8, song_no);
				searchView.SetEnterAndLayerType(11, 0);
				isSingerSong = true;

				Message msg3 = Message.obtain();
				msg3.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESHDATA_FINISH;
				msg3.obj = list2;
				msg3.arg1 = 11;
				msg3.arg2 = 0;
				fragmentHander.sendMessage(msg3);

				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST:
				if (!VideoViewFullScreen && !isSearchViewShow && currentpage[0]==1){
					adapter_songlist1.refresh();
					currentpage[0]=1;
				}
				MyApplication.isSelectAll = true;
				//Log.i("song", "refresh yidian ====");
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_YIDIAN_LIST:
				listView_songlist.setVisibility(View.GONE);
				searchView.setVisibility(View.VISIBLE);
				isYidianListShow = false;
				// Log.i("song","hide yidian ====");
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_YIDIAN_LIST:
				VideoViewInit();
				listView_songlist.setVisibility(View.VISIBLE);
				searchView.setVisibility(View.GONE);
				etInput.setVisibility(View.GONE);
				isYidianListShow = true;
				// Log.i("song","show yidian ====");
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_RECORD_SHARE_DIALOG:
				SongSearchBean song = (SongSearchBean) msg.obj;
				ShareRecordFragment shareDialog = new ShareRecordFragment(
						MainActivity.this, style.MyDialog, song);
				shareDialog.show();
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_TOPBUTTON_FOCUS:
				btnPhone.setFocusable(false);
				btnSearch.setFocusable(false);
				btnDownload.setFocusable(false);
				wifiBotton.setFocusable(false);
				// Log.i("song","ban top button focus ====");
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS:
				btnPhone.setFocusable(true);
				btnSearch.setFocusable(true);
				btnDownload.setFocusable(true);
				wifiBotton.setFocusable(true);
				// Log.i("song","reset top button focus ====");
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS:
				 Log.i("song","ban yidian button focus ====");
				View childAt = listView_songlist.getChildAt(0);
				if (childAt != null)
					childAt.findViewById(R.id.top_back).setFocusable(false);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS:
				 Log.i("song","reset yidian button focus ====");
				View childAt1 = listView_songlist.getChildAt(0);
				if (childAt1 != null)
					childAt1.findViewById(R.id.top_back).setFocusable(true);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_YIDIAN:
				View childAt3 = listView_songlist.getChildAt(0);
				if (childAt3 != null) {
					Button btn = (Button) childAt3.findViewById(R.id.top_back);
					btn.setFocusable(true);
					btn.requestFocus();
				}
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_TOAST_TIP:
				// Log.i("song","receiver last page tip ===="+MyApplication.isSingerListFocus);
				if (!MyApplication.isSingerListFocus)
					Toast.makeText(MainActivity.this,
							getResources().getString(R.string.last_page_tip),
							Toast.LENGTH_SHORT).show();
				break;
			/*
			 * case FragmentMessageConstant.FRAGMENT_MESSAGE_DESTORY_LINEVIEW:
			 * mLineView.Destroy(); mLineView.setVisibility(View.GONE);
			 * ScorePicView.setVisibility(View.GONE);
			 * Log.i("song","FRAGMENT_MESSAGE_DESTORY_LINEVIEW===="); break;
			 */
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_TOP_SEARCH_KEY:
				hideSearchSoftInput();
				isYidianListShow = true;
				if (!TextUtils.isEmpty(searchKey)) {
					etInput.setVisibility(View.VISIBLE);
					etInput.setText(getResources().getString(R.string.search)
							+ searchKey);
				}
				adapter_songlist1.refresh();
				currentpage[0]=1;
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SCROLLING:
				subtitleView.setVisibility(View.GONE);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SCROLLING:
				subtitleView.setVisibility(View.VISIBLE);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_LOGO:
				logoImage.setVisibility(View.GONE);
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_LOGO:
				logoImage.setVisibility(View.VISIBLE);
				break;
			/*
			 * case FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SONG_CLASS:
			 * songFragment.hideClass(); break; case
			 * FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SONG_CLASS:
			 * songFragment.showClass(); break;
			 */
			case FragmentMessageConstant.FRAGMENT_MESSAGE_HIDE_SINGER_CLASS:
				singerFragment.hideClass();
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_SINGER_CLASS:
				singerFragment.showClass();
				break;
			case FragmentMessageConstant.FRAGMENT_MESSAGE_IMMEDIATE_PLAY:// 插播
				// 切到全屏，先暂停当前播放，显示下载进度，下载完成后切歌
				VideoViewFullScreen();
				downloadProgressBar.setVisibility(View.VISIBLE);
				AnimationDrawable animation = (AnimationDrawable) ivAnimation
						.getBackground();
				animation.start();
				
				currentDownloadProgress = 0;
				final StringBuffer sb = new StringBuffer();

				// 显示下载进度
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						sb.setLength(0);
						downloadJson = JNILib.getSongDownPercent();
						Log.i("song", "download percent==" + downloadJson);
						if (downloadJson!=null && downloadJson.contains("down_percent")) {
							isOnDownLoad = true;
							try {
								JSONObject object = new JSONObject(downloadJson);
								
								if(object.getInt("cloud_status")<0){//下载失败
									Log.i("song","download fail=======");
									tvDownloadProgress.setText(getResources().getString(R.string.down_fail_play));
								//	Toast.makeText(MainActivity.this, getResources().getString(R.string.down_fail),Toast.LENGTH_LONG).show();
								handler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										downloadProgressBar.setVisibility(View.GONE);
										isOnDownLoad = false;
										if(!PreviewVideo.isPlaying()){
											pauseVideo();
											mMainNoticeLayout.setVisibility(View.GONE);	
										}
										
										if(SongJsonParseUtils.getSongDatas3(0, "", 0, 22, 0, 0, 1, "").size()==0)
											preLoading.setVisibility(View.VISIBLE);
											VideoViewInit();//退出全屏	
										
									}
								}, 2000);
									
									return;
								}else{																	
									Log.i("song","download successful=======");
								if(PreviewVideo.isPlaying()){
									// 暂停
									pauseVideo();
									RefreshVideoDoubleScreenPauseStatus();
									if (getPauseStatus() == 0) {
										isPause = true;
										if (isScore)
											mLineView.stopSing();
									} else {
										isPause = false;
										if (!isMute)
											startRecoverVulume();
										if (isScore)
											mLineView.startSing();
									}
								}
								
								currentDownloadProgress = object
										.getInt("down_percent");
								sb.append(
										getResources().getString(
												R.string.on_loading))
										.append(currentDownloadProgress)
										.append("%").append("(")
										.append(object.getString("down_speed"))
										.append("/s)");
								tvDownloadProgress.setText(sb.toString());
								}
								postDelayed(this, 1000);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								isOnDownLoad = false;
								postDelayed(this, 1000);
								Log.i("song",
										"download parse error==="
												+ e.toString());
								// currentDownloadProgress = 100;
								// downloadProgressBar.setVisibility(View.GONE);
							}
						} else {
							Log.i("song","download finish=======");
							currentDownloadProgress = 100;
							downloadProgressBar.setVisibility(View.GONE);
							isOnDownLoad = false;
							fragmentHander
									.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
						}

					}
				}, 2000);

				break;
			default:

				break;

			}
		};
	};

	private boolean isPageDown = false;
	private boolean isPageUp = false;
	private boolean isZX = false;
	private FrameLayout fragmentContainer, pip_fragment;
	private LinearLayout top_botton_ln;
	private String samba_str;
	private boolean isRecording = false;
	private int currentPageCount = 0;
	private int currentKeyPosition = 0;
	private int currentSongListPosition = 0;
	private boolean isFromHome = false;// 按Home�?
	private ProgressBar pb;
	private TextView etInput;
	private boolean isSearchViewShow = false;
	private boolean isOrderClick = false;
	private boolean isVodOrderSelect = false;
	private Micphone micphone;
	private FrameLayout preLoading;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// Log.i("CHW","MSG====="+msg.what);
			switch (msg.what) {

			case JNILib.JLINK_TIMER: // 音量渐长
				Log.v(TAG,
						"====JSL====volume, handleMessage JLINK_TIMER vlume_now:"
								+ vlume_now + ", vlume_set:" + vlume_set
								+ ", ismute:" + isMute);

				if (!isPause && !isMute && (vlume_now < vlume_set)) {
					vlume_now++;
					setVolume(vlume_now);
					Log.v(TAG,
							"====JSL====volume, handleMessage JLINK_TIMER vlume_now:"
									+ vlume_now + ", vlume_set:" + vlume_set);

					Message message1 = new Message();
					message1.what = JNILib.JLINK_TIMER;
					handler.sendMessageDelayed(message1, 100);
				}
				break;

			case JNILib.JLINK_START_PLAY:
				Log.v("song", "handleMessage JLINK_START_PLAY");
				PlayFirstSongDoubleScreen();
				PIPWinMode = JNILib.getCurrentPIPMode();
				initSoundStatus();
				// setDiskLed();
				/*
				 * if (null != presentation){ presentation.hideLoadingtxt();
				 * presentation.getLogo(); presentation.initGifView();
				 * presentation.initOSD(); }
				 */

				break;

			case JNILib.JLINK_GET_PREVIEW_POSION: // 定时获取进度
				if ((isPlayPreview == 1) && (mPreviewProgressThreadRun == 1)) {
					int pos_now = PreviewVideo.getCurrentPosition() * 100
							/ PreviewVideo.getDuration();
					if (mPreviewPosCur != pos_now) {
						// Log.v(TAG,"====JSL====, preview position now:"+pos_now);
						mPreviewPosCur = pos_now;
						JNILib.PreviewSetPlayBar(mPreviewPosCur, 0);
					}

					Message message1 = new Message();
					message1.what = JNILib.JLINK_GET_PREVIEW_POSION;
					handler.sendMessageDelayed(message1, 500);
				}
				break;

			// ///////////////////// 系统相关命令 /////////////////////////////
			case JNILib.JLINK_ENTER_PREVIEW:
				// Log.v(TAG,"handleMessage JLINK_ENTER_PREVIEW");
				isPlayPreview = 1;
				// vgaVideoPath = JNILib.getHttpUrl();
				// PlayVideoOnlyVga(vgaVideoPath);

				break;
			case JNILib.JLINK_EXIT_PREVIEW:
				// Log.v(TAG,"handleMessage JLINK_STOP_PREVIEW");
				// stopVideoOnlyVga();
				isPlayPreview = 0;
				// playBackGroundVideo();
				break;
			case JNILib.JLINK_REFRESH_SUBTITLE:
				// Log.v(TAG,"handleMessage JLINK_REFRESH_SUBTITLE");
				ScollMessage = JNILib.getScollMessage();
				setPaoMaDeng(ScollMessage);
				break;
			case JNILib.JLINK_DIALOG_INPUT:
				// Log.v(TAG,"handleMessage JLINK_DIALOG_INPUT");
				InputDialog2 dialog2 = new InputDialog2(MainActivity.this,
						"Enter:");
				dialog2.show();
				break;
			case JNILib.JLINK_DIALOG_INPUT2:
				// Log.v(TAG,"handleMessage JLINK_DIALOG_INPUT");
				InputDialog dialog = new InputDialog(MainActivity.this,
						"Enter:");
				dialog.show();
				break;
			case JNILib.JLINK_DIALOG_INPUT3:
				// Log.v(TAG,"handleMessage JLINK_DIALOG_INPUT");
				InputDialog2 dialog3 = new InputDialog2(MainActivity.this,
						"Enter:");
				dialog3.show();
				break;
			case JNILib.JLINK_HANDWRITE:
				// Log.v(TAG,"handleMessage JLINK_HANDWRITE");
				handWrite();
				break;
			case JNILib.JLINK_PIP_FULLSCREEN:
				// Log.v(TAG,"handleMessage JLINK_PIP_FULLSCREEN");
				PIPWinMode = 1;
				// SetVideoDoubleScreenVgaWin(PIPWinMode);
				break;
			case JNILib.JLINK_PIP_SMALLWIN:
				// Log.v(TAG,"handleMessage JLINK_PIP_SMALLWIN");
				PIPWinMode = 0;
				// SetVideoDoubleScreenVgaWin(PIPWinMode);
				break;
			case JNILib.JLINK_PREVIEW_SEEK:
				// Log.v(TAG,"handleMessage JLINK_PREVIEW_SEEK");
				if (1 == isPlayPreview) {
					mPreviewPosCur = JNILib.PreviewGetPlayBar();
					// seekVideoOnlyVga(mPreviewPosCur);
				}
				break;
			case JNILib.JLINK_PIP_SEEK:
			// Log.v(TAG,"handleMessage JLINK_PIP_SEEK");
			// if (null != presentation)
			{
				int pos_s = JNILib.PIPGetPlayBar();
				// presentation.seekTo(pos_s);
				JNILib.PIPEnableUi();
			}
				break;
			case JNILib.JLINK_PIP_GET_CURRENT_POS:
				// Log.v(TAG,"handleMessage JLINK_PIP_GET_CURRENT_POS");
				/*
				 * if (null != presentation) {
				 * Log.v(TAG,"====JSL====, current:"+
				 * presentation.getCurrentPos() + "  duration:" +
				 * presentation.getDuration());
				 * JNILib.PIPSetPlayBar(presentation.getDuration(),
				 * presentation.getCurrentPos()); } else {
				 * JNILib.PIPSetPlayBar(0, 0); }
				 */
				break;
			case JNILib.JLINK_PIP_SEEK_RIGHT:
			// Log.v(TAG,"handleMessage JLINK_PIP_SEEK_RIGHT");
			// if (null != presentation)
			{
				// presentation.seekRight();
			}
				break;
			case JNILib.JLINK_PIP_SEEK_LEFT:
			// Log.v(TAG,"handleMessage JLINK_PIP_SEEK_LEFT");
			// if (null != presentation)
			{
				// presentation.seekLeft();
			}
				break;
			case JNILib.JLINK_SAMBA_CMD: // SAMBA mount命令
				samba_str = JNILib.GetMountSMB();
				MountSamba(samba_str);
				// Log.v("samba","samba_str======="+samba_str) ;
				break;

			case JNILib.JLINK_SYSTEM_CHMOD:
				/*
				 * String cmdParam = JNILib.GetSystemCmd();
				 * Log.i(TAG,"chmod param ==============="+cmdParam);
				 * runSystemCMD(cmdParam);
				 */
				break;

			// ///////////////////// 播放控制命令 /////////////////////////////
			case JNILib.JLINK_PLAY_VOICE:
				// Log.v(TAG,"handleMessage JLINK_PLAY_VOICE");

				voice_mode = getVoiceMode();
				Log.v(TAG, "voice mode now:" + voice_mode);
				if (voice_mode == JLINK_VOICE_MOD_YUANCHANG) {
					voice_mode = JLINK_VOICE_MOD_BANCHANG;
					setNoticeImage(JLINK_NOTICE_TYPE_BANCHANG, 0);
				} else {
					voice_mode = JLINK_VOICE_MOD_YUANCHANG;
					setNoticeImage(JLINK_NOTICE_TYPE_YUANCHANG, 0);
				}
				// Log.v(TAG,"set voice mode(0:yuanchang, 1:banchang):"+voice_mode);
				setVoiceMode(voice_mode);
				break;
			case JNILib.JLINK_PLAY_VOICE_YUANCHANG:
			// Log.v(TAG,"handleMessage JLINK_PLAY_VOICE_YUANCHANG");
			// if (null != presentation)
			{
				voice_mode = JLINK_VOICE_MOD_YUANCHANG;
				setNoticeImage(JLINK_NOTICE_TYPE_YUANCHANG, 0);
				Log.v(TAG, "set voice mode to yuanchang: " + voice_mode);
				setVoiceMode(JLINK_VOICE_MOD_YUANCHANG);
			}
				break;
			case JNILib.JLINK_PLAY_VOICE_BANCHANG:
			// Log.v(TAG,"handleMessage JLINK_PLAY_VOICE_BANCHANG");
			// if (null != presentation)
			{
				voice_mode = JLINK_VOICE_MOD_BANCHANG;
				setNoticeImage(JLINK_NOTICE_TYPE_BANCHANG, 0);
				Log.v(TAG, "set voice mode to banchang: " + voice_mode);
				setVoiceMode(JLINK_VOICE_MOD_YUANCHANG);
			}
				break;

			case JNILib.JLINK_PLAY_PAUSE:
				// Log.v(TAG,"handleMessage JLINK_PLAY_PAUSE");
				pauseVideo();
				RefreshVideoDoubleScreenPauseStatus();
				if (getPauseStatus() == 0) {
					isPause = true;
					if (isScore)
						mLineView.stopSing();
				} else {
					isPause = false;
					if (!isMute)
						startRecoverVulume();
					if (isScore)
						mLineView.startSing();
				}
				break;
			case JNILib.JLINK_PLAY_VOL_UP:
				// Log.v(TAG,"handleMessage JLINK_PLAY_VOL_UP,Volume up");
				if (getMute()) {
					setMute(false);
				}
				setVolumeMode(1);
				vlume_set = getVolume();
				refreshSoundStatus();
				if (vlume_set == 0) {
					setNoticeImage(JLINK_NOTICE_TYPE_VOLUME, 0);
				} else {
					setNoticeImage(JLINK_NOTICE_TYPE_VOLUME, vlume_set);
				}
				break;
			case JNILib.JLINK_PLAY_VOL_DN:
				// Log.v(TAG,"handleMessage JLINK_PLAY_VOL_DN,Volume down");
				if (getMute()) {
					setMute(false);
				}
				setVolumeMode(0);
				vlume_set = getVolume();
				refreshSoundStatus();
				if (vlume_set == 0) {
					setNoticeImage(JLINK_NOTICE_TYPE_VOLUME, 0);
				} else {
					setNoticeImage(JLINK_NOTICE_TYPE_VOLUME, vlume_set);
				}

				break;

			case JNILib.JLINK_PLAY_MUTE:
				// Log.v(TAG,"handleMessage JLINK_PLAY_MUTE,set to mute");
				// vlume_set = getVolume();
				isMute = true;
				setMute(true);

				refreshSoundStatus();
				setNoticeImage(JLINK_NOTICE_TYPE_MUTE, 0);
				break;
			case JNILib.JLINK_PLAY_UNMUTE:
				// Log.v(TAG,"====JSL====,  handleMessage JLINK_PLAY_UNMUTE,set to unmute");
				// vlume_set = getVolume();
				// if (vlume_now <= 1) //如果音量�?，在取消静音时设置为1
				startRecoverVulume();
				refreshSoundStatus();
				setNoticeImage(JLINK_NOTICE_TYPE_UNMUTE, 0);
				break;
			case JNILib.JLINK_PLAY_NEXT:
			// Log.v(TAG,"handleMessage JLINK_PLAY_NEXT");
			// if (null != presentation)
			{
				// if (!presentation.getStartStatus())
				{
					Log.v(TAG, "start not ok, don't play again ");
					// break;
				}
				if (isPause) {
					isPause = false;
					startRecoverVulume();
				}
				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				setNoticeImage(JLINK_NOTICE_TYPE_UNPAUSE, 0);
				//setNoticeImage(JLINK_NOTICE_TYPE_NEXT, 0);

				playVideoNext();

			}
				break;
			case JNILib.JLINK_PLAY_RPEATE:
			// Log.v(TAG,"handleMessage JLINK_PLAY_RPEATE");
			// if (null != presentation)
			{
				RepeatVideo();
				if (getPauseStatus() == 0) {
					pauseVideo();
					RefreshVideoDoubleScreenPauseStatus();
				}
				if (isPause) {
					isPause = false;
					startRecoverVulume();
				}
				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				setNoticeImage(JLINK_NOTICE_TYPE_REPEAT, 0);
			}
				break;

			// ///////////////////// KTV程序获取播放相关状�?
			// /////////////////////////////
			case JNILib.JLINK_PLAY_GET_VOICE:
				// Log.v(TAG,"handleMessage JLINK_PLAY_GET_VOICE");

				voice_mode = getVoiceMode();
				JNILib.NoticeVoice(voice_mode);
				break;
			case JNILib.JLINK_PLAY_GET_PAUSE:
			// Log.v(TAG,"handleMessage JLINK_PLAY_GET_PAUSE");
			// if (null != presentation)
			{
				if (getPauseStatus() == 0)
					JNILib.NoticePause(1);
				else
					JNILib.NoticePause(0);
			}
				/*
				 * else { Log.v(TAG,
				 * "JLINK_PLAY_GET_VOICE, video not play return pause status 0"
				 * ); JNILib.NoticePause(0); }
				 */
				break;
			case JNILib.JLINK_PLAY_GET_VOL:
				// Log.v(TAG,"handleMessage JLINK_PLAY_GET_VOL");
				vlume_now = getVolume();
				JNILib.NoticeVolume(vlume_now);
				break;
			case JNILib.JLINK_PLAY_GET_MUTE:
				// Log.v(TAG,"handleMessage JLINK_PLAY_GET_MUTE");
				if (getMute()) {
					JNILib.NoticeMute(1);
				} else {
					JNILib.NoticeMute(0);
				}
				break;

			// ///////////////////// 气氛命令 /////////////////////////////
			case JNILib.JLINK_CLOSE_QF:
				// Log.v(TAG,"handleMessage JLINK_CLOSE_QF");
				if (1 == isPlayScene) {
					// CurrentPlayPath = JNILib.playNextSong(1);
					PlayVideoDoubleScreenNoRepeat(CurrentPlayPath);
					isPlayScene = 0;
				}
				break;
			case JNILib.JLINK_Atmosphere:
				Log.v(TAG, "JLINK_EXPRESION,表情");
				break;
			case JNILib.JLINK_Site:
				Log.v(TAG, "JLINK_FL,送花");
				break;
			case JNILib.JLINK_SOUND:
				Log.v(TAG, "JLINK_SOUND,送花");
				break;
			case JNILib.JLINK_GRAFFITI:
				Log.v(TAG, "JLINK_GRAFFITI,涂鸦");
				break;
			case JNILib.JLINK_BLESS:
				Log.v(TAG, "JLINK_BLESS,祝福语");
				break;
			case JNILib.JLINK_BARRAGE:// 弹幕
				Log.v(TAG, "JLINK_BARRAGE,弹幕");
				String barrage = JNILib.getBarrage();
				BarrageAdd(barrage);
				Log.v(TAG, "JLINK_BARRAGE,弹幕==" + barrage);
				break;
			case JNILib.JLINK_ATMOSPHERE_HECAI:
				Log.v(TAG, "handleMessage JLINK_ATMOSPHERE_HECAI");
				// if (null != presentation)
				{
					setNoticeImage(JLINK_NOTICE_TYPE_QIFEN, 1);
					playBackMusic("/mnt/link2src/sys_file/media/hecai.wav");
				}
				break;
			case JNILib.JLINK_ATMOSPHERE_DAOCAI:
				Log.v(TAG, "handleMessage JLINK_ATMOSPHERE_DAOCAI");
				// if (null != presentation)
				{
					setNoticeImage(JLINK_NOTICE_TYPE_QIFEN, 2);
					playBackMusic("/mnt/link2src/sys_file/media/daocai.wav");
				}
				break;
			case JNILib.JLINK_ATMOSPHERE_HUANHU:
				Log.v(TAG, "handleMessage JLINK_ATMOSPHERE_HUANHU");
				// if (null != presentation)
				{
					setNoticeImage(JLINK_NOTICE_TYPE_QIFEN, 3);
					playBackMusic("/mnt/link2src/sys_file/media/huanhu.wav");
				}
				break;

			case JNILib.JLINK_SCENE_STOP:
				Log.v(TAG, "handleMessage JLINK_SCENE_STOP");
				if (1 == isPlayScene) {
					CurrentPlayPath = JNILib.playNextSong(1);
					PlayVideoDoubleScreenNoRepeat(CurrentPlayPath);
					isPlayScene = 0;
				}
				break;
			case JNILib.JLINK_SCENE_HUIYI:
				Log.v(TAG, "handleMessage JLINK_SCENE_HUIYI");

				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				PlayVideoDoubleScreenRepeat("/mnt/link2src/sys_file/media/changjing_huiyi.mpg");
				isPlayScene = 1;
				ClearVideoDoubleScreenPauseStatus();
				break;
			case JNILib.JLINK_SCENE_CHEZHAN:
				Log.v(TAG, "handleMessage JLINK_SCENE_CHEZHAN");

				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				PlayVideoDoubleScreenRepeat("/mnt/link2src/sys_file/media/changjing_chezhan.mpg");
				isPlayScene = 1;
				ClearVideoDoubleScreenPauseStatus();
				break;
			case JNILib.JLINK_SCENE_MALU:
				Log.v(TAG, "handleMessage JLINK_SCENE_MALU");

				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				PlayVideoDoubleScreenRepeat("/mnt/link2src/sys_file/media/changjing_malu.mpg");
				isPlayScene = 1;
				ClearVideoDoubleScreenPauseStatus();
				break;
			case JNILib.JLINK_SCENE_MAJIANG:
				Log.v(TAG, "handleMessage JLINK_SCENE_MAJIANG");
				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				PlayVideoDoubleScreenRepeat("/mnt/link2src/sys_file/media/changjing_majiang.mpg");
				isPlayScene = 1;
				ClearVideoDoubleScreenPauseStatus();
				break;
			case JNILib.JLINK_SCENE_GONGDI:
				Log.v(TAG, "handleMessage JLINK_SCENE_GONGDI");
				setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
				PlayVideoDoubleScreenRepeat("/mnt/link2src/sys_file/media/changjing_gongdi.mpg");
				isPlayScene = 1;
				ClearVideoDoubleScreenPauseStatus();
				break;

			// ///////////////////// 其他系统相关命令 /////////////////////////////
			case JNILib.JLINK_START_ANDROID:
				Log.v(TAG, "handleMessage JLINK_START_ANDROID");
				// restore_video_audio_port();
				// dialog1 = ProgressDialog.show(MainActivity.this, "提示",
				// "请稍�?.....", false, true);
				// ProgressDialog dialog1 = new
				// ProgressDialog(MainActivity.this);
				// dialog1.show();
				Intent intent = new Intent();
				intent.setClassName("com.justlink.boxplayer",
						"com.justlink.boxplayer.activity.MainActivity");
				startActivity(intent);
				// stopVideoOnlyVga();
				DestroyVideoDoubleScreen();
				stopBackMusic();
				break;
			case JNILib.JLINK_START_MOIVE:
				Log.v(TAG, "handleMessage JLINK_START_MOIVE");
				// restore_video_audio_port();
				Intent intent1 = new Intent();
				intent1.setClassName("com.hisilicon.videocenter",
						"com.hisilicon.videocenter.HomeActivity");
				startActivity(intent1);
				// stopVideoOnlyVga();
				DestroyVideoDoubleScreen();
				stopBackMusic();
				break;
			case JNILib.JLINK_SYSTEM_SETTING:
				Log.v(TAG, "handleMessage JLINK_START_MOIVE");
				// 进入系统设置界面
				Intent intent2 = new Intent();
				intent2.setClassName("com.android.hisiliconsetting",
						"com.android.hisiliconsetting.MainActivity");
				startActivity(intent2);
				// 调出小白点程�?
				Intent intent3 = new Intent();
				intent3.setClassName("com.flyaudio",
						"com.flyaudio.AssistiveTouchService");
				startService(intent3);

				// stopVideoOnlyVga();
				DestroyVideoDoubleScreen();
				stopBackMusic();
				break;
			case JNILib.JLINK_SHUTDOWN:
				Log.v(TAG, "handleMessage JLINK_SHUTDOWN");
				dialog1 = ProgressDialog.show(MainActivity.this, "提示",
						"请稍候...", false, true);
				doKeyAction(KeyEvent.KEYCODE_POWER);

				break;

			case JNILib.JLINK_LANGUAGE:
				Log.v(TAG, "handleMessage JLINK_LANGUAGE_CHANGE");
				LanguangeChange();
				break;
			case JNILib.JLINK_RECORD_START:
				Log.v(TAG, "handleMessage JLINK_RECORD_START");
				Log.v(TAG, "handleMessage JLINK_PLAY_RPEATE");
				// if (null != presentation)
				{
					RepeatVideo();
					if (getPauseStatus() == 0) {
						pauseVideo();
						RefreshVideoDoubleScreenPauseStatus();
					}
					if (isPause) {
						isPause = false;
						startRecoverVulume();
					}
					setNoticeImage(JLINK_NOTICE_TYPE_RECORD_START, 0);
					setNoticeImage(JLINK_NOTICE_TYPE_REPEAT, 0);
				}
				// LanguangeChange();
				isRecording = true;
				break;
			case JNILib.JLINK_RECORD_STOP:
				Log.v(TAG, "handleMessage JLINK_RECORD_STOP");

				// if (null != presentation)
				{
					/*
					 * if (!presentation.getStartStatus()) {
					 * Log.v(TAG,"start not ok, don't play again "); break; }
					 */
					if (isPause) {
						isPause = false;
						startRecoverVulume();
					}
					setNoticeImage(JLINK_NOTICE_TYPE_RECORD_STOP, 0);
					setNoticeImage(JLINK_NOTICE_TYPE_UNPAUSE, 0);
					setNoticeImage(JLINK_NOTICE_TYPE_NEXT, 0);
					playVideoNext();
					isRecording = false;

				}
				// LanguangeChange();
				break;
			case MESSAGE_REFRESH_NOTICE_IMAGE:
				tishiText.setVisibility(View.GONE);
				if (NoticeState_bPause == 1)
					setNoticeImage(JLINK_NOTICE_TYPE_PAUSE, 0);
				else if (NoticeState_bMute == 1)
					setNoticeImage(JLINK_NOTICE_TYPE_MUTE, 0);
				else
					mMainNoticeLayout.setVisibility(View.GONE);
				break;

			case MESSAGE_UPDATE_QR_IMAGE:
				UpdateQrDisp();
				break;
			case MESSAGE_UPDATE_GIF_IMAGE:
				GifViewDismiss();
				break;
			case MESSAGE_UPDATE_OSD:
				dissMissOSD();
				break;
			case MESSAGE_UPDATE_BAR:
				dissMissBar();
				break;

			case JNILib.JLINK_SCORE_START:// 评分
				if (!isScore)
					return;
				isOnScore = true;
				/*
				 * if(isFirstScore){ GifViewShow(8); isFirstScore = false;
				 * 
				 * }
				 */
				oldScore = 0.0;
				picPath = "";
				songName = "";
				singer = "";
				String scoreDate = JNILib.getScorePath();
				if (VideoViewFullScreen) {
					ScorePicView.setVisibility(View.VISIBLE);
				} else {
					ScorePicView.setVisibility(View.GONE);
					mLineView.setVisibility(View.GONE);
				}

				ScorePicView.setText("0.0");
				// Log.i("line","====scoreDate=="+scoreDate);
				try {
					JSONObject object = new JSONObject(scoreDate);
					String file_path = object.getString("score_path");
					Log.v("score", "=" + file_path);
					readLine(file_path);
					mLineView.startMove();
					startSing();
					songName = object.getString("name");
					singer = object.getString("singer");
					if (object.has("pic")) {
						picPath = object.getString("pic");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				// GifViewShow(1);
				break;
			case JNILib.JLINK_SCORE_STOP:// 评分停止
				isOnScore = false;
				mLineView.Destroy();
				mLineView.setVisibility(View.GONE);
				ScorePicView.setVisibility(View.GONE);
				if (scoreDialog != null) {
					scoreDialog.dismiss();
				}
				oldScore = 0.0;
				picPath = "";
				songName = "";
				singer = "";
				Log.v("hander", "score stop");
				break;

			case 111111:// 显示分数

				// Log.v("hander","111111");
				int score = mLineView.getScorea();
				// jLanguage=JNILib.getGlobalLanguage();
				if (scoreDialog == null) {
					scoreDialog = new SingScoreDialog(MainActivity.this,
							style.MyDialog);
				}
				if (VideoViewFullScreen)
					scoreDialog.show();
				if (score <= 60) {
					tipIndex = 0;
					scoreDialog
							.setScoreInfo(score, songName,
									VideoString.systemTips1[jLanguage], singer,
									picPath);
				} else if (score > 60 && score <= 80) {
					tipIndex = 1;
					scoreDialog
							.setScoreInfo(score, songName,
									VideoString.systemTips2[jLanguage], singer,
									picPath);
				} else if (score > 80) {
					tipIndex = 2;
					scoreDialog
							.setScoreInfo(score, songName,
									VideoString.systemTips3[jLanguage], singer,
									picPath);
				}

				scoreDialog.showScore();

				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (scoreDialog != null) {
							scoreDialog.dismiss();

						}
					}
				}, 7000);
				isOnScore = false;
				mLineView.Destroy();

				mLineView.setVisibility(View.GONE);
				ScorePicView.setVisibility(View.GONE);
				// ScorePicShow(score);
				// GifViewShow(1);
				break;

			case DISPLAY_REALTIME_SCORE:// 显示实时分数
				// if(oldScore!=realtimeScore && realtimeScore!=0){
				ScorePicView.setText(realtimeScore + "");
				oldScore = realtimeScore;

				/*
				 * if(realtimeScore>59 && realtimeScore<=60){ GifViewShow(7);
				 * }else if(realtimeScore>79 && realtimeScore<=80 ){
				 * GifViewShow(9); }else if(realtimeScore>=90){ GifViewShow(15);
				 * }else{ GifViewDismiss(); }
				 */

				// }

				break;

			case MESSAGE_GET_PLAYER_POSION: // 定时获取进度
				int CurPos_s = 0;
				if (isMusic && (mediaPlayerMusic != null)) {
					if ((isPrepared_music == true))
						CurPos_s = mediaPlayerMusic.getCurrentPosition() / 1000;
					else
						CurPos_s = 0;

					// Log.v(TAG,"====JSL====,music CurPos_s :"+CurPos_s+" music duration:"
					// + getMusicDuration() + " video duration:" +
					// PreviewVideo.getDuration());

					if ((PreviewVideo != null)
							&& (getMusicDuration() > (PreviewVideo
									.getDuration() + 1))) {
						if (PreviewVideo.getCurrentPosition() > PreviewVideo
								.getDuration() * 95 / 100) // 视频播放�?5%从头播放
						{
							PreviewVideo.RepeatVideo();
						}
					}

				} else if (PreviewVideo != null) {
					if (isPrepared == true)
						CurPos_s = PreviewVideo.getCurrentPosition();
					else
						CurPos_s = 0;
				}
				//
				if (mPIPPlayPosCur_s != CurPos_s) {
					// Log.v(TAG,"====JSL====, player position now:"+CurPos_s+" Duration:"
					// + getDuration());
					mPIPPlayPosCur_s = CurPos_s;
					// JNILib.PIPSetPlayBar(videoView.getDuration(),
					// mPIPPlayPosCur_s);
					// Log.i("song","isSeekbarTraking==="+mPIPPlayPosCur_s * 100 / getDuration());
	
					if(mPIPPlayPosCur_s*100/getDuration()>95 && VideoViewFullScreen){//next play info
						// 当前播放信息
						ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
								0, "", 0, 31, 0, 0, 2, "");
						if (list != null && list.size() > 0) {
							llNextPlay.setVisibility(View.VISIBLE);
							SongSearchBean currentSong = list.get(0);
							tvNowPlaying.setText(currentSong.getSong()+"------"+currentSong.getSinger());
							if(list.size()==2){
								SongSearchBean nextSong = list.get(1);					
								tvNextPlay.setText(nextSong.getSong()+"------"+nextSong.getSinger());
							}else{
								SongSearchBean currentSong1 = list.get(0);
								tvNextPlay.setText(currentSong1.getSong()+"------"+currentSong.getSinger());
							}

						}
					}else{
						llNextPlay.setVisibility(View.GONE);
					}
					
					
					
					if (SeekBarshow && !isSeekbarTraking) {
						setSeekBar(mPIPPlayPosCur_s * 100 / getDuration());
					}
				//	Log.i("song","current progress==="+mPIPPlayPosCur_s*100/getDuration());
					if(mPIPPlayPosCur_s*100/getDuration()>0){
						preLoading.setVisibility(View.GONE);
					}
				}

				Message message1 = new Message();
				message1.what = MESSAGE_GET_PLAYER_POSION;
				handler.sendMessageDelayed(message1, 500);
				break;

			default:
				Log.v(TAG, "msg.what = " + msg.what);
				break;
			}
		};
	};

	private Handler micHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case KARAOKE_USB_MIC_CONNECT:// USB MIC链接
				initMicPhone();
				break;
			case KARAOKE_USB_MIC_DISCONNECT:// USB MCI 断开
				micphone.stop();
				micphone.release();
				//Log.i("song", "mic stop==========");
				break;
			default:
				break;
			}
		};
	};

	private int mFragmentPosition = 1;
	private int mHomePosition = 1;

	public int getFragmentPositon() {
		return mFragmentPosition;
	}

	public void setFragmentPosition(int positon) {
		this.mFragmentPosition = positon;
	}

	public int getHomePosition() {
		return mHomePosition;
	}

	public void setHomePosition(int positon) {
		this.mHomePosition = positon;
	}

	private String fileName = "/mnt/link2src/sys_file/draw/logo/logo.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Log.i("song","oncreate===================");
		int default_start = 0;
		if (getIntent().getBooleanExtra("home", false)
				|| MyApplication.isLanguageChange) {
			isPlayNext = true;
			// killApp();
			// Log.i("song","from home===================");
		} else {
			isPlayNext = false;
		}
		// 创建图片缓存文件夹
		File pictureCache = new File(pictureCachePath);
		if (!pictureCache.exists())
			CreateSpecifiedSizeFileUtil.createFile(pictureCachePath, 20,
					FileUnit.MB);

		setGpio();
		// getWindow().addPrivateFlags(WindowManager.LayoutParams.PRIVATE_FLAG_HOMEKEY_DISPATCHED);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_karaok);

		// initMicPhone();//初始化MIC
		
		MyApplication.MAC = getLocalMacAddress();

		setLanguage();
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
		// | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		PreviewVideo = (HisiVideoView) findViewById(R.id.preview_video);
		// PreviewVideo.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		/*
		 * ViewGroup.LayoutParams lp = PreviewVideo.getLayoutParams(); lp.width
		 * = 192*4; lp.height =108*4;
		 */

		// killApp();

		initOSD();

		if (MyApplication.logo_switch.equals("1"))
			logoImage.setVisibility(View.VISIBLE);
		else
			logoImage.setVisibility(View.GONE);
		Log.i(TAG, "===========karaok apk start========");
		// Log.i(TAG,"    UI number:" + UInumber);

		button_pip = (AnimationButton) findViewById(R.id.pip_botton);
		animationBkImage = (ImageView) button_pip
				.findViewById(R.id.animation_bk_image);
		pipFocusChange();

		// 弹幕
		mDanmakuView = (DanmakuView) findViewById(R.id.danmakuView);
		// List<IDanmakuItem> list = initItems();
		// Collections.shuffle(list);
		// mDanmakuView.addItem(list, true);
		mDanmakuView.show();

		initFragment();
		VideoViewInit();
		
		
		int a = JNILib.setBoxSide();
		Log.i(TAG, "===========告诉底层库是点歌�?======" + a);
		int b = JNILib.toAssetManager(getAssets());
		Log.i(TAG, "===========getAssets()=======" + b);
		JNILib.setGlobalVersion(UInumber);
		
	
		setMachineVersion();
		
		
		Log.i(TAG, "===========UInumber=======");
		default_start = JNILib.getDefaultStart();
		Log.i(TAG, "    start android:" + default_start);
		// setFirstRun();

		if (1 == default_start) {
			isStartAndroid = true;
		} else {
			isStartAndroid = false;
		}

		init_video_audio_port();
		JNILib.init(1920, 1080);
		// qrCode = JNILib.qrGet();

		registMICReceiver();
		sataRegisterReceiver();
		registerOutsideReceiver();
		// myRegisterReceiver();
		// netRegisterReceiver();
		// sataRegisterReceiver();
		// ys952 = new YS952();
		// ys952.Init(0);
		// Log.v(TAG,"====JSL====, ys952 init over");
		PreviewVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer arg0) {
				isPrepared = true;

				setVoiceMode(mVoiceMode);
				{
					Message message1 = new Message();
					message1.what = MESSAGE_GET_PLAYER_POSION;
					handler.sendMessageDelayed(message1, 500);
				}
			}
		});

		PreviewVideo.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {

				if (!isMusic) {
					if (1 == isRepeat) {
						Log.v(TAG, "====JSL====, onCompletion isRepeat = "
								+ isRepeat);
						PreviewVideo.start();
					} else {
						Log.v(TAG, "====JSL====, onCompletion isRepeat = "
								+ isRepeat);
						playVideoNext();

					}
				} else {
					PreviewVideo.start();
				}
			}
		});

		button_pip.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				Log.i("VideoViewFullScreen1",
						"====JSL==== VideoViewFullScreen= "
								+ VideoViewFullScreen);
				if (VideoViewFullScreen) {
					VideoViewInit();
				} else {
					VideoViewFullScreen();
				}

			}
		});

		/*
		 * ImageLoaderConfiguration configuration = ImageLoaderConfiguration
		 * .createDefault(this); ImageLoader.getInstance().init(configuration);
		 */

		File cacheDir = StorageUtils.getOwnCacheDirectory(this, "cpgame/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(480, 800)
				// maxwidth, max height，即保存的每个缓存文件的�?��长宽
				.threadPoolSize(3)
				// 线程池内加载的数�?
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// 设置任务的处理顺�?
				.memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以�?过自己的内存缓存实现
				.memoryCacheSize(5 * 1024 * 1024)
				.diskCacheSize(10 * 1024 * 1024)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时�?的URI名称用MD5 加密
				.diskCacheFileCount(100)
				// 缓存的文件数�?
				.diskCache(new UnlimitedDiskCache(cacheDir))
				// 自定义缓存路�?
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				.writeDebugLogs() // Remove for releaseapp
				.build();
		ImageLoader.getInstance().init(config);

		// showWeixinCode();

		// printDisplay();

		usbActionDetectViaTimer();
	}

	private void setMachineVersion() {
		// TODO Auto-generated method stub
		HashMap<String, String> machieInfo = new HashMap<String, String>();
		machieInfo.put("mac", MyApplication.MAC);
		//machieInfo.put("pkg",UInumber);
		machieInfo.put("model","longtv");
		machieInfo.put("sw_ver","Longtv_karaoke_v1.1");
		machieInfo.put("hw_ver","longtv");	
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(machieInfo);
			JNILib.setMachineVersion(json);
			Log.i("song","machine info==="+json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("home", false)) {
			isPlayNext = true;
			isFromHome = true;
		} else {
			isPlayNext = false;
			isFromHome = false;
			// Log.i("song","newintent from home===================false");
		}
		VideoViewInit();
		// jump2model();
		setIntent(intent);
	}

	/*
	 * @Override public boolean dispatchTouchEvent(MotionEvent event) { // TODO
	 * Auto-generated method stub if(event.getAction()==MotionEvent.ACTION_MOVE
	 * || event.getAction()==MotionEvent.ACTION_UP ||
	 * event.getAction()==MotionEvent.ACTION_DOWN){
	 * Log.i("song","searchview dispatch======"); return false; } return
	 * super.dispatchTouchEvent(event); }
	 * 
	 * @Override public boolean dispatchKeyEvent(KeyEvent event) {
	 * if(event.getAction()==MotionEvent.ACTION_MOVE ||
	 * event.getAction()==MotionEvent.ACTION_UP ||
	 * event.getAction()==MotionEvent.ACTION_DOWN){
	 * Log.i("song","searchview dispatchkey event======"); return true; } //
	 * TODO Auto-generated method stub return super.dispatchKeyEvent(event); }
	 */

	public void VideoViewInit() {
		mQrPlayTime = 0;
		isSelectFocus = false;
		
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				420 * 1920 / 1280, 236 * 1920 / 1280);// 宽高420, 236
		rl.addRule(RelativeLayout.ALIGN_LEFT);// 左对齐，RelativeLayout上下左右对齐的常量，按自己需要�?�?
		rl.setMargins(60 * 1920 / 1280, 151 * 1920 / 1280, 0, 0);// 设置偏移值，(74*151)
		PreviewVideo.setLayoutParams(rl);
		ivMusicbg.setLayoutParams(rl);
		preLoading.setLayoutParams(rl);
		downloadProgressBar.setLayoutParams(rl);
		
		listView_songlist = (ListView) findViewById(R.id.list_song);
		listView_songlist.setFocusable(false);
		// tvEmpty = (TextView) findViewById(R.id.tv_empty);
		empty_layout = (LinearLayout) findViewById(R.id.tv_empty);
		empty_view_wx = (ImageView) findViewById(R.id.iv_weixin);

		listView_songlist.setEmptyView(empty_layout);
		songs = new ArrayList<SongSearchBean>();
		adapter_songlist1 = new MyAdapter_songlist1(MainActivity.this, songs);
		listView_songlist.setAdapter(adapter_songlist1);
		listView_songlist.setItemsCanFocus(true);
		// adapter_songlist1.refresh();
		fragmentHander.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				adapter_songlist1.refresh();
				currentpage[0]=1;
			}
		}, 100);

		OSDinit = false;
		searchView = (SoftInputSearchView) findViewById(R.id.searchView);
		searchView.setHandler(fragmentHander);
		fragmentContainer = (FrameLayout) findViewById(R.id.center_fragment);
		pip_fragment = (FrameLayout) findViewById(R.id.pip_fragment);
		top_botton_ln = (LinearLayout) findViewById(R.id.top_botton);
		showViewInInit();
	}

	public void VideoViewFullScreen() {
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				192 * 10, 108 * 10);
		rl.addRule(RelativeLayout.ALIGN_LEFT);// 左对齐，RelativeLayout上下左右对齐的常量，按自己需要
		rl.setMargins(0, 0, 0, 0);//
		PreviewVideo.setLayoutParams(rl);
		ivMusicbg.setLayoutParams(rl);
		preLoading.setLayoutParams(rl);
		downloadProgressBar.setLayoutParams(rl);
		VideoViewFullScreen = true;
		hideViewInFullscreen();
	}

	@SuppressLint("NewApi")
	public void initFragment() {

		FragmentManager fManager_temp = getFragmentManager();

		int count = fManager_temp.getBackStackEntryCount();
		while (fManager_temp.popBackStackImmediate()) {
			Log.i("====JL====", "popBackStackImmediate");
		}

		FragmentManager fManager = getFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out);
		fg = new HomeFragment(fragmentHander);
		transaction.replace(R.id.center_fragment, fg);
		transaction.commit();
	}

	public void hideViewInFullscreen() {
		isFromHome = false;
		if (MyApplication.scrolling_switch.equals("1"))
			subtitleView.setVisibility(View.GONE);
		mDanmakuView.setVisibility(View.VISIBLE);

		// mNoticeImage.setVisibility(View.VISIBLE);
		if (isOnScore) {
			mLineView.setVisibility(View.VISIBLE);
			ScorePicView.setVisibility(View.VISIBLE);
		}
		/*
		 * listView.setVisibility(View.VISIBLE);
		 * listView_key.setVisibility(View.VISIBLE);
		 * listView_type.setVisibility(View.VISIBLE);
		 * listView_sort.setVisibility(View.VISIBLE);
		 * listView_main.setVisibility(View.VISIBLE);
		 */
		Log.i("song", "全屏隐藏======");

		listView_songlist.setVisibility(View.GONE);
		empty_layout.setVisibility(View.GONE);
		searchView.setVisibility(View.GONE);
		fragmentContainer.setVisibility(View.GONE);
		pip_fragment.setVisibility(View.GONE);
		top_botton_ln.setVisibility(View.GONE);
		button_pip.setVisibility(View.GONE);
		btnPhone.setVisibility(View.GONE);
		btnSearch.setVisibility(View.GONE);
		wifiBotton.setVisibility(View.GONE);
		btnDownload.setVisibility(View.GONE);
		preLoading.setVisibility(View.GONE);
		showBar();

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(180,
				225);
		rl.addRule(RelativeLayout.CENTER_IN_PARENT);
		mMainNoticeLayout.setLayoutParams(rl);

		VideoViewFullScreen = true;
		OSDinit = true;

		pb.setVisibility(View.GONE);
	}

	public void showViewInInit() {
		subtitleView.setVisibility(View.GONE);
		mDanmakuView.setVisibility(View.GONE);
		mLineView.setVisibility(View.GONE);
		ScorePicView.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		listView_key.setVisibility(View.GONE);
		listView_type.setVisibility(View.GONE);
		listView_sort.setVisibility(View.GONE);
		listView_main.setVisibility(View.GONE);
		empty_layout.setVisibility(View.GONE);

		if ((MyApplication.isSongNameFragment || MyApplication.isSingerFragment)
				&& !isYidianListShow) {
			searchView.setVisibility(View.VISIBLE);
			searchView.refresh(false);
		} else {
			searchView.setVisibility(View.GONE);
		}
		fragmentContainer.setVisibility(View.VISIBLE);
		pip_fragment.setVisibility(View.VISIBLE);
		top_botton_ln.setVisibility(View.VISIBLE);
		button_pip.setVisibility(View.VISIBLE);
		btnPhone.setVisibility(View.VISIBLE);
		btnSearch.setVisibility(View.VISIBLE);
		wifiBotton.setVisibility(View.VISIBLE);
		btnDownload.setVisibility(View.VISIBLE);
		dissMissBar();

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(180,225);
		rl.addRule(RelativeLayout.ALIGN_LEFT);
		rl.setMargins(320, 300, 0, 0);
		mMainNoticeLayout.setLayoutParams(rl);

		VideoViewFullScreen = false;
		OSDinit = false;

		if (!MyApplication.isSearchViewShow){
			listView_songlist.setVisibility(View.VISIBLE);
			adapter_songlist1.refresh();
			currentpage[0]=1;
		}else{
			listView_songlist.setVisibility(View.GONE);
		}
	}

	public void showWeixinCode() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				do {
					weixinCode = JNILib.GetWechatTip(1);
					SystemClock.sleep(1000);
					Log.i("song", "weixinCode==" + weixinCode);
				} while (!weixinCode.contains("id"));

				runOnUiThread(new Runnable() {
					public void run() {
						try {
							// SystemClock.sleep(2000);
							if (!TextUtils.isEmpty(weixinCode)) {
								pb.setVisibility(View.GONE);
								Bitmap wxCodeBitmap = EncodingHandler
										.createQRCode(weixinCode, 300);
								empty_view_wx.setImageBitmap(wxCodeBitmap);
								empty_view_wx.setVisibility(View.VISIBLE);

							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

			}
		}).start();

	}

	private int firstLoadPosition = -1;
	private boolean isOnloading = false;
	private String downloadSong,loadPositionSong;
	public class MyAdapter_songlist1 extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<SongSearchBean> songs;
		private int downloadProgress;
		
		private DownloadThread downloadThread;
		private int downStatus;
		private Handler uiHandler = new Handler(getMainLooper());

		public MyAdapter_songlist1(Context context,
				ArrayList<SongSearchBean> songs) {
			this.mInflater = LayoutInflater.from(context);
			this.songs = songs;
			downloadThread = new DownloadThread();
			//firstLoadPosition = -1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return songs.size()==0?0:5;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// ****************************************final方法
		// 注意原本getView方法中的int position变量是非final的，现在改为final
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int type = getItemViewType(position);
			
			ViewHolder_type holder_type = new ViewHolder_type();
			final LastViewHolder_type holder_type3 = new LastViewHolder_type();
			TopViewHolder_type holder_type2 = new TopViewHolder_type();
			
			// if (convertView == null) {
			switch (type) {
			case 1:
					convertView = mInflater.inflate(R.layout.yidian_top_songl, null);
					holder_type2.top_title = (Button) convertView
							.findViewById(R.id.top_back);
					holder_type2.marqueeTv = (MarqueeTextView) convertView
							.findViewById(R.id.top_info);
					convertView.setTag(holder_type2);
					holder_type2.icon = (ImageView) convertView.findViewById(R.id.music_3);										
					
					if(position < songs.size()){
						final SongSearchBean bean = songs.get(position);
						Log.i("song","select first item is cloud=="+bean.isCloud()+"=="+bean.getSong()+"=="+downloadSong);
						if(bean.isCloud()){
							holder_type2.icon.setBackgroundResource(R.drawable.cloud_select);
							if(!isOnloading || bean.getSong().equals(downloadSong)){
								setFirstLoadPosition(position);
								loadPositionSong = bean.getSong();
								isOnloading = true;	
							}
						}else{
							holder_type2.icon.setBackgroundResource(R.drawable.music_3);
						}
				holder_type2.marqueeTv.setText(bean.getSong());
				if(currentpage[0]==1)
				holder_type2.marqueeTv.setTextColor(Color.parseColor("#00f6ff"));
				holder_type2.top_title.setText(getResources().getString(
						R.string.yidian_1)
						+ SongJsonParseUtils.getTotal()
						+ getResources().getString(R.string.yidian_2));
				holder_type2.top_title
						.setTextColor(Color.parseColor("#ffffff"));
				
				holder_type2.top_title.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if(arg1){
							isSelectFocus = true;
						}
					}
				});
				
				
			/*	holder_type2.top_title.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
						// TODO Auto-generated method stub
						if(arg2.getAction()==KeyEvent.ACTION_DOWN){
							isSelectFocus = false;
						View view = listView_songlist.getChildAt(1);
							if(view!=null && arg2.getKeyCode()==20){
								Button btn = (Button)view.findViewById(R.id.view_btn5);
								Log.i("song","select item 2=="+btn);
								if(btn != null){
									btn.requestFocusFromTouch();
									btn.setFocusableInTouchMode(true);
									btn.setFocusable(true);
									btn.requestFocus();
									Log.i("song","select item 2 btn request focus==");
									return true;
									
								}
								
							}
						//	sendBroadcast(new Intent("justlink.intent.action.ban_home_focus"));
							Log.i("song","select action down======");
						}
						return false;
					}
				});*/
				
				holder_type2.top_title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {// 全部已点
								
								MyApplication.currentSinger = "/"
										+ getResources().getString(
												R.string.yidian_1);
								SetSearchEnterAndLayerType(31, 0);
								replaceFragment(R.id.center_fragment,
										new SongNameFragment(fragmentHander,
												31, 0));
								hideSearchSoftInput();
							}
						});
			
				
					}
				break;

			case 2:
				
				// 可以理解为从vlist获取view 之后把view返回给ListView
					convertView = mInflater.inflate(R.layout.yidian_type_slist, null);											
					holder_type.info = (MarqueeTextView) convertView
							.findViewById(R.id.top_info);				
					holder_type.imageView = (ImageView) convertView
							.findViewById(R.id.music_3);           
					holder_type.chabo = (Button) convertView
							.findViewById(R.id.view_btn5);
					holder_type.youxian = (Button) convertView
							.findViewById(R.id.view_btn3);
					convertView.setTag(holder_type);
					if(position < songs.size()){
						holder_type.info.setVisibility(View.VISIBLE);
						holder_type.imageView.setVisibility(View.VISIBLE);
						holder_type.chabo.setVisibility(View.VISIBLE);
						holder_type.youxian.setVisibility(View.VISIBLE);
						final SongSearchBean bean2 = songs.get(position);
				holder_type.info.setTextColor(Color.parseColor("#e4e4e4"));
				if (bean2.isCloud()) {
					if(!isOnloading || bean2.getSong().equals(downloadSong)){
						Log.i("song","select 2=="+bean2.getSong()+"=="+downloadSong);
						setFirstLoadPosition(position);
						loadPositionSong = bean2.getSong();
						isOnloading = true;	
					}
					holder_type.chabo.setVisibility(View.GONE);
					holder_type.youxian.setVisibility(View.GONE);
					holder_type.imageView
							.setBackgroundResource(R.drawable.cloud_select);
					holder_type.info.setText(bean2.getSong() + "("
							+ getResources().getString(R.string.wait_loading)
							+ ")");
				} else {
					holder_type.chabo.setVisibility(View.VISIBLE);
					holder_type.youxian.setVisibility(View.VISIBLE);
					holder_type.imageView
							.setBackgroundResource(R.drawable.music_3);
					holder_type.info.setText(bean2.getSong());
				}

				holder_type.chabo.setOnClickListener(new OnClickListener() {// 插播

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								SongJsonParseUtils.selectSong("song_order",
										bean2.getSongNumber(), 1,
										bean2.getOrderId());
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 6);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									// Log.i(TAG,
									// "====JSL==== cmdjObject = "+cmdjObject.toString()
									// );
									// Log.i(TAG,
									// "====JSL==== cmdjObject res= "+res );
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

				holder_type.youxian.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						SongJsonParseUtils.selectSong("song_order",
								bean2.getSongNumber(), 1, bean2.getOrderId());
						refresh();
					}
				});
				
				//holder_type.chabo.setOnKeyListener(new MyKeyDownListener(position));
				//holder_type.youxian.setOnKeyListener(new MyKeyDownListener(position));
				}else{
					holder_type.info.setVisibility(View.INVISIBLE);
					holder_type.imageView.setVisibility(View.INVISIBLE);
					holder_type.chabo.setVisibility(View.INVISIBLE);
					holder_type.youxian.setVisibility(View.INVISIBLE);
				}
				break;
			case 3:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				
					convertView = mInflater.inflate(R.layout.select_last_item, null);
					
					holder_type3.page = (TextView) convertView
							.findViewById(R.id.page);
					holder_type3.pre_page = (Button) convertView
							.findViewById(R.id.pre_page);
					holder_type3.next_page = (Button) convertView
							.findViewById(R.id.next_page);

					if(!pipPageUp)
					holder_type3.pre_page.setFocusable(false);			
					
					holder_type3.next_page.setOnKeyListener(new OnKeyListener() {
						
						@Override
						public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
							// TODO Auto-generated method stub
							if(arg2.getAction()==KeyEvent.ACTION_DOWN && arg2.getKeyCode()==21){
								holder_type3.pre_page.setFocusable(true);
							}else if(arg2.getAction()==KeyEvent.ACTION_DOWN && arg2.getKeyCode()==22){
								pipPageDown = false;
								pipPageUp = false;
								sendBroadcast(new Intent("justlink.intent.action.homefragment_requst_focus"));
							}
							return false;
						}
					});
					convertView.setTag(holder_type3);
				
				if (SongJsonParseUtils.getTotal()%4 == 0) {
					page[0] = SongJsonParseUtils.getTotal()/4;
					Log.i("song", "4 times count==" + page[0]);
				} else {
					page[0] = SongJsonParseUtils.getTotal()/4 + 1;
					Log.i("song", "4 times count +1 ==" + page[0]);
				}
				if(currentpage[0]==0)
					currentpage[0] = 1;
				if(holder_type3.page != null)
				holder_type3.page.setText(currentpage[0]+"/" + page[0]);
				holder_type3.next_page
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								pipPageDown = true;
								pipPageUp = false;
								if (currentpage[0] == page[0]){
									/*ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas2(
											0, "", 0, 31, 0,0, 4, "");
									refreshPage(list);*/
									return;
								}
								currentpage[0] += 1;
							//	Log.i("song", "next click currentpage ==" + currentpage[0]);
								holder_type3.page.setText(currentpage[0] + "/"
										+ page[0]);
								ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
										0, "", 0, 31, 0, (currentpage[0]-1)*4, 4, "");
								refreshPage(list,holder_type3.next_page);

							}
						});
				holder_type3.pre_page.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						pipPageUp = true;
						pipPageDown = false; 
						if (page[0] == 1 || currentpage[0] == 1){
							ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
									0, "", 0, 31, 0, 0, 4, "");
									refreshPage(list,holder_type3.pre_page);						
							return;
						}
						currentpage[0] -= 1;
						ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
								0, "", 0, 31, 0, ((currentpage[0]-1)*4), 4, "");
						
						//Log.i("song", "pre click currentpage ==" + currentpage[0]);
						holder_type3.page.setText(currentpage[0] + "/"
								+ page[0]);
						refreshPage(list,holder_type3.pre_page);

					} 
				});
				break;
			}
		
			return convertView;
		}

		// 每个convert view都会调用此方法，获得当前�?��要的view样式
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int p = position;
			if (p == 0)
				return 1;
			else if (p == 4)
				return 3;
			else
				return 2;
		}

		public void refresh() {
			if (VideoViewFullScreen || !isYidianListShow)
				return;
			Log.i("song", "refresh has select list========= ");
			ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
					0, "", 0, 31, 0, 0, 4, "");
			if (list == null || list.size() == 0) {
				empty_layout.setVisibility(View.VISIBLE);
				listView_songlist.setVisibility(View.GONE);
				
			} else {
				empty_layout.setVisibility(View.GONE);

				listView_songlist.setVisibility(View.VISIBLE);
				songs.clear();
				songs.addAll(list);
				notifyDataSetChanged();

			}
		}

		private void refreshPage(ArrayList<SongSearchBean> datas,Button btn){
			this.songs = datas;
			notifyDataSetChanged();
			onClickPageButton(listView_songlist,btn);
		}		
		
		
		public void onClickPageButton(final ListView listview,Button btn) {
		uiHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (pipPageDown || pipPageUp) {
					listview.setSelection(4);
					View item = (View) listview.getChildAt(4);
					Button next = (Button) item.findViewById(R.id.next_page);
					Button pre = (Button) item.findViewById(R.id.pre_page);
					if (pipPageDown) {
						pre.clearFocus();
						next.requestFocus();
						pipPageDown = false;
						pre.setFocusable(true);
					}

					if (pipPageUp) {
						next.clearFocus();
						pre.requestFocus();
						pipPageUp = false;
					}
				}
			}
		}, 50);

		}
		
		@SuppressLint("NewApi")
		private void replaceFragment(int id, Fragment fragment) {
			FragmentManager fManager = getFragmentManager();
			FragmentTransaction transaction = fManager.beginTransaction();
			transaction.setCustomAnimations(android.R.animator.fade_in,
					android.R.animator.fade_out);
			transaction.replace(id, fragment);
			transaction.addToBackStack("yidian_fragment");
			transaction.commit();
			fManager.executePendingTransactions();
		}

		private void setFirstLoadPosition(int position) {
		//	Log.i("song","设置当前下载位置====="+position);
			firstLoadPosition = position;
		}

		// 刷新某一条item
		private void refreshItemView(final int downloadProgress,final String songname,final int downStatus) {
			// TODO Auto-generated method stub
			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
				//	Log.i("song", "first load position=="+firstLoadPosition);
					View firstRefreshView = listView_songlist
							.getChildAt(firstLoadPosition);
					if (firstRefreshView!=null && downloadSong.equals(loadPositionSong)) {
						MarqueeTextView titel = (MarqueeTextView) firstRefreshView.findViewById(R.id.top_info);
						Button select = (Button)firstRefreshView.findViewById(R.id.top_back);
						if(downStatus==0){
							titel.setText(songname+"("+getResources().getString(R.string.on_loading)+ downloadProgress + "%)");	
						}else{
							titel.setText(songname+"("+getResources().getString(R.string.download_failed));
							isOnloading = false;
							firstLoadPosition = -1;
						}
						Log.i("song","is select focus=="+isSelectFocus+"==button=="+select);
						if(isSelectFocus && select!=null){
							select.requestFocus();
						}
						
					} else {
						Log.i("song", "firstview == null===firstLoadPositon==="
								+ firstLoadPosition);
					}
				}
			});

		}

		public class DownloadThread extends Thread {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("song","download thread start=========");
				while (true) {
					//Log.i("song","isonloading========"+isOnloading);
					if(isOnloading){
						String json = JNILib.getSongDownPercent();
						//Log.i("song", "download percent===" + json);
						try {
							if (json!=null && json.contains("down_percent")) {
								JSONObject object = new JSONObject(json);
								downloadProgress = object.getInt("down_percent");
								downloadSong = object.getString("song_name");
								downStatus = object.getInt("cloud_status");
								refreshItemView(downloadProgress,downloadSong,downStatus);
							//	Log.i("song","oldprogress=="+oldDownProgress+"==currentprogress=="+downloadProgress);
									if(oldDownProgress > downloadProgress){
									fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
									isOnloading = false;
									}
									oldDownProgress = downloadProgress;
								SystemClock.sleep(1000);
							} else {
								isOnloading = false;
								firstLoadPosition = -1;
								fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_YIDIAN_LIST);
								//TODO
								runOnUiThread(new Runnable() {//第一次点歌，下载完成后自动播放
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
												0, "", 0, 31, 0, 0, 4, "");
										if(list!=null && list.size()==1)
											playVideoNext();	
									}
								});
								
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
						
					}
					SystemClock.sleep(1000);
					// Log.i("song","downloadProgress======="+JNILib.getSongDownPercent());
				}
				// super.run();
			}
		}
		
		public class MyListFocusChangeListener implements OnFocusChangeListener{

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS);
				}
			}
			
		}
		
		public class MyKeyDownListener implements OnKeyListener{
			private int position = -1;
			public MyKeyDownListener(int pos){
				position = pos;
			}
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg2.getAction()==KeyEvent.ACTION_DOWN && arg2.getKeyCode()==20){
					if(position<3){
						View view = listView_songlist.getChildAt(position+1);
						if(view!=null ){
							Button btn = (Button)view.findViewById(R.id.view_btn5);
							Log.i("song","select item positon=="+position);
							if(btn != null){
								btn.requestFocusFromTouch();
								btn.setFocusableInTouchMode(true);
								btn.setFocusable(true);
								btn.requestFocus();
								Log.i("song","select item 3 btn request focus==");
								return true;
								
							}
							
						}else{
						Button btn =(Button) listView_songlist.getChildAt(4).findViewById(R.id.next_page);
						btn.requestFocusFromTouch();
						btn.setFocusableInTouchMode(true);
						btn.setFocusable(true);
						btn.requestFocus();
						Log.i("song","select item 3 btn request focus==");
						return true;
						}	
					}else{
						Button btn =(Button) listView_songlist.getChildAt(4).findViewById(R.id.next_page);
						btn.requestFocusFromTouch();
						btn.setFocusableInTouchMode(true);
						btn.setFocusable(true);
						btn.requestFocus();
						Log.i("song","select item 4 btn request focus==");
						return true;
					}
					
				}
				return false;
			}
			
		}
		

	}

	public void BarrageitemAdd(IDanmakuItem item) {

		Log.v(TAG, "====JSL====, mDanmakuView = " + mDanmakuView.getWidth());
		mDanmakuView.addItemToHead(item);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if ((MotionEvent.ACTION_DOWN == event.getAction())
				|| (MotionEvent.ACTION_MOVE == event.getAction())) {
			Log.i("onTouchEvent", "====JSL==== onTouchEvent= ");
			if (OSDinit) {
				if (!SeekBarshow) {
					if (!OSDshow)
						showBar();
				} else {

				}
			}
			return false;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("onKeyUp", "====JSL==== onKeyDown= " + keyCode
				+ "currentSongListPosition==" + currentSongListPosition);
		isSeekbarTraking = false;
		SingerFragment.onKeyUp(keyCode, event);
		SongNameFragment.onKeyUp(keyCode, event);
		if (keyCode == 19 && currentSongListPosition == 7
				&& listView.getVisibility() == 0 && enterType != 40) {
			listView.requestFocus();
			listView.requestFocusFromTouch();
			listView.setSelection(currentPageCount);

			return true;

		}

		return super.onKeyUp(keyCode, event);
	}

	
	private long lastPressTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("onKeyDown", "====JSL==== onKeyDown= " + keyCode);
		if (keyCode != 4
				&& (MyApplication.isSongNameFragment || MyApplication.isSingerFragment || MyApplication.isRecommendFragment)) {
			songFragment.onKeyDown(keyCode, event);
			singerFragment.onKeyDown(keyCode, event);
			recommendSongNameFragment.onKeyDown(keyCode, event);
		}

		if (keyCode == 8 || keyCode == 132) {
			// Log.i("VideoViewFullScreen1", "====JSL==== VideoViewFullScreen= "
			// +VideoViewFullScreen);
			if (VideoViewFullScreen) {
				VideoViewInit();

			} else {
				VideoViewFullScreen();
				OSDPlayTime = 15;
				NoticePicPlayTime = 15;
			}
		} else if (keyCode == 24 || keyCode == 25) {
			mMainNoticeLayout.setVisibility(View.GONE);
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((!TextUtils.isEmpty(MyApplication.searchKey)
					|| (!MyApplication.isInHomeFragment && MyApplication.isOnSelectSong))
					&& !VideoViewFullScreen) {
				//fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);
				fragmentHander
						.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
				MyApplication.currentSingerNum = "";
				MyApplication.isOnSelectSong = false;
				searchView.clear(true);
				Log.i("song", "clear2222222");
				if (!isSearchViewShow)
					showSearchSoftInput();
				etInput.setText("");
				etInput.setVisibility(View.GONE);
				Log.i("song", "back1111111111======");
				return true;
			}

			if (VideoViewFullScreen) {
				Log.i("song", "back222222222======");
				if (listView_main.getVisibility()==0 || isOrderClick) {
					dissMissOSD();
					isOrderClick = false;
				} else {
					isPageDown = false;
					isPageUp = false;
					pipPageDown = false;
					pipPageUp = false;
					if (OSDshow) {
						// Log.i(TAG,
						// "====JSL==== KEYCODE_BACK =OSDshow= "+OSDshow );
						try {
							if(isVodOrderSelect){
								isVodOrderSelect = false;
								cmdjObject = new JSONObject(cmd);
								cmdjObject.put("key", 0);
								cmdjObject.put("cmd", 3);
								JNILib.getTvData(cmdjObject.toString());
							}
							cmdjObject = new JSONObject(cmd);
							cmdjObject.put("key", 0);
							cmdjObject.put("cmd", 3);
							String res = JNILib
									.getTvData(cmdjObject.toString());
							Log.i(TAG, "====JSL==== NILib.getTvData=1 ");
							OsdConsole(res, 0);
							// Log.i(TAG,
							// "====JSL==== cmdjObject = "+cmdjObject.toString()
							// );
							// Log.i(TAG, "====JSL==== cmdjObject res= "+res );
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (SeekBarshow) {
						dissMissBar();
					} else {
						VideoViewInit();
					}

					return false;

				}
			} else if (MyApplication.isInHomeFragment && !VideoViewFullScreen) {// pip
				Log.i("song", "back333333333======"); // 界面返回时焦点到pip�?
				button_pip.requestFocus();
				
				if(System.currentTimeMillis()-lastPressTime<1000){
					MyApplication.isInHomeFragment = false;
					android.os.Process.killProcess(android.os.Process.myPid());
				}else{
					lastPressTime = System.currentTimeMillis();
					Toast.makeText(MainActivity.this,getResources().getString(R.string.exit_tip), Toast.LENGTH_SHORT).show();
				}
				
				
			}
			Log.i("song", "back44444444======");
			getFragmentManager().popBackStack();
			return false;
		} else if (keyCode == 82) {// 点歌台
			OSDPlayTime = 15;
			NoticePicPlayTime = 15;
			if (VideoViewFullScreen) {
				if (listView_main.getVisibility() == 0) {
					dissMissOSD();
				} else {
					String res = JNILib.getTvData(cmd);
					// Log.i(TAG, "====JSL==== NILib.getTvData=2 ");
					OsdConsole(res, 0);
					// Log.i(TAG, "====JSL==== KEYCODE_MENU =OSDshow= ");
				}
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mediaControllerLayout.getVisibility() != 0) {
				// return super.onKeyDown(keyCode, event);
				if (!SeekBarshow && VideoViewFullScreen) {
					if (!OSDshow)
						showBar();
					isSeekbarTraking = false;
					return true;
				}
			} else {
				OSDPlayTime = 15;
				NoticePicPlayTime = 15;
				if (isSeekBarSelected) {
					// MySeekBar.setCanMove(true);
					// btnLinearLayout.getChildAt(defaultFocus).requestFocus();
					// isSeekBarSelected=false;
				} else {
					videoSeekBar.requestFocus();
					isSeekBarSelected = true;
				}

			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

			if (MyApplication.isPipFocus && isSearchViewShow
					&& !isYidianListShow) {
				fragmentHander
						.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_SEARCHVIEW);
				Log.i("song", "keydown focus searchView====");
				return true;
			} else if (MyApplication.isPipFocus && isYidianListShow) {
				fragmentHander
						.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_UNFOCUS_SEARCHVIEW);
				fragmentHander
						.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_FOCUS_YIDIAN);
				Log.i("song", "keydown focus yidian====");
				return true;
			}
			if (mediaControllerLayout.getVisibility() != 0) {
				// return super.onKeyDown(keyCode, event);
				if (!SeekBarshow && VideoViewFullScreen) {
					if (!OSDshow)
						showBar();
					isSeekbarTraking = false;
					// return false;
				}
			} else {
				OSDPlayTime = 15;
				NoticePicPlayTime = 15;
				if (isSeekBarSelected) {
					// MySeekBar.setCanMove(true);
					// btnLinearLayout.getChildAt(defaultFocus).requestFocus();
					qiege_button.requestFocus();
					isSeekBarSelected = false;
				} else {
					// videoSeekBar.requestFocus();
					// isSeekBarSelected=true;
				}

			}

			if (listView.getVisibility() == 0 && currentKeyPosition == 7
					&& currentPageCount < 6) {// 处理空白item
				listView.requestFocus();
				listView.requestFocusFromTouch();
				listView.setSelection(7);

			}

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 23) {
			OSDPlayTime = 15;
			NoticePicPlayTime = 15;

			if (VideoViewFullScreen) {
			/*	try {//暂停
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 0);
					cmdjObject.put("cmd", 7);
					String res = JNILib.getTvData(cmdjObject.toString());
					// Log.i("song", "pause 点击 = "+cmdjObject.toString() );
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				if (!SeekBarshow) {
					if (!OSDshow)
						showBar();
					isSeekbarTraking = false;
					// return false;
				} else {
					// Log.i(TAG,
					// "====JSL==== onKeyDown() onKeyDown=defaultFocus= "+defaultFocus);
				}
				return false;
			}
		} else if (keyCode == 1004) {// 重唱 KEY_SUBTITLE
			
			OSDPlayTime = 15;
			NoticePicPlayTime = 15;
			try {
				cmdjObject = new JSONObject(cmd);
				cmdjObject.put("key", 0);
				cmdjObject.put("cmd", 5);
				String res = JNILib.getTvData(cmdjObject.toString());
				// Log.i(TAG, "====JSL==== cmdjObject = "+cmdjObject.toString()
				// );
				// Log.i(TAG, "====JSL==== cmdjObject res= "+res );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (keyCode == 167) {// 切歌 KEY_CHANNELDOWN
			OSDPlayTime = 15;
			NoticePicPlayTime = 15;
			mLineView.Destroy();
			mLineView.setVisibility(View.GONE);
			ScorePicView.setVisibility(View.GONE);
			try {
				cmdjObject = new JSONObject(cmd);
				cmdjObject.put("key", 0);
				cmdjObject.put("cmd", 6);
				String res = JNILib.getTvData(cmdjObject.toString());
				// Log.i(TAG, "====JSL==== cmdjObject = "+cmdjObject.toString()
				// );
				// Log.i(TAG, "====JSL==== cmdjObject res= "+res );
			} catch (Exception e) {
				e.printStackTrace();
			}

			// adapter_songlist1.refresh();//刷新已点列表

		} else if (keyCode == 1010) {// 原伴�?KEY_AUDIO
			OSDPlayTime = 15;
			NoticePicPlayTime = 15;
			try {
				cmdjObject = new JSONObject(cmd);
				cmdjObject.put("key", 0);
				cmdjObject.put("cmd", 8);
				String res = JNILib.getTvData(cmdjObject.toString());
				// Log.i(TAG,
				// "====JSL==== cmdjObject = "+cmdjObject.toString());
				// Log.i(TAG, "====JSL==== cmdjObject res= "+res );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (keyCode == 21 || keyCode == 22) {
			if (!SeekBarshow && VideoViewFullScreen && !OSDshow) {
				showBar();
				videoSeekBar.requestFocus();
			}
			OSDPlayTime = 15;
			NoticePicPlayTime = 15;
			// fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);
		} else if (keyCode == 131) {// 已点
			if (VideoViewFullScreen) {
				try {
					String res = JNILib.getTvData(cmd);
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 11);
					cmdjObject.put("cmd", 2);
					res = JNILib.getTvData(cmdjObject.toString());
					// Log.i(TAG, "====JSL==== NILib.getTvData=8 ");
					OsdConsole(res, 0);
					// Log.i(TAG,
					// "====JSL==== cmdjObject = "+cmdjObject.toString() );
					// Log.i(TAG, "====JSL==== cmdjObject res= "+res );
					dissMissBar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (keyCode == 164) {// 静音
			isMute = !isMute;
			if (isMute) {
				setNoticeImage(JLINK_NOTICE_TYPE_MUTE, 0);
			} else {
				setNoticeImage(JLINK_NOTICE_TYPE_UNMUTE, 0);
			}
		} else if (keyCode == 67 && isSearchViewShow) {// 删除
			searchView.back();

		}
		if (fg instanceof HomeFragment) {
			if (true == HomeFragment.onKeyDown(keyCode, event)) {
				return true;
			}
		}

		// songFragment.onKeyDown(keyCode,event);
		// singerFragment.onKeyDown(keyCode,event);
		return super.onKeyDown(keyCode, event);
	}

	public void MountSamba(String cmd) {

	}

	public void initOSD() {
		// Log.v(TAG,"====JSL====, initOSD =============================== " );
		// mGridView = (GridView) findViewById(R.id.key_grid_view);

		main_info1[0] = getResources().getString(R.string.menu_main_1);
		main_info1[1] = getResources().getString(R.string.menu_main_3);
		main_info1[2] = getResources().getString(R.string.menu_main_5);
		main_info1[3] = getResources().getString(R.string.menu_main_7);
		main_info2[0] = getResources().getString(R.string.menu_main_2);
		main_info2[1] = getResources().getString(R.string.menu_main_4);
		main_info2[2] = getResources().getString(R.string.menu_main_6);
		main_info2[3] = getResources().getString(R.string.menu_main_8);

		mMainNoticeLayout = (LinearLayout) findViewById(R.id.main_notice);
		mLoadingtxtLayout = (LinearLayout) findViewById(R.id.loading_txt);
		mNoticeImage = (ImageView) findViewById(R.id.main_notice_image);
		mNoticeText = (TextView) findViewById(R.id.main_notice_text);
		mLoading1Text = (TextView) findViewById(R.id.loading1_text);
		mLoading2Text = (TextView) findViewById(R.id.loading2_text);
		mappAddressImage = (ImageView) findViewById(R.id.main_app_image);
		logoImage = (TextView) findViewById(R.id.logo);
		mqrImage = (ImageView) findViewById(R.id.main_qr_image);
		mWinxinLinkImage = (ImageView) findViewById(R.id.weixin_qr_image);
		mappAddrtext = (TextView) findViewById(R.id.app_addr_info);
		mqrtext = (TextView) findViewById(R.id.qr_code);
		mwxinfotext = (TextView) findViewById(R.id.qr_weixin);

		// luyinTime = (TextView) findViewById(R.id.luyin_time);
		mqrinfotext = (TextView) findViewById(R.id.qr_info);
		recordText = (StrokeTextView) findViewById(R.id.ktv_luyin);
		tishiText = (StrokeTextView) findViewById(R.id.ktv_tishi);
		tishiText.bringToFront();
		// publictext= (StrokeTextView) findViewById(R.id.ktv_publicStatus);

		mLineView = (LineView) findViewById(R.id.line_view);
		mLineView.setHandler(handler);

		ScorePicView = (TextView) findViewById(R.id.score_view);

		jLanguage = JNILib.getGlobalLanguage();
		Log.i("CHW", "language===" + jLanguage);
		// mLoading1Text.setText(VideoString.Loading1[jLanguage]);
		mLoading2Text.setText(" ");
		mLoadingtxtLayout.setVisibility(View.VISIBLE);
		subtitleView = (MyTextView) findViewById(R.id.sub_view);
		// subtitleView.startMove();

		mediaControllerLayout = findViewById(R.id.mediaControllerLayout);
		btnLinearLayout = (LinearLayout) findViewById(R.id.btnLinearLayout);
		videoSeekBar = (MySeekBar) findViewById(R.id.videoSeekBar);
		videoSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
		app_button = (Button) findViewById(R.id.app_button);
		luyin_button = (Button) findViewById(R.id.luyin_button);
		chongchang_button = (Button) findViewById(R.id.chongchang_button);
		qiege_button = (Button) findViewById(R.id.qiege_button);
		play_pause = (Button) findViewById(R.id.play_pause);
		banchang_button = (Button) findViewById(R.id.banchang_button);
		yidian_button = (Button) findViewById(R.id.yidian_button);
		diange_button = (Button) findViewById(R.id.diange_button);
		gongju_button = (Button) findViewById(R.id.gongju_button);
		sound_button = (Button) findViewById(R.id.soundeffect_button);

		gongju_button.setVisibility(View.GONE);
		luyin_button.setVisibility(View.GONE);

		app_button_text = (TextView) findViewById(R.id.app_name);
		chongchang_button_text = (TextView) findViewById(R.id.chongchang_name);
		qiege_button_text = (TextView) findViewById(R.id.qiege_name);
		play_pause_text = (TextView) findViewById(R.id.play_name);
		banchang_button_text = (TextView) findViewById(R.id.banchang_name);
		yidian_button_text = (TextView) findViewById(R.id.yidian_name);
		diange_button_text = (TextView) findViewById(R.id.diange_name);
		timeText = (TextView) findViewById(R.id.timeText);
		timetotal = (TextView) findViewById(R.id.timetotal);
		listView_main = (ListView) findViewById(R.id.list_main);
		adapter_main = new MyAdapter_main(MainActivity.this);
		listView_main.setAdapter(adapter_main);
		listView_main.setItemsCanFocus(true);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new MyAdapter(MainActivity.this);
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(true);
		// listView.setOnItemClickListener(new MyOnItemClickListener());
		// listView.setOnItemSelectedListener(new MyOnItemSelectedListener());
		listView_key = (ListView) findViewById(R.id.list_key);
		adapter_key = new MyAdapter_key(MainActivity.this);
		listView_key.setAdapter(adapter_key);
		listView_key.setItemsCanFocus(true);
		listView_type = (ListView) findViewById(R.id.list_type);
		adapter_type = new MyAdapter_type(MainActivity.this);
		listView_type.setAdapter(adapter_type);
		listView_type.setItemsCanFocus(true);
		listView_sort = (ListView) findViewById(R.id.list_sort);
		adapter_sort = new MyAdapter_sort(MainActivity.this);
		listView_sort.setAdapter(adapter_sort);
		listView_sort.setItemsCanFocus(true);

		tishiText = (StrokeTextView) findViewById(R.id.ktv_tishi);
		tishiText.bringToFront();


		btnPhone = (Button) findViewById(R.id.qr_code_button);

		btnPhone.setOnClickListener(new View.OnClickListener() {//Local

			@Override
			public void onClick(View arg0) { // TODO Auto-generated
	
				SetSearchEnterAndLayerType(22, 0);

				FragmentManager fManager = getFragmentManager();
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);
				transaction.replace(R.id.center_fragment, new SongNameFragment(
						fragmentHander,22, 0));
				transaction.addToBackStack("local_fragment");
				transaction.commit();
				fManager.executePendingTransactions();

			}
		});
		
		wifiBotton = (Button) findViewById(R.id.lwifi_button);
		wifiBotton.setOnClickListener(new OnClickListener() {//Mine			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentManager fManager = getFragmentManager();
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);
				transaction.replace(R.id.center_fragment, new My_Fragment(fragmentHander));
				transaction.addToBackStack("my_fragment");
				transaction.commit();
				fManager.executePendingTransactions();				
			}
		});
		
		btnSearch = (Button) findViewById(R.id.lsearch_button);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetSearchEnterAndLayerType(99, 0);

				FragmentManager fManager = getFragmentManager();
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);
				transaction.replace(R.id.center_fragment, new SongNameFragment(
						fragmentHander, 99, 0));
				transaction.addToBackStack("search_fragment");
				transaction.commit();
				fManager.executePendingTransactions();

				showSearchSoftInput();

			}
		});

		btnDownload = (Button) findViewById(R.id.ldownload_button);
		btnDownload.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// SetSearchEnterAndLayerType(21, 2);
				// fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_SHOW_YIDIAN_LIST);
				MyApplication.currentSinger = "/"
						+ getResources().getString(R.string.ldownload_name);
				FragmentManager fManager = getFragmentManager();
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);
				transaction.replace(R.id.center_fragment, new SongNameFragment(
						fragmentHander, 21, 2));
				transaction.addToBackStack("download_fragment");
				transaction.commit();
				fManager.executePendingTransactions();

				// showSearchSoftInput();
			}
		});

		btnPhone.setOnFocusChangeListener(new TopButtonFocusListener());
		btnSearch.setOnFocusChangeListener(new TopButtonFocusListener());
		btnDownload.setOnFocusChangeListener(new TopButtonFocusListener());
		wifiBotton.setOnFocusChangeListener(new TopButtonFocusListener());
		chongchang_button.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * setNoticeImage(JLINK_NOTICE_TYPE_REPEAT,-1); RepeatVideo();
				 * SystemClock.sleep(1000); NoticePicPlayTime = 0;
				 */
				mLineView.Destroy();
				try {
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 0);
					cmdjObject.put("cmd", 5);
					String res = JNILib.getTvData(cmdjObject.toString());
					Log.i(TAG,
							"====JSL==== cmdjObject = " + cmdjObject.toString());
					Log.i(TAG, "====JSL==== cmdjObject res= " + res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		/*
		 * gongju_button.setOnClickListener(new NoDoubleClickListener() {
		 * 
		 * @Override void onNoDoubleClick(View v) { // TODO Auto-generated
		 * method stub if(tool_dialog == null) tool_dialog = new
		 * KTVToolDialog(MainActivity.this,"",fragmentHander,R.style.MyDialog);
		 * 
		 * tool_dialog.show(); } });
		 */

		yidian_button.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				// TODO Auto-generated method stub
				isOrderClick = true;
				try {
					String res = JNILib.getTvData(cmd);
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 11);
					cmdjObject.put("cmd", 2);
					res = JNILib.getTvData(cmdjObject.toString());
					// Log.i(TAG, "====JSL==== NILib.getTvData=8 ");

					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 1);
					cmdjObject.put("cmd", 2);
					res = JNILib.getTvData(cmdjObject.toString());

					OsdConsole(res, 0);
					dissMissBar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		banchang_button.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * setNoticeImage(JLINK_NOTICE_TYPE_BANCHANG,-1);
				 * 
				 * SystemClock.sleep(1000); NoticePicPlayTime = 0;
				 */

				try {
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 0);
					cmdjObject.put("cmd", 8);
					String res = JNILib.getTvData(cmdjObject.toString());
					// Log.i(TAG,
					// "====JSL==== cmdjObject = "+cmdjObject.toString() );
					// Log.i(TAG, "====JSL==== cmdjObject res= "+res );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		play_pause.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * if(PreviewVideo.isPlaying())
				 * setNoticeImage(JLINK_NOTICE_TYPE_PAUSE,-1); else
				 * setNoticeImage(JLINK_NOTICE_TYPE_UNPAUSE,-1);
				 * 
				 * pauseVideo();
				 * 
				 * SystemClock.sleep(1000); NoticePicPlayTime = 0;
				 */
				try {
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 0);
					cmdjObject.put("cmd", 7);
					String res = JNILib.getTvData(cmdjObject.toString());
					// Log.i("song", "pause 点击 = "+cmdjObject.toString() );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		qiege_button.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				/*
				 * setNoticeImage(JLINK_NOTICE_TYPE_NEXT,-1); RepeatVideo();
				 * 
				 * SystemClock.sleep(1000); NoticePicPlayTime = 0;
				 */
				isRecordMusic = false;
				isOnScore = false;
				// TODO Auto-generated method stub
				mLineView.Destroy();
				mLineView.setVisibility(View.GONE);
				ScorePicView.setVisibility(View.GONE);

				try {
					cmdjObject = new JSONObject(cmd);
					cmdjObject.put("key", 0);
					cmdjObject.put("cmd", 6);
					String res = JNILib.getTvData(cmdjObject.toString());
					Log.i(TAG,
							"====JSL==== cmdjObject = " + cmdjObject.toString());
					Log.i(TAG, "====JSL==== cmdjObject res= " + res);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!VideoViewFullScreen && !isSearchViewShow){
					adapter_songlist1.refresh();// 刷新已点列表
					currentpage[0]=1;
				}

			}

		});

		diange_button.setOnClickListener(new NoDoubleClickListener() {

			@Override
			void onNoDoubleClick(View v) {
				// TODO Auto-generated method stub
				try {
					String res = JNILib.getTvData(cmd);
					// Log.i(TAG, "====JSL==== NILib.getTvData=2 ");
					OsdConsole(res, 0);
					// Log.i(TAG, "====JSL==== KEYCODE_MENU =OSDshow= ");
					dissMissBar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		sound_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dissMissBar();
				if (soundDialog == null)
					soundDialog = new Sound_effect_dialog(MainActivity.this, R.style.SoundDialog);
				soundDialog.setSoundChangerListener(new OnsetSoundListener() {

					@Override
					public void onSetSound(int progress) {
						setTone(progress);
					}

				});

				soundDialog.setMicVolumeListener(new OnSetMicListener() {

					@Override
					public void onSetMic(int progress) { // TODO Auto-generated
						if(micphone != null)
						micphone.setVolume(progress);
					}
				});
				soundDialog.show();
			}
		});

		OSDinit = true;

		songFragment = new SongNameFragment();
		singerFragment = new SingerFragment(fragmentHander);
		recommendSongNameFragment = new RecommendSongNameFragment(fragmentHander, -1);

		pb = (ProgressBar) findViewById(R.id.pb_1);
		pb.setVisibility(View.GONE);
		etInput = (TextView) findViewById(R.id.et_input);
		etInput.setVisibility(View.GONE);

		downloadProgressBar = (FrameLayout) findViewById(R.id.download_progress);
		tvDownloadProgress = (TextView) findViewById(R.id.tv_download_progress);
		ivAnimation = (ImageView) findViewById(R.id.iv_animation);
		tvCurrentSinger = (TextView) findViewById(R.id.tv_current_singer);
		tvCurrentSong = (TextView) findViewById(R.id.tv_current_song);
		tvCurrentSongNum =  (TextView) findViewById(R.id.tv_current_song_num);
		ivMusicbg = (ImageView) findViewById(R.id.iv_music_bg);
		ivMusicbg.setVisibility(View.GONE);
		
		llNextPlay = (FrameLayout) findViewById(R.id.next_play_info);
		tvNowPlaying = (TextView) findViewById(R.id.tv_now_playing);
		tvNextPlay = (TextView) findViewById(R.id.tv_next_play);
		
		preLoading = (FrameLayout) findViewById(R.id.pre_pb);
		
	}

	// 规避短时间频繁点�?
	public abstract class NoDoubleClickListener implements View.OnClickListener {

		public static final int MIN_CLICK_DELAY_TIME = 800;
		private long lastClickTime = 0;

		@Override
		public void onClick(View v) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				// Log.i("CHW","点击生效==========");
				onNoDoubleClick(v);
			}
		}

		abstract void onNoDoubleClick(View v);

	}

	public class TopButtonFocusListener implements View.OnFocusChangeListener {
		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if (arg1)
				MyApplication.isTopButtonFocus = true;
			else
				MyApplication.isTopButtonFocus = false;
		}
	}

	OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {
		public void onStopTrackingTouch(SeekBar seekBar) {

			// startTime = System.currentTimeMillis();
			/*
			 * if(isRecordMusic){//TODO 录音回放禁止快进(AV不同�? /* //int current = (new
			 * BigDecimal( progress * 1.0 / seekBar.getMax() *
			 * mediaPlayerMusic.getDuration())).intValue(); int current =
			 * (int)Math.rint( progress * 1.0 / seekBar.getMax() *
			 * mediaPlayerMusic.getDuration());
			 * mediaPlayerMusic.seekTo(current); //int backMusic = (new
			 * BigDecimal(current/1000).setScale(0,
			 * BigDecimal.ROUND_HALF_UP)).intValue(); //int backMusic =
			 * (int)Math.rint(current); PreviewVideo.seekTo(current,true);
			 * //PreviewVideo.seekTo((int)(progress * 1.0 / seekBar.getMax() *
			 * PreviewVideo.getDuration()));
			 * Log.i("CHW","progress=="+progress+"max=="
			 * +seekBar.getMax()+"current=="+current+"video--current--");
			 */
			/*
			 * }else{ PreviewVideo.seekTo((int) (seekBarChangeProgress * 1.0 /
			 * seekBar.getMax() * PreviewVideo.getDuration())*1000,false); }
			 */
			isSeekbarTraking = false;
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			NoticePicPlayTime = 15;
			// isSeekbarTraking = true;
		}

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			seekBarChangeProgress = progress;			
			if (fromUser) {
				isSeekbarTraking = true;
				// startTime = System.currentTimeMillis();
				if (isRecordMusic || isRecording) {// TODO 录音回放禁止快进(AV不同�?
					/*
					 * //int current = (new BigDecimal( progress * 1.0 /
					 * seekBar.getMax() *
					 * mediaPlayerMusic.getDuration())).intValue(); int current
					 * = (int)Math.rint( progress * 1.0 / seekBar.getMax() *
					 * mediaPlayerMusic.getDuration());
					 * mediaPlayerMusic.seekTo(current); //int backMusic = (new
					 * BigDecimal(current/1000).setScale(0,
					 * BigDecimal.ROUND_HALF_UP)).intValue(); //int backMusic =
					 * (int)Math.rint(current);
					 * PreviewVideo.seekTo(current,true);
					 * //PreviewVideo.seekTo((int)(progress * 1.0 /
					 * seekBar.getMax() * PreviewVideo.getDuration()));
					 * Log.i("CHW"
					 * ,"progress=="+progress+"max=="+seekBar.getMax()
					 * +"current=="+current+"video--current--");
					 */
				} else {
					PreviewVideo
							.seekTo((int) (progress * 1.0 / seekBar.getMax() * PreviewVideo
									.getDuration()) * 1000, false);
				}
			} else {
				isSeekbarTraking = false;
			}
		}
	};

	private void showBar() {
		if (OSDinit) {
			OSDPlayTime = 15;
			// mediaControllerLayout.requestFocus();
			jLanguage = JNILib.getGlobalLanguage();
			app_button_text.setText(VideoString.APP[jLanguage]);
			chongchang_button_text.setText(VideoString.Repeat[jLanguage]);
			qiege_button_text.setText(VideoString.Next[jLanguage]);
			yidian_button_text.setText(VideoString.Order[jLanguage]);
			diange_button_text.setText(VideoString.Menus[jLanguage]);
			if (voice_mode == JLINK_VOICE_MOD_YUANCHANG) {
				banchang_button
						.setBackgroundResource(R.drawable.yuanchang_button);
				banchang_button_text.setText(VideoString.Original[jLanguage]);
			} else {
				banchang_button
						.setBackgroundResource(R.drawable.banchang_button);
				banchang_button_text.setText(VideoString.Accomp[jLanguage]);
			}
			if (isPause) {
				play_pause.setBackgroundResource(R.drawable.play_button);
				play_pause_text.setText(VideoString.Play[jLanguage]);
			} else {
				play_pause.setBackgroundResource(R.drawable.pause_button);
				play_pause_text.setText(VideoString.Stop[jLanguage]);
			}
			mediaControllerLayout.setVisibility(View.VISIBLE);
			// btnLinearLayout.getChildAt(defaultFocus).requestFocus();
			qiege_button.requestFocus();
			isSeekBarSelected = false;
			SeekBarshow = true;

			// 当前播放信息
			ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
					0, "", 0, 31, 0, 0, 1, "");
			if (list != null && list.size() > 0) {
				SongSearchBean currentSong = list.get(0);
				tvCurrentSong.setText(currentSong.getSong());
				tvCurrentSinger.setText(currentSong.getSinger());
				tvCurrentSongNum.setText("("+currentSong.getSongNumber()+")");
			}

			// Log.i(TAG, "====JSL==== showBar() = " );
		}
	}

	private void dissMissBar() {
		// mediaControllerLayout.requestFocus();
		mediaControllerLayout.setVisibility(View.GONE);
		SeekBarshow = false;
		// Log.i(TAG, "====JSL==== showBar() = " );
	}

	private void setSeekBar(int position) {

		videoSeekBar.setProgress(position);
		// Log.i(TAG, "====JSL====setBeekVar "+position);
		if (isRecordMusic) {
			timeText.setText(getTimeFormatValue(mediaPlayerMusic
					.getCurrentPosition() / 1000));
		} else {
			timeText.setText(getTimeFormatValue((int) PreviewVideo
					.getCurrentPosition()));
		}

		if (!isRecordMusic)
			timetotal.setText(getTimeFormatValue((int) PreviewVideo
					.getDuration()));
	}

	public String getTimeFormatValue(long time) {
		long t = time;

		return MessageFormat.format(
				"{0,number,00}:{1,number,00}:{2,number,00}", t / 60 / 60,
				t / 60 % 60, t % 60);
	}

	public void setPaoMaDeng(String msg) {
		if (null != subtitleView) {
			/*
			 * subtitleView.setVisibility(View.GONE); subtitleView.setText(msg);
			 * subtitleView.startFrom0();
			 */
			subtitleView.SeekTo0();
			subtitleView.SetMoveString(msg);
		}
	}

	private void setGpio() {

	}

	/*
	 * public void ScorePicShow(int score){
	 * ScorePicView.setVisibility(View.VISIBLE); ScorePicView.setScore(score,
	 * ScoreWithPicView.INCREMENT); }
	 */

	public void GifViewShow(int num) {
		if (gifShow) {
			GifViewDismiss();
		}
		gifPlayTime = 4;
		switch (num) {
		case 0:
			if (gf1 != null) {
				gf1.setVisibility(View.VISIBLE);
			} else {
				gf1 = (GifView) findViewById(R.id.gif1_view);
				gf1.setGifImage(R.drawable.gif1);
				gf1.setVisibility(View.VISIBLE);
			}
			break;
		case 1:
			if (gf2 != null) {
				gf2.setVisibility(View.VISIBLE);
			} else {
				gf2 = (GifView) findViewById(R.id.gif2_view);
				gf2.setGifImage(R.drawable.gif2);
				gf2.setVisibility(View.VISIBLE);
			}
			break;
		case 2:
			if (gf3 != null) {
				gf3.setVisibility(View.VISIBLE);
			} else {
				gf3 = (GifView) findViewById(R.id.gif3_view);
				gf3.setGifImage(R.drawable.gif3);
				gf3.setVisibility(View.VISIBLE);
			}
			break;
		case 3:
			if (gf4 != null) {
				gf4.setVisibility(View.VISIBLE);
			} else {
				gf4 = (GifView) findViewById(R.id.gif4_view);
				gf4.setGifImage(R.drawable.gif4);
				gf4.setVisibility(View.VISIBLE);
			}
			break;
		case 4:
			if (gf5 != null) {
				gf5.setVisibility(View.VISIBLE);
			} else {
				gf5 = (GifView) findViewById(R.id.gif5_view);
				gf5.setGifImage(R.drawable.gif5);
				gf5.setVisibility(View.VISIBLE);
			}
			break;
		case 5:
			if (gf6 != null) {
				gf6.setVisibility(View.VISIBLE);
			} else {
				gf6 = (GifView) findViewById(R.id.gif6_view);
				gf6.setGifImage(R.drawable.gif6);
				gf6.setVisibility(View.VISIBLE);
			}
			break;
		case 6:
			if (gf7 != null) {
				gf7.setVisibility(View.VISIBLE);
			} else {
				gf7 = (GifView) findViewById(R.id.gif7_view);
				gf7.setGifImage(R.drawable.gif7);
				gf7.setVisibility(View.VISIBLE);
			}
			break;
		case 7:
			if (gf8 != null) {
				gf8.setVisibility(View.VISIBLE);
			} else {
				gf8 = (GifView) findViewById(R.id.gif8_view);
				gf8.setGifImage(R.drawable.gif8);
				gf8.setVisibility(View.VISIBLE);
			}
			break;
		case 8:
			if (gf9 != null) {
				gf9.setVisibility(View.VISIBLE);
			} else {
				gf9 = (GifView) findViewById(R.id.gif9_view);
				gf9.setGifImage(R.drawable.gif9);
				gf9.setVisibility(View.VISIBLE);
			}
			break;
		case 9:
			if (gf10 != null) {
				gf10.setVisibility(View.VISIBLE);
			} else {
				gf10 = (GifView) findViewById(R.id.gif10_view);
				gf10.setGifImage(R.drawable.gif10);
				gf10.setVisibility(View.VISIBLE);
			}
			break;
		case 10:
			if (gf11 != null) {
				gf11.setVisibility(View.VISIBLE);
			} else {
				gf11 = (GifView) findViewById(R.id.gif11_view);
				gf11.setGifImage(R.drawable.gif11);
				gf11.setVisibility(View.VISIBLE);
			}
			break;
		case 11:
			if (gf12 != null) {
				gf12.setVisibility(View.VISIBLE);
			} else {
				gf12 = (GifView) findViewById(R.id.gif12_view);
				gf12.setGifImage(R.drawable.gif12);
				gf12.setVisibility(View.VISIBLE);
			}
			break;
		case 12:
			if (gf13 != null) {
				gf13.setVisibility(View.VISIBLE);
			} else {
				gf13 = (GifView) findViewById(R.id.gif13_view);
				gf13.setGifImage(R.drawable.gif13);
				gf13.setVisibility(View.VISIBLE);
			}
			break;
		case 13:
			if (gf14 != null) {
				gf14.setVisibility(View.VISIBLE);
			} else {
				gf14 = (GifView) findViewById(R.id.gif14_view);
				gf14.setGifImage(R.drawable.gif14);
				gf14.setVisibility(View.VISIBLE);
			}
			break;
		case 14:
			if (gf15 != null) {
				gf15.setVisibility(View.VISIBLE);
			} else {
				gf15 = (GifView) findViewById(R.id.gif15_view);
				gf15.setGifImage(R.drawable.gif15);
				gf15.setVisibility(View.VISIBLE);
			}
			break;
		case 15:
			if (gf16 != null) {
				gf16.setVisibility(View.VISIBLE);
			} else {
				gf16 = (GifView) findViewById(R.id.gif16_view);
				gf16.setGifImage(R.drawable.gif16);
				gf16.setVisibility(View.VISIBLE);
			}
			break;
		case 16:
			if (gf17 != null) {
				gf17.setVisibility(View.VISIBLE);
			} else {
				gf17 = (GifView) findViewById(R.id.gif17_view);
				gf17.setGifImage(R.drawable.gif17);
				gf17.setVisibility(View.VISIBLE);
			}
			break;
		case 17:
			if (gf18 != null) {
				gf18.setVisibility(View.VISIBLE);
			} else {
				gf18 = (GifView) findViewById(R.id.gif18_view);
				gf18.setGifImage(R.drawable.gif18);
				gf18.setVisibility(View.VISIBLE);
			}
			break;
		}
		gifShow = true;
		// }

	}

	public void GifViewDismiss() {
		if (gf1 != null)
			gf1.setVisibility(View.GONE);
		if (gf2 != null)
			gf2.setVisibility(View.GONE);
		if (gf3 != null)
			gf3.setVisibility(View.GONE);
		if (gf4 != null)
			gf4.setVisibility(View.GONE);
		if (gf5 != null)
			gf5.setVisibility(View.GONE);
		if (gf6 != null)
			gf6.setVisibility(View.GONE);
		if (gf7 != null)
			gf7.setVisibility(View.GONE);
		if (gf8 != null)
			gf8.setVisibility(View.GONE);
		if (gf9 != null)
			gf9.setVisibility(View.GONE);
		if (gf10 != null)
			gf10.setVisibility(View.GONE);
		if (gf11 != null)
			gf11.setVisibility(View.GONE);
		if (gf12 != null)
			gf12.setVisibility(View.GONE);
		if (gf13 != null)
			gf13.setVisibility(View.GONE);
		if (gf14 != null)
			gf14.setVisibility(View.GONE);
		if (gf15 != null)
			gf15.setVisibility(View.GONE);
		if (gf16 != null)
			gf16.setVisibility(View.GONE);
		if (gf17 != null)
			gf17.setVisibility(View.GONE);
		if (gf18 != null)
			gf18.setVisibility(View.GONE);

		gifShow = false;

	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 8;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// ****************************************final方法
		// 注意原本getView方法中的int position变量是非final的，现在改为final
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			int type = getItemViewType(position);
			holder2 = new TopViewHolder();
			holder = new ViewHolder();
			holder3 = new LastViewHolder();
			// if (convertView == null) {
			switch (type) {
			case 1:

				convertView = mInflater.inflate(R.layout.top_item, null);
				holder2.top_title = (Button) convertView
						.findViewById(R.id.top_back);
				holder2.top_info = (TextView) convertView
						.findViewById(R.id.top_info);
				holder2.top_info.setText(TV_type);
				holder2.top_title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (enterType == 40) {
									dissMissOSD();
									try {
										String res = JNILib.getTvData(cmd);
										Log.i(TAG,
												"====JSL==== NILib.getTvData=2 ");
										OsdConsole(res, 0);
										Log.i(TAG,
												"====JSL==== KEYCODE_MENU =OSDshow= ");
										dissMissBar();
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									try {
										cmdjObject = new JSONObject(cmd);
										cmdjObject.put("key", 0);
										cmdjObject.put("cmd", 3);
										String res = JNILib
												.getTvData(cmdjObject
														.toString());
										Log.i(TAG,
												"====JSL==== NILib.getTvData=3 ");
										OsdConsole(res, 0);
										Log.i(TAG, "====JSL==== cmdjObject = "
												+ cmdjObject.toString());
										Log.i(TAG,
												"====JSL==== cmdjObject res= "
														+ res);
									} catch (Exception e) {
										e.printStackTrace();
									}
									// showInfo(position);
								}

							}
						});
				break;

			case 2:
				// 可以理解为从vlist获取view 之后把view返回给ListView

				/*
				 * if(enterType==40){ convertView =
				 * mInflater.inflate(R.layout.vlist_of_record_item, null);
				 * }else{
				 */
				convertView = mInflater.inflate(R.layout.vlist, null);
				// }
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.sItemIcon);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				holder.viewBtn = (Button) convertView
						.findViewById(R.id.view_btn);
				holder.viewBtn2 = (Button) convertView
						.findViewById(R.id.view_btn2);
				holder.viewBtn3 = (Button) convertView
						.findViewById(R.id.view_btn3);
				// convertView.setTag(holder);

				holder.viewBtn.setVisibility(View.GONE);

				if (TextUtils.isEmpty(title[position - 1])) {
					holder.viewBtn3.setVisibility(View.GONE);
					holder.viewBtn2.setVisibility(View.GONE);
					holder.viewBtn.setVisibility(View.GONE);

				} else {

					holder.viewBtn2.setVisibility(View.VISIBLE);
					holder.viewBtn.setVisibility(View.GONE);

					if (enterType == 40) {// 已录
						holder.viewBtn3.setVisibility(View.VISIBLE);
						holder.viewBtn3
								.setBackgroundResource(R.drawable.btn_share_selecter);
						holder.viewBtn2
								.setBackgroundResource(R.drawable.play_button_selector);
						holder.viewBtn
								.setBackgroundResource(R.drawable.btn_shanchu_selecter);
						// holder.imageView.setFocusable(false);
					} else {
						holder.viewBtn3.setVisibility(View.VISIBLE);
						holder.viewBtn3
								.setBackgroundResource(R.drawable.btn_gengduo_selecter);
						// holder.imageView.setFocusable(true);
					}

					if (enterType == 43) {// 公播
						holder.viewBtn3.setVisibility(View.GONE);
						holder.viewBtn2.setVisibility(View.GONE);
						holder.viewBtn
								.setBackgroundResource(R.drawable.btn_shanchu_selecter);
					}

					/*
					 * if(enterType==40){//已录
					 * holder.viewBtn3.setVisibility(View.GONE);
					 * holder.viewBtn2.
					 * setBackgroundResource(R.drawable.play_button_selector);
					 * holder.viewBtn.setBackgroundResource(R.drawable.
					 * btn_shanchu_selecter); }
					 */

					if (enterType == 20) {// 云端
						holder.viewBtn3.setVisibility(View.GONE);
						holder.viewBtn2
								.setBackgroundResource(R.drawable.xiazai_button);

					}

					if (enterType == 21) {// 下载
						holder.viewBtn2
								.setBackgroundResource(R.drawable.btn_youxian_selecter);
						holder.viewBtn
								.setBackgroundResource(R.drawable.btn_shanchu_selecter);
						holder.viewBtn3.setVisibility(View.GONE);
					}
					
					if(enterType==31 && isCloud[position-1]){
						holder.viewBtn3.setVisibility(View.GONE);
						holder.viewBtn2.setVisibility(View.GONE);
						holder.viewBtn.setVisibility(View.GONE);
						holder.title.setText(title[position - 1]+"("+getResources().getString(R.string.on_loading)+")");
					}

				}
				if(enterType!=31 || (enterType==31 && !isCloud[position-1]))
				holder.title.setText(title[position - 1]);
				holder.info.setText(title_name[position - 1]);
				/*
				 * if(isZX && enterType==40)
				 * holder.viewBtn3.setVisibility(View.GONE);
				 */

				holder.viewBtn
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl(
								holder.title, null, 0, position));
				holder.viewBtn2
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl(
								holder.title, null, 0, position));
				holder.viewBtn3
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl(
								holder.title, null, 0, position));
				// holder.title.setOnFocusChangeListener(new
				// OnFocusChangeListenerImpl());

				// holder.viewBtn.setTag(position);
				// 给Button添加单击事件 添加Button之后ListView将失去焦�? �?��的直接把Button的焦点去�?

				holder.title.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							cmdjObject = new JSONObject(cmd);
							cmdjObject.put("key", button2_type + position);
							cmdjObject.put("cmd", 2);
							String res = JNILib.getTvData(cmdjObject.toString());
							Log.i(TAG, "====JSL==== button2_type== "
									+ button2_type);
							OsdConsole(res, 1);
							Log.i(TAG,
									"====JSL==== cmdjObject = "
											+ cmdjObject.toString());
							Log.i(TAG, "====JSL==== cmdjObject res= " + res);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				holder.viewBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							cmdjObject = new JSONObject(cmd);
							cmdjObject.put("key", button1_type + position);
							cmdjObject.put("cmd", 2);
							String res = JNILib.getTvData(cmdjObject.toString());
							Log.i(TAG, "====JSL==== button1_type== "
									+ button1_type);
							OsdConsole(res, 1);
							Log.i(TAG,
									"====JSL==== cmdjObject = "
											+ cmdjObject.toString());
							Log.i(TAG, "====JSL==== cmdjObject res= " + res);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				// holder.viewBtn2.setTag(position);
				// 给Button添加单击事件 添加Button之后ListView将失去焦�? �?��的直接把Button的焦点去�?

				holder.viewBtn2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (enterType == 40) {
							mLineView.setVisibility(View.GONE);
							ScorePicView.setVisibility(View.GONE);
							mLineView.Destroy();

							// 消除录音回播前一秒有爆音
							// Log.i("song","录音回放静音==========");
							// mute();
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									// unmute();
									// Log.i("song","录音回放恢复音量==========");
								}
							}, 2000);
						}
						try {
							cmdjObject = new JSONObject(cmd);
							cmdjObject.put("key", button2_type + position);
							cmdjObject.put("cmd", 2);
							String res = JNILib.getTvData(cmdjObject.toString());
							Log.i(TAG, "====JSL==== button2_type== "
									+ button2_type);
							OsdConsole(res, 1);
							Log.i(TAG,
									"====JSL==== cmdjObject = "
											+ cmdjObject.toString());
							Log.i(TAG, "====JSL==== cmdjObject res= " + res);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				holder.viewBtn3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (enterType == 40) {// 已录,分享
							StringBuffer sb = new StringBuffer();
							sb.append(title[position - 1]).append(";")
									.append(title_name[position - 1])
									.append(";")
									.append(record_time[position - 1]);
							// Log.i("jlink","录音信息=="+sb.toString()+"录音歌号=="+title_num[position]+"录音ID==="+record_id[position-1]);
							// if(shareDialog == null)
							shareDialog = new Share_Recoder_Dialog(
									MainActivity.this, songNumber[position - 1]
											+ ";" + record_id[position - 1], sb
											.toString(), style.MyDialog,
									record_upload[position - 1]);
							// shareDialog.setSongInfo(songNumber[position-1]+";"+record_id[position-1],sb.toString());
							shareDialog.show();

							sb.setLength(0);
							shareDialog.upload();

						} else {// 更多操作

							if (mOptionDialog == null) {
								mOptionDialog = new MoreOptionDialogOfVod(
										MainActivity.this, tishiText, 15,
										R.style.MyDialog, fragmentHander, true);
							}
							mOptionDialog.setSongNum(songNumber[position - 1]);
							Window window = mOptionDialog.getWindow();
							LayoutParams params = new LayoutParams();
							params.gravity = Gravity.RIGHT | Gravity.TOP;
							params.width = 420;
							params.height = 130;
							params.x = 100;
							params.y = 120 * position;
							//params.alpha = 0.1f;
							window.addFlags(LayoutParams.FLAG_DIM_BEHIND);
							window.setAttributes(params);
							mOptionDialog.show();
						}
					}
				});
				/*
				 * holder.imageView.setOnFocusChangeListener(new
				 * Button.OnFocusChangeListener(){
				 * 
				 * @Override
				 * 
				 * public void onFocusChange(View v, boolean hasFocus) {
				 * 
				 * // TODO Auto-generated method stub
				 * 
				 * if(hasFocus == true) { listView_key.postDelayed(new
				 * Runnable() {
				 * 
				 * @Override public void run() { listView_key.requestFocus();
				 * listView_key.requestFocusFromTouch();
				 * listView_key.setSelection(position); } }, 200); }else{
				 * 
				 * 
				 * }
				 * 
				 * }
				 * 
				 * });
				 */

				holder.viewBtn.setOnKeyListener(new OnKeyListenerImpl(
						holder.viewBtn));
				holder.viewBtn2.setOnKeyListener(new OnKeyListenerImpl(
						holder.viewBtn2));
				holder.viewBtn3.setOnKeyListener(new OnKeyListenerImpl(
						holder.viewBtn3));

				break;
			case 3:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.last_item, null);
				holder3.page = (TextView) convertView.findViewById(R.id.page);
				holder3.pre_page = (Button) convertView
						.findViewById(R.id.pre_page);
				holder3.next_page = (Button) convertView
						.findViewById(R.id.next_page);
				holder3.pre_page
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl(
								null, holder3.pre_page, 1, 7));
				holder3.next_page
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl(
								null, holder3.next_page, 2, 7));
				convertView.setTag(holder3);
				holder3.page.setText(TV_page);
				holder3.pre_page.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						isPageUp = true;
						currentSongListPosition = 7;
						try {
							cmdjObject = new JSONObject(cmd);
							cmdjObject.put("key", 101);
							cmdjObject.put("cmd", 2);
							if (KEY_type != "") {
								cmdjObject.put("search_key", KEY_type);
								cmdjObject.put("search_type", 0);
							}
							String res = JNILib.getTvData(cmdjObject.toString());
							Log.i(TAG, "====JSL==== NILib.getTvData=6 ");
							// reflishGM2();
							OsdConsole(res, 0);
							Log.i(TAG,
									"====JSL==== cmdjObject = "
											+ cmdjObject.toString());
							Log.i(TAG, "====JSL==== cmdjObject res= " + res);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				holder3.next_page
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								isPageDown = true;
								currentSongListPosition = 7;
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 102);
									cmdjObject.put("cmd", 2);
									if (KEY_type != "") {
										cmdjObject.put("search_key", KEY_type);
										cmdjObject.put("search_type", 0);
									}
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG, "====JSL==== NILib.getTvData=7 ");
									OsdConsole(res, 0);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				break;
			}
			/*
			 * }else { switch(type){ case 1: holder2 =
			 * (TopViewHolder)convertView.getTag(); break; case 2: holder =
			 * (ViewHolder)convertView.getTag(); break; case 3: holder3 =
			 * (LastViewHolder)convertView.getTag(); } }
			 */

			return convertView;
		}

		private class OnKeyListenerImpl implements OnKeyListener {
			private Button btn;

			public OnKeyListenerImpl(Button b) {
				this.btn = b;
			}

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				// Log.i("onKeyDown","key listener====code=="+arg2.getKeyCode()+"currentPageCount=="+currentPageCount+"currentSongListPosition=="+currentSongListPosition);
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg2.getKeyCode() == 20
							&& currentSongListPosition == currentPageCount
							&& listView.getVisibility() == 0) {
						listView.requestFocus();
						listView.requestFocusFromTouch();
						listView.setSelection(7);
						currentSongListPosition = 7;
						return true;
					} else if (arg2.getKeyCode() == 21
							&& btn.getId() == R.id.view_btn2
							&& listView_key.getVisibility() == 0) {
						listView_key.requestFocus();
						listView_key.requestFocusFromTouch();
						listView_key.setSelection(currentSongListPosition);
						return true;
					}
				}

				return false;
			}

		}

		private class OnFocusChangeListenerImpl implements
				Button.OnFocusChangeListener {
			private TextView tvTitel;
			private Button page;
			private int type;
			private int position;

			public OnFocusChangeListenerImpl(TextView tv, Button btn, int t,
					int pos) {
				if (tv != null)
					tvTitel = tv;
				if (btn != null)
					page = btn;
				type = t;
				position = pos;
			}

			public void onFocusChange(View v, boolean hasFocus) {
				currentSongListPosition = position;
				OSDPlayTime = 20;
				if (tvTitel != null) {

					if (hasFocus) {
						tvTitel.setTextColor(Color.parseColor("#ff4400"));
					} else {
						tvTitel.setTextColor(Color.parseColor("#ffffff"));
					}
				}
				// Log.i("jlink","type=="+type+"focus=="+hasFocus);
				if (page != null && type == 1) {
					if (hasFocus)
						isPageUp = false;
				} else if (page != null && type == 2) {
					if (!hasFocus)
						isPageUp = false;
				}
			}
		}

		// 刷新显示搜索字符
		public void update_type() {
			View view = listView.getChildAt(0);
			TextView txt = (TextView) view.findViewById(R.id.top_info);
			txt.setText(TV_type);
		}

		// 刷新显示搜索字符
		public void update_list(int style) {
			for (int i = 1; i < 8; i++) {
				if (i < 7) {
					if (style == 0) {// 点歌
						Log.i(TAG, "====JSL==== update_list=0");
						View view = listView.getChildAt(i);
						if (view == null)
							return;
						TextView title1 = (TextView) view
								.findViewById(R.id.title);
						TextView info = (TextView) view.findViewById(R.id.info);
						ImageView imageView = (ImageView) view
								.findViewById(R.id.sItemIcon);
						Button button1 = (Button) view
								.findViewById(R.id.view_btn);
						Button button2 = (Button) view
								.findViewById(R.id.view_btn2);
						Button button3 = (Button) view
								.findViewById(R.id.view_btn3);
						title1.setText("");
						info.setText("");
						info.setText(title_name[i - 1]);
						if (TextUtils.isEmpty(title[i - 1])) {
							button3.setVisibility(View.GONE);
							button2.setVisibility(View.GONE);
							button1.setVisibility(View.GONE);
						} else {

							button2.setVisibility(View.VISIBLE);
							button1.setVisibility(View.GONE);

							button1.setBackgroundResource(R.drawable.btn_youxian_selecter);
							// button2.setBackgroundResource(R.drawable.btn_detail_selecter);

							if (enterType == 20) {
								button3.setVisibility(View.GONE);
								button2.setBackgroundResource(R.drawable.xiazai_button);
								title1.setNextFocusRightId(R.id.view_btn2);
							} else {
								button3.setVisibility(View.VISIBLE);
								button3.setBackgroundResource(R.drawable.btn_gengduo_selecter);
								button2.setBackgroundResource(R.drawable.btn_detail_selecter);
								title1.setNextFocusRightId(R.id.view_btn3);
							}

							imageView.setVisibility(View.VISIBLE);
							button1_type = 20;
							button2_type = 0;
							title1.setText(title[i - 1]);
							info.setText(title_name[i - 1]);
						}

					} else if (style == 1) {// 已点
						Log.i(TAG, "====JSL==== update_list=1====enterType"
								+ enterType);

						View view = listView.getChildAt(i);
						TextView title1 = (TextView) view
								.findViewById(R.id.title);
						TextView info = (TextView) view.findViewById(R.id.info);
						Button button1 = (Button) view
								.findViewById(R.id.view_btn);
						Button button2 = (Button) view
								.findViewById(R.id.view_btn2);
						Button button3 = (Button) view
								.findViewById(R.id.view_btn3);
						ImageView imageView = (ImageView) view
								.findViewById(R.id.sItemIcon);

						if (TextUtils.isEmpty(title[i - 1])) {
							button3.setVisibility(View.GONE);
							button2.setVisibility(View.GONE);
							button1.setVisibility(View.GONE);
							title1.setText("");
							info.setText("");
						} else {

							button3.setVisibility(View.GONE);
							imageView.setVisibility(View.GONE);
							title1.setNextFocusRightId(R.id.view_btn);

							button1.setVisibility(View.VISIBLE);
							button1.setBackgroundResource(R.drawable.btn_shanchu_selecter);
							// imageView.setFocusable(true);
							if (enterType == 43) {
								button2.setVisibility(View.GONE);
							} else if (enterType == 31 && enterType != 32) {
								button2.setVisibility(View.VISIBLE);
								button2.setBackgroundResource(R.drawable.btn_youxian_selecter);
							} else if (enterType == 40) {
								button2.setVisibility(View.VISIBLE);

								button2.setBackgroundResource(R.drawable.play_button_selector);

								button3.setVisibility(View.VISIBLE);
								button3.setBackgroundResource(R.drawable.btn_share_selecter);
								// imageView.setFocusable(false);
							} else if (enterType == 20) {
								button2.setVisibility(View.VISIBLE);
								button2.setBackgroundResource(R.drawable.xiazai_button);
								button1.setBackgroundResource(R.drawable.btn_youxian_selecter);
							} else if (enterType == 21) {// 下载
								Log.i(TAG, "进入下载列表============");
								button2.setVisibility(View.VISIBLE);
								button2.setBackgroundResource(R.drawable.btn_youxian_selecter);
								button1.setBackgroundResource(R.drawable.btn_shanchu_selecter);
								button3.setVisibility(View.GONE);
							} else if(enterType==31 && isCloud[i-1]){
								button1.setVisibility(View.GONE);
								button2.setVisibility(View.GONE);
								button3.setVisibility(View.GONE);
							}else{
								button2.setVisibility(View.VISIBLE);
								button2.setBackgroundResource(R.drawable.btn_detail_selecter);
								button3.setVisibility(View.VISIBLE);
								button3.setBackgroundResource(R.drawable.btn_gengduo_selecter);
								button1.setVisibility(View.GONE);
								// button1.setBackgroundResource(R.drawable.btn_youxian_selecter);

							}

							/*
							 * if(isZX && enterType==40)
							 * button3.setVisibility(View.GONE);
							 */

							if (enterType == 31 || enterType == 40
									|| enterType == 21 || enterType == 43) {
								button1_type = 60;
								button2_type = 20;
							} else {
								button1_type = 20;
								button2_type = 0;
							}

							title1.setText(title[i - 1]);
							info.setText(title_name[i - 1]);
						}

					} else if (style == 2) {// 已下�?
						Log.i(TAG, "====JSL==== update_list=2");
						View view = listView.getChildAt(i);
						TextView title1 = (TextView) view
								.findViewById(R.id.title);
						TextView info = (TextView) view.findViewById(R.id.info);
						Button button1 = (Button) view
								.findViewById(R.id.view_btn);
						Button button2 = (Button) view
								.findViewById(R.id.view_btn2);
						Button button3 = (Button) view
								.findViewById(R.id.view_btn3);

						if (TextUtils.isEmpty(title[i - 1])) {
							button3.setVisibility(View.GONE);
							button2.setVisibility(View.GONE);
							button1.setVisibility(View.GONE);
							title1.setText("");
							info.setText("");
						} else {
							button3.setVisibility(View.VISIBLE);
							button2.setVisibility(View.VISIBLE);
							button1.setVisibility(View.VISIBLE);
							ImageView imageView = (ImageView) view
									.findViewById(R.id.sItemIcon);
							button1.setBackgroundResource(R.drawable.btn_youxian_selecter);
							button2.setBackgroundResource(R.drawable.btn_detail_selecter);
							imageView.setVisibility(View.GONE);
							button1_type = 20;
							button2_type = 0;
							title1.setText(title[i - 1]);
							info.setText(title_name[i - 1]);
						}
					}
				} else if (i == 7) {
					View view = listView.getChildAt(i);
					TextView page = (TextView) view.findViewById(R.id.page);
					page.setText(TV_page);

				}
			}
		}

		// 每个convert view都会调用此方法，获得当前�?��要的view样式
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int p = position;
			if (p == 0)
				return 1;
			else if (p == 7)
				return 3;
			else
				return 2;
		}
	}

	public class MyAdapter_main extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter_main(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		// ****************************************final方法
		// 注意原本getView方法中的int position变量是非final的，现在改为final
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final int[] position1 = new int[1];
			final int[] position2 = new int[1];
			int type = getItemViewType(position);
			holder_main2 = new TopViewHolder_main();
			holder_main = new ViewHolder_main();
			holder_main3 = new LastViewHolder_main();
			// if (convertView == null) {
			switch (type) {
			case 1:
				convertView = mInflater.inflate(R.layout.top_item, null);
				holder_main2.top_title = (Button) convertView
						.findViewById(R.id.top_back);
				holder_main2.top_info = (TextView) convertView
						.findViewById(R.id.top_info);
				holder_main2.top_info.setText(TV_type);
				convertView.setTag(holder_main2);
				holder_main2.top_title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// list_main.setVisibility(View.GONE);
								dissMissOSD();
							}
						});
				break;

			case 2:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.main_vlist, null);
				holder_main.button1 = (Button) convertView
						.findViewById(R.id.main_view_btn);
				holder_main.button2 = (Button) convertView
						.findViewById(R.id.main_view_btn2);
				holder_main.imageView1 = (ImageView) convertView
						.findViewById(R.id.sItemIcon_main);
				holder_main.imageView2 = (ImageView) convertView
						.findViewById(R.id.sItemIcon2_main);
				holder_main.button2.setFocusableInTouchMode(true);
				holder_main.button2.requestFocus();
				// holder_main.button1.setTag(position);

				if (isScore) {
					holder_main.button1.setText(main_info1[position - 1]);
					holder_main.button2.setText(main_info2[position - 1]);
					holder_main.imageView1
							.setBackgroundResource(main_img1[position - 1]);
					holder_main.imageView2
							.setBackgroundResource(main_img2[position - 1]);
					holder_main.button2.setEnabled(true);
					holder_main.button2.setFocusable(true);
					holder_main.imageView2.setEnabled(true);
					holder_main.imageView2.setFocusable(true);
					JNILib.SetScoreFlag(0);
				} else {

					if (position < 7) {
						holder_main.button1.setText(main_info1[position - 1]);
						holder_main.button2.setText(main_info2[position - 1]);
						holder_main.imageView1
								.setBackgroundResource(main_img1[position - 1]);
						holder_main.imageView2
								.setBackgroundResource(main_img2[position - 1]);
						holder_main.button2.setEnabled(true);
						holder_main.button2.setFocusable(true);
						holder_main.imageView2.setEnabled(true);
						holder_main.imageView2.setFocusable(true);
					} else {
						holder_main.button1.setText(main_info1[position - 1]);
						holder_main.imageView1
								.setBackgroundResource(main_img1[position - 1]);
						holder_main.button2.setEnabled(false);
						holder_main.button2.setFocusable(false);
						holder_main.imageView2.setEnabled(false);
						holder_main.imageView2.setFocusable(false);
					}

				}

				convertView.setTag(holder_main);
				holder_main.button1
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_main.button2
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());

				switch (position) {
				case 1:
					position1[0] = 1;
					position2[0] = 2;
					break;
				case 2:
					position1[0] = 3;
					position2[0] = 4;
					break;
				case 3:
					position1[0] = 7;
					position2[0] = 6;
					break;
				case 4:
					position1[0] = 11;
					position2[0] = 8;
					break;

				default:
					break;
				}

				holder_main.button1
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								try {
									String res = "";
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", position1[0]);
									cmdjObject.put("cmd", 2);
									res = JNILib.getTvData(cmdjObject
											.toString());
									/*
									 * Log.i(TAG,
									 * "====JSL==== NILib.getTvData=8 ");
									 * Log.i(TAG,
									 * "====JSL==== cmdjObject = "+cmdjObject
									 * .toString()); Log.i(TAG,
									 * "====JSL==== cmdjObject res= "+res );
									 */

									if (position == 4) {
										cmdjObject = new JSONObject(cmd);
										cmdjObject.put("key", 1);
										cmdjObject.put("cmd", 2);
										res = JNILib.getTvData(cmdjObject
												.toString());
										isVodOrderSelect = true;
									}

									OsdConsole(res, 0);

								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});
				holder_main.button2
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (!isScore && position == 7)
									return;
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", position2[0]);
									cmdjObject.put("cmd", 2);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									OsdConsole(res, 0);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				// holder.viewBtn2.setTag(position);

				break;
			case 3:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.last_item, null);
				holder_main3.page = (TextView) convertView
						.findViewById(R.id.page);
				holder_main3.pre_page = (Button) convertView
						.findViewById(R.id.pre_page);
				holder_main3.next_page = (Button) convertView
						.findViewById(R.id.next_page);
				holder_main3.page.setText(TV_page);
				/*
				 * if(isPageDown){ holder_main3.next_page.requestFocus();
				 * isPageDown = false; }
				 * 
				 * if(isPageUp){ holder_main3.pre_page.requestFocus(); isPageUp
				 * = false; }
				 */

				// holder_main3.pre_page.setTag(position);
				convertView.setTag(holder_main3);
				holder_main3.pre_page
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {

								isPageUp = true;

								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 101);
									cmdjObject.put("cmd", 2);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG, "====JSL==== NILib.getTvData=9 ");
									OsdConsole(res, 1);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				holder_main3.next_page
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {

								isPageDown = true;

								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 102);
									cmdjObject.put("cmd", 2);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=10 ");
									OsdConsole(res, 1);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				break;
			}
			/*
			 * }else { switch(type){ case 1: holder_main2 =
			 * (TopViewHolder_main)convertView.getTag(); break; case 2:
			 * //holder_main = (ViewHolder_main)convertView.getTag(); break;
			 * case 3: holder_main3 = (LastViewHolder_main)convertView.getTag();
			 * break; } }
			 */

			return convertView;
		}

		private class OnFocusChangeListenerImpl implements
				Button.OnFocusChangeListener {

			public void onFocusChange(View v, boolean hasFocus) {
				OSDPlayTime = 20;
			}
		}

		// 每个convert view都会调用此方法，获得当前�?��要的view样式
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int p = position;
			if (p == 0)
				return 1;
			else
				return 2;
		}
	}

	public class MyAdapter_sort extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter_sort(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 8;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		// ****************************************final方法
		// 注意原本getView方法中的int position变量是非final的，现在改为final
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int type = getItemViewType(position);
			holder_sort2 = new TopViewHolder_sort();
			holder_sort = new ViewHolder_sort();
			holder_sort3 = new LastViewHolder_sort();
			// if (convertView == null) {
			switch (type) {
			case 1:
				convertView = mInflater.inflate(R.layout.top_item, null);
				holder_sort2.top_title = (Button) convertView
						.findViewById(R.id.top_back);
				holder_sort2.top_info = (TextView) convertView
						.findViewById(R.id.top_info);
				holder_sort2.top_info.setText(TV_type);
				holder_sort2.top_title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 3);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=11 ");
									OsdConsole(res, 0);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// showInfo(position);

							}
						});
				break;

			case 2:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.sort_vlist, null);
				holder_sort.imageView = (ImageView) convertView
						.findViewById(R.id.sort_sItemIcon);
				holder_sort.title = (Button) convertView
						.findViewById(R.id.sort_title);
				holder_sort.title.setText(info[position - 1]);
				convertView.setTag(holder_sort);
				holder_sort.title
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_sort.title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", position);
									cmdjObject.put("cmd", 2);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=12 ");
									if (res != null) {
										OsdConsole(res, 0);
									}
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// showInfo(position);

							}
						});
				break;
			case 3:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.last_item, null);
				holder_sort3.page = (TextView) convertView
						.findViewById(R.id.page);
				holder_sort3.pre_page = (Button) convertView
						.findViewById(R.id.pre_page);
				holder_sort3.next_page = (Button) convertView
						.findViewById(R.id.next_page);
				holder_sort3.pre_page
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_sort3.next_page
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				convertView.setTag(holder_sort3);
				holder_sort3.page.setText(TV_page);

				/*
				 * if(isPageDown){ holder_sort3.next_page.requestFocus();
				 * isPageDown = false; }
				 * 
				 * if(isPageUp){ holder_sort3.pre_page.requestFocus(); isPageUp
				 * = false; }
				 */

				holder_sort3.pre_page
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								isPageUp = true;

								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 101);
									cmdjObject.put("cmd", 2);
									if (KEY_type != "") {
										cmdjObject.put("search_key", KEY_type);
										cmdjObject.put("search_type", 0);
									}
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=13 ");
									// reflishGM2();
									OsdConsole(res, 1);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				holder_sort3.next_page
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								isPageDown = true;

								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 102);
									cmdjObject.put("cmd", 2);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=14 ");
									OsdConsole(res, 1);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				break;
			}
			/*
			 * }else { switch(type){ case 1: holder_sort2 =
			 * (TopViewHolder_sort)convertView.getTag(); break; case 2:
			 * holder_sort = (ViewHolder_sort)convertView.getTag(); break; case
			 * 3: holder_sort3 = (LastViewHolder_sort)convertView.getTag(); } }
			 */

			return convertView;
		}

		private class OnFocusChangeListenerImpl implements
				Button.OnFocusChangeListener {

			public void onFocusChange(View v, boolean hasFocus) {
				OSDPlayTime = 20;
			}
		}

		// 刷新显示搜索字符
		public void update_sort() {
			for (int i = 1; i < 8; i++) {
				if (i < 7) {
					View view = listView_sort.getChildAt(i);
					Button title1 = (Button) view.findViewById(R.id.sort_title);
					title1.setText(info[i - 1]);
				} else if (i == 7) {
					View view = listView_sort.getChildAt(i);
					TextView page = (TextView) view.findViewById(R.id.page);
					page.setText(TV_page);
				}
			}
		}

		// 每个convert view都会调用此方法，获得当前�?��要的view样式
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int p = position;
			if (p == 0)
				return 1;
			else if (p == 7)
				return 3;
			else
				return 2;
		}
	}

	public class MyAdapter_type extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter_type(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 8;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		// ****************************************final方法
		// 注意原本getView方法中的int position变量是非final的，现在改为final
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int type = getItemViewType(position);
			holder_type2 = new TopViewHolder_type();
			holder_type = new ViewHolder_type();
			holder_type3 = new LastViewHolder_type();
			// if (convertView == null) {
			switch (type) {
			case 1:
				convertView = mInflater.inflate(R.layout.top_item, null);
				holder_type2.top_title = (Button) convertView
						.findViewById(R.id.top_back);
				holder_type2.top_info = (TextView) convertView
						.findViewById(R.id.top_info);
				holder_type2.top_info.setText(TV_type);
				holder_type2.top_title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 3);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=15 ");
									OsdConsole(res, 0);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// showInfo(position);

							}
						});
				break;

			case 2:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.type_vlist, null);
				holder_type.imageView = (ImageView) convertView
						.findViewById(R.id.type_sItemIcon);
				//holder_type.imageView.setVisibility(View.GONE);
				holder_type.title = (Button) convertView
						.findViewById(R.id.type_title);
				holder_type.title
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				convertView.setTag(holder_type);
				holder_type.title.setText(title_name[position - 1]);
				holder_type.title
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", position);
									cmdjObject.put("cmd", 2);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=16 ");
									KEY_type = "";
									OsdConsole(res, 0);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// showInfo(position);

							}
						});
				holder_type.imageView
						.setOnFocusChangeListener(new Button.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (hasFocus == true) {
									listView_key.requestFocus();
									listView_key.requestFocusFromTouch();
									listView_key.setSelection(position);
								} else {
								}
							}
						});
				break;
			case 3:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.last_item, null);
				holder_type3.page = (TextView) convertView
						.findViewById(R.id.page);
				holder_type3.pre_page = (Button) convertView
						.findViewById(R.id.pre_page);
				holder_type3.next_page = (Button) convertView
						.findViewById(R.id.next_page);

				/*
				 * if(isPageDown){ holder_type3.next_page.requestFocus();
				 * isPageDown = false; }
				 * 
				 * if(isPageUp){ holder_type3.pre_page.requestFocus(); isPageUp
				 * = false; }
				 */

				holder_type3.pre_page
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_type3.next_page
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				convertView.setTag(holder_type3);
				holder_type3.page.setText(TV_page);
				// holder_type3.pre_page.setTag(position);
				holder_type3.pre_page
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								isPageUp = true;

								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 101);
									cmdjObject.put("cmd", 2);
									cmdjObject.put("search_key", KEY_type);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=17 ");
									OsdConsole(res, 1);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									// Log.i(TAG,
									// "====JSL==== cmdjObject res= "+res );
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				// holder_type3.next_page.setTag(position);
				holder_type3.next_page
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								isPageDown = true;

								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 102);
									cmdjObject.put("cmd", 2);
									cmdjObject.put("search_key", KEY_type);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=18 ");
									OsdConsole(res, 1);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				break;
			}
			/*
			 * }else { switch(type){ case 1: holder_type2 =
			 * (TopViewHolder_type)convertView.getTag(); break; case 2:
			 * holder_type = (ViewHolder_type)convertView.getTag(); break; case
			 * 3: holder_type3 = (LastViewHolder_type)convertView.getTag(); } }
			 */

			return convertView;
		}

		private class OnFocusChangeListenerImpl implements
				Button.OnFocusChangeListener {

			public void onFocusChange(View v, boolean hasFocus) {
				OSDPlayTime = 20;
			}
		}

		// 刷新显示字符
		public void update_type() {
			for (int i = 1; i < 8; i++) {
				if (i < 7) {
					View view = listView_type.getChildAt(i);
					Button title1 = (Button) view.findViewById(R.id.type_title);
					title1.setText(title_name[i - 1]);
				} else if (i == 7) {
					View view = listView_type.getChildAt(i);
					TextView page = (TextView) view.findViewById(R.id.page);
					page.setText(TV_page);
				}
			}
		}

		// 每个convert view都会调用此方法，获得当前�?��要的view样式
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int p = position;
			if (p == 0)
				return 1;
			else if (p == 7)
				return 3;
			else
				return 2;
		}
	}

	public class MyAdapter_key extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter_key(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 8;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		// ****************************************final方法
		// 注意原本getView方法中的int position变量是非final的，现在改为final
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			currentKeyPosition = position;
			int type = getItemViewType(position);
			holder_key = new KeyViewHolder();
			holder_key2 = new KeyViewHolder_Top();
			// if (convertView == null) {
			switch (type) {
			case 1:
				convertView = mInflater.inflate(R.layout.key_top_item, null);
				holder_key2.key_info = (TextView) convertView
						.findViewById(R.id.key_top_info);
				holder_key2.key_info.setText(KEY_type);

				break;

			case 2:
				// 可以理解为从vlist获取view 之后把view返回给ListView
				convertView = mInflater.inflate(R.layout.key_item, null);
				holder_key.key_button1 = (Button) convertView
						.findViewById(R.id.key_1);
				holder_key.key_button2 = (Button) convertView
						.findViewById(R.id.key_2);
				holder_key.key_button3 = (Button) convertView
						.findViewById(R.id.key_3);
				holder_key.key_button4 = (Button) convertView
						.findViewById(R.id.key_4);
				holder_key.key_button5 = (Button) convertView
						.findViewById(R.id.key_5);
				holder_key.img_1 = (ImageView) convertView
						.findViewById(R.id.img_1);
				holder_key.img_2 = (ImageView) convertView
						.findViewById(R.id.img_2);
				holder_key.img_3 = (ImageView) convertView
						.findViewById(R.id.img_3);
				holder_key.img_4 = (ImageView) convertView
						.findViewById(R.id.img_4);
				holder_key.key_button5.setFocusable(true);
				convertView.setTag(holder_key);
				holder_key.img_1.setBackgroundResource(key_list1[position - 1]);
				holder_key.img_2.setBackgroundResource(key_list2[position - 1]);
				holder_key.img_3.setBackgroundResource(key_list3[position - 1]);
				holder_key.img_4.setBackgroundResource(key_list4[position - 1]);
				/*
				 * holder_key.key_button1.setText(key_slist1[position-1]);
				 * holder_key.key_button2.setText(key_slist2[position-1]);
				 * holder_key.key_button3.setText(key_slist3[position-1]);
				 * holder_key.key_button4.setText(key_slist4[position-1]);
				 */
				holder_key.key_button1
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								KEY_type = KEY_type + key_slist1[position - 1];
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 4);
									cmdjObject.put("search_key", KEY_type);
									cmdjObject.put("search_type", 0);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=19 ");
									// OsdConsole(res);
									OsdSearch(res);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				holder_key.key_button2
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								KEY_type = KEY_type + key_slist2[position - 1];
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 4);
									cmdjObject.put("search_key", KEY_type);
									cmdjObject.put("search_type", 0);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=20 ");
									// OsdConsole(res);
									OsdSearch(res);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				holder_key.key_button3
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (position != 7) {
									KEY_type = KEY_type
											+ key_slist3[position - 1];
								} else {
									KEY_type = "";
								}
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 4);
									cmdjObject.put("search_key", KEY_type);
									cmdjObject.put("search_type", 0);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=21 ");
									// OsdConsole(res);
									OsdSearch(res);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				holder_key.key_button4
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (position != 7) {
									KEY_type = KEY_type
											+ key_slist4[position - 1];
								} else {
									if (KEY_type.length() > 0) {
										KEY_type = KEY_type.substring(0,
												KEY_type.length() - 1);
									} else {
										KEY_type = "";
									}
								}
								try {
									cmdjObject = new JSONObject(cmd);
									cmdjObject.put("key", 0);
									cmdjObject.put("cmd", 4);
									cmdjObject.put("search_key", KEY_type);
									cmdjObject.put("search_type", 0);
									String res = JNILib.getTvData(cmdjObject
											.toString());
									Log.i(TAG,
											"====JSL==== NILib.getTvData=22 ");
									// OsdConsole(res);
									OsdSearch(res);
									Log.i(TAG, "====JSL==== cmdjObject = "
											+ cmdjObject.toString());
									Log.i(TAG, "====JSL==== cmdjObject res= "
											+ res);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				holder_key.key_button1
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_key.key_button2
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_key.key_button3
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());
				holder_key.key_button4
						.setOnFocusChangeListener(new OnFocusChangeListenerImpl());

				/*
				 * holder_key.key_button1.setOnFocusChangeListener(new
				 * Button.OnFocusChangeListener(){
				 * 
				 * @Override
				 * 
				 * public void onFocusChange(View v, boolean hasFocus) {
				 * Log.i("TAG","键盘1焦点改变=="+position); OSDPlayTime=20; // TODO
				 * Auto-generated method stub if(hasFocus ){ if(position==1){
				 * holder_key.key_button1.setNextFocusUpId(R.id.key_1); }
				 * if(position==7){
				 * holder_key.key_button1.setNextFocusDownId(R.id.key_1); } }
				 * 
				 * }
				 * 
				 * }); holder_key.key_button4.setOnFocusChangeListener(new
				 * Button.OnFocusChangeListener(){
				 * 
				 * @Override
				 * 
				 * public void onFocusChange(View v, boolean hasFocus) {
				 * 
				 * // TODO Auto-generated method stub OSDPlayTime=20;
				 * if(hasFocus ){ if(position==1){
				 * holder_key.key_button4.setNextFocusUpId(R.id.key_4); }
				 * if(position==7){
				 * holder_key.key_button4.setNextFocusDownId(R.id.key_4); } }
				 * 
				 * }
				 * 
				 * });
				 * 
				 * holder_key.key_button2.setOnFocusChangeListener(new
				 * Button.OnFocusChangeListener(){
				 * 
				 * @Override
				 * 
				 * public void onFocusChange(View v, boolean hasFocus) {
				 * 
				 * // TODO Auto-generated method stub OSDPlayTime=20;
				 * if(hasFocus ){ if(position==1){
				 * holder_key.key_button2.setNextFocusUpId(R.id.key_2); }
				 * if(position==7){
				 * holder_key.key_button2.setNextFocusDownId(R.id.key_2); } }
				 * 
				 * }
				 * 
				 * }); holder_key.key_button3.setOnFocusChangeListener(new
				 * Button.OnFocusChangeListener(){
				 * 
				 * @Override
				 * 
				 * public void onFocusChange(View v, boolean hasFocus) {
				 * 
				 * // TODO Auto-generated method stub OSDPlayTime=20;
				 * if(hasFocus ){ if(position==1){
				 * holder_key.key_button3.setNextFocusUpId(R.id.key_3); }
				 * if(position==7){
				 * holder_key.key_button3.setNextFocusDownId(R.id.key_3); } }
				 * 
				 * }
				 * 
				 * });
				 */

				holder_key.key_button5
						.setOnFocusChangeListener(new Button.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {

								// TODO Auto-generated method stub

								if (hasFocus) {
									listView.postDelayed(new Runnable() {

										@Override
										public void run() {
											if (listView.getVisibility() == 0) {
												if (position != 7) {
													listView.requestFocus();
													listView.requestFocusFromTouch();
													listView.setItemsCanFocus(true);
													if (currentPageCount == 6)
														listView.setSelection(position);
													else
														listView.setSelection(currentPageCount);
												} else {
													listView.requestFocus();
													listView.requestFocusFromTouch();
													listView.setSelection(7);
												}

											} else {
												if (position != 7) {
													listView_type
															.requestFocus();
													listView_type
															.requestFocusFromTouch();
													listView_type
															.setSelection(position);
												} else {
													listView_type
															.requestFocus();
													listView_type
															.requestFocusFromTouch();
													listView_type
															.setSelection(6);
												}

											}

										}
									}, 100);
								} else {

								}

							}

						});
				break;

			}

			return convertView;
		}

		private class OnFocusChangeListenerImpl implements
				Button.OnFocusChangeListener {

			public void onFocusChange(View v, boolean hasFocus) {
				OSDPlayTime = 20;
			}
		}

		// 刷新显示搜索字符
		public void update_type() {
			View view = listView_key.getChildAt(0);
			TextView txt = (TextView) view.findViewById(R.id.key_top_info);
			txt.setText(KEY_type);
		}

		// 每个convert view都会调用此方法，获得当前�?��要的view样式
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int p = position;
			if (p == 0)
				return 1;
			else if (p == 7)
				return 2;
			else
				return 2;
		}
	}

	public final class ViewHolder {
		public ImageView imageView;
		public TextView title;
		public TextView info;
		public Button viewBtn;
		public Button viewBtn2;
		public Button viewBtn3;
	}

	public final class TopViewHolder {
		public Button top_title;
		public TextView top_info;
	}

	public final class ViewHolder_type {
		public ImageView imageView;
		public Button title, youxian, chabo;
		public MarqueeTextView info;
	}

	public final class LastViewHolder {
		public Button pre_page;
		public TextView page;
		public Button next_page;
	}

	public final class ViewHolder_main {
		public ImageView imageView1;
		public ImageView imageView2;
		public Button button1;
		public Button button2;
	}

	public final class TopViewHolder_main {
		public Button top_title;
		public TextView top_info;
	}

	public final class LastViewHolder_main {
		public Button pre_page;
		public TextView page;
		public Button next_page;
	}

	public final class LastViewHolder_type {
		public Button pre_page;
		public TextView page;
		public Button next_page;
	}

	public final class TopViewHolder_type {
		public Button top_title;
		public TextView top_info;
		public ImageView icon;
		public Button top_name;
		public MarqueeTextView marqueeTv;
	}

	public final class ViewHolder_sort {
		public ImageView imageView;
		public Button title;
	}

	public final class LastViewHolder_sort {
		public Button pre_page;
		public TextView page;
		public Button next_page;
	}

	public final class TopViewHolder_sort {
		public Button top_title;
		public TextView top_info;
	}

	public final class KeyViewHolder {
		public Button key_button1;
		public Button key_button2;
		public Button key_button3;
		public Button key_button4;
		public Button key_button5;
		public ImageView img_1;
		public ImageView img_2;
		public ImageView img_3;
		public ImageView img_4;
	}

	public final class KeyViewHolder_Top {
		public TextView key_info;
	}

	public void OsdConsole(String jsonMessage, int reflish) {
		OSDPlayTime = 20;
		Log.i(TAG, "====JSL==== OsdConsole(jsonMessage) === " + jsonMessage);
		// Log.i(TAG, "====JSL==== OsdConsole(reflish) === "+reflish);
		if (jsonMessage == null)
			return;
		try {
			myjObject = new JSONObject(jsonMessage);
			int enter_type = myjObject.getInt("enter_type");
			enterType = enter_type;
			int layer = myjObject.getInt("layer");
			TV_type = myjObject.getString("tip");
			currentPageCount = myjObject.getInt("count");
			// Log.i("jlink", "====JSL==== OsdConsole() enterType= "
			// +enterType+"==enterType=="+enterType);
		/*	for (int i = 0; i < 6; i++) {
				isCloud[i] = false;
			}*/
			switch (enter_type) {
			case 0:// 主页
				/*
				 * for(int i=0;i<4;i++){ try{
				 * main_info2[i]=myjObject.getString("list"+(2*i+1));
				 * main_info1[i]=myjObject.getString("list"+2*i); } catch
				 * (Exception e) { // TODO: handle exception } }
				 */
				showTVMain();
				break;
			case 1: // 歌名
			case 16:// 评分
			case 42:// 公播
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
						//	isCloud[i]=listjObject.getString("cloud_flag").equals("1002")?false:true;
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 2: // 歌星
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 3) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							// title[i]=listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title_name[i] = "";
						}
					}
					showGX2(reflish);
				}
				break;
			case 3: // 排行
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 4: // 舞曲
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 5: // 电影
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 6: // 新歌
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 7: // 高清
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 8: // 分类
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 9: // 流行
				Log.i("CHW", "热门====" + jsonMessage);
				if (layer == 1) {

					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							Log.i(TAG, "====JSL==== info[i]= " + i + "="
									+ info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 11: // 歌星反查
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 20: // 云端
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(reflish);
				}
				break;
			case 21:// 下载
				if (layer == 1) {
					TV_page = myjObject.getString("page");
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showYD2(reflish);
				}
				break;
			case 22:// 已下�?
				if (layer == 1) {
					TV_page = myjObject.getString("page");
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showYB2(reflish);
				}
				break;
			case 31: // 已点
			case 41:
				try {
					TV_page = myjObject.getString("page");
				} catch (Exception e) {
					TV_page = "0/0";
				}
				if (layer == 1) {
					// TV_page=myjObject.getString("page");
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {

					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							isCloud[i]=listjObject.getString("cloud_flag").equals("1002")?false:true;
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							Log.i(TAG, "====JSL=exception== " + e.toString());
							title[i] = "";
							title_name[i] = "";
							isCloud[i] = false;
						}
					}
					showYD2(reflish);
				}
				break;

			case 40:// 已录
			case 43:// 更多公播

				try {
					TV_page = myjObject.getString("page");
				} catch (Exception e) {
					TV_page = "0/0";
				}
				for (int i = 0; i < 6; i++) {
					try {
						info[i] = myjObject.getString("list" + i);
						// Log.i(TAG, "====JSL==== info[i]= "+i+"="+info[i]);
						listjObject = new JSONObject(info[i]);
						title[i] = listjObject.getString("song_name");
						title_name[i] = listjObject.getString("singer");
						songNumber[i] = listjObject.getString("number");
						if (listjObject.has("record_id"))
							record_id[i] = listjObject.getString("record_id");
						if (listjObject.has("record_upload"))
							record_upload[i] = listjObject
									.getInt("record_upload");
						if (listjObject.has("record_time"))
							record_time[i] = listjObject.getInt("record_time");
					} catch (Exception e) {
						// TODO: handle exception
						title[i] = "";
						title_name[i] = "";
					}
				}
				showYD2(reflish);
				break;
			case 32: // 已播
				if (layer == 1) {
					// TV_page=myjObject.getString("page");
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(reflish);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							songNumber[i] = listjObject.getString("number");
							// Log.i(TAG,
							// "====JSL==== title[i]= "+i+"="+title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showYB2(reflish);
				}
				break;

			case 47:// 评分
				break;
			}
			// }
			try {
				if (!myjObject.has("msg"))
					return;
				String osd_msg = myjObject.getString("msg");
				Log.i(TAG, "====JSL==== OsdConsole() osd_msg= " + osd_msg);
				if (osd_msg != null) {
					tishiText.setText(osd_msg);
					tishiText.setVisibility(View.VISIBLE);
					NoticePicPlayTime = 12;
				}
			} catch (JSONException e) {
				Log.i(TAG, "====JSL==== OsdConsole() ===no msg");
			}

		} catch (JSONException e) {
			Log.i(TAG,
					"====JSL==== OsdConsole() ===JSONException " + e.toString());
		}
	}

	private void showTVMain() {

		// Log.i("CHW", "====JSL====showTVMain ");
		if (OSDinit) {
			listView_sort.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			listView_type.setVisibility(View.GONE);
			listView_key.setVisibility(View.GONE);
			KEY_type = "";
			listView_main.setVisibility(View.VISIBLE);
			adapter_main.notifyDataSetChanged();
			// listView_main.postDelayed(new Runnable() {
			// @Override
			// public void run() {
			listView_main.requestFocus();
			listView_main.requestFocusFromTouch();
			listView_main.setSelection(1);
			// }
			// }, 1000);
			dissMissBar();
			OSDshow = true;
			mQrPlayTime = 20;
			UpdateQrDisp();
		}

	}

	private void dissMissOSD() {
		listView_sort.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		listView_type.setVisibility(View.GONE);
		listView_key.setVisibility(View.GONE);
		KEY_type = "";
		listView_main.setVisibility(View.GONE);

		OSDshow = false;

		if (mOptionDialog != null) {
			mOptionDialog.dismiss();
		}

		isPageDown = false;
		isPageUp = false;

	}

	private void showGM1(int reflish) {
		Log.i("CHW", "====JSL====showGM1 ");
		if (reflish == 0) {
			listView.setVisibility(View.GONE);
			listView_type.setVisibility(View.GONE);
			listView_main.setVisibility(View.GONE);
			listView_key.setVisibility(View.GONE);
			KEY_type = "";
			listView_sort.setVisibility(View.VISIBLE);
			adapter_sort.notifyDataSetChanged();
			// listView_sort.postDelayed(new Runnable() {
			// @Override
			// public void run() {
			listView_sort.requestFocus();
			listView_sort.requestFocusFromTouch();
			listView_sort.setSelection(1);
			onClickPageButton(listView_sort);
			// }
			// }, 1000);
		} else if (reflish == 1) {
			adapter_sort.update_sort();
		}
		OSDshow = true;

	}

	private void showGM2(int reflish) {
		Log.i("CHW", "====JSL====showGM2 ");
		if (reflish == 0) {
			Log.i(TAG, "====JSL==== OsdConsole() ===showGM2 reflish=" + reflish);
			listView.setVisibility(View.VISIBLE);
			listView_key.setVisibility(View.VISIBLE);
			listView_type.setVisibility(View.GONE);
			listView_main.setVisibility(View.GONE);
			listView_sort.setVisibility(View.GONE);
			adapter_key.notifyDataSetChanged();
			button1_type = 20;
			button2_type = 0;
			// adapter.notifyDataSetChanged();
			listView.requestFocus();
			listView.requestFocusFromTouch();
			listView.setSelection(1);
			adapter.update_list(0);
			adapter.update_type();
			adapter_key.update_type();
			// adapter.notifyDataSetChanged();
			onClickPageButton(listView);
		} else if (reflish == 1) {
			adapter.update_list(1);
			Log.i(TAG, "====JSL==== OsdConsole() ===showGM2 reflish=" + reflish);
		}
		OSDshow = true;
	}

	private void showYB2(int reflish) {
		Log.i("CHW", "====JSL====shoyB2 ");
		if (reflish == 0) {
			listView_type.setVisibility(View.GONE);
			listView_main.setVisibility(View.GONE);
			listView_sort.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView_key.setVisibility(View.GONE);
			KEY_type = "";
			button1_type = 20;
			button2_type = 0;
			adapter.notifyDataSetChanged();
			// listView.postDelayed(new Runnable() {
			// @Override
			// public void run() {
			listView.requestFocus();
			listView.requestFocusFromTouch();
			listView.setSelection(1);
			onClickPageButton(listView);
			// }
			// }, 1000)
		} else if (reflish == 1) {
			adapter.update_list(2);
			Log.i(TAG, "====JSL==== OsdConsole() ===showGM2 reflish=" + reflish);
		}
		OSDshow = true;

	}

	private void showYD2(int reflish) {
		Log.i(TAG, "====JSL====showYD2+== reflish" + reflish);
		if (reflish == 0) {
			listView_type.setVisibility(View.GONE);
			listView_main.setVisibility(View.GONE);
			listView_sort.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView_key.setVisibility(View.GONE);
			KEY_type = "";

			button1_type = 60;
			button2_type = 20;
			// adapter.notifyDataSetChanged();
			adapter.update_list(1);
			adapter.update_type();
			listView.requestFocus();
			listView.requestFocusFromTouch();
			listView.setSelection(1);
			onClickPageButton(listView);
		} else if (reflish == 1) {
			adapter.update_list(1);
		}
		OSDshow = true;

	}

	private void reflishGM2() {

		adapter.notifyDataSetChanged();

	}

	private void showGX2(int reflish) {
		Log.i("CHW", "====JSL====showGx2 ");
		if (reflish == 0) {
			listView_main.setVisibility(View.GONE);
			listView_sort.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			listView_type.setVisibility(View.VISIBLE);
			listView_key.setVisibility(View.VISIBLE);
			adapter_type.notifyDataSetChanged();
			adapter_key.notifyDataSetChanged();
			// listView_type.postDelayed(new Runnable() {
			// @Override
			// public void run() {
			listView_type.requestFocus();
			listView_type.requestFocusFromTouch();
			listView_type.setSelection(1);
			onClickPageButton(listView_type);
			// }
			// }, 1000);
		} else if (reflish == 1) {
			adapter_type.update_type();
		}

		OSDshow = true;

	}

	public void onClickPageButton(ListView listview) {
		Log.i("jlink", " page change............." + isPageDown + "==="
				+ isPageUp);
		if (isPageDown || isPageUp) {
			listview.setSelection(7);
			View item = (View) listview.getChildAt(7);
			Button next = (Button) item.findViewById(R.id.next_page);
			Button pre = (Button) item.findViewById(R.id.pre_page);
			if (isPageDown) {
				next.requestFocus();
				isPageDown = false;
				Log.i("jlink", "next requestFocus======");
			}

			if (isPageUp) {
				pre.requestFocus();
				isPageUp = false;
			}
		}
	}

	public void OsdSearch(String jsonMessage) {
		OSDPlayTime = 20;
		TV_page = "";
		Log.i(TAG, "====JSL==== OsdConsole() === " + jsonMessage);
		try {
			myjObject = new JSONObject(jsonMessage);
			int enter_type = myjObject.getInt("enter_type");
			int layer = myjObject.getInt("layer");
			TV_type = myjObject.getString("tip");
			Log.i(TAG, "====JSL==== OsdConsole() enter_type= " + enter_type);
			switch (enter_type) {
			case 0:// 主页
				break;
			case 1: // 歌名
			case 16:// 评分
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							Log.i(TAG, "====JSL==== info[i]= " + i + "="
									+ info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							Log.i(TAG, "====JSL==== title[i]= " + i + "="
									+ title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					adapter.notifyDataSetChanged();
					adapter_key.update_type();
				}
				break;
			case 2: // 歌星
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							Log.i(TAG, "====JSL==== info[i]= " + i + "="
									+ info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(0);
				} else if (layer == 3) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							// title[i]=listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							Log.i(TAG, "====JSL==== title[i]= " + i + "="
									+ title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title_name[i] = "";
						}
					}
					adapter_type.notifyDataSetChanged();
					adapter_key.update_type();
				}
				break;
			case 3:// 排行
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 20:
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							Log.i(TAG, "====JSL==== info[i]= " + i + "="
									+ info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							Log.i(TAG, "====JSL==== title[i]= " + i + "="
									+ title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					adapter.notifyDataSetChanged();
					adapter_key.update_type();
				}
				break;
			case 11: // 歌星反查
				if (layer == 1) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							Log.i(TAG, "====JSL==== info[i]= " + i + "="
									+ info[i]);
						} catch (Exception e) {
							// TODO: handle exception
							info[i] = "";
						}
					}
					showGM1(0);
				} else if (layer == 2) {
					try {
						TV_page = myjObject.getString("page");
					} catch (Exception e) {
						TV_page = "0/0";
					}
					for (int i = 0; i < 6; i++) {
						try {
							info[i] = myjObject.getString("list" + i);
							// Log.i(TAG,
							// "====JSL==== info[i]= "+i+"="+info[i]);
							listjObject = new JSONObject(info[i]);
							title[i] = listjObject.getString("song_name");
							title_name[i] = listjObject.getString("singer");
							Log.i(TAG, "====JSL==== title[i]= " + i + "="
									+ title[i]);
						} catch (Exception e) {
							// TODO: handle exception
							title[i] = "";
							title_name[i] = "";
						}
					}
					showGM2(0);
				}
				break;
			case 21:// 下载-已下�?
				break;
			case 22:// 下载-已下�?
				break;
			case 31: // 已点-已播
				break;
			case 32: // 已点-已播
				break;
			}
			// }
		} catch (JSONException e) {
			Log.i(TAG, "====JSL==== OsdConsole() ===JSONException ");
		}
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String result = bundle.getString("result");
			Log.i(TAG, "resul================" + result);

			if (result.equals("upgrade")) {
				// showUpgradeDialog();
			} else if (result.equals("reboot")) {
				showRebootDialog();
			}

		}
	}

	public class SataBroadcastReceiver extends BroadcastReceiver {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.i("song", "usb connect ============="+intent.getAction());
			if ("android.intent.action.MEDIA_MOUNTED"
					.equals(intent.getAction())) {
				isUsbMediaConnect = true;
			} else {
				isUsbMediaConnect = false;
			}
		}

	}
	
	
	public class FromOutsideInitReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if("justlink.action.intent.data_inited_outside_singer".equals(arg1.getAction())){
				Message msg = Message.obtain();
				msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER_DATA;
				msg.obj = outSideSinger;
				fragmentHander.sendMessage(msg);
				sendBroadcast(new Intent("justlink.action.intent.data_inited"));
				
			}else if("justlink.action.intent.data_inited_outside_song".equals(arg1.getAction())){
				SongJsonParseUtils.selectSong("song_select", outSideSong, 4, "");
				fragmentHander
						.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_IMMEDIATE_PLAY);
			}
		}
		
	}

	private void init_video_audio_port() {
		/*
		 * int ret = 0; int mHdmi = 0; int mSpdif = 0; int mDac = 0; String
		 * HdmiSet; String SpdifSet;
		 * 
		 * if (mAudioManager == null) { mAudioManager = new HiAudioManager(); }
		 * if (mDisplayManager == null) { mDisplayManager = new
		 * HiDisplayManager(); }
		 * 
		 * mHdmi_index =
		 * mAudioManager.getAudioOutput(HiAudioManager.AUDIO_OUTPUT_PORT_HDMI);
		 * mSpdif_index =
		 * mAudioManager.getAudioOutput(HiAudioManager.AUDIO_OUTPUT_PORT_SPDIF);
		 * 
		 * 
		 * 
		 * 
		 * mDac = getAudioDac_ktv(); mHdmi = getAudioHdmi_ktv(); mSpdif =
		 * getAudioSpdif_ktv();
		 * 
		 * 
		 * if(mDac==0){ DacMute(); }else{ DacUnmute(); }
		 * mAudioManager.setAudioOutput(HiAudioManager.AUDIO_OUTPUT_PORT_HDMI,
		 * mHdmi); //设置为解码输�?
		 * mAudioManager.setAudioOutput(HiAudioManager.AUDIO_OUTPUT_PORT_SPDIF,
		 * mSpdif);//设置为解码输�?/ }
		 * 
		 * private void restore_video_audio_port(){ /* int ret = 0; int
		 * mHdmi_index = 0; int mSpdif_index = 0; int mHbr_index = 0; int
		 * mRatio_index = 0; int mTran_index = 0; String HdmiSet; String
		 * SpdifSet;
		 * 
		 * if (mAudioManager == null) { mAudioManager = new HiAudioManager(); }
		 * if (mDisplayManager == null) { mDisplayManager = new
		 * HiDisplayManager(); }
		 * 
		 * HdmiSet = SystemProperties.get("persist.sys.audio.hdmi.sysset");
		 * SpdifSet = SystemProperties.get("persist.sys.audio.spdif.sysset");
		 * 
		 * mHdmi_index = new Integer(HdmiSet).intValue(); mSpdif_index = new
		 * Integer(SpdifSet).intValue(); Log.i(TAG,
		 * "====JSL====, restore_video_audio_port mHdmi_index="+mHdmi_index);
		 * Log.i(TAG,
		 * "====JSL====, restore_video_audio_port mSpdif_index="+mSpdif_index);
		 * Log.i(TAG, "====JSL====, restore_video_audio_port HdmiSet="+HdmiSet);
		 * Log.i(TAG,
		 * "====JSL====, restore_video_audio_port SpdifSet="+SpdifSet);
		 * 
		 * setAudioDac(getAudioDac());
		 * mAudioManager.setAudioOutput(HiAudioManager.AUDIO_OUTPUT_PORT_HDMI,
		 * mHdmi_index); //设置为系统设�?
		 * mAudioManager.setAudioOutput(HiAudioManager.AUDIO_OUTPUT_PORT_SPDIF,
		 * mSpdif_index);//设置为系统设�?
		 */
	}

	private int KTV_HardwareVerNum() {

		return 0;
	}

	private void upgrate() {

	}

	public static boolean isWorked(Context context, String packageName) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	public void showUpgradeDialog() {
		CommonDialog dialog = new CommonDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen,
				new TouchOnCallback());
		dialog.show();
		dialog.SetText("发现新版本是否后台升级");
	}

	private class TouchOnCallback implements OnCallback {

		@Override
		public void OkcallBack() {
			/*
			 * Intent regIntent = new Intent(MainActivity.this,
			 * UpgradeService.class); regIntent.putExtra("download", "true");
			 * startService(regIntent);
			 */

		}

		@Override
		public void CancelcallBack() {

		}

		@Override
		public void MidcallBack() {
			// TODO Auto-generated method stub
		}
	}

	public void showRebootDialog() {
		CommonDialog dialog = new CommonDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen,
				new TouchOnCallback1());
		dialog.show();
		dialog.SetText("下载完毕，是否重启？");
	}

	private class TouchOnCallback1 implements OnCallback {

		@Override
		public void OkcallBack() {
			// handler.sendEmptyMessage(0);
			execCommand("system busybox mount -o remount,rw /system");
			StringBuffer sb1 = new StringBuffer();
			sb1.append("system cp -rf ").append(SDCARD_PATH).append("/")
					.append(FILE_NAME).append(" /system/app/")
					.append(FILE_NAME).append(" && chmod 777 /system/app/")
					.append(FILE_NAME).append(" &&cp -rf ").append(SDCARD_PATH)
					.append("/").append(LIB_NAME).append(" /system/lib/")
					.append(LIB_NAME).append(" && chmod 777 /system/lib/")
					.append(LIB_NAME).append("&& sleep 1 && reboot");
			Log.v("upgrade", sb1.toString());
			execCommand(sb1.toString());
			// execCommand("system busybox mount -o remount,ro /system");
			// execCommand("system reboot");

		}

		@Override
		public void CancelcallBack() {

		}

		@Override
		public void MidcallBack() {
			// TODO Auto-generated method stub
		}
	}

	private void execCommand(String cmd) {
		if (isEmptyString(cmd))
			return;
		SocketClient socketClient = new SocketClient();
		socketClient.writeMess(cmd);
		socketClient.readNetResponseSync();
	}

	public static boolean isEmptyString(String str) {
		return null == str || "".equals(str);
	}

	public static void doKeyAction(int keycode) {
		String command = "system input keyevent " + keycode;
		SocketClient socket = new SocketClient();
		socket.writeMess(command);

	}

	private void setLanguage() {
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		String country = locale.getCountry();
		Log.i(TAG, "===============getLanguage()==================" + language);
		Log.i(TAG, "===============getCountry()==================" + country);

		if (language.endsWith("zh")) {
			if (country.endsWith("CN")) {
				JNILib.setGlobalLanguage(0);
			} else {
				JNILib.setGlobalLanguage(1);
			}
		} else {
			JNILib.setGlobalLanguage(2);
		}
	}

	private void get_id() {
		// for(int i=0;i<2;i++){
		try {
			deviceinfo = readFromFile("/dev/block/platform/hi_mci.1/by-name/deviceinfo");
			deviceinfo = deviceinfo.substring(25, 34);
		} catch (Exception e) {
			// Log.i(TAG, "deviceinfo=================false" );
			// str =
			// "system chmod 777 /dev/block/platform/hi_mci.1/by-name/deviceinfo";
			// socket = new SocketClient();
			// socket.writeMess(str);
		}
		// }
		// Log.i(TAG, "deviceinfo=================" + deviceinfo);
		if (deviceinfo != null) {
			JNILib.setSN(deviceinfo);
			// deviceinforead=false;
		}
		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { while (deviceinforead) { try{
		 * deviceinfo
		 * =readFromFile("/dev/block/platform/hi_mci.1/by-name/deviceinfo");
		 * deviceinfo=deviceinfo.substring(25,34); Log.i(TAG,
		 * "deviceinfo=================" + deviceinfo); if (deviceinfo!=null){
		 * JNILib.setSN(deviceinfo); deviceinforead=false; } }catch(Exception
		 * e){ //socket.writeMess(str); }
		 * 
		 * }
		 * 
		 * } }).start();
		 */

	}

	private String readFromFile(String paramString) throws IOException {
		BufferedReader localBufferedReader = new BufferedReader(new FileReader(
				paramString), 1024);

		try {
			String str = localBufferedReader.readLine();
			return str;
		} finally {
			localBufferedReader.close();
		}
	}

	private void LanguangeChange() {
		int jLanguage = JNILib.getGlobalLanguage();
		// Log.i(TAG,
		// "===============LanguangeChange()=================="+jLanguage);
		/*
		 * try { IActivityManager am = ActivityManagerNative.getDefault();
		 * Configuration config = am.getConfiguration(); Locale locale = null;
		 * if(jLanguage==0){ locale = new Locale("zh"); config.locale =
		 * Locale.SIMPLIFIED_CHINESE; }else if(jLanguage==1){ locale = new
		 * Locale("zh"); config.locale = Locale.TAIWAN; }else{ locale = new
		 * Locale("en_US"); config.locale = Locale.ENGLISH; }
		 * Locale.setDefault(locale); am.updateConfiguration(config);
		 * BackupManager.dataChanged("com.android.providers.settings"); } catch
		 * (RemoteException e) { // Intentionally left blank //Log.i(TAG,
		 * "===============LanguangeChange()=========RemoteException=========");
		 * }
		 */
	}

	private void writeSocket(String msg) {
		SocketClient socketClient = new SocketClient();
		socketClient.writeMess(msg);
		socketClient.readNetResponseSync();
	}

	private Thread thread = null, thread_time = null;

	private int state =0;
	protected void onResume() {
		super.onResume();
		// initMicPhone();
		initFragment();
		killApp();
		/*
		 * ActivityManager am = (ActivityManager)
		 * getSystemService(Context.ACTIVITY_SERVICE);
		 * //am.forceStopPackage("com.justlink.remoteservicedemo");
		 * am.killBackgroundProcesses("com.justlink.remoteservicedemo");
		 * killApp(); Log.i("song", "onResume"); if(isZX) setLogoImage();
		 */
		
		if (thread == null) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						// Log.v(TAG, "111111111");
						try {
							Thread.sleep(100);
						} catch (Exception e) {
						}
						if(state < 3){
							state = JNILib.GetBootStep();
						Log.i("song","data init progress"+state);
						}else if(state == 3){// 数据整理完成，默认播放
							state = 4;
							MyApplication.isDataInit = true;
							if(isFromOutSideOrder){
							sendBroadcast(new Intent("justlink.action.intent.data_inited_outside_order"));
							}else if(isFromOutSideSinger){
								sendBroadcast(new Intent("justlink.action.intent.data_inited_outside_singer"));
							}else if(isFromOutSideSong){
								sendBroadcast(new Intent("justlink.action.intent.data_inited_outside_song"));
							}
							
							ArrayList<SongSearchBean> listLocal = SongJsonParseUtils
									.getSongDatas3(0, "", 0, 22, 0, 0, 1, "");
									
							ArrayList<SongSearchBean> list = SongJsonParseUtils.getSongDatas3(
									0, "", 0, 31, 0, 0, 4, "");                                                                                                                 
							
							Log.i("song","local size=="+listLocal.size()+"==select size=="+list.size());
							
							if (listLocal == null || listLocal.size() == 0) {								
								fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_PLAY_DEFAULT_VIDEO);																									
								
							}else if(list!=null && list.size()>0){
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										playVideoNext();		
									}
								});
								
							}else{
								Log.i("song","play local song========");
								SongJsonParseUtils.selectSong("song_select",
										listLocal.get(0).getSongNumber(), 0, "");
							}
						}

						// TODO Auto-generated method stub
						int ret = JNILib.step(0, 0, 0, 0);
						if (ret != 0) {
							// Log.v(TAG, "================= ret = " + ret);
							// mHandler.sendEmptyMessage(ret);
							handler.sendEmptyMessage(ret);
						}
					}
				}
			});
			NoticeThreadRun = 1;
			if (thread_time == null)
				thread_time = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {

						}
						while (1 == NoticeThreadRun) {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {

							}

							// Log.i(TAG, "====JSL==== NoticePicPlayTime = " +
							// NoticePicPlayTime);
							if (-1 == NoticePicPlayTime) {

							} else if (0 == NoticePicPlayTime) {
								// Log.i(TAG,
								// "====JSL==== disappear pic, NoticePicPlayTime = "
								// + NoticePicPlayTime);;
								Message message = new Message();
								message.what = MESSAGE_REFRESH_NOTICE_IMAGE;
								handler.sendMessage(message);
							} else {
								NoticePicPlayTime--;
							}

							// 二维码显示时�?
							if (0 == mQrPlayTime) {
								Message message = new Message();
								message.what = MESSAGE_UPDATE_QR_IMAGE;
								handler.sendMessage(message);
							} else {
								mQrPlayTime--;
							}

							// OSD显示时间
							if (0 == OSDPlayTime) {
								if (OSDshow) {
									Message message = new Message();
									message.what = MESSAGE_UPDATE_OSD;
									handler.sendMessage(message);
								}
								if (SeekBarshow) {
									Message message = new Message();
									message.what = MESSAGE_UPDATE_BAR;
									handler.sendMessage(message);
								}
							} else {
								OSDPlayTime--;
								Log.i("OSDPlayTime", "OSDPlayTime--");
							}

							// gif图显示时�?
							if (0 == gifPlayTime) {
								if (gifShow) {
									Message message = new Message();
									message.what = MESSAGE_UPDATE_GIF_IMAGE;
									handler.sendMessage(message);
								}
							} else {
								gifPlayTime--;
							}

						}

					}
				});
			thread.start();
			thread_time.start();
			adapter_songlist1.downloadThread.start();
		}
		// Log.i("song","isplaynext=="+isPlayNext+"===="+PreviewVideo.getPauseStatus());

		// if(getIntent().getBooleanExtra("home", false))
		isPlayNext = true;
		// if(isPlayNext && PreviewVideo.getPauseStatus()!=1)
		//playVideoNext();

		pb.setVisibility(View.GONE);
		Log.i(TAG, "onresume end ");

	
		JNILib.NoticeRefresh(1);
		jump2model();

		try {
			new ProcessBuilder(order).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//new UpdateSongTxtDialog(this, R.style.MyDialog, "歌单").show();
		// new HiKaraokeTest(this).startRecorder();
	}
	
	

	protected void onPause() {
		super.onPause();

		// restore_video_audio_port();
		isPlayNext = true;
		// Log.i("====JSL====", "isPlayNext="+true);
		if (isScore && isFromHome) {
			mLineView.Destroy();
			mLineView.setVisibility(View.GONE);
			ScorePicView.setVisibility(View.GONE);

		}

		// mGlView.onPause();
		// JNILib.CloseUart();
	}

	// 判断文件夹是否存�? 存在 true, 不存�?false
	private boolean isFileExist(String strFolder) {
		File file = new File(strFolder);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void killApp() {
		// Log.i("====JSL====", "killApp");
		/*
		 * PackageManager packageManager = getPackageManager(); Intent
		 * mainIntent = new Intent(Intent.ACTION_MAIN, null);
		 * mainIntent.addCategory(Intent.CATEGORY_HOME); final List<ResolveInfo>
		 * apps = packageManager.queryIntentActivities( mainIntent, 0);
		 * 
		 * ActivityManager am = (ActivityManager)
		 * getSystemService(Context.ACTIVITY_SERVICE);
		 * List<ActivityManager.RunningAppProcessInfo> processes = am
		 * .getRunningAppProcesses(); for (int j = 0; j < processes.size(); j++)
		 * { ActivityManager.RunningAppProcessInfo actRSI = processes.get(j); if
		 * (actRSI.flags !=
		 * ActivityManager.RunningAppProcessInfo.FLAG_PERSISTENT && actRSI.flags
		 * != ActivityManager.RunningAppProcessInfo.FLAG_HAS_ACTIVITIES &&
		 * !PACKAGE_SKYPLAY.equals(actRSI.processName) &&
		 * !isLiveWallPaper(actRSI.processName)
		 * &&!"com.hisilicon.multiscreen.vime".equals(actRSI.processName)
		 * &&!"com.hisilicon.android.inputmethod.remote"
		 * .equals(actRSI.processName)
		 * &&!"com.sohu.inputmethod.sogouoem".equals(actRSI.processName)
		 * &&!"com.jsl.ktv".equals(actRSI.processName)
		 * &&!"com.sohu.inputmethod.sogou.tv".equals(actRSI.processName)
		 * &&!"android.process.media".equals(actRSI.processName)
		 * &&!"android.process.acore".equals(actRSI.processName)) {
		 * am.forceStopPackage(actRSI.processName);
		 * Log.i("onCreate","stop1111111"+actRSI.processName); } else if
		 * (actRSI.flags ==
		 * ActivityManager.RunningAppProcessInfo.FLAG_HAS_ACTIVITIES) { int flag
		 * = 0; Log.i("onCreate",""+apps.size()); for (int s = 0; s <
		 * apps.size(); s++) { if (actRSI.processName
		 * .equals(apps.get(s).activityInfo.applicationInfo.packageName)) { flag
		 * = 1; break; } } if (flag == 0 && !isLiveWallPaper(actRSI.processName)
		 * &&!"com.jsl.ktv".equals(actRSI.processName)) {
		 * am.forceStopPackage(actRSI.processName);
		 * Log.i("onCreate","stop2222222"+actRSI.processName); } } }
		 */
	}

	private boolean isLiveWallPaper(String packageName) {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo info = packageManager.getPackageInfo(packageName,
					PackageManager.GET_CONFIGURATIONS);
			if (info.reqFeatures != null) {
				for (int i = 0; i < info.reqFeatures.length; i++) {
					FeatureInfo reqFeatures = info.reqFeatures[i];
					if ("android.software.live_wallpaper"
							.equals(reqFeatures.name)) {
						return true;
					}
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
		return false;
	}

	protected void handWrite() {
	}

	private void printDisplay() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		float density = dm.density;
		float densitydp = dm.densityDpi;
		Log.v(TAG, "width = " + width);
		Log.v(TAG, "height = " + height);
		Log.v(TAG, "density = " + density);
		Log.v(TAG, "densitydp = " + densitydp);
	}

	/*
	 * @Override public boolean dispatchKeyEvent(KeyEvent event) { if (null !=
	 * presentation) { return presentation.dispatchKeyEvent(event); } return
	 * super.dispatchKeyEvent(event); }
	 */

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		micphone.stop();
		micphone.release();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		micphone.stop();
		micphone.release();

		// mIme.release();
		DestroyVideoDoubleScreen();
		stopBackMusic();
		// restore_video_audio_port();
		unregisterReceiver(mVolumeReceiver);
		unregisterReceiver(netStatReceiver);
		unregisterReceiver(sataReceiver);
		unregisterReceiver(mMICReceiver);
		unregisterReceiver(outSideReceiver);
		// SocketClient socketClient = null;
		// socketClient = new SocketClient();
		// socketClient.writeMess("system busybox killall mediaserver");
		// socketClient.readNetResponseSync();

		// dialog1.dismiss();
		// android.os.Process.killProcess(android.os.Process.myPid());
		Log.i("song", "on destroy============");

	}

	private void PlayVideoDoubleScreen(String path) {
		// if (null != presentation) {
		Log.v(TAG, "====JSL====, presentation is created,play path:" + path);
		stopVideo();
		playVideo(path);
		/*
		 * } else {
		 * Log.v(TAG,"====JSL====, creat presentation, and play path:"+path);
		 * presentation = new VideoPresentation(this, hdmiDisplay, path);
		 * presentation.show(); }
		 */
	}

	private void PlayVideoDoubleScreenNoRepeat(String path) {
		PlayVideoDoubleScreen(path);
		setRepeat(0);
	}

	private void PlayVideoDoubleScreenRepeat(String path) {
		PlayVideoDoubleScreen(path);
		setRepeat(1);
	}

	private void PlayFirstSongDoubleScreen() {
		// if (null != presentation) {
		playVideoNext();
		//Log.i(TAG, "PLAY NEXT 5555555");
		// }
		// else
		// {
		CurrentPlayPath = JNILib.playNextSong(1);
		// Log.v(TAG,"====JSL====, get first VideoPath = " + CurrentPlayPath);
	//	PlayVideoDoubleScreenNoRepeat(CurrentPlayPath);
		//ClearVideoDoubleScreenPauseStatus();
		// }
	}

	public void RefreshVideoDoubleScreenPauseStatus() {
		// if (null != presentation) {
		if (getPauseStatus() == 0)
			setNoticeImage(JLINK_NOTICE_TYPE_PAUSE, 0);
		else {
			setNoticeImage(JLINK_NOTICE_TYPE_UNPAUSE, 0);
		}
		// }
	}

	public void ClearVideoDoubleScreenPauseStatus() {
		if (isPause) {
			isPause = false;
			startRecoverVulume();
		}
		// if (null != presentation) {
		// presentation.setNoticeImage(presentation.JLINK_NOTICE_TYPE_UNPAUSE,
		// 0);
		// }
	}

	private void DestroyVideoDoubleScreen() {
		/*
		 * if (null != presentation) { Log.v(TAG,"DestroyVideoDoubleScreen");
		 * presentation.stopVideo(); presentation.stopThread();
		 * presentation.dismiss(); presentation = null; }
		 */
	}

	public void setVoiceMode(int VoiceMode) {
		mVoiceMode = VoiceMode;
		if (isPrepared) {
			if (isMusic) {
				PreviewVideo.setVoiceMode(2);
			} else {
				PreviewVideo.setVoiceMode(mVoiceMode);
			}
		}
	}

	public int setMusicTone(int tone) {
		int pCmdId = HiMediaPlayerInvoke.CMD_SET_SOUND_TONE;
		int pArg = tone;
		boolean pIsGet = false;
		String IMEDIA_PLAYER = "android.media.IMediaPlayer";

		Parcel Request = Parcel.obtain();
		Parcel Reply = Parcel.obtain();

		Request.writeInterfaceToken(IMEDIA_PLAYER);
		Request.writeInt(pCmdId);
		Request.writeInt(pArg);
		if ((mediaPlayerMusic != null)) {

			Class<?> cls = mMediaPlayer.getClass();

			try {
				Method method = cls.getDeclaredMethod("invoke", Parcel.class,
						Parcel.class);
				method.setAccessible(true); // 如果隐藏接口是public�? 这句可以不要
				method.invoke(mMediaPlayer, Request, Reply);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// mediaPlayerMusic.invoke(Request, Reply);
		}

		if (pIsGet) {
			Reply.readInt();
		}

		int Result = Reply.readInt();

		Request.recycle();
		Reply.recycle();

		return Result;
	}

	public void setTone(int tone) {// 变调-5~5
		mTone = tone;
		if (isPrepared) {
			if (isMusic && (mediaPlayerMusic != null)) {
				setMusicTone(tone);
			} else {
				PreviewVideo.setAudioTone(tone);
			}
		}
	}

	public int getTone() {
		return mTone; // videoView.getAudioTone();
	}

	public void initMicPhone() {
		micphone = (Micphone) getSystemService("Micphone");
		micphone.initial();
		micphone.start();
		micphone.setVolume(80);
		Log.i("song", "mic init finish===========");
	}

	public void setRepeat(int bRepeat) {
		isRepeat = bRepeat;
	}

	public void playVideo(String path) {
		if (path == null)
			return;
		int PathType = getAVPath(path);
		// String ext_name = getExtensionName(path);

		// Log.v(TAG,"play path:"+path);
		// Log.v(TAG,"PathType :"+PathType);
		if (PathType <= 0) {
			Log.v(TAG, "path error, play default video" + path);
			PathType = 1;
			path = "/mnt/sdcard/jlink/video/start.mpg";

		}
		isPrepared = false;
		isPrepared_music = false;
		mPIPPlayPosCur_s = 0;
		mMusicDuration_s = 0;

		if (PathType == 2) // audio + video
		{
			// Log.v(TAG,"audio path :"+mAudioPath);
			// Log.v(TAG,"video path :"+mVideoPath);
			isMusic = true;
			PreviewVideo.setDisplayType(2);
			PreviewVideo.playVideo(mVideoPath);
			playMusic(mAudioPath);
			setVoiceMode(mVoiceMode);
		} else {
			isMusic = false;
			PreviewVideo.setDisplayType(-1);
			PreviewVideo.playVideo(mVideoPath);
		}

	}

	/*
	 * 拆分音视频播放路径
	 */
	private int getAVPath(String filename) {
		int type = -1;
		int dot = 0;
		Log.i(TAG, "current play path===" + filename);
		if ((filename != null) && (filename.length() > 0)) {
			dot = filename.lastIndexOf('|');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				type = 2;
				mVideoPath = filename.substring(0, dot);
				mAudioPath = filename.substring(dot + 1);
				if (!new File(mVideoPath).exists()) {
					type = 2;
					// mVideoPath = "/mnt/sdcard/jlink/video/start.mpg";
					// Log.v(TAG," audio file type:"+type);
					ivMusicbg.setVisibility(View.VISIBLE);
					return type;
				}
			} else {
				String extFileName;
				dot = filename.lastIndexOf('.');
				if ((dot > -1) && (dot < (filename.length() - 1))) {
					extFileName = filename.substring(dot + 1);
					Log.v(TAG, "getAVPath extFileName :" + extFileName);
					if ((extFileName.equals("mp3"))
							|| extFileName.equals("wav")
							|| extFileName.equals("aac")
							|| extFileName.equals("wma")
							|| extFileName.equals("mp2")
							|| extFileName.equals("ogg")
							|| extFileName.equals("mpa")
							|| extFileName.equals("m4a")
							|| extFileName.equals("ape")
							|| extFileName.equals("flac")) {
						type = 2;
						// mVideoPath = "/mnt/sdcard/jlink/video/start.mpg";
						ivMusicbg.setVisibility(View.VISIBLE);
						mAudioPath = filename;
						// Log.v(TAG," audio file type:"+type);
						return type;
					}
				}

				type = 1;
				mVideoPath = filename;
				ivMusicbg.setVisibility(View.GONE);
			}
		}

		return type;
	}

	public void RepeatVideo() {
		// isRecordMusic = false;
		mPIPPlayPosCur_s = 0;
		if (isMusic && (mediaPlayerMusic != null)) {
			if (isPrepared_music)
				mediaPlayerMusic.seekTo(0);
		}

		if (PreviewVideo != null) {
			PreviewVideo.RepeatVideo();
		}
	}

	public void stopVideo() {
		isPlay = false;
		isPrepared = false;
		mPIPPlayPosCur_s = 0;
		mMusicDuration_s = 0;
		if (null != mediaPlayerMusic) {
			stopMusic();
		}
		if (PreviewVideo != null) {
			PreviewVideo.stopPlayback();
		}
	}

	private void stopMusic() {
		if (null != mediaPlayerMusic) {
			mediaPlayerMusic.stop();
			mediaPlayerMusic.release();
			mMusicDuration_s = 0;
			isPrepared_music = false;
			isMusic = false;
			mediaPlayerMusic = null;
		}
	}

	public void pauseVideo() {
		if (isMusic && (mediaPlayerMusic != null)) {
			if (mediaPlayerMusic.isPlaying() == true) {
				mediaPlayerMusic.pause();
			} else {
				mediaPlayerMusic.start();
			}
		}

		if (PreviewVideo != null) {
			PreviewVideo.pauseVideo();
		}

	}

	private void playMusic(String path) {
		if (null == mediaPlayerMusic) {
			isRecordMusic = true;
			mediaPlayerMusic = new MediaPlayer();
			mediaPlayerMusic
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer arg0) {
							UpdateQrDisp();
							if (isMusic) {
								isPrepared_music = true;
								{
									// Message message1 =new Message();
									// message1.what =
									// MESSAGE_GET_PLAYER_POSION;
									// handler.sendMessageDelayed(message1,
									// 500);
								}
							}
						}
					});

			mediaPlayerMusic
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// 播出完毕事件
						@Override
						public void onCompletion(MediaPlayer arg0) {
							if (isMusic) {
								isRecordMusic = false;
								playVideoNext();

							}
						}
					});
		} else {
			mediaPlayerMusic.reset();
		}

		try {
			mediaPlayerMusic.setDataSource(path);
			mediaPlayerMusic.prepare();
			timetotal
					.setText(getTimeFormatValue(mediaPlayerMusic.getDuration() / 1000));
			// Log.i("CHW","PATH=="+path+"==time=="+mediaPlayerMusic.getDuration());
			mediaPlayerMusic.start();
			mMusicDuration_s = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPauseStatus() {
		if (isMusic && (mediaPlayerMusic != null)) {
			if (mediaPlayerMusic.isPlaying() == true) {
				return 1; // play
			} else {
				return 0; // pause
			}
		} else if (PreviewVideo != null) {
			return PreviewVideo.getPauseStatus();
		}

		return 1;

	}

	public void playVideoNext() {
		// if(isOnDownLoad)
		// return;
		//preLoading.setVisibility(View.VISIBLE);
		isRecording = false;
		isRecordMusic = false;
		isSeekbarTraking = false;
		isOnScore = false;
		mLineView.Destroy();
		mLineView.setVisibility(View.GONE);
		ScorePicView.setVisibility(View.GONE);
		tmpVideoPath = JNILib.playNextSong(1);
		recordText.setVisibility(View.GONE);
		// setNoticeImage(JLINK_NOTICE_TYPE_UNMUTE, 0);
		Log.v("song", "====JSL====,playVideoNext get next Path:" + tmpVideoPath);
		if (tmpVideoPath == null || tmpVideoPath.length() <= 0) {
			Log.v("song", "====JSL====,get next VideoPath failed ");
			if (PreviewVideo != null)
				PreviewVideo.start();
		} else {
			stopVideo();
			playVideo(tmpVideoPath);
			setRepeat(0);
			CurrentPlayPath = tmpVideoPath;
		}
		// int publicStatus = JNILib.getPublicStatus();
		// if(publicStatus == 1){
		// jLanguage=JNILib.getGlobalLanguage();
		// publictext.setText(VideoString.Public[jLanguage]);
		// publictext.setVisibility(View.VISIBLE);
		// }else{
		// publictext.setVisibility(View.INVISIBLE);
		// }
		if (!VideoViewFullScreen && !isSearchViewShow){
			adapter_songlist1.refresh();// 刷新已点列表
			currentpage[0] = 1;
		}
		
		mMainNoticeLayout.setVisibility(View.GONE);
		
	}

	public boolean getStartStatus() {
		return PreviewVideo.getStartStatus();
	}

	public int getMusicDuration() {
		if (isMusic) {
			if (null != mediaPlayerMusic) {
				if (mMusicDuration_s == 0) {
					mMusicDuration_s = mediaPlayerMusic.getDuration() / 1000;
				}
				return mMusicDuration_s;
			} else {
				return 0;
			}
		} else
			return 0;

	}

	public int getDuration() {
		if (isMusic) {
			return getMusicDuration();
		} else {
			if (null != PreviewVideo) {
				return PreviewVideo.getDuration();
			} else {
				return 0;
			}
		}
	}

	private void playBackMusic(String path) {
		if (!isBackMusicPrepare) {
			Log.v(TAG,
					"====JSL====, playBackMusic too fast and return. isBackMusicPrepare="
							+ isBackMusicPrepare);
			return;
		}

		isBackMusicPrepare = false;
		if (null == mediaPlayerBackMusic) {
			mediaPlayerBackMusic = new MediaPlayer();
			mediaPlayerBackMusic
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer arg0) {
							isBackMusicPrepare = true;
						}
					});

			mediaPlayerBackMusic
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// 播出完毕事件
						@Override
						public void onCompletion(MediaPlayer arg0) {
							// arg0.start();
						}
					});
		} else {
			mediaPlayerBackMusic.reset();
		}

		// if (mediaPlayerBackMusic.isPlaying())
		// mediaPlayerBackMusic.reset();

		try {
			mediaPlayerBackMusic.setDataSource(path);
			mediaPlayerBackMusic.prepare();
			mediaPlayerBackMusic.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopBackMusic() {
		isBackMusicPrepare = true;
		if (null != mediaPlayerBackMusic) {
			mediaPlayerBackMusic.stop();
			mediaPlayerBackMusic.release();
			mediaPlayerBackMusic = null;
		}
	}

	public void readQrDisp() {
		// Log.v(TAG,"readQrDisp=");
		if (isFileExist(TOUCH_FILE_NAME)) {
			String text = null;
			text = readFileSdcard(TOUCH_FILE_NAME);
			String s[] = text.split(" ");
			/*
			 * Log.v(TAG,"readQrDisp0="+s[0]); Log.v(TAG,"readQrDisp1="+s[1]);
			 * Log.v(TAG,"readQrDisp2="+s[2]); Log.v(TAG,"readQrDisp3="+s[3]);
			 * Log.v(TAG,"readQrDisp4="+s[4]);
			 */
			appDownAddress = s[4];
			Log.i(TAG, "读取二维码信�?=readQrDisp=" + text + "app url ==="
					+ appDownAddress);
		}

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

	public void UpdateQrDisp() {
		/*
		 * int bDisp = JNILib.qrIsDisp(); // Log.i("CHW",
		 * "是否显示二维�?="+bDisp+"mQrPlayTime=="+mQrPlayTime); if(isZX) weixinCode =
		 * JNILib.GetWechatTip(0); else weixinCode = JNILib.GetWechatTip(1);
		 * //Log.i("jlink","weixinCode==="+weixinCode); // if ((bDisp == 1) &&
		 * (mQrPlayTime > 0)) if (mQrPlayTime > 0) { try { //readQrDisp();
		 * appDownAddress = JNILib.GetAppDownloadUrl(); SystemClock.sleep(50);
		 * Log.i(TAG,"app url =="+appDownAddress); if
		 * (!appDownAddress.equals("")) { Bitmap appAddressBitmap =
		 * EncodingHandler.createQRCode(appDownAddress, 300);
		 * mappAddressImage.setImageBitmap(appAddressBitmap);
		 * mappAddressImage.setVisibility(View.VISIBLE); //
		 * jLanguage=JNILib.getGlobalLanguage();
		 * mappAddrtext.setText(VideoString.Download[jLanguage]);
		 * mappAddrtext.setVisibility(View.VISIBLE); }else {
		 * mappAddressImage.setVisibility(View.GONE);
		 * mappAddrtext.setVisibility(View.GONE); } } catch (Exception e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * try { if (!qrCode.equals("")) { Bitmap qrCodeBitmap =
		 * EncodingHandler.createQRCode(qrCode, 300);
		 * mqrImage.setImageBitmap(qrCodeBitmap);
		 * mqrImage.setVisibility(View.VISIBLE); //
		 * jLanguage=JNILib.getGlobalLanguage();
		 * mqrinfotext.setText(VideoString.Connect[jLanguage]);
		 * mqrinfotext.setVisibility(View.VISIBLE);
		 * mqrtext.setText("("+qrCode+")"); mqrtext.setVisibility(View.VISIBLE);
		 * }else { mqrImage.setVisibility(View.GONE);
		 * mqrinfotext.setVisibility(View.GONE);
		 * mqrtext.setVisibility(View.GONE); }
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * try { if (!TextUtils.isEmpty(weixinCode)) { Bitmap wxCodeBitmap =
		 * EncodingHandler.createQRCode(weixinCode, 300);
		 * mWinxinLinkImage.setImageBitmap(wxCodeBitmap);
		 * mWinxinLinkImage.setVisibility(View.VISIBLE); //
		 * jLanguage=JNILib.getGlobalLanguage();
		 * mwxinfotext.setText(VideoString.Weixin[jLanguage]);
		 * mwxinfotext.setVisibility(View.VISIBLE);
		 * 
		 * }else { mWinxinLinkImage.setVisibility(View.GONE);
		 * mwxinfotext.setVisibility(View.GONE); }
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * } else { mqrImage.setVisibility(View.GONE);
		 * mappAddressImage.setVisibility(View.GONE);
		 * mappAddrtext.setVisibility(View.GONE);
		 * mqrinfotext.setVisibility(View.GONE);
		 * mqrtext.setVisibility(View.GONE);
		 * mWinxinLinkImage.setVisibility(View.GONE);
		 * mwxinfotext.setVisibility(View.GONE); }
		 */
	}

	private void setNoticeImage(int NoticeType, int NoticeValue) {
		int iconVolID = 0;
		int NOTICE_ICON_PLAY_TIME = 3;
		int Volume_disp = 0;
		String str_notice;
		switch (NoticeType) {
		case JLINK_NOTICE_TYPE_PAUSE:
			mNoticeImage.setVisibility(View.VISIBLE);
			mNoticeImage.setBackgroundResource(R.drawable.icon_pause);
			jLanguage = JNILib.getGlobalLanguage();
			mNoticeText.setText(VideoString.Stop[jLanguage]);
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			NoticePicPlayTime = -1;
			NoticeState_bPause = 1;
			play_pause.setBackgroundResource(R.drawable.play_button);
			play_pause_text.setText(VideoString.Play[jLanguage]);
			break;
		case JLINK_NOTICE_TYPE_UNPAUSE:
			mNoticeImage.setVisibility(View.VISIBLE);
			mMainNoticeLayout.setVisibility(View.GONE);
			NoticePicPlayTime = 0;
			NoticeState_bPause = 0;
			play_pause.setBackgroundResource(R.drawable.pause_button);
			play_pause_text.setText(VideoString.Stop[jLanguage]);
			if (NoticeState_bMute == 1)
				setNoticeImage(JLINK_NOTICE_TYPE_MUTE, 0);
			break;
		case JLINK_NOTICE_TYPE_QIFEN:
			mNoticeImage.setVisibility(View.VISIBLE);
			if (NoticeValue == 1) {
				iconVolID = R.drawable.icon_hecai;
				jLanguage = JNILib.getGlobalLanguage();
				str_notice = VideoString.Good[jLanguage];
			} else if (NoticeValue == 2) {
				iconVolID = R.drawable.icon_daocai;
				jLanguage = JNILib.getGlobalLanguage();
				str_notice = VideoString.Come[jLanguage];
			} else if (NoticeValue == 3) {
				iconVolID = R.drawable.icon_huanhu;
				jLanguage = JNILib.getGlobalLanguage();
				str_notice = VideoString.Great[jLanguage];
			}

			else {
				iconVolID = R.drawable.icon_hecai;
				jLanguage = JNILib.getGlobalLanguage();
				str_notice = VideoString.Good[jLanguage];
			}

			mNoticeImage.setBackgroundResource(iconVolID);
			mNoticeText.setText(str_notice);
			NoticePicPlayTime = NOTICE_ICON_PLAY_TIME + 2;
			mMainNoticeLayout.setVisibility(View.VISIBLE);

			break;
		case JLINK_NOTICE_TYPE_NEXT:
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			mNoticeImage.setVisibility(View.VISIBLE);
			mNoticeImage.setBackgroundResource(R.drawable.icon_next);
			jLanguage = JNILib.getGlobalLanguage();
			mNoticeText.setText(VideoString.Next[jLanguage]);
			NoticePicPlayTime = NOTICE_ICON_PLAY_TIME - 1;

			break;
		case JLINK_NOTICE_TYPE_REPEAT:
			mNoticeImage.setVisibility(View.VISIBLE);
			mNoticeImage.setBackgroundResource(R.drawable.icon_chongchang);
			jLanguage = JNILib.getGlobalLanguage();
			mNoticeText.setText(VideoString.Repeat[jLanguage]);
			NoticePicPlayTime = NOTICE_ICON_PLAY_TIME - 1;
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			break;
		case JLINK_NOTICE_TYPE_YUANCHANG:
			mNoticeImage.setVisibility(View.VISIBLE);
			mNoticeImage.setBackgroundResource(R.drawable.icon_yuanchang);
			jLanguage = JNILib.getGlobalLanguage();
			mNoticeText.setText(VideoString.Original[jLanguage]);
			NoticePicPlayTime = NOTICE_ICON_PLAY_TIME;
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			banchang_button.setBackgroundResource(R.drawable.yuanchang_button);
			banchang_button_text.setText(VideoString.Original[jLanguage]);
			break;
		case JLINK_NOTICE_TYPE_BANCHANG:
			mNoticeImage.setVisibility(View.VISIBLE);
			mNoticeImage.setBackgroundResource(R.drawable.icon_banchang);
			jLanguage = JNILib.getGlobalLanguage();
			mNoticeText.setText(VideoString.Accomp[jLanguage]);
			NoticePicPlayTime = NOTICE_ICON_PLAY_TIME;
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			banchang_button.setBackgroundResource(R.drawable.banchang_button);
			banchang_button_text.setText(VideoString.Accomp[jLanguage]);
			break;
		case JLINK_NOTICE_TYPE_MUTE:
			mNoticeImage.setVisibility(View.VISIBLE);
			mNoticeImage.setBackgroundResource(R.drawable.icon_mute);
			jLanguage = JNILib.getGlobalLanguage();
			mNoticeText.setText(VideoString.Mute[jLanguage]);
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			NoticePicPlayTime = -1;
			NoticeState_bMute = 1;
			break;
		case JLINK_NOTICE_TYPE_UNMUTE:
			NoticePicPlayTime = 0;
			mMainNoticeLayout.setVisibility(View.GONE);
			NoticeState_bMute = 0;
			break;
		case JLINK_NOTICE_TYPE_VOLUME:
			mMainNoticeLayout.setVisibility(View.VISIBLE);
			if (NoticeValue == 0) {
				mNoticeImage.setBackgroundResource(R.drawable.icon_mute);
				jLanguage = JNILib.getGlobalLanguage();
				mNoticeText.setText(VideoString.Mute[jLanguage]);
				mMainNoticeLayout.setVisibility(View.VISIBLE);
				NoticePicPlayTime = -1;
				NoticeState_bMute = 1;
			} else {
				iconVolID = R.drawable.icon_15;
				Volume_disp = NoticeValue * 5;
				if (NoticeValue == 1) {
					iconVolID = R.drawable.icon__1;
					Volume_disp = 5;
				} else if (NoticeValue == 2) {
					iconVolID = R.drawable.icon__2;
					Volume_disp = 10;
				} else if (NoticeValue == 3) {
					iconVolID = R.drawable.icon__3;
					Volume_disp = 15;
				} else if (NoticeValue == 4) {
					iconVolID = R.drawable.icon__4;
					Volume_disp = 20;
				} else if (NoticeValue == 5) {
					iconVolID = R.drawable.icon__5;
					Volume_disp = 25;
				} else if (NoticeValue == 6) {
					iconVolID = R.drawable.icon__5;
					Volume_disp = 30;
				} else if (NoticeValue == 7) {
					iconVolID = R.drawable.icon__5;
					Volume_disp = 35;
				} else if (NoticeValue == 8) {
					iconVolID = R.drawable.icon__6;
					Volume_disp = 40;
				} else if (NoticeValue == 9) {
					iconVolID = R.drawable.icon__6;
					Volume_disp = 45;
				} else if (NoticeValue == 10) {
					iconVolID = R.drawable.icon__7;
					Volume_disp = 50;
				} else if (NoticeValue == 11) {
					iconVolID = R.drawable.icon__7;
					Volume_disp = 55;
				} else if (NoticeValue == 12) {
					iconVolID = R.drawable.icon__8;
					Volume_disp = 60;
				} else if (NoticeValue == 13) {
					iconVolID = R.drawable.icon__9;
					Volume_disp = 65;
				} else if (NoticeValue == 14) {
					iconVolID = R.drawable.icon_10;
					Volume_disp = 70;
				} else if (NoticeValue == 15) {
					iconVolID = R.drawable.icon_11;
					Volume_disp = 75;
				} else if (NoticeValue == 16) {
					iconVolID = R.drawable.icon_12;
					Volume_disp = 80;
				} else if (NoticeValue == 17) {
					iconVolID = R.drawable.icon_13;
					Volume_disp = 85;
				} else if (NoticeValue == 18) {
					iconVolID = R.drawable.icon_14;
					Volume_disp = 90;
				} else if (NoticeValue == 19) {
					iconVolID = R.drawable.icon_14;
					Volume_disp = 95;
				} else if (NoticeValue == 20) {
					iconVolID = R.drawable.icon_15;
					Volume_disp = 100;
				}

				mNoticeImage.setBackgroundResource(iconVolID);
				jLanguage = JNILib.getGlobalLanguage();
				str_notice = VideoString.Volume[jLanguage] + Volume_disp;
				mNoticeText.setText(str_notice);
				NoticePicPlayTime = NOTICE_ICON_PLAY_TIME;
				mMainNoticeLayout.setVisibility(View.VISIBLE);
				NoticeState_bMute = 0;
			}
			break;
		case JLINK_NOTICE_TYPE_RECORD_START:
			mNoticeImage.setVisibility(View.VISIBLE);
			recordText.setVisibility(View.VISIBLE);
			break;
		case JLINK_NOTICE_TYPE_RECORD_STOP:
			mNoticeImage.setVisibility(View.VISIBLE);
			recordText.setVisibility(View.INVISIBLE);
			break;

		default:

			break;
		}

	}

	public void setVolumeMode(int mode) {
		AudioManager am = (AudioManager) this
				.getSystemService(this.AUDIO_SERVICE);
		if (mode == 0 || mode == 1) // ++ --
		{
			int audioMaxVolumn = am
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int audioCurrentVolumn = am
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			// Log.v(TAG,"audioMaxVolumn = " + audioMaxVolumn);
			if (mode == 1)// +
			{
				if (audioCurrentVolumn < audioMaxVolumn) {
					audioCurrentVolumn += 1;
				}
			} else {
				if (audioCurrentVolumn > 0) {
					audioCurrentVolumn -= 1;
				}
			}
			// Log.v(TAG,"audioCurrentVolumn = " + audioCurrentVolumn);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, audioCurrentVolumn, 0);
		} else {// mute
				// am.setStreamMute(AudioManager.STREAM_MUSIC, 0);
		}
	}

	public void setVolume(int volume) {
		AudioManager am = (AudioManager) this
				.getSystemService(this.AUDIO_SERVICE);
		int audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		if ((volume != audioCurrentVolumn) && (volume <= audioMaxVolumn)) {
			am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
		}
	}

	public int getVolume() {
		if (getMute()) {
			setMute(false);
			AudioManager am = (AudioManager) this
					.getSystemService(this.AUDIO_SERVICE);
			int audioMaxVolumn = am
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int audioCurrentVolumn = am
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			setMute(true);
			return audioCurrentVolumn * 20 / audioMaxVolumn;
		} else {
			AudioManager am = (AudioManager) this
					.getSystemService(this.AUDIO_SERVICE);
			int audioMaxVolumn = am
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int audioCurrentVolumn = am
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			return audioCurrentVolumn * 20 / audioMaxVolumn;
		}
	}

	public void setMute(boolean bMute) {
		AudioManager am = (AudioManager) this
				.getSystemService(this.AUDIO_SERVICE);

		am.setStreamMute(AudioManager.STREAM_MUSIC, bMute);
	}

	public boolean getMute() {
		/*
		 * AudioManager am =
		 * (AudioManager)this.getSystemService(this.AUDIO_SERVICE);
		 * 
		 * return am.isStreamMute(AudioManager.STREAM_MUSIC);
		 */
		return false;
	}

	public int getVoiceMode() {
		return mVoiceMode; // videoView.getVoiceMode();
	}

	public void startRecoverVulume() {
		if (vlume_now == vlume_set) {
			vlume_set = getVolume();
		}
		vlume_now = 1;
		setVolume(1);
		setMute(false);
		Message message1 = new Message();
		message1.what = JNILib.JLINK_TIMER;
		handler.sendMessageDelayed(message1, 100);
		Log.v(TAG,
				"====JSL====, startRecoverVulume sendMessageDelayed JLINK_TIMER vlume_set:"
						+ vlume_set);
	}

	public void refreshSoundStatus() {
		Log.v(TAG, "====JSL====, refreshSoundStatus check volume and mute");
		int re_vlume = getVolume();
		isMute = getMute();
		JNILib.NoticeVolume(re_vlume);
		JNILib.NoticeMute(isMute ? 1 : 0);

		/*
		 * if (null != presentation) { if (isMute || (0 == re_vlume)) {
		 * Log.v(TAG,"====JSL====, now show mute icon"); //
		 * presentation.setNoticeImage(presentation.JLINK_NOTICE_TYPE_MUTE, 0);
		 * } else { Log.v(TAG,"====JSL====, show volume icon, volume:"+
		 * re_vlume); //setNoticeImage(presentation.JLINK_NOTICE_TYPE_UNMUTE,
		 * 0); //setNoticeImage(presentation.JLINK_NOTICE_TYPE_VOLUME,
		 * re_vlume); } }
		 */

	}

	public void initSoundStatus() {
		setMute(false);
		if (getFirstRun()) {
			vlume_set = JNILib.getDefaultVolume();
			Log.v(TAG, "====JSL====, first run default volume is:" + vlume_set);
			if ((vlume_set > 100) || (vlume_set <= 5)) {
				vlume_set = getVolume();
			} else {
				vlume_set = vlume_set / 5;
			}
			isMute = false;
		} else {
			vlume_set = getVolume();
			isMute = getMute();
		}
		Log.v(TAG, "====JSL====, initSoundStatus now volume is:" + vlume_set);

		if (isMute) {
			setMute(true);
		} else {
			startRecoverVulume();
		}

		refreshSoundStatus();
	}

	public void setFirstRun() {
		if (isFileExist("/mnt/obb/firstrun.txt")) {
			Log.v(TAG, "====JSL====, not first run");
			isFirstRun = false;
		} else {
			// unmute();
			Log.v(TAG, "====JSL====, run first time and creat firstrun.txt");
			SocketClient socket = new SocketClient();
			socket.writeMess("system touch /mnt/obb/firstrun.txt && chmod 777 /dev/block/platform/hi_mci.1/by-name/deviceinfo");
			socket.readNetResponseSync();
			// String str =
			// "system chmod 777 /dev/block/platform/hi_mci.1/by-name/deviceinfo";
			isFirstRun = true;
		}
	}

	public boolean getFirstRun() {
		return isFirstRun;
	}

	/**
	 * 注册当网络发生变化时接收的广�?
	 */
	private void netRegisterReceiver() {
		netStatReceiver = new NetStatReceiver();
		registerReceiver(netStatReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	public void sataRegisterReceiver() {
		sataReceiver = new SataBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
		intentFilter.addAction("android.intent.action.MEDIA_EJECT");
		intentFilter.addDataScheme("file");
		registerReceiver(sataReceiver, intentFilter);
	}
	
	public void registerOutsideReceiver(){
		outSideReceiver = new FromOutsideInitReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("justlink.action.intent.data_inited_outside_singer");
		intentFilter.addAction("justlink.action.intent.data_inited_outside_song");
		//intentFilter.addDataScheme("file");
		registerReceiver(outSideReceiver, intentFilter);
	}

	private class NetStatReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				/*
				 * boolean noConnectivity = intent.getBooleanExtra(
				 * "noConnectiviy", false); if (noConnectivity) {
				 * topNetType.setImageResource(R.drawable.et_disconnected); }
				 * else {
				 * topNetType.setImageResource(R.drawable.et_connect_normal); }
				 */

				ConnectivityManager localConnectivityManager = (ConnectivityManager) context
						.getSystemService("connectivity");

				NetworkInfo localNetworkInfo = localConnectivityManager
						.getActiveNetworkInfo();
				if (localNetworkInfo != null) {
					WifiManager Wifi_manager = ((WifiManager) getSystemService(Context.WIFI_SERVICE));
					WifiInfo info = Wifi_manager.getConnectionInfo();

					/*
					 * Log.i("====JSL====",
					 * "WIFI net work ID:"+info.getNetworkId());
					 * Log.i("====JSL====",
					 * "WIFI net work ip:"+info.getIpAddress());
					 * Log.i("====JSL====",
					 * "WIFI net work speed:"+info.getLinkSpeed());
					 * Log.i("====JSL====",
					 * "WIFI net work rssi:"+info.getRssi());
					 * Log.i("====JSL====",
					 * "WIFI net work ssid:"+info.getSSID());
					 */

					if ((localNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
							&& (info.getNetworkId() >= 0)
							&& (info.getIpAddress() != 0)) {
						Log.i(TAG, "WIFI Availabel");
						JNILib.setCurrentNetstat(1);
					} else {
						Log.i(TAG, "ETH Availabel");
						JNILib.setCurrentNetstat(1);
					}
				} else {
					Log.i(TAG, "net disconnect");
					JNILib.setCurrentNetstat(0);
				}

			}
		}
	}

	/**
	 * 注册当音量发生变化时接收的广�?
	 */
	private void myRegisterReceiver() {
		mVolumeReceiver = new MyVolumeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.media.VOLUME_CHANGED_ACTION");
		registerReceiver(mVolumeReceiver, filter);
	}

	/**
	 * 处理音量变化时的界面显示
	 * 
	 * @author long
	 */
	private class MyVolumeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 如果音量发生变化则更改seekbar的位�?
			if (intent.getAction()
					.equals("android.media.VOLUME_CHANGED_ACTION")) {
				// if (getMute()){
				// setMute(false);
				// Log.v(TAG,"====JSL====BroadcastReceiver==="+"setMute(false)");
				// }
				mMainNoticeLayout.setVisibility(View.GONE);
				refreshSoundStatus();
			}
		}
	}

	/**
	 * MIC 热插拔监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyMICplugReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equalsIgnoreCase(
					"com.cmcc.intent.action.MICPHONE_PLUG_IN")) {
				initMicPhone();
				Log.i("song", "接收到MIC 插入广播=========");
			} else if (intent.getAction().equalsIgnoreCase(
					"com.cmcc.intent.action.MICPHONE_PLUG_OUT")) {
				Log.i("song", "接收到MIC 拔出广播=========");
				micphone.stop();
				micphone.release();
			}
		}

	}

	private void registMICReceiver() {
		mMICReceiver = new MyMICplugReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cmcc.intent.action.MICPHONE_PLUG_IN");
		filter.addAction("com.cmcc.intent.action.MICPHONE_PLUG_OUT");
		registerReceiver(mMICReceiver, filter);
		Log.i("ls", "注册MIC 插拔广播完成=========");
	}

	public void BarrageAdd(String obj) {
		try {
			barjObject = new JSONObject(obj);
			int mode = barjObject.getInt("mode");
			if (mode == 0) {
				String data = barjObject.getString("data");
				int size = barjObject.getInt("size");
				int speed = barjObject.getInt("speed");
				int colornub = barjObject.getInt("color");
				Log.i(TAG, "====JSL==== getUnsignedIntt() === " + colornub);
				// Log.i(TAG, "====JSL==== long1.toString() === "+str);
				// Log.i(TAG,
				// "====JSL==== long1.toString() === "+Integer.parseInt(str,16));
				String zhengze = "<jl[0-9]{2}>"; // 正则表达式，用来判断消息内是否有表情
				try {
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(this, data, zhengze);
					IDanmakuItem item = new DanmakuItem(this, spannableString,
							1280, 0, color[colornub], size, speed + 1);
					BarrageitemAdd(item);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} else if (mode == 1) {
				int data = barjObject.getInt("data");
				GifViewShow(data);
			}
		} catch (JSONException e) {
			Log.i(TAG, "====JSL==== BarrageAdd() ===JSONException ");
		}
	}

	public long getUnsignedIntt(int data) {
		return data & 0x0FFFFFFFFl;
	}

	/**
	 * Mic数据监听UDP
	 */
	private void startSing() {
		try {
			mUdpMessageTool = UdpMessageTool.getInstance();
		} catch (Exception e) {
			// TODO 自动生成�?catch �?
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// Log.v("rescult====","run");
					try {
						String rescult = mUdpMessageTool.receive(20001);
						if (rescult != null) {
							singerDate(rescult);
						}
					} catch (Exception e) {
						// socket.writeMess(str);
					}

				}

			}
		}).start();
	}

	/**
	 * 评分标准文件读取
	 */
	private void readLine(String FilePath) {
		// Log.v("line", "readLine===="+getAssets().toString());
		if (VideoViewFullScreen)
			mLineView.setVisibility(View.VISIBLE);
		// Log.v("line", FilePath);
		try {
			// File urlFile = new File("/sdcard/9000075.mzi");
			File urlFile = new File(FilePath);
			// Log.v("line", FilePath);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					urlFile), "UTF-8");
			// InputStreamReader isr = new InputStreamReader(
			// getAssets().open("9000075.mzi"), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			int line_num = 0;
			String mimeTypeLine = null;
			while ((mimeTypeLine = br.readLine()) != null) {
				str = mimeTypeLine;
				line_num++;
				// Log.v("line",line_num+"===="+str);
				if (line_num > 4 && str != null) {
					dataSet(str);
				}
			}
		} catch (Exception e) {
			Log.v("data", "error");
			e.printStackTrace();
		}
	}

	/**
	 * 评分数据处理:�?��时间、长度�?音调高低
	 */
	@SuppressLint("NewApi")
	private void dataSet(String str) {
		String s[] = str.split("\\ ");
		if (!s[2].isEmpty()) {
			int t = 0, l = 0, y = 0;
			t = Integer.parseInt(s[0]);
			l = Integer.parseInt(s[1]);
			// y = Integer.parseInt(s[2]);
			y = Integer.parseInt(s[2], 16);
			// Log.v("data", "t==="+t);
			// Log.v("data", "l==="+l);
			// Log.v("data", "y==="+y);

			mLineView.setLinePoint(t, l, y);

		}
	}

	/**
	 * Mic数据处理:�?��时间、持续时间�?两路高度、两路最终分数�?两路实时分数
	 * 例：|�?��时间|持续时间|key1|key2|分数1|分数2|实时�?|实时�?|
	 */
	private void singerDate(String str) {
		str = str.substring(1, str.length() - 1);
		// Log.v("rescult","="+str);
		String mic[] = str.split("[|]");
		if (mic != null && mic.length == 8) {
			/*
			 * Log.v("rescult","0="+mic[0]); Log.v("rescult","1="+mic[1]);
			 * Log.v("rescult","2="+mic[2]); Log.v("rescult","3="+mic[3]);
			 * Log.v("rescult","4="+mic[4]); Log.v("rescult","5="+mic[5]);
			 */
			int sT, sL, sK1, sK2, S1, S2, real1, real2, averageSK, averageS;
			sT = Integer.parseInt(mic[0]);
			sL = Integer.parseInt(mic[1]);
			sK1 = Integer.parseInt(mic[2]);
			sK2 = Integer.parseInt(mic[3]);
			S1 = Integer.parseInt(mic[4]);
			S2 = Integer.parseInt(mic[5]);
			averageSK = (sK1 + sK2) / 2;
			averageS = (S1 + S2) / 2;
			real1 = Integer.parseInt(mic[6]);
			real2 = Integer.parseInt(mic[7]);
			realtimeScore = (real1 + real2) / 2;
			// Log.i("line","分数 sk1=="+sK1+"sk2=="+sK2+"s1=="+S1+"s2=="+S2);
			mLineView.setSingPoint(sT, sL, averageSK, averageS);
			if (oldScore != realtimeScore && realtimeScore != 0) {
				Message message1 = new Message();
				message1.what = DISPLAY_REALTIME_SCORE;
				handler.sendMessage(message1);
			}
		}

	}

	public void showSearchSoftInput() {
		searchView.setVisibility(View.VISIBLE);
		isSearchViewShow = true;
		isYidianListShow = false;
		pipPageDown = false;
		pipPageUp = false;
		MyApplication.isSearchViewShow = true;
		searchView.refresh(true);
		Log.i("song", "refresh soft searchview =======");
		listView_songlist.setVisibility(View.GONE);
		empty_layout.setVisibility(View.GONE);
		pb.setVisibility(View.GONE);
	}

	public void hideSearchSoftInput() {
		searchView.setVisibility(View.GONE);
		isSearchViewShow = false;
		isYidianListShow = true;
		pipPageDown = false;
		pipPageUp = false;
		MyApplication.isSearchViewShow = false;
		listView_songlist.setVisibility(View.VISIBLE);
		// empty_layout.setVisibility(View.VISIBLE);
	}

	public void SetSearchEnterAndLayerType(int enterType, int layerType) {
		searchView.SetEnterAndLayerType(enterType, layerType);
	}

	public boolean runSystemCMD(String myfile) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su -c " + myfile);
			process.waitFor();
			Log.i("comread", "执行system命令======");
		} catch (Exception e) {
			Log.d("comread",
					"Unexpected error - Here is what I know: " + e.getMessage());
			return false;
		} finally {
			try {
				process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}
		return true;
	}

	public void pipFocusChange() {

		button_pip.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					fragmentHander
							.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_YIDIAN_BUTTON_FOCUS);
					fragmentHander
							.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_RESET_TOPBUTTON_FOCUS);
					MyApplication.isPipFocus = true;
					MyApplication.isGridviewFocus = false;
					MyApplication.isSingerListFocus = false;
					isSelectFocus = false;
					animationBkImage.setVisibility(View.VISIBLE);
					//MyApplication.isInHomeFragment = false;
				} else {
					// fragmentHander.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_BAN_YIDIAN_BUTTON_FOCUS);
					animationBkImage.setVisibility(View.GONE);
					MyApplication.isPipFocus = false;
					//MyApplication.isInHomeFragment = true;
				}

			}
		});

	}

	private class MyAnimationListener implements AnimationListener {
		private boolean hasFocus;

		public MyAnimationListener(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (!hasFocus) {
				animationBkImage.setVisibility(View.GONE);
				// animationText.setVisibility(View.GONE);
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (hasFocus) {
				animationBkImage.setVisibility(View.VISIBLE);
				// animationText.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}

	// 检测USB设备
	private void usbActionDetectViaTimer() {
		final Timer detectTimer = new Timer();
		TimerTask detectTimerTask = new TimerTask() {
			@SuppressLint("NewApi")
			public void run() {
				UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
				HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
				switch (deviceList.size()) {
				case 0:
					if (isUsbMicConnect) {
						micHandler.sendEmptyMessage(KARAOKE_USB_MIC_DISCONNECT);
						isUsbMicConnect = false;
					}
					break;
				case 1:
					if (isUsbMediaConnect) {
						micHandler.sendEmptyMessage(KARAOKE_USB_MIC_DISCONNECT);
					} else if (!isUsbMicConnect) {
						micHandler.sendEmptyMessage(KARAOKE_USB_MIC_CONNECT);
						isUsbMicConnect = true;
					}
					break;
				case 2:
					if (!isUsbMicConnect) {
						micHandler.sendEmptyMessage(KARAOKE_USB_MIC_CONNECT);
						isUsbMicConnect = true;
					}
					break;
				default:
					break;
				}
			}
		};

		detectTimer.scheduleAtFixedRate(detectTimerTask, 0, 3000);
	}

	private void jump2model() {
		int state = JNILib.GetBootStep();//数据整理状态
		if(state < 3)
		SystemClock.sleep(2000);
		Intent intent = getIntent();
		if (intent == null)
			return;
		int vodType = intent.getIntExtra("vodType", -1);
		Log.i("song", "receiver intent jump vodType========" + vodType);
		switch (vodType) {
		case 1:
			singerFragment = new SingerFragment(fragmentHander);
			replaceFragment(R.id.center_fragment, singerFragment);
			setHomePosition(1);
	
			break;
		case 2:
			SetSearchEnterAndLayerType(1, 0);
			songFragment = new SongNameFragment(fragmentHander, 1, 0);
			replaceFragment(R.id.center_fragment, songFragment);
			MyApplication.currentSinger = "/"
					+ getResources().getString(R.string.home_2);
			setHomePosition(2);
			
			break;
		case 3:
			replaceFragment(R.id.center_fragment, new Quz_Fragment(
					fragmentHander));
			setHomePosition(3);
			break;
		case 4:
			replaceFragment(R.id.center_fragment, new RecommendFragment(
					fragmentHander));
			setHomePosition(4);
			break;
		case 5:
			setHomePosition(5);
			replaceFragment(R.id.center_fragment, new RankingFragment(
					fragmentHander));
			break;
		case 6:
			SetSearchEnterAndLayerType(22, 0);
			replaceFragment(R.id.center_fragment, new SongNameFragment(
					fragmentHander, 22, 0));
			setHomePosition(6);
			MyApplication.currentSinger = "/"
					+ getResources().getString(R.string.home_6);
			break;
		case 7:
			setHomePosition(7);
			fragmentHander
					.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_DESTORY_LINEVIEW);
			replaceFragment(R.id.center_fragment, new Wuqu_Fragment(
					fragmentHander));
			break;
		case 8:
			setHomePosition(13);
			replaceFragment(R.id.center_fragment, new My_Fragment(
					fragmentHander));
			break;

		default:
			break;
		}

		String singerNum = intent.getStringExtra("singerNum");
		Log.i("song", "receiver intent jump singerNum========" + singerNum);
		if (!TextUtils.isEmpty(singerNum)) {
			isFromOutSideSinger = true;
			outSideSinger = singerNum;
			replaceFragment(R.id.center_fragment, new SongNameFragment(
					fragmentHander, 0, 0));
			// TODO 获取跳转歌星名
			// MyApplication.currentSinger = searchBean.getSinger();
		/*	Message msg = Message.obtain();
			msg.what = FragmentMessageConstant.FRAGMENT_MESSAGE_REFRESH_SINGER_DATA;
			msg.obj = singerNum;
			fragmentHander.sendMessage(msg);*/
		}

		final String songNum = intent.getStringExtra("songNum");
		Log.i("song", "receiver intent jump songNum========" + songNum);
		if (!TextUtils.isEmpty(songNum)) {
			// VideoViewFullScreen();
			isFromOutSideSong = true;
			outSideSong = songNum;
		/*	handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					SongJsonParseUtils.selectSong("song_select", songNum, 4, "");
					fragmentHander
							.sendEmptyMessage(FragmentMessageConstant.FRAGMENT_MESSAGE_IMMEDIATE_PLAY);
				}
			}, 1000);	*/	
		}
		
		int orderId = intent.getIntExtra("orderId", -1);
		Log.i("song", "receiver intent jump orderid========" + orderId);
		if(orderId != -1){
			isFromOutSideOrder = true;
			replaceFragment(R.id.center_fragment, new RecommendSongNameFragment(
					fragmentHander, orderId));
		}
	}

	@SuppressLint("NewApi")
	private void replaceFragment(int id, Fragment fragment) {
		MyApplication.isInHomeFragment = false;
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out);
		transaction.replace(id, fragment);
		 transaction.addToBackStack("home_fragment");
		transaction.commit();
		fManager.executePendingTransactions();
		// Log.i("song","replace fragment ok======");
	}
	
	private  String getLocalMacAddress() {  
		   String macSerial = null;
		       String str = "";
		       try {
		               Process pp = Runtime.getRuntime().exec("busybox ifconfig");
		               InputStreamReader ir = new InputStreamReader(pp.getInputStream());
		               LineNumberReader input = new LineNumberReader(ir);


		               for (; null != str;) {
		                       str = input.readLine();		                   
		                       if (str != null) {
		                               macSerial = str.substring(str.length()-19, str.length());
		                               break;
		                       }
		               }
		               Log.i("song","mac==="+macSerial);
		       } catch (IOException ex) {
		               // 赋予默认值
		               ex.printStackTrace();
		       }
		       return macSerial;
	
}
	
	
	
}
