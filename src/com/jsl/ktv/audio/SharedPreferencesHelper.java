package com.jsl.ktv.audio;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper sharedPreferencesHelper = null;  
    private SharedPreferencesHelper(){}
    private static SharedPreferences sp;
    //单例模式，把Context传进�? 
    public static SharedPreferencesHelper getInstance(Context context) {  
        if (sharedPreferencesHelper == null) {  
            synchronized (SharedPreferencesHelper.class) {  
                if (sharedPreferencesHelper == null) {  
                    sharedPreferencesHelper = new SharedPreferencesHelper();  
                    sharedPreferencesHelper.setContext(context);  
                    sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);  
                    return sharedPreferencesHelper;  
                }  
            }  
        }  
  
        return sharedPreferencesHelper;  
    }  
  
    private Context context;  
  
    public void setContext(Context context) {  
        this.context = context;  
    }  
    
    //取整�? 
    public int getInt(String key, int defaultValue) {  
        try {  
		//Log.i("EQSetting","sp get sussefully===="+sp.getInt(key, defaultValue));
            return sp.getInt(key, defaultValue);  
        } catch (Exception e) {  
            Log.d("EQSetting", "" + e);  
            return defaultValue;  
  
        }  
    }  
  
    //存整�? 
    public void putInt(String key, int value) {  
        try {  
            SharedPreferences.Editor editor = sp.edit();  
            editor.putInt(key, value);  
            editor.commit();  
		//	Log.i("EQSetting","sp sussefully====");
        } catch (Exception e) {  
            Log.d("hcj", "" + e);  
        }  
    }  
    

   
}
