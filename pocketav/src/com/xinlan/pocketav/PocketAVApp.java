package com.xinlan.pocketav;

import java.util.List;

import com.xinlan.pocketav.model.Actress;

import android.app.Application;

public class PocketAVApp extends Application {
	private List<Actress> actressList;

	public List<Actress> getActressList() {
		return actressList;
	}

	public void setActressList(List<Actress> actressList) {
		this.actressList = actressList;
	}
}
