package com.xinlan.pocketav.thread;

import java.io.IOException;

import com.xinlan.pocketav.utils.PicProcessUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class LoadImageThread extends Thread {
	private Handler handler;
	private Context context;
	private String filename;

	public LoadImageThread(Handler handler, Context context, String filename) {
		this.handler = handler;
		this.context = context;
		this.filename = filename;
	}

	@Override
	public void run() {
		Bitmap bm=null;
		try {
			bm = BitmapFactory.decodeStream(context.getAssets().open(
					filename));
			bm=PicProcessUtils.getRoundedCornerBitmap(bm, 40f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Message msg = new Message();
		msg.obj=bm;
		handler.sendMessage(msg);
	}
}
