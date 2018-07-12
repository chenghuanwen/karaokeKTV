package com.jsl.ktv.karaok;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Parcel;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.jsl.ktv.util.HiMediaPlayerInvoke;
//import com.hisilicon.android.mediaplayer.HiMediaPlayerInvoke;
//import com.hisilicon.android.mediaplayer.HiMediaPlayer;
//import com.hisilicon.android.mediaplayer.HiMediaPlayerDefine;

public class HisiVideoView extends SurfaceView {

    private final String TAG = "jlink";
    private final String IMEDIA_PLAYER = "android.media.IMediaPlayer";

    private final boolean DEBUG = true;

    private Context mContext;

    public MediaPlayer mMediaPlayer = null;

    private SurfaceHolder mSurfaceHolder = null;

    private Surface mSubSurface;

    private Uri mUri;

    private boolean mIsPrepared = false;
    private boolean mStartWhenPrepared;
    private int mSeekWhenPrepared;
    private boolean mIsStarted = true;

    private int mDisplayType = -1;
    private int sndType = 0;
    private int mDuration;

    private OnPreparedListener mOnPreparedListener;

    private OnCompletionListener mOnCompletionListener;
    
    private int mVoideMode = JLINK_VOICE_MOD_YUANCHANG;    // 0:yuan chang  1:ban chang   
    
    public final static int JLINK_VOICE_MOD_YUANCHANG = 0;
    public final static int JLINK_VOICE_MOD_BANCHANG = 1;

	public static int g_Voice_Value = 0;
	
    public void setOnPreparedListener(OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setSndType(int type) {
        this.sndType = type;
    }

    public HisiVideoView(Context context) {
        this(context, null);
    }

    public HisiVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HisiVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initVideoView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// TODO Auto-generated method stub
    	 int width = getDefaultSize(0, widthMeasureSpec);
    	  int height = getDefaultSize(0, heightMeasureSpec);
    	  setMeasuredDimension(width, height);
    }
    
    
    private void initVideoView() {
        getHolder().addCallback(mSHCallback);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        getHolder().setFormat(PixelFormat.RGBA_8888);
    }

