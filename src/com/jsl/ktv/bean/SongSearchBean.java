package com.jsl.ktv.bean;

public class SongSearchBean {
	private String song,singer,language,path,songNumber;
	public String getSongNumber() {
		return songNumber;
	}

	public void setSongNumber(String songNumber) {
		this.songNumber = songNumber;
	}

	public boolean isScore,hasAdd,isCloud;
	
	
	
	public boolean isCloud() {
		return isCloud;
	}

	public void setCloud(boolean isCloud) {
		this.isCloud = isCloud;
	}

	public boolean isScore() {
		return isScore;
	}

	public void setScore(boolean isScore) {
		this.isScore = isScore;
	}

	public boolean isHasAdd() {
		return hasAdd;
	}

	public void setHasAdd(boolean hasAdd) {
		this.hasAdd = hasAdd;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	private int enterType;
	public int getEnterType() {
		return enterType;
	}

	public void setEnterType(int enterType) {
		this.enterType = enterType;
	}
		private String orderId;	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	private int recordTime;
	
	public int getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(int recordTime) {
		this.recordTime = recordTime;
	}

	private boolean isUpload;
	public boolean isUpload() {
		return isUpload;
	}

	public void setUpload(boolean isUpload) {
		this.isUpload = isUpload;
	}
	
	
}
