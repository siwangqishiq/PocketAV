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
 * 主页面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity {
	private DatabaseService dbService;
	private Settings settingsService;
	private int curActressIndex;// 当前显示女优索引值
	private List<Actress> actressList;

	private ImageView photoImg;// 女优照片控件
	private TextView infoText;// 信息控件
	private TextView descText;// 描述控件
	private Button preBtn;// 前一位女优按钮
	private Button nextBtn;// 后一位女优Button
	private Button searchBtn;// 进入搜索界面
	private Handler imageHandler;
	private Context context;

	public static final String IMAGE_PATH = "images/";// 图片路径
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
	 * 获取控件句柄
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
	 * 注入服务
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
						Animation.RELATIVE_TO_SELF, 0);// 向左移动动画
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
			}// 下一位
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

		photoImg.setOnClickListener(new OnClickListener() {// 点击女优头像照片
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("picsArray", actressList.get(curActressIndex)
						.getPicsArray());
				intent.setClass(MainActivity.this, GalleryActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {// 进入女优搜索界面
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
	 * 设置控件视图
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
		// 查询该女优详细信息
		if (actressList == null) {
			actressList = dbService.selectAllActressList();
		}
		Actress actress = actressList.get(curActressIndex);
		showNextActress(actress);
	}

	/**
	 * 展示下一位Actress
	 * 
	 * @param actress
	 * @returnd
	 */
	private int showNextActress(Actress actress) {
		TranslateAnimation moveToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);// 向左移动动画
		moveToLeft.setDuration(DURATION);
		moveToLeft.setFillAfter(true);
		photoImg.setAnimation(moveToLeft);
		new LoadImageThread(imageHandler, this, IMAGE_PATH
				+ actress.getPicmin()).start();
		infoText.setText(actress.getInfos());// 女优基本信息
		descText.setText(actress.getDescript());// 相关描述
		return 0;
	}

	/**
	 * 切换女优 1下一位 -1前一位
	 * 
	 * @param list
	 * @param direct
	 */
	private void switchActress(List<Actress> list, int direct) {
		int cur = curActressIndex + direct;
		if (cur < 0 || cur >= list.size()) {
			Toast.makeText(context, "已经没有女优了哦! 请等待更新版本", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		curActressIndex = cur;
		showNextActress(list.get(curActressIndex));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);// 结束程序
	}

	@Override
	public void finish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("您确定退出吗？");
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				System.exit(0);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
}// end class