    /* 
     * Java文件操作 获取文件扩展�?
     *  
     */   
    private static String getExtensionName(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length() - 1))) {    
                return filename.substring(dot + 1);    
            }    
        }    
        return filename;    
    }  

    /* 
     * Java文件操作 获取不带扩展名的文件�?
     */   
    private static String getFileNameNoEx(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length()))) {    
                return filename.substring(0, dot);    
            }    
        }    
        return filename;    
    } 
    
    //判断文件夹是否存�?  存在 true,  不存�?false
	private boolean isFileExist(String strFileName) {
		if(TextUtils.isEmpty(strFileName))
			return false;
		File file = new File(strFileName);
		if (file.exists()) {
			return true;
		}
		return false;
	}
   
    public void setVideoPath(String path) {
        if (!isFileExist(path)) 
        {
           // Log.i(TAG, "====JSL====, video path not found: " + path);
            path = "/mnt/link2src/sys_file/media/default.mpg";
          //  Log.i(TAG, "====JSL====, play default: " + path);
        }
        String ext_name = getExtensionName(path);
        
        //Log.i(TAG, "VideoPath ext_name is: " + ext_name);
        
        if (ext_name.equals("jlv"))
        {
            //Log.i(TAG, ".jlv file use decret programe!");

            mUri = Uri.parse("jlvprotocol://" + path);
        }
        else
        {
            mUri = Uri.parse(path);
        }
        mStartWhenPrepared = false;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    private void setDisplayTypeInvoke(int displayType) {
        int ret = -1;
        if (mMediaPlayer != null) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(displayType);

            invoke(request, reply);
            reply.setDataPosition(0);
            ret = reply.readInt();

            if (ret != 0) {
                Log.e(TAG, "DisplayType Invoke call set failed , ret = " + ret);
            } else
                Log.i(TAG, "DisplayType Invoke Sucessfull !");

            request.recycle();
            reply.recycle();
			
			if(displayType==2){
            	mMediaPlayer.setVolume(0, 0);
            }else{
            	  AudioManager audioManager=(AudioManager)mContext.getSystemService(Service.AUDIO_SERVICE);
            	  mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            	  mMediaPlayer.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
            }
        }else{
			Log.i(TAG, "mMediaPlayer == null");
		}
    }

    private void setSoundTypeInvoke(int type) {
        int ret = -1;
        if (mMediaPlayer != null) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(type);

            invoke(request, reply);
            reply.setDataPosition(0);
            ret = reply.readInt();

            if (ret != 0) {
                Log.e(TAG, "setSoundTypeInvoke Invoke call set failed , ret = "
                        + ret);
            } else
                Log.i(TAG, "setSoundTypeInvoke Invoke Sucessfull !");

            request.recycle();
            reply.recycle();
        }
    }

    private void setSubtitleSurfaceInvoke(final Surface surface) {
        int ret = -1;
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        request.writeInterfaceToken(IMEDIA_PLAYER);
        request.writeInt(HiMediaPlayerInvoke.CMD_SET_SUB_SURFACE);
        int temp = request.dataPosition();

        if (surface != null) {
            surface.writeToParcel(request, 1);
        }
        request.setDataPosition(temp);
        if (DEBUG)
            Log.i(TAG, " parcel offset " + request.dataPosition() + " is "
                    + request.readString());
        if (DEBUG)
            Log.i(TAG, " parcel offset " + request.dataPosition() + " is "
                    + request.readStrongBinder());
        request.setDataPosition(temp);
        surface.readFromParcel(request);
        if (DEBUG)
            Log.i(TAG, " parcel offset " + request.dataPosition() + " is "
                    + surface);

        invoke(request, reply);
        request.setDataPosition(temp);
        surface.readFromParcel(request);
        if (DEBUG)
            Log.i(TAG, " parcel offset " + request.dataPosition() + " is "
                    + surface);
        reply.setDataPosition(0);
        ret = reply.readInt();
        if (ret != 0) {
            Log.e(TAG, "Subtitle Invoke call set failed , ret = " + ret);
        } else
            Log.i(TAG, "Subtitle Invoke Sucessfull !");

        request.recycle();
        reply.recycle();
    }

    public void setSubtitleSurface(Surface SubSurface) {
        mSubSurface = SubSurface;
    }

    public void setDisplayType(int mDisplayType) {
        this.mDisplayType = mDisplayType;
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void openVideo() {
        if ((mUri == null) || (mSurfaceHolder == null)) {
            if (DEBUG)
                Log.i(TAG, " openVideo return ! ");
            return;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            Log.i(TAG, "openVideo DataSource :" + mUri);
            try {
                Log.i(TAG, "openVideo mIsPrepared :" + mIsPrepared);
                mIsPrepared = false;
                mIsStarted = false;
                mDuration = -1;
                mMediaPlayer.setDataSource(mContext, mUri);
                //setSoundTypeInvoke(sndType);
                setDisplayTypeInvoke(mDisplayType);
                if (mSubSurface != null) {
                    //setSubtitleSurfaceInvoke(mSubSurface);
                } else {
                    Log.e(TAG,
                            "Error : Before call Subtitle Invoke , the Subtitle Surface is null!");
                }
                if (mMediaPlayer != null)
                    mMediaPlayer.prepareAsync();
            } catch (IOException ex) {
                Log.w(TAG, "Unable to open content: " + mUri, ex);
                return;
            } catch (IllegalArgumentException ex) {
                Log.w(TAG, "Unable to open content: " + mUri, ex);
                return;
            }
            return;
        }

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mIsPrepared = false;
            mIsStarted = false;
            mDuration = -1;
            Log.i(TAG, "DataSource :" + mUri);
            Log.i(TAG, "mIsPrepared :" + mIsPrepared);
            
            mMediaPlayer.setDataSource(mContext, mUri);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            Surface mSurface;
            //mSurface = mSurfaceHolder.getSurface();
            //setSoundTypeInvoke(sndType);
            setDisplayTypeInvoke(mDisplayType);
            // Subtitle Invoke Call
            if (mSubSurface != null) {
                //setSubtitleSurfaceInvoke(mSubSurface);
            } else {
                Log.e(TAG,
                        "Error : Before call Subtitle Invoke , the Subtitle Surface is null!");
            }
            
            if (mMediaPlayer != null)
                mMediaPlayer.prepareAsync();
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        }
    }

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            mIsPrepared = true;
            Log.i(TAG, "onPrepared enter,  mIsPrepared="+ mIsPrepared);
            if (null != mOnPreparedListener) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }

            if (mSeekWhenPrepared != 0)
            {
                mMediaPlayer.seekTo(mSeekWhenPrepared);
                mSeekWhenPrepared = 0;
            }

            if (mStartWhenPrepared) {
                Log.i(TAG, "start play in prapare,  mStartWhenPrepared="+ mStartWhenPrepared);
                mMediaPlayer.start();
                setVoiceMode(mVoideMode);
                mStartWhenPrepared = false;
                mIsStarted = true;
                Log.i(TAG, "start play in prapare over mIsStarted="+ mIsStarted);
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (null != mOnCompletionListener) {
                mOnCompletionListener.onCompletion(mMediaPlayer);
            }
        }
    };

    private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        public void surfaceChanged(SurfaceHolder holder, int format, int w,int h) {
        	Log.i("song","surfaceview onchange=========");
        	if(mMediaPlayer != null)
        	mMediaPlayer.setDisplay(holder);
        }

        public void surfaceCreated(SurfaceHolder holder) {
        	Log.i("song","surfaceview oncreate=========");
            mSurfaceHolder = holder;
            openVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        	Log.i("song","surfaceview ondestory=========");
            destroyPlayer();
        }
    };

    public void invoke(Parcel request, Parcel reply) {
        if ((mMediaPlayer != null)) {
        	
        	 Class<?> cls = mMediaPlayer.getClass();
           
			try {
				  Method method = cls.getDeclaredMethod("invoke", Parcel.class, Parcel.class);
				  method.setAccessible(true); //如果隐藏接口是public�? 这句可以不要
		          method.invoke(mMediaPlayer, request, reply); 
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
           

        	
           // mMediaPlayer.invoke(request, reply);
        }
    }

    public void start() {
        if ((mMediaPlayer != null) && mIsPrepared) {
            Log.i(TAG, "start play mIsPrepared="+ mIsPrepared);

            mMediaPlayer.start();
            setVoiceMode(mVoideMode);
            mStartWhenPrepared = false;
            mIsStarted = true;
            Log.i(TAG, "start play over mIsStarted="+ mIsStarted);
        } else {
            Log.i(TAG, "start play,not ready mIsPrepared="+ mIsPrepared);
            mStartWhenPrepared = true;
        }
    }

    public void destroyPlayer() {
        if (mSurfaceHolder != null) {
            mSurfaceHolder = null;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    
//======================================================================
    public int getAudioChannelPid()
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            Parcel _Request = Parcel.obtain();
            Parcel _Reply = Parcel.obtain();

            _Request.writeInterfaceToken(IMEDIA_PLAYER);
            _Request.writeInt(HiMediaPlayerInvoke.CMD_GET_AUDIO_CHANNEL_MODE);

            Class<?> cls = mMediaPlayer.getClass();            
			try {
				  Method method = cls.getDeclaredMethod("invoke", Parcel.class, Parcel.class);
				  method.setAccessible(true); //
		          method.invoke(mMediaPlayer, _Request, _Reply); 
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
          //  mMediaPlayer.invoke(_Request, _Reply);

            _Reply.setDataPosition(0);
            _Reply.readInt();
            int ChannelPid = _Reply.readInt();
            return ChannelPid;
        }

        return -1;
    }

    public int setAudioChannelPid(int pAudioChannelId)
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            return setAudioChannel(pAudioChannelId);
        }

        return -1;
    }

    public int setAudioChannel(int channel)
    {
        int flag = HiMediaPlayerInvoke.CMD_SET_AUDIO_CHANNEL_MODE;
        
        
        Parcel Request = Parcel.obtain();
        Parcel Reply = Parcel.obtain();

        Request.writeInterfaceToken(IMEDIA_PLAYER);
        Request.writeInt(flag);
        Request.writeInt(channel);

        invoke(Request, Reply);

        
        int Result = Reply.readInt();

        Request.recycle();
        Reply.recycle();

        return Result;
        //return excuteCommand(flag, channel, false);
    }
    
    public synchronized List <String> getAudioInfoList()
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            Parcel _Request = Parcel.obtain();
            Parcel _Reply = Parcel.obtain();

            _Request.writeInterfaceToken(IMEDIA_PLAYER);
            _Request.writeInt(HiMediaPlayerInvoke.CMD_GET_AUDIO_INFO);
            
            Class<?> cls = mMediaPlayer.getClass();            
			try {
				  Method method = cls.getDeclaredMethod("invoke", Parcel.class, Parcel.class);
				  method.setAccessible(true); //如果隐藏接口是public�? 这句可以不要
		          method.invoke(mMediaPlayer, _Request, _Reply); 
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

         //   mMediaPlayer.invoke(_Request, _Reply);

            List <String> _AudioInfoList = new ArrayList <String>();

            // for get
            _Reply.readInt();
            int _Num = _Reply.readInt();
            String _Language = "";
            String _Format = "";
            String _SampleRate = "";
            String _Channel = "";

            for (int i = 0; i < _Num; i++)
            {
                _Language = _Reply.readString();
                if (_Language == null || _Language.equals("und"))
                {
                    _Language = "";
                }

                _AudioInfoList.add(_Language);

                _Format = Integer.toString(_Reply.readInt());
                _AudioInfoList.add(_Format);

                _SampleRate = Integer.toString(_Reply.readInt());
                _AudioInfoList.add(_SampleRate);

                int _ChannelNum = _Reply.readInt();
                switch (_ChannelNum)
                {
                case 0:
                case 1:
                case 2:
                    _Channel = _ChannelNum + ".0";
                    break;
                default:
                    _Channel = (_ChannelNum - 1) + ".1";
                    break;
                }

                _AudioInfoList.add(_Channel);
            }

            _Request.recycle();
            _Reply.recycle();

            return _AudioInfoList;
        }

        return null;
    }
    public int setAudioTrackPid(int pAudioId)
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            return setAudioTrack(pAudioId);
        }

        return -1;
    }   
    public int setAudioTrack(int track)
    {
        int flag = HiMediaPlayerInvoke.CMD_SET_AUDIO_TRACK_PID;

        return excuteCommand(flag, track, false);
    }
    
    private int excuteCommand(int pCmdId, int pArg, boolean pIsGet)
    {
        Parcel Request = Parcel.obtain();
        Parcel Reply = Parcel.obtain();

        Request.writeInterfaceToken(IMEDIA_PLAYER);
        Request.writeInt(pCmdId);
        Request.writeInt(pArg);

        invoke(Request, Reply);

        if (pIsGet)
        {
            Reply.readInt();
        }

        int Result = Reply.readInt();

        Request.recycle();
        Reply.recycle();

        return Result;
    }

