package com.xinlan.pocketav.factory;

import android.content.Context;

import com.xinlan.pocketav.data.DatabaseService;
import com.xinlan.pocketav.data.Settings;

public class PocketavFactory {
	public static DatabaseService getInstanceDBService(Context context) {
		return new DatabaseService(context);
	}

	public static Settings getInstanceSettings(Context context) {
		return new Settings(context);
	}
}
