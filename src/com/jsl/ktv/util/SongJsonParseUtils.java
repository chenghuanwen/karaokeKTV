package com.jsl.ktv.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.jsl.ktv.bean.SongSearchBean;
import com.player.boxplayer.karaok.JNILib;
import com.jsl.ktv.karaok.VideoString;
import com.jsl.ktv.view.MyApplication;

public class SongJsonParseUtils {

	private static JSONObject cmdObject2;
	private static int totalCount,listConut;
	private static String searchKey="";

	/**
	 * int search_type;        搜索类型(参数和之前TV对接�?��)
            char search_key[20];    搜索字段(参数和之前TV对接�?��)
            int cloud_flag;         云端标签(暂时无用)
            int enter_type;         大分类ep: 歌名,歌星,分类,排行�?
            int layer_type;         子分�? ep:歌名下面的语别分�?   分类下面的子分类�?
            int list_start;         �?��获取数据的序列号
            int list_count;         获取数据数目
	 * @return
	 */
	public static ArrayList<SongSearchBean> getSongDatas2(int search_type,String search_key,int cloud_flag,
			int enter_type,int layer_type,int list_start,int list_count,String song_no ){

		try {
			cmdObject2 = new JSONObject();
			cmdObject2.put("search_type", search_type);
			cmdObject2.put("search_key", search_key);//searchType 搜索字段
			cmdObject2.put("cloud_flag", cloud_flag);
			cmdObject2.put("enter_type", enter_type);
			cmdObject2.put("layer_type", layer_type);
			cmdObject2.put("list_start", list_start);
			cmdObject2.put("list_count", list_count);
			cmdObject2.put("song_number", song_no);
			searchKey = search_key;
			String result = JNILib.getSongData(cmdObject2.toString());
			//Log.i("song","搜索命令==="+cmdObject2.toString());
			//Log.i("song","搜索结果222==="+result);
			if(TextUtils.isEmpty(result))
				return null;
			return parseJSONObect(result,false);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("song","JSONException==="+e.toString());
		}
		
		
		return null;
	}
	
	
	
	public static ArrayList<SongSearchBean> getSongDatas3(int search_type,String search_key,int cloud_flag,
			int enter_type,int layer_type,int list_start,int list_count,String song_no ){

		try {
			cmdObject2 = new JSONObject();
			cmdObject2.put("search_type", search_type);
			cmdObject2.put("search_key", search_key);//searchType 搜索字段
			cmdObject2.put("cloud_flag", cloud_flag);
			cmdObject2.put("enter_type", enter_type);
			cmdObject2.put("layer_type", layer_type);
			cmdObject2.put("list_start", list_start);
			cmdObject2.put("list_count", list_count);
			cmdObject2.put("song_number", song_no);
			searchKey = search_key;
			String result = JNILib.getSongData(cmdObject2.toString());
			//Log.i("song","搜索命令==="+cmdObject2.toString());
			//Log.i("song","搜索结果==333="+result);
			if(TextUtils.isEmpty(result))
				return null;
			return parseJSONObect(result,true);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("song","JSONException==="+e.toString());
		}
		
		
		return null;
	}
	
	
	
	public static ArrayList<SongSearchBean> getRecommendOrRankSongs(String ids){
		return parseJSONObect(JNILib.getSongListData(ids));
	}


