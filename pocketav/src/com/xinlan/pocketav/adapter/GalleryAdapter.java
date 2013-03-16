package com.xinlan.pocketav.adapter;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
	private String[] picsArray;
	private Context context;
	private int SCREEN_HEIGHT;
	private int SCREEN_WIDTH;
	private Bitmap[] bitmapArray;
	private static final int DEVIATION = 10;// 偏差值
	private static final String IMAGE_PATH = "images/";

	public GalleryAdapter(String[] picsArray, Context context) {
		this.picsArray = picsArray;
		this.context = context;

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
		SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();
		bitmapArray = new Bitmap[picsArray.length];
		try {
			for (int i = 0; i < picsArray.length; i++) {
				bitmapArray[i] = BitmapFactory.decodeStream(context.getAssets()
						.open(IMAGE_PATH + picsArray[i]));
			}// end for
		} catch (Exception e) {// 接受所有异常
			e.printStackTrace();
		}
	}

	public int getCount() {
		return picsArray.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		imageView.setImageBitmap(bitmapArray[position]);
		imageView.setLayoutParams(new Gallery.LayoutParams(SCREEN_WIDTH,
				SCREEN_HEIGHT - 7 * DEVIATION));// 显示图片高度与宽度
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);// set scale type
		return imageView;
	}
	
	public void releasePictureResource(){
		for(int i=0;i<bitmapArray.length;i++){
			//System.out.println("释放图片");
			bitmapArray[i].recycle();
			bitmapArray[i]=null;
		}
		bitmapArray=null;
	}
}
