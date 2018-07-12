package com.jsl.ktv.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jsl.ktv.tile.Tile;
import com.jsl.ktv.tile.TileGroup;

public class TileXmlHandler extends DefaultHandler {
	private static final String SEARCH = "search";
	private List<TileGroup> tgs;
	private TileGroup tg;

	private final String CATE = "cate";
	private final String TILE = "tile";
	private final String IMG = "image";
	private final String NAME = "name";
	private final String DESC = "desc";
	private final String TARGET = "target";
	private final String ACTIVITY = "activity";

	private final String KEY = "key";
	private final String KEYTYPE = "keytype";
	private final String EXTRA = "extra";
	private final String BUNDLENAME = "bundlename";
	private final String BUNDLEKEY = "bundlekey";

	private Tile search;

	public void setDir(String dir) {
	}

	public List<TileGroup> getTileGroups() {
		return this.tgs;
	}

	public Tile getSearchTile() {
		return search;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		tgs = new ArrayList<TileGroup>();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(CATE)) {
			tg = new TileGroup();
			tg.setTitle(attributes.getValue(NAME));
		} else if (localName.equalsIgnoreCase(TILE)) {
			Tile tile = new Tile();
			tile.setTitle(attributes.getValue(NAME));
			tile.setImageUrl(attributes.getValue(IMG));
			tile.setDesc(attributes.getValue(DESC));
			tile.setTarget(attributes.getValue(TARGET));
			tile.setActivity(attributes.getValue(ACTIVITY));

			tile.setKey(attributes.getValue(KEY));
			tile.setKeytype(attributes.getValue(KEYTYPE));
			tile.setExtra(attributes.getValue(EXTRA));
			tile.setBundlename(attributes.getValue(BUNDLENAME));
			tile.setBundlekey(attributes.getValue(BUNDLEKEY));
			tg.addTile(tile);
		} else if (localName.equalsIgnoreCase(SEARCH)) {
			if (search == null)
				search = new Tile();
			search.setTarget(attributes.getValue(TARGET));
			search.setActivity(attributes.getValue(ACTIVITY));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if (localName.equals(CATE)) {
			tgs.add(tg);
		}
	}

}
