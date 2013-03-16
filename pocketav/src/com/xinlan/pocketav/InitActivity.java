package com.xinlan.pocketav;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xinlan.pocketav.Handler.ActressParseXmlHandler;
import com.xinlan.pocketav.data.DatabaseService;
import com.xinlan.pocketav.factory.PocketavFactory;
import com.xinlan.pocketav.model.Actress;
import com.xinlan.pocketav.utils.ReadFileUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

/**
 * ��������,�����Ҫ������.
 * 
 * @author Administrator
 * 
 */
public class InitActivity extends Activity {
	private Handler handler;
	private List<Actress> actressList;
	private DatabaseService dbService;
	
	public static final long WAIT_TIME=1500;
	
	PocketAVApp app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		app=(PocketAVApp)getApplication();
		setContentView(R.layout.activity_init);
		initData();
	}

	private void initData() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent();
				intent.setClass(InitActivity.this, MainActivity.class);
				InitActivity.this.startActivity(intent);// �л�����ҳ��
				InitActivity.this.finish();
			}
		};

		// �����������ݵ��̣߳�������� ֪ͨ���߳�
		new Thread(new Runnable() {
			public void run() {
				loadData();
				handler.sendMessage(new Message());
			}
		}).start();
	}

	private void loadData() {// ��������
		long beforeTime=System.currentTimeMillis();//��������֮ǰ��ʱ��
		dbService = PocketavFactory.getInstanceDBService(this);
		if(dbService.selectActressCount()<=0){//������
			parseXmlData(ReadFileUtils.readAssetTextFile("index.xml",this));//��ȡԭʼ����
			dbService.loadListActressData(actressList); //��������
		}
		app.setActressList(dbService.selectAllActressList());
		long endTime=System.currentTimeMillis();//����ʱ��
		long distanceTime=endTime-beforeTime;
		if(distanceTime<WAIT_TIME){
			try {
				Thread.sleep(WAIT_TIME-distanceTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ����xml�ļ�
	 * @param fileContent
	 */
	private void parseXmlData(String fileContent){
		SAXParserFactory factory=SAXParserFactory.newInstance();
		try {
			XMLReader reader = factory.newSAXParser().getXMLReader();
			actressList = new ArrayList<Actress>();
			reader.setContentHandler(new ActressParseXmlHandler(actressList));
			reader.parse(new InputSource(new StringReader(fileContent)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}//end class
