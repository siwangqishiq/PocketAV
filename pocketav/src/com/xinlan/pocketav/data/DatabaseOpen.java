package com.xinlan.pocketav.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpen extends SQLiteOpenHelper{

	public DatabaseOpen(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table ACTRESS (ID INTEGER primary key autoincrement," +
				"NAME varchar(50),JAPANESE_NAME varchar(50),ENGLISH_NAME varchar(50)," +
				"BIRTHDAY varchar(50),BLOOD varchar(10),HEIGHT varchar(20),VITAL varchar(40)," +
				"HOBBY varchar(50),HOMETOWN varchar(50),PICMIN varchar(30),PICS varchar(100)," +
				"DESCRIPT varchar(1000))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
