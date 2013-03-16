package com.xinlan.pocketav.data;

import java.util.ArrayList;
import java.util.List;

import com.xinlan.pocketav.model.Actress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseService {
	private DatabaseOpen databaseOpen;

	public DatabaseService(Context context) {
		databaseOpen = new DatabaseOpen(context, "db", null, 1);
	}

	public SQLiteDatabase getWritableDatabase() {
		return databaseOpen.getWritableDatabase();
	}

	public SQLiteDatabase getReadableDatabase() {
		return databaseOpen.getReadableDatabase();
	}

	/**
	 * 查询ACTRESS数量
	 * 
	 * @return
	 */
	public int selectActressCount() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("ACTRESS", new String[] { "ID" }, null, null,
				null, null, null);
		int ret = cursor.getCount();
		cursor.close();
		db.close();
		return ret;
	}

	/**
	 * 插入一条Actress数据
	 * 
	 * @param actress
	 * @return
	 */
	public long addNewActress(Actress actress) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ID", actress.getId());
		values.put("NAME", actress.getName());
		values.put("JAPANESE_NAME", actress.getJapaneseName());
		values.put("ENGLISH_NAME", actress.getEnglishName());
		values.put("BIRTHDAY", actress.getBirthday());
		values.put("HEIGHT", actress.getHeight());
		values.put("VITAL", actress.getVital());
		values.put("HOBBY", actress.getHobby());
		values.put("BLOOD", actress.getBlood());
		values.put("HOMETOWN", actress.getHometown());
		values.put("PICMIN", actress.getPicmin());
		values.put("PICS", actress.getPics());
		values.put("DESCRIPT", actress.getDescript());
		long ret = db.insert("ACTRESS", null, values);
		db.close();
		return ret;
	}

	/**
	 * 讲ListAcctress内容存入Database
	 * 
	 * @param actressList
	 */
	public void loadListActressData(List<Actress> actressList) {
		for (int i = 0; i < actressList.size(); i++) {
			addNewActress(actressList.get(i));
		}// end for
	}

	/**
	 * 根据ID选择Actress
	 * 
	 * @param actressId
	 * @return
	 */
	public Actress selectActressById(int actressId) {
		if (actressId <= 0) {
			actressId = 1;
		}
		SQLiteDatabase db = getReadableDatabase();
		Actress actress = null;
		Cursor cursor = db.query("ACTRESS", new String[] { "ID", "NAME",
				"JAPANESE_NAME", "ENGLISH_NAME", "BIRTHDAY", "BLOOD", "HEIGHT",
				"VITAL", "HOBBY", "HOMETOWN", "PICMIN", "PICS", "DESCRIPT" },
				"ID=?", new String[] { actressId + "" }, null, null, null, "1");
		while (cursor.moveToNext()) {
			actress = new Actress();
			actress.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			actress.setName(cursor.getString(cursor.getColumnIndex("NAME")));
			actress.setJapaneseName(cursor.getString(cursor
					.getColumnIndex("JAPANESE_NAME")));
			actress.setEnglishName(cursor.getString(cursor
					.getColumnIndex("ENGLISH_NAME")));
			actress.setBirthday(cursor.getString(cursor
					.getColumnIndex("BIRTHDAY")));
			actress.setBlood(cursor.getString(cursor.getColumnIndex("BLOOD")));
			actress.setHeight(cursor.getString(cursor.getColumnIndex("HEIGHT")));
			actress.setVital(cursor.getString(cursor.getColumnIndex("VITAL")));
			actress.setHobby(cursor.getString(cursor.getColumnIndex("HOBBY")));
			actress.setHometown(cursor.getString(cursor
					.getColumnIndex("HOMETOWN")));
			actress.setPicmin(cursor.getString(cursor.getColumnIndex("PICMIN")));
			actress.setPics(cursor.getString(cursor.getColumnIndex("PICS")));
			actress.setDescript(cursor.getString(cursor
					.getColumnIndex("DESCRIPT")));
			actress.setPicsArray(actress.getPics().split(","));// 设置pics列表
		}// end while
		cursor.close();
		db.close();
		if (actress == null) {
			return selectActressById(1);
		}
		return actress;
	}

	/**
	 * 选择所有女友列表返回
	 * 
	 * @return
	 */
	public List<Actress> selectAllActressList() {
		List<Actress> list = new ArrayList<Actress>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("ACTRESS", new String[] { "ID", "NAME",
				"JAPANESE_NAME", "ENGLISH_NAME", "BIRTHDAY", "BLOOD", "HEIGHT",
				"VITAL", "HOBBY", "HOMETOWN", "PICMIN", "PICS", "DESCRIPT" },
				null, null, null, null, null);
		while (cursor.moveToNext()) {
			Actress actress = new Actress();
			actress.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			actress.setName(cursor.getString(cursor.getColumnIndex("NAME")));
			actress.setJapaneseName(cursor.getString(cursor
					.getColumnIndex("JAPANESE_NAME")));
			actress.setEnglishName(cursor.getString(cursor
					.getColumnIndex("ENGLISH_NAME")));
			actress.setBirthday(cursor.getString(cursor
					.getColumnIndex("BIRTHDAY")));
			actress.setBlood(cursor.getString(cursor.getColumnIndex("BLOOD")));
			actress.setHeight(cursor.getString(cursor.getColumnIndex("HEIGHT")));
			actress.setVital(cursor.getString(cursor.getColumnIndex("VITAL")));
			actress.setHobby(cursor.getString(cursor.getColumnIndex("HOBBY")));
			actress.setHometown(cursor.getString(cursor
					.getColumnIndex("HOMETOWN")));
			actress.setPicmin(cursor.getString(cursor.getColumnIndex("PICMIN")));
			actress.setPics(cursor.getString(cursor.getColumnIndex("PICS")));
			actress.setDescript(cursor.getString(cursor
					.getColumnIndex("DESCRIPT")));
			actress.setPicsArray(actress.getPics().split(","));// 设置pics列表
			list.add(actress);
		}// end while
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 根据姓名进行模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public List<Actress> selectActressByName(String name) {
		List<Actress> list = new ArrayList<Actress>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("ACTRESS", new String[] { "ID", "NAME" },
				"NAME like ?", new String[] { "%" + name + "%" }, null, null,
				null);
		while (cursor.moveToNext()) {
			Actress actress = new Actress();
			actress.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			actress.setName(cursor.getString(cursor.getColumnIndex("NAME")));
			list.add(actress);
		}// end while
		cursor.close();
		db.close();
		return list;
	}
	
}// end class
