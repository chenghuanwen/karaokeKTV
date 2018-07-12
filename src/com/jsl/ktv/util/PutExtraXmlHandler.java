package com.jsl.ktv.util;

/**
 * 解析xml的bundle字段的数据�?
 */

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jsl.ktv.tile.BundleTile;

public class PutExtraXmlHandler extends DefaultHandler {

	private final String ITEM = "item";
	private final String BUNDLEKEY = "bundlekey";
	private final String VALUETYPE = "valuetype";
	private final String VALUE = "value";
	private List<BundleTile> bundleList;
	private BundleTile bundle;

	public List<BundleTile> getBundleDataInfo() {
		return bundleList;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		bundleList.add(bundle);
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		bundleList = new ArrayList<BundleTile>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals(ITEM)) {
			bundle = new BundleTile();

			bundle.setBundleKey(attributes.getValue(BUNDLEKEY));
			bundle.setValueType(attributes.getValue(VALUETYPE));
			bundle.setValue(attributes.getValue(VALUE));
		}
	}
}
