package com.jsl.ktv.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.std.StdArraySerializers.IntArraySerializer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jsl.ktv.bean.RecommendOrRankBean;
import com.jsl.ktv.listener.RecommendOrRankLoadListener;
import com.jsl.ktv.listener.RecommendOrRankSongListLoadListener;
import com.jsl.ktv.view.MyApplication;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetDataParseUtil {
	private static NetDataParseUtil instance;
	private OkHttpClient httpClient;
	private int totalCount = 0;
	private HashMap<String,String> map = new HashMap<String, String>();
	
	private NetDataParseUtil(){
		httpClient = new OkHttpClient.Builder().build();
	};
	
	public static NetDataParseUtil getIntance(){
		if(instance == null){
			instance = new NetDataParseUtil();
		}
		return instance;
	}
	
	
	
	public void getRecommendList(int type,final RecommendOrRankLoadListener listener){
		final ArrayList<RecommendOrRankBean> list = new ArrayList<RecommendOrRankBean>();
	//	FormBody body = new FormBody.Builder().add("mac", MyApplication.MAC).add("type", type+"").build();
		map.clear();
		map.put("mac", MyApplication.MAC);
		map.put("type", String.valueOf(type));
		String json = "" ;
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
		
		Request request = new Request.Builder().post(body).url("http://api.jiashilian.com:8888/Api/songlist/getlist").build();
		httpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				String body = arg1.body().string();
				//Log.i("song","recommend data==mac=="+MyApplication.MAC+"==result=="+body);
				try {
					JSONObject object = new JSONObject(body);
					int result = object.getInt("result");
					//TODO 需对result结果码进行判断
					if(result==0){
						JSONArray  array = object.getJSONArray("songlist");
						for (int i = 0; i < array.length(); i++) {
							JSONObject o = array.getJSONObject(i);
							RecommendOrRankBean bean = new RecommendOrRankBean();
							bean.setId(o.getInt("songlistid"));
							bean.setTitel(o.getString("name"));
							bean.setUrl(o.getString("pic"));
							list.add(bean);
						}
						listener.onLoadFinish(list);	
					}else{
						listener.onLoadFinish(null);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					listener.onLoadFinish(null);
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				listener.onLoadFinish(null);
			}
		});
	}
	
	
	public void getRecommendOrRankSongList(int orderId,final RecommendOrRankSongListLoadListener listener){
		//FormBody body = new FormBody.Builder().add("songlistid", orderId+"").add("mac", MyApplication.MAC).build();
		map.clear();
		map.put("mac", MyApplication.MAC);
		map.put("songlistid", String.valueOf(orderId));
		String json = "" ;
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
		
		Request request = new Request.Builder().url(" http://api.jiashilian.com:8888/Api/songlist/getinfo").post(body).build();
		httpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				String resp = arg1.body().string();
			//	Log.i("song","recommend song list======"+resp);
				try {
					JSONObject object = new JSONObject(resp);
					int result = object.getInt("result");
					//TODO 需对result进行判断
					if(result==0){	
					int count = object.getInt("count");
					List<String> list = new ArrayList<String>();
					totalCount = count;
					JSONArray array = object.getJSONArray("songlist");
					for (int i = 0; i < array.length(); i++) {
						list.add(array.getJSONObject(i).getString("songid"));
					}
					listener.onLoadFinish(list);
					}else{
						listener.onLoadFinish(null);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					listener.onLoadFinish(null);
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) { 
				// TODO Auto-generated method stub
				listener.onLoadFinish(null);
				
			}
		});
	}
	
	public int getTotalCount(){
		return totalCount;
	}
}