	private static JSONObject resultObject;

	
	private static ArrayList<SongSearchBean> parseJSONObect(String result,boolean isAuto) {
		// TODO Auto-generated method stub
		ArrayList<SongSearchBean> list = new ArrayList<SongSearchBean>();
		try {
			resultObject = new JSONObject(result);	
			totalCount  = resultObject.getInt("total_count");//当前类别总数�?
			//Log.i("song","MyApplication.totalSearchCount=="+MyApplication.totalSearchCount);
			int startCount = resultObject.getInt("list_start");//本次起始序列�?
			int currentCount = resultObject.getInt("list_count");//当次返回数量
			int enterType = resultObject.getInt("enter_type");
			if(!isAuto)
			listConut = currentCount;
			if(currentCount>0){
			
			for (int i = 0; i <currentCount; i++) {
				JSONObject listObject = resultObject.getJSONObject("list"+i);
				SongSearchBean song = new SongSearchBean();
				song.setEnterType(enterType);
				if(listObject.has("select_flag"))
				song.setHasAdd(listObject.getInt("select_flag")==0?false:true);
				if(listObject.has("score_file"))
				song.setScore(listObject.getInt("score_file")==0?false:true);
				if(listObject.has("cloud_flag"))
				song.setCloud((listObject.getInt("cloud_flag")==0 || listObject.getInt("cloud_flag")==1002)?false:true);
				if(listObject.has("upload"))
				song.setUpload(listObject.getInt("upload")==0?false:true);
				if(listObject.has("language") && listObject.getInt("language")>0 && listObject.getInt("language")<18){
				song.setLanguage(VideoString.song_search_chinese[listObject.getInt("language")-1]);	
				}else{
				song.setLanguage(VideoString.song_search_chinese[0]);
				}
				
				if(listObject.has("singer2")){
					song.setSinger(listObject.getString("singer1")+"/"+listObject.getString("singer2"));
				}else{
					song.setSinger(listObject.getString("singer1"));	
				}
				
				if(listObject.has("down_stat"))
					song.setDownSta(listObject.getInt("down_stat"));
				
				song.setSong(listObject.getString("song_name"));
				song.setSongNumber(listObject.getString("song_number"));
				if(listObject.has("rec_id"))
				song.setOrderId(listObject.getString("rec_id"));
				if(listObject.has("order_id"))
				song.setOrderId(listObject.getInt("order_id")+"");
				if(listObject.has("rec_time"))
				song.setRecordTime(listObject.getInt("rec_time"));
				list.add(song);
			}
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("song","JSONException==="+e.toString());
		}
		
		return list;
		
	}
	
	
	
	
	private static ArrayList<SongSearchBean> parseJSONObect(String result) {
		// TODO Auto-generated method stub
		ArrayList<SongSearchBean> list = new ArrayList<SongSearchBean>();
		try {
			JSONArray arrays = new JSONArray(result);			
			for (int i = 0; i <arrays.length(); i++) {
				JSONObject listObject = arrays.getJSONObject(i);
				SongSearchBean song = new SongSearchBean();
				if(listObject.has("select_flag"))
				song.setHasAdd(listObject.getInt("select_flag")==0?false:true);
				if(listObject.has("score_file"))
				song.setScore(listObject.getInt("score_file")==0?false:true);
				if(listObject.has("cloud_flag"))
				song.setCloud((listObject.getInt("cloud_flag")==0 || listObject.getInt("cloud_flag")==1002)?false:true);
				if(listObject.has("upload"))
				song.setUpload(listObject.getInt("upload")==0?false:true);
				if(listObject.getInt("language")>0){
				song.setLanguage(VideoString.song_search_chinese[listObject.getInt("language")-1]);	
				}else{
				song.setLanguage(VideoString.song_search_chinese[0]);
				}
				
				if(listObject.has("singer2")){
					song.setSinger(listObject.getString("singer1")+"/"+listObject.getString("singer2"));
				}else{
					song.setSinger(listObject.getString("singer1"));	
				}
				if(listObject.has("down_stat"))
					song.setDownSta(listObject.getInt("down_stat"));
				song.setSong(listObject.getString("song_name"));
				song.setSongNumber(listObject.getString("song_number"));
				if(listObject.has("rec_id"))
				song.setOrderId(listObject.getString("rec_id"));
				if(listObject.has("order_id"))
				song.setOrderId(listObject.getInt("order_id")+"");
				if(listObject.has("rec_time"))
				song.setRecordTime(listObject.getInt("rec_time"));
				list.add(song);
			}			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("song","JSONException==="+e.toString());
		}
		
		return list;
		
	}
	
	
	/**
	
	 字串: song_number: 对应歌号
    字串: cmd_type: 对应标签如下

        1> 字串: song_select: 选歌(点歌和下载等均使用其)
            数字: mode: 3-设置公播, 2-收藏 1-优先, 0-普�?
            返回�?  1-成功, 0-失败, 设置公播/收藏 时�?[2-已满, 3-已存在]

            ep:优先点歌
            {
                "cmd_type": "song_select",
                "song_number": "1234567",
                "mode": 1,
            }

        2> 字串: song_order : 已点列表操作
            数字: order_id : 已点对应的ID(已点列表会有对应参数)
            数字: mode: 1-列表优先, 0-列表删除
            返回�?  1-成功, 0-失败

            ep:已点歌曲优先
            {
                "cmd_type": "song_order",
                "song_number": "1234567",
                "order_id": 666,
                "mode": 1,
            }

        3> 字串: cloud_order : 云下载列表操�?
            数字: mode: 2-暂停�?��, 1-列表优先, 0-列表删除
            返回�?  1-成功, 0-失败

        4> 字串: rec_order : 录音列表操作
            字串: rec_id : 录音列表ID(录音列表会有对应参数)
            数字: mode: 3-录音上传, 2-录音删除, 1-保存到U�? 0-录音回播
            返回�? 2-正在录音, 2-回播�? 1-回播成功, 0-失败

            ep:录音回播
            {
                "cmd_type": "rec_order",
                "song_number": "1234567",
                "rec_id": "A050A050",
                "mode": 0,
            }

        5> 字串: public_order: 公播删除
            返回�?  1-成功, 0-失败

            ep:公播删除
            {
                "cmd_type": "public_order",
                "song_number": "1234567",
            }
	
	*/
	private static JSONObject selectObject;
	public static int  selectSong(String cmdType,String number,int mode,String id) {
		try {
			selectObject = new JSONObject();
			selectObject.put("cmd_type", cmdType);
			selectObject.put("song_number", number);
			if(mode != -1)
			selectObject.put("mode", mode);
			if("song_order".equals(cmdType))
			selectObject.put("order_id", Integer.parseInt(id));
			if("rec_order".equals(cmdType))
			selectObject.put("rec_id", id);	
		//Log.i("song","点歌命令==="+selectObject.toString());
			return JNILib.SelectSong(selectObject.toString());
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public static int getTotal(){
		return totalCount;
	}
	
	public static int getListCount(){
		return listConut;
	}
	
	public static String getSearchKey(){
		return searchKey;
	}
	
}