//======================================================================
    public void playVideo(String path)
    {
        //if(mMediaPlayer != null)
        {
            setVideoPath(path);
            start();
        }
    }

    public void RepeatVideo()
    {
        if ((mMediaPlayer != null) && mIsPrepared) {
            Log.i(TAG, "RepeatVideo ");

            mMediaPlayer.seekTo(0);
        }

    }
    
    public void setVideoLoop(boolean isloop)
    {
        if(mMediaPlayer != null)
        {
            mMediaPlayer.setLooping(isloop);
        }
    }
    
        
    public void pauseVideo()
    {
        if(mMediaPlayer != null)
        {
            if(mMediaPlayer.isPlaying() == true)
            {
                mMediaPlayer.pause();
            }
            else
            {
                mMediaPlayer.start();
            }
        }
    }

    public int getPauseStatus()
    {
        if(mMediaPlayer != null)
        {
            if(mMediaPlayer.isPlaying() == true)
            {
                return 1;   //play
            }
            else
            {
                return 0;    //pause
            }
        }

        return -1;
    }

    public boolean getStartStatus()
    {

        return mIsStarted;
    }
    
    public int getDuration()
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            if (mDuration > 0)
            {
                return mDuration/1000;
            }

            mDuration = mMediaPlayer.getDuration();
            return mDuration/1000;
        }

        mDuration = -1;
        return mDuration;
    }

    public int getCurrentPosition()
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            return mMediaPlayer.getCurrentPosition()/1000;
        }

        return 0;
    }

    public void seekTo(int second,boolean isRecord)
    {
        if ((mMediaPlayer != null) && mIsPrepared)
        {
			if(isRecord){
			mMediaPlayer.pause();
            mMediaPlayer.seekTo(second);
			mMediaPlayer.start();
             SystemClock.sleep(100);
             mMediaPlayer.seekTo(second);			 
			}else{
			 mMediaPlayer.seekTo(second);			
			}
			
			
		//	Log.i("CHW","SEEKTO OK");
        }
        else
        {
            mSeekWhenPrepared = second;
			//Log.i("CHW","SEEKTO NOT OK");
        }
    }
    
    public void setVoiceMode(int mode) { // 0:yuan chang  1:ban chang  2:立体声输�?    
        int mTrackCurrentIndex = 0;
        int AudioChannelMod = 0;
    //    String machinemodel = SystemProperties.get("ro.product.machinemodel");
     //   Log.i(TAG,"====JSL==== setVoiceMode machinemodel"+machinemodel);

        Log.i(TAG,"====JSL==== set voice mod(0:yuanchang 1:banchang 2:music): "+mode);
        if((mMediaPlayer != null) && (sndType == 0))
        {
            if (2 == mode)
            {
                //int channel = getAudioChannelPid();
                //Log.i(TAG,"getAudioChannelPid now ="+channel);
                int ret = setAudioChannelPid(0);
                AudioChannelMod = 1;
                Log.i(TAG,"setAudioChannelPid set to (2:banchang, 3:yuanchang 1:music): "+AudioChannelMod);
            }
            else
            {
                List audioinfoList = getAudioInfoList();
                //Log.i(TAG,"audio info = "+audioinfoList);
                int tracknum = 0;
                int tempsize = audioinfoList.size();
                if(tempsize > 0)
                {
                    tracknum = tempsize/4;
                }
                Log.i(TAG,"track num="+tracknum); //音轨�?
        	    //Mark		
        	  //  g_Voice_Value = JNILib.getCurrentPlayVoice(); //得到歌曲的原伴唱信息
        	    Log.i(TAG,"g_Voice_Value="+g_Voice_Value);  

    	        //Mark:根据传入的参�?设置参数的�?: 单音轨参�?AudioChannelMod 多音轨参�?mTrackCurrentIndex
                if(mode == JLINK_VOICE_MOD_YUANCHANG)
                {
                    if(g_Voice_Value == 2)
            		{//可能是单音轨，也可能是多音轨
            		   mTrackCurrentIndex = 1;   //多音�?         
            		   AudioChannelMod = 2;	//单音�?      
            		 }
                    else
            		{//可能是单音轨，也可能是多音轨
            		   mTrackCurrentIndex = 0; //多音�?         
            		   AudioChannelMod = 3; //单音�?
            		}	       
                }
    	        else  if(mode == JLINK_VOICE_MOD_BANCHANG)
                {
            		if(g_Voice_Value == 2)
            		{//可能是单音轨，也可能是多音轨
            		   mTrackCurrentIndex = 0;  //多音�?       
            		   AudioChannelMod = 3;//单音�? 
            		   }
                    else
            		{//可能是单音轨，也可能是多音轨
            		   mTrackCurrentIndex = 1;  //多音�?   
            		   AudioChannelMod = 2; //单音�?	
            		}
                }

    	        //根据上面设置的参数配置原伴唱
                if (tracknum == 1)
                {//只有�?��音轨，需要切换声道，AudioChannelMod的�?只能�?或�?3，哪个是原唱，要依据歌曲原伴唱信息g_Voice_Value决定
                    int channel = getAudioChannelPid();
                    Log.i(TAG,"getAudioChannelPid now ="+channel);
                    int ret = setAudioChannelPid(AudioChannelMod);
                    Log.i(TAG,"setAudioChannelPid set to (2:banchang, 3:yuanchang): "+AudioChannelMod);
                }
                else if (tracknum > 1)
                {//有两个音轨，采用切换音轨的方式，mTrackCurrentIndex只能�?或�?1，哪个是原唱，要依据歌曲自带的原伴唱信息g_Voice_Value决定
                    Log.i(TAG,"setAudioTrack to " + mTrackCurrentIndex);
                  /*  if ((machinemodel != null) && (machinemodel == "KV-500"))
                    {
                        if (0 == mTrackCurrentIndex)
                        {
                            setAudioTrackPid(1);
                        }
                        else
                        {
                            setAudioTrackPid(0);
                        }
                    }else {*/
                        int ret = setAudioTrackPid(mTrackCurrentIndex);
                        Log.i(TAG,"setAudioTrack ret="+ret);

                 //   }
                }
            }
        }
        
        mVoideMode = mode;  //没有设置到mediaplayer也要记在这里，启动播放时配置进去

    }

    public int getVoiceMode(){
        return mVoideMode;

    }

    public int setAudioTone(int tone)
    {
        int flag = HiMediaPlayerInvoke.CMD_SET_SOUND_TONE;

        return excuteCommand(flag, tone, false);
    }
	
    public int getAudioTone()
	{
        if ((mMediaPlayer != null) && mIsPrepared)
        {
            Parcel _Request = Parcel.obtain();
            Parcel _Reply = Parcel.obtain();

            _Request.writeInterfaceToken(IMEDIA_PLAYER);
            _Request.writeInt(HiMediaPlayerInvoke.CMD_GET_SOUND_TONE);
            
            Class<?> cls = mMediaPlayer.getClass();            
			try {
				  Method method = cls.getDeclaredMethod("invoke", Parcel.class, Parcel.class);
				  method.setAccessible(true); //如果隐藏接口是public�? 这句可以不要
		          method.invoke(mMediaPlayer, _Request, _Reply); 
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

        //    mMediaPlayer.invoke(_Request, _Reply);

            _Reply.setDataPosition(0);
            _Reply.readInt();
            int Tone = _Reply.readInt();
            return Tone;
        }

        return -1;
    }  

    public void setVideoFreezeMode(int FreezeMode) // mode=1 black  mode=0 last
    {
        int ret = -1;
        if (mMediaPlayer != null) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(HiMediaPlayerInvoke.CMD_SET_VIDEO_FREEZE_MODE);
            request.writeInt(FreezeMode);

            invoke(request, reply);
            reply.setDataPosition(0);
            ret = reply.readInt();

            if (ret != 0) {
                Log.e(TAG, "setVideoFreezeMode Invoke call set failed , ret = "
                        + ret);
            } else
                Log.i(TAG, "setVideoFreezeMode Invoke Sucessfull !");

            request.recycle();
            reply.recycle();
        }
    }

    public void setVideoWin(int top, int left, int width, int height)
    {
        int ret = -1;
        if (mMediaPlayer != null) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(HiMediaPlayerInvoke.CMD_SET_OUTRANGE);
            request.writeInt(top);
            request.writeInt(left);
            request.writeInt(width);
            request.writeInt(height);

            invoke(request, reply);
            reply.setDataPosition(0);
            ret = reply.readInt();

            if (ret != 0) {
                Log.e(TAG, "setVideoWin Invoke call set failed , ret = "
                        + ret);
            } else
                Log.i(TAG, "setVideoWin Invoke Sucessfull !");

            request.recycle();
            reply.recycle();
        }
    }

    public boolean isPlaying(){
    	if(mMediaPlayer.isPlaying())
    		return true;
    	else
    		return false;
    }


    
    
  
    
        
//======================================================================
}



