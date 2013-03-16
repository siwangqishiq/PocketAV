package com.xinlan.pocketav.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {
	private static final String CUR_INDEX="cur_index";
	
	private Context mContext;
	private SharedPreferences sharePreferences;

	public Settings(Context context) {
		mContext = context;
		sharePreferences = mContext.getSharedPreferences(Constants.NAMESPACE,
				Context.MODE_PRIVATE);
	}

	/**
	 * 设置 索引
	 * @param index
	 */
	public void setCurIndex(int index) {
		Editor	edit=sharePreferences.edit();
		edit.putInt(CUR_INDEX, index);
		edit.commit();
	}
	
	/**
	 * 获取索引
	 * @return
	 */
	public int getCurIndex(){
		return sharePreferences.getInt(CUR_INDEX, 0);
	}
}// end class
