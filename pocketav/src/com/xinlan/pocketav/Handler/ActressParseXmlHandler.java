package com.xinlan.pocketav.Handler;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.xinlan.pocketav.model.Actress;

/**
 * 处理xml文件 读取Actress信息
 * 
 * @author Administrator
 * 
 */
public class ActressParseXmlHandler extends DefaultHandler {
	private List<Actress> actressList;
	private String tagName;
	private Actress actress;

	public ActressParseXmlHandler(List<Actress> actressList) {
		this.actressList = actressList;
		tagName = "";
		actress = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str = new String(ch, start, length);
		if (tagName.equals("name")) {
			actress.setName(str);
		} else if (tagName.equals("japanese_name")) {
			actress.setJapaneseName(str);
		} else if (tagName.equals("english_name")) {
			actress.setEnglishName(str);
		} else if (tagName.equals("birthday")) {
			actress.setBirthday(str);
		} else if (tagName.equals("blood")) {
			actress.setBlood(str);
		} else if (tagName.equals("height")) {
			actress.setHeight(str);
		} else if (tagName.equals("vital")) {
			actress.setVital(str);
		} else if (tagName.equals("hobby")) {
			actress.setHobby(str);
		} else if (tagName.equals("hometown")) {
			actress.setHometown(str);
		} else if (tagName.equals("picmin")) {
			actress.setPicmin(str);
		} else if (tagName.equals("pics")) {
			actress.setPics(str);
		} else if (tagName.equals("descript")) {
			actress.setDescript(str);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		tagName = "";
		if ("actress".equals(localName)) {
			actressList.add(actress);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		if ("actress".equals(localName)) {
			actress = new Actress();
		}
	}
	
}// end class
