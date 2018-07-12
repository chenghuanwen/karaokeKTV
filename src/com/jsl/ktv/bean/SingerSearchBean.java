package com.jsl.ktv.bean;

public class SingerSearchBean {
	private String singer,singerNumber,path;
	public String getSingerNumber() {
		return singerNumber;
	}

	public void setSingerNumber(String singerNumber) {
		this.singerNumber = singerNumber;
	}

	private boolean isScore,hasAdd;
	

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
	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	

}
