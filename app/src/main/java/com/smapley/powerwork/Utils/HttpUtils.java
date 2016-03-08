package com.smapley.powerwork.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

	private static final String TAG="HttpUtils";
	public static String getJsonContent(String url_path) {
		try {
			System.out.println("访问" + url_path);
			URL url = new URL(url_path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			int code = connection.getResponseCode();
			System.out.println(code);
			if (code == 200) {
				return changeInputStream(connection.getInputStream());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}

	public static String updata(HashMap<String, Object> map, String urlString) {
		// 作为StringBuffer初始化的字符串
		StringBuffer buffer = new StringBuffer();
		try {
			if (map != null && !map.isEmpty()) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					// 完成转码操作
					buffer.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue()
									.toString(), "utf-8")).append("&");
				}
				buffer.deleteCharAt(buffer.length() - 1);
			}
			Log.i(TAG, "----------------------->connection：" + urlString);
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);// 表示从服务器获取数据
			connection.setDoOutput(true);// 表示向服务器写数据
			Log.i(TAG, "--------->data:" + buffer.toString());
			// 获得上传信息的字节大小以及长度
			byte[] mydata = buffer.toString().getBytes();
			// 表示设置请求体的类型是文本类型
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					String.valueOf(mydata.length));
			// 获得输出流,向服务器输出数据
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();
			// 获得服务器响应的结果和状态码
			int responseCode = connection.getResponseCode();
			Log.i(TAG,"--------->responseCode:"+responseCode);
			if (responseCode == 200) {
				return changeInputStream(connection.getInputStream());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "";
	}



	private static String changeInputStream(InputStream inputStream) {
		// TODO Auto-generated method stub

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder StringBuilder = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				StringBuilder.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result=StringBuilder.toString();
		Log.i(TAG,"------------->result:"+result);
		return result;
	}

}
