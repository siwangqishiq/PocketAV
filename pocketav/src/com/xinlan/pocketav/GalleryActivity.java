package com.xinlan.pocketav;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.pocketav.adapter.GalleryAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.RelativeLayout;

public class GalleryActivity extends Activity {
	private Gallery gallery;
	RelativeLayout adContainer;
	DomobAdView mAdview;
	private GalleryAdapter galleryAdapter;
	
	public void addDombAD() {
		adContainer = (RelativeLayout) this.findViewById(R.id.galleryAdContainer);
		mAdview = new DomobAdView(this, getResources().getString(
				R.string.appcode), DomobAdView.INLINE_SIZE_320X50);
		adContainer.addView(mAdview);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		init();
		addDombAD();
	}
	
	private void init(){
		Intent intent=getIntent();
		String[] picsArray=intent.getStringArrayExtra("picsArray");
		gallery=(Gallery)findViewById(R.id.gallery);
		galleryAdapter=new GalleryAdapter(picsArray,this);
		gallery.setAdapter(galleryAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		galleryAdapter.releasePictureResource();//释放所有图片资源
		this.finish();
	}

}//end class
