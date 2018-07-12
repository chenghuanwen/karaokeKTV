package com.jsl.ktv.fragment;

import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.widget.Toast;
import com.jsl.ktv.karaok.MainActivity;

@SuppressLint("NewApi")
public class CommonFragment extends Fragment {
/*
	public void startAPK(String packageName, String activityName) {
		try {
			Intent intent = new Intent();
			ComponentName componentName = new ComponentName(packageName,
					activityName);
			intent.setComponent(componentName);
			startActivity(intent);
		} catch (Exception e) {
			showPromptDialog(packageName);
			e.printStackTrace();
		}
	}
*/
	public void startAPK(String packageName) {
		/*try 
		{
			Context c1 = MainActivity.getInstance();
			PackageManager pm_mc = c1.getPackageManager();
			Intent intent_mc = pm_mc.getLaunchIntentForPackage(packageName);
			startActivity(intent_mc); 
		} catch (Exception e) {
			//showPromptDialog(packageName);
			e.printStackTrace();
		}*/
	}
	/*public void showPromptDialog(final String packageName) {
        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getString(R.string.dialog_message));
		builder.setTitle(getString(R.string.dialog_title));
		builder.setPositiveButton(getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String url = getApURL(packageName);
						if (isEmptyString(url))
							Toast.makeText(getActivity(),
									getString(R.string.download_fail),
									Toast.LENGTH_SHORT).show();
						else {
							Intent intent = new Intent(getActivity(),
									DownloadActivity.class);
							intent.putExtra("url", url);
							startActivity(intent);
						}
					}
				});
		builder.setNegativeButton(getString(R.string.btn_cancel), null);
		builder.create().show();
	}*/

	public String getApURL(String packageName) {
		String url = null;
		Properties properties = new Properties();
		try {
			properties.load(getActivity().getAssets().open(
					"property.properties"));
			url = properties.getProperty(packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public boolean isEmptyString(String str) {
		return null == str || "".equals(str);
	}

}