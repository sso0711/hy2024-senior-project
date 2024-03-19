package com.mirlab.metric.northbound;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mir.ui.GUI;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mir.ui.N_Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年4月14日 下午12:02:33 类说明
 */
public class syncClientHttpFTC2 {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	static int jishu = 0;

	public void go() throws ClientProtocolException, IOException {
		version0();
	}

	private void version0() throws ClientProtocolException, IOException {

		SyncForwTabCapaThread FTC = new SyncForwTabCapaThread();
		FTC.start();
	
		for (int i = 0; i < 35000; i++) {

			// Topojson.get("devices").toString());
			// devices[i] = Topojson.get("devices").toString();

			// File file1 = new File("C:\\Users\\洋\\Desktop\\a");
			// if (file1.exists()) {
			// } else {
			// file1.mkdir();
			// }
			// File file2 = new File("C:\\Users\\洋\\Desktop\\a\\test.txt");
			//
			// if (file2.exists()) {
			// } else {
			// try {
			// file2.createNewFile(); // 文件的创建，注意与文件夹创建的区别
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// try {
			// FileWriter fileWriter = new FileWriter(file2, true);
			// String s = new String("Topology Information! \n" + Topojson +
			// "\r\n");
			// fileWriter.write(s);
			// fileWriter.close(); // 关闭数据流
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// br.close();
			// input.close();
			// } else {
			// System.out.println("failed!");
			// }

			//
			// System.out.println("its over");
			// // fileWriter.write(devices[i]+ "\r\n");
			// Main.N_progressBar.setValue(Main.N_progressBar.getMaximum());
			// Main.N_progressBarTotal.setValue(1);
			// N_Result.ADD_RESULT(devices[i] + "", 0, 5);
			// Initializer.INITIAL_CHANNEL_POOL();

			JSONObject jsobjpop = new JSONObject();
			jsobjpop.put("appId", "org.onosproject.testfwd");
			jsobjpop.put("groupId", 0);
			jsobjpop.put("priority", i);
			jsobjpop.put("timeout", 10000);
			jsobjpop.put("deviceId", "of:0000000000000002");// 需要变
			jsobjpop.put("state", "ADDED");
			jsobjpop.put("life", 9069);
			jsobjpop.put("isPermanent", "false");
			jsobjpop.put("packets", 18129);
			jsobjpop.put("bytes", 1776642);
			JSONArray popinstructions = new JSONArray();

			JSONObject popinstructionsObj = new JSONObject();
			popinstructionsObj.put("type", "OUTPUT");
			popinstructionsObj.put("port", 2); // 需要变
			popinstructions.put(0, popinstructionsObj);
			JSONArray popdeferred = new JSONArray();
			JSONObject poptreatment = new JSONObject();
			poptreatment.put("instructions", popinstructions);
			poptreatment.put("deferred", popdeferred);
			jsobjpop.put("treatment", poptreatment);

			JSONArray popcriteria = new JSONArray();

			JSONObject popcriteria1 = new JSONObject();
			popcriteria1.put("type", "ETH_DST");
			popcriteria1.put("mac", "4E:1F:61:B1:2E:25");
			popcriteria.put(popcriteria1);
			//
			JSONObject popcriteria2 = new JSONObject();
			popcriteria2.put("type", "ETH_SRC");
			popcriteria2.put("mac", "52:91:81:26:2A:01"); // 需要变
			popcriteria.put(popcriteria2);

			JSONObject popcriteria3 = new JSONObject();
			popcriteria3.put("type", "IN_PORT");
			popcriteria3.put("port", 3); // 需要变
			popcriteria.put(popcriteria3);

			JSONObject popselectorCont = new JSONObject();
			popselectorCont.put("criteria", popcriteria);
			jsobjpop.put("selector", popselectorCont);

			try {
				System.out.println("进了" + (i + 1) + "循环 没post");
				post(jsobjpop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println("task over"+jsobjpop);
		}
	}

	public static void post(JSONObject json) throws Exception {

		HttpClient client = new DefaultHttpClient();
		String URL = "http://192.168.0.89:8181/onos/v1/flows/of%3A0000000000000002";
		// why URL IP is diffierent related to FTC1 *** 

		HttpPost post = new HttpPost(URL);
		// System.out.println("@@@@@@@@@@" + URL);
		String encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
		post.setHeader("Authorization", "Basic " + encoding);
		post.addHeader("Content-Type", "application/json");

		try {

			StringEntity s = new StringEntity(json.toString(), "utf-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(s);
			System.out.println("post发送请求已发送");
			// 发送请求
			HttpResponse httpResponse3 = client.execute(post);
			StatusLine statusLine = httpResponse3.getStatusLine();
			int responseCode = statusLine.getStatusCode();
			System.out.println(responseCode);
			if (responseCode == 201) {

				jishu = jishu + 1;
				System.out.println("niubi chulaile" + jishu);
				// } else {
				// System.out.println("its
				// over!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
				//// Initializer.INITIAL_CHANNEL_POOL();
				//
				// }
			}
		} catch (Exception e) {
			System.out.println("请求异常");
			throw new RuntimeException(e);
		}

		// System.out.println("keyishezhilujinflew");

	}

}
