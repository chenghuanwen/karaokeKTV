package com.jsl.ktv.tile;

public class Tile {
	private String title, subTitle, url;
	private String desc;
	private String type;
	private String activity;
	private String key;

	private String keytype;
	private String extra;
	private String bundle;
	private String bundlekey;
	private String bundlename;

	public String getBundlename() {
		return bundlename;
	}

	public void setBundlename(String bundlename) {
		this.bundlename = bundlename;
	}

	public String getBundlekey() {
		return bundlekey;
	}

	public void setBundlekey(String bundlekey) {
		this.bundlekey = bundlekey;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getKeytype() {
		return keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	// public Tile(String title) {
	// super();
	// }

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setImageUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return url;
	}

	public void setDesc(String value) {
		// TODO Auto-generated method stub
		this.desc = value;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getTarget() {
		return this.type;
	}

	public void setTarget(String target) {
		this.type = target;
	}

	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}
