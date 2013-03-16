package com.xinlan.pocketav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xinlan.pocketav.data.Constants;
import com.xinlan.pocketav.data.DatabaseService;
import com.xinlan.pocketav.factory.PocketavFactory;
import com.xinlan.pocketav.model.Actress;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * 女优快速搜索
 * 
 * @author Administrator
 * 
 */
public class SearchActivity extends ListActivity {
	private DatabaseService dbService;
	private List<Actress> actressList;
	private EditText searchText;
	PocketAVApp app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}

	protected void init() {
		dbService = PocketavFactory.getInstanceDBService(this);// DB服务
		app = (PocketAVApp) getApplication();
		searchText=(EditText)findViewById(R.id.searchText);
		searchText.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable edit) {
				String searchName=edit.toString().trim();
				actressList=dbService.selectActressByName(searchName);
				setShowList(actressList);
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});
		actressList = app.getActressList();
		if(actressList==null){
			actressList=dbService.selectAllActressList();
		}
		setShowList(actressList);
	}

	/**
	 * 设置当前显示列表
	 * 
	 * @param showList
	 */
	private void setShowList(List<Actress> showList) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < showList.size(); i++) {
			Actress actress = showList.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item_name", actress.getName());
			list.add(map);
		}// end for
		SimpleAdapter listAdapter = new SimpleAdapter(this, list,
				R.layout.list_item, new String[] { "item_name" },
				new int[] { R.id.item_name });
		listAdapter.notifyDataSetChanged();
		setListAdapter(listAdapter);
	}

	/**
	 * 选择列表中的女优
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this, ""+actressList.get(position).getName(), Toast.LENGTH_SHORT).show();
		Intent intent=new Intent();
		//intent.setClass(this, MainActivity.class);
		intent.putExtra(Constants.SELECT_INDEX, actressList.get(position).getId()-1);
		//this.startActivity(intent);
		intent.setAction(Constants.FILTER_SELECT_ACTION);
		sendBroadcast(intent);//发送广播消息
		this.finish();
	}
	
}// end class
