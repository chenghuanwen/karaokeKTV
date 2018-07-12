package com.jsl.ktv.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.jsl.ktv.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.renderscript.FieldPacker;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UpdateAppUtils {
	private Context mContext;
	private Activity mActivity;
    private int localVersionCode = 0;
    private String localVersionName="";
    private OkHttpClient okHttpClient;
    private String serverVersionUrl = "";
    private String serverVersion = "";
	private Dialog checkVersionDialog;
	private Dialog downProgressDialog;
	private FileOutputStream out = null ;
	private InputStream in = null;
	private TextView tvDownProgress;
	private String appDownUrl = "http://internal.jiashilian.com/longs/JSLktv.apk";
	private String savePath = "/mnt/sdcard/jlink/apk";
	private StringBuffer sb;
   public UpdateAppUtils(Context context,Activity activity){
	   this.mContext = context;
	   this.mActivity = activity;
	   okHttpClient = new OkHttpClient.Builder().build();
	   getAPPLocalVersion(mContext);
	   sb = new StringBuffer();
   }
    //获取apk的版本号 currentVersionCode
    private  void getAPPLocalVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            localVersionName = info.versionName; // 版本名
            localVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
               
    }

    private String getAppServerVersion(){
    	/*Request request = new Request.Builder().url(serverVersionUrl).build();
    	okHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				String result = arg1.body().string();
				Log.i("song","server app version======"+result);
				try {
					JSONObject object = new JSONObject(result);
					if(result.contains("version")){
						serverVersion =  object.getString("version");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});*/
    	return serverVersion;
    }
       

    public void checkNewVersion(){
    	if(localVersionName.equals(getAppServerVersion())){
    		Toast.makeText(mContext, mContext.getResources().getString(R.string.newest_version), Toast.LENGTH_LONG).show();
    	}else{
    		showConfirmDialog();
    	}
    }
    
	
public void showConfirmDialog(){
	
	View view = mActivity.getLayoutInflater().inflate(R.layout.cover_child_path_dialog, null);
	TextView tvcancel = (TextView) view.findViewById(R.id.tv_cancel);
	TextView tvsure = (TextView) view.findViewById(R.id.tv_sure);
	TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);
	tvTip.setText(mContext.getResources().getString(R.string.upgrade_tip));
	tvcancel.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			checkVersionDialog.dismiss();
		}
	});
	tvsure.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			checkVersionDialog.dismiss();
			showDownloadProgressDialog();
			downloadApp();
			
		}

	
	});
	
	if(checkVersionDialog==null){
		checkVersionDialog = new Dialog(mContext, R.style.MyDialog);
		checkVersionDialog.setContentView(view);
	}
	checkVersionDialog.getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);
	checkVersionDialog.show();
	
}
	
	private void showDownloadProgressDialog() {
	// TODO Auto-generated method stub
		View view = mActivity.getLayoutInflater().inflate(R.layout.download_app_progress, null);
		tvDownProgress = (TextView) view.findViewById(R.id.tv_download_progress);
		ImageView ivAnimation = (ImageView)view.findViewById(R.id.iv_animation);
		AnimationDrawable animation = (AnimationDrawable) ivAnimation.getBackground();
		Button cancel = (Button)view.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				downProgressDialog.dismiss();
			}
		});
		if(downProgressDialog == null){
			downProgressDialog = new Dialog(mContext);
			downProgressDialog.setContentView(view);
		}
		downProgressDialog.setCanceledOnTouchOutside(false);
		downProgressDialog.show();
		animation.start();
	}

	private void downloadApp(){
	
			try {
				File dir = new File(savePath);
				if(!dir.exists())
					dir.mkdir();
				File apkFile = new File(dir, "JSLktv.apk");
				if(apkFile.exists())
					apkFile.delete();
					apkFile.createNewFile();				
				out = new FileOutputStream(apkFile);
				
				Request request = new Request.Builder().url(appDownUrl).build();
				okHttpClient.newCall(request).enqueue(new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response arg1) throws IOException {
						// TODO Auto-generated method stub
						in = arg1.body().byteStream();
						long total = arg1.body().contentLength();
						Log.i("song","apk 总长度==="+total);
						Log.i("song", "download stream =="+in);
						byte[] buffer = new byte[1024];
						long progress = 0;
						int len = -1;
						while((len=in.read(buffer)) >= 0){
							out.write(buffer, 0, len);
							progress += len;
							Log.i("song","has download len==="+progress);
							refreshProgress((int) (progress*100/total));
						}
						out.flush();
						if(out != null)
							out.close();
						if(in != null)
							in.close();
						refreshProgress(100);
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						// TODO Auto-generated method stub
						refreshProgress(-1);
					}
				});
								
			} catch (IOException e) {
				// TODO Auto-generated catch block
				refreshProgress(-1);
				e.printStackTrace();
			} /*finally {
                try {
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                }
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                }
            }*/
	}

	
	public void refreshProgress(final int progress){
		sb.setLength(0);
		 sb.append(mContext.getResources().getString(R.string.on_loading))
         .append(progress)
         .append("%");
		 Log.i("song","下载进度==="+progress);
		 mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 tvDownProgress.setText(sb.toString());
		         if(progress == 100){
		        	 tvDownProgress.setText(mContext.getResources().getString(R.string.down_sucess));
		        	 installAPK();
		         }else if(progress == -1){
		        	 tvDownProgress.setText(mContext.getResources().getString(R.string.down_fail));
		         }
			}
		});
		 
        
	}
	
	
	private void installAPK() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= 24) { //aandroid N的权限问题
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mActivity, "com.jsl.ktv.fileprovider", new File(savePath, "JSLktv.apk"));//注意修改
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(savePath, "JSLktv.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mActivity.startActivity(intent);
	}
    
}
