package com.jsl.ktv.fragment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.jsl.ktv.R;
import com.jsl.ktv.upgrade.UpdateAppUtils;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CheckVersionFragment extends CommonFragment implements OnClickListener{

	private View myview;
	private TextView tvCheck,tvMac,tvVersion;
	private UpdateAppUtils uAppUtils;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.version_info_fragment, null);
		initView(myview);
		return myview;
	}
	
	
	@SuppressLint("NewApi")
	private void initView(View myview2) {
		// TODO Auto-generated method stub
		tvCheck = (TextView)myview2.findViewById(R.id.tv_check_version);
		tvMac = (TextView) myview2.findViewById(R.id.tv_mac);
		tvVersion = (TextView) myview2.findViewById(R.id.tv_version);
		tvMac.setText(getLocalMacAddress());
		tvVersion.setText(getAppLocalVersion());
		tvCheck.setOnClickListener(this);
		tvCheck.requestFocus();
		uAppUtils = new UpdateAppUtils(getActivity(), getActivity());
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		uAppUtils.checkNewVersion();
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
		       } catch (IOException ex) {
		               // 赋予默认值
		               ex.printStackTrace();
		       }
		       return "MAC: "+macSerial;
	
}
	
	@SuppressLint("NewApi")
	private String getAppLocalVersion(){
		String version = "";
		try {
			version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
}