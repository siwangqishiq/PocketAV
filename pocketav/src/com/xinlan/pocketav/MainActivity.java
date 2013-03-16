package com.xinlan.pocketav;

import java.io.IOException;
import java.util.List;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.pocketav.data.Constants;
import com.xinlan.pocketav.data.DatabaseService;
import com.xinlan.pocketav.data.Settings;
import com.xinlan.pocketav.factory.PocketavFactory;
import com.xinlan.pocketav.model.Actress;
import com.xinlan.pocketav.thread.LoadImageThread;
import com.xinlan.pocketav.utils.PicProcessUtils;
import com.xinlan.pocketav.utils.ReadFileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��ҳ��
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity {
	private DatabaseService dbService;
	private Settings settingsService;
	private int curActressIndex;// ��ǰ��ʾŮ������ֵ
	private List<Actress> actressList;

	private ImageView photoImg;// Ů����Ƭ�ؼ�
	private TextView infoText;// ��Ϣ�ؼ�
	private TextView descText;// �����ؼ�
	private Button preBtn;// ǰһλŮ�Ű�ť
	private Button nextBtn;// ��һλŮ��Button
	private Button searchBtn;// ������������
	private Handler imageHandler;
	private Context context;

	public static final String IMAGE_PATH = "images/";// ͼƬ·��
	public static final int DURATION = 500;

	PocketAVApp app;

	RelativeLayout adContainer;
	DomobAdView mAdview;

	public void addDombAD() {
		adContainer = (RelativeLayout) this.findViewById(R.id.mainAdContainer);
		mAdview = new DomobAdView(this, getResources().getString(
				R.string.appcode), DomobAdView.INLINE_SIZE_320X50);
		adContainer.addView(mAdview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		addDombAD();
	}

	protected void init() {
		getCompontent();
		injectService();
		addListener();
		initView();
	}

	/**
	 * ��ȡ�ؼ����
	 */
	protected void getCompontent() {
		context = this;
		app = (PocketAVApp) getApplication();
		photoImg = (ImageView) findViewById(R.id.photo);
		infoText = (TextView) findViewById(R.id.main_text_info);
		descText = (TextView) findViewById(R.id.main_text_desc);
		preBtn = (Button) findViewById(R.id.preButton);
		nextBtn = (Button) findViewById(R.id.nextButton);
		searchBtn = (Button) findViewById(R.id.searchButton);
	}

	/**
	 * ע�����
	 */
	protected void injectService() {
		dbService = PocketavFactory.getInstanceDBService(this);
		settingsService = PocketavFactory.getInstanceSettings(this);
		actressList = app.getActressList();
	}

	protected void addListener() {
		imageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bitmap bmp = (Bitmap) msg.obj;
				photoImg.setImageBitmap(bmp);
				TranslateAnimation moveToLeft = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, -1f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);// �����ƶ�����
				moveToLeft.setDuration(DURATION);
				moveToLeft.setFillAfter(true);
				photoImg.setAnimation(moveToLeft);
				photoImg.setVisibility(View.INVISIBLE);
				photoImg.setVisibility(View.VISIBLE);
			}
		};

		nextBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
				}
				return false;
			}// ��һλ
		});

		nextBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switchActress(actressList, 1);
			}
		});

		preBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switchActress(actressList, -1);
			}
		});

		photoImg.setOnClickListener(new OnClickListener() {// ���Ů��ͷ����Ƭ
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("picsArray", actressList.get(curActressIndex)
						.getPicsArray());
				intent.setClass(MainActivity.this, GalleryActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {// ����Ů����������
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, SearchActivity.class);
						MainActivity.this.startActivity(intent);
					}
				});
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.FILTER_SELECT_ACTION);
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				int curIndex = 0;
				if (intent.getExtras() != null) {
					curIndex = intent.getExtras()
							.getInt(Constants.SELECT_INDEX);
					curActressIndex = curIndex;
				}
				Actress actress = actressList.get(curActressIndex);
				showNextActress(actress);
			}
		}, filter);
	}

	/**
	 * ���ÿؼ���ͼ
	 */
	private void initView() {
		// curActressIndex = settingsService.getCurIndex();
		// curActressIndex=0;
		Intent intent = getIntent();
		int curIndex = 0;
		if (intent.getExtras() != null) {
			curIndex = intent.getExtras().getInt(Constants.SELECT_INDEX);
			curActressIndex = curIndex;
		}
		// Actress actress = dbService.selectActressById(curActressId);//
		// ��ѯ��Ů����ϸ��Ϣ
		if (actressList == null) {
			actressList = dbService.selectAllActressList();
		}
		Actress actress = actressList.get(curActressIndex);
		showNextActress(actress);
	}

	/**
	 * չʾ��һλActress
	 * 
	 * @param actress
	 * @returnd
	 */
	private int showNextActress(Actress actress) {
		TranslateAnimation moveToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);// �����ƶ�����
		moveToLeft.setDuration(DURATION);
		moveToLeft.setFillAfter(true);
		photoImg.setAnimation(moveToLeft);
		new LoadImageThread(imageHandler, this, IMAGE_PATH
				+ actress.getPicmin()).start();
		infoText.setText(actress.getInfos());// Ů�Ż�����Ϣ
		descText.setText(actress.getDescript());// �������
		return 0;
	}

	/**
	 * �л�Ů�� 1��һλ -1ǰһλ
	 * 
	 * @param list
	 * @param direct
	 */
	private void switchActress(List<Actress> list, int direct) {
		int cur = curActressIndex + direct;
		if (cur < 0 || cur >= list.size()) {
			Toast.makeText(context, "�Ѿ�û��Ů����Ŷ! ��ȴ����°汾", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		curActressIndex = cur;
		showNextActress(list.get(curActressIndex));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);// ��������
	}

	@Override
	public void finish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("��ȷ���˳���");
		builder.setPositiveButton("�˳�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				System.exit(0);
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
	}
}// end class
