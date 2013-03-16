package com.xinlan.pocketav.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 读文件操作
 * @author Panyi
 *
 */
public class ReadFileUtils {
	/**
	 * 从ASSERT文件中读取文本文件
	 * @param filename
	 * @param context
	 * @return
	 */
	public static String readAssetTextFile(String filename, Context context) {
		AssetManager assetManager = context.getAssets();
		try {
			return readTextFromInputStream(assetManager.open(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String readTextFromInputStream(InputStream inputStream) {
		StringBuffer sb = new StringBuffer();
		BufferedReader buffer = null;
		String line = null;
		try {
			buffer = new BufferedReader(new InputStreamReader(inputStream,
					"utf-8"));
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line);
			}//end while
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}// end class
