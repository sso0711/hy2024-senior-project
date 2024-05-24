package com.mirlab.metric.northbound;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ui.GUI;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mir.ui.N_Result;

//20170308
//修改成多线程，之后不显示时间
public class SyncForwTabCapaThread extends Thread {

	public SyncForwTabCapaThread() {

	}

	public void run() {

		int jishu = 0;
		int b = 0;
		String[] flows = new String[1000];

		n: for (int i = 0; i < 100000; i++) {
			String path = "http://192.168.0.89" + ":8181/onos/v1/flows";
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(path);
		
			String encoding = null;
			try {
				encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			httpget.setHeader("Authorization", "Basic " + encoding);
			HttpResponse response = null;
			try {
				response = httpclient.execute(httpget);
			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			StatusLine statusLine = response.getStatusLine();
			int responseCode = statusLine.getStatusCode();
			System.out.println(responseCode);

			if (responseCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream input = null;
				try {
					input = entity.getContent();
				} catch (IllegalStateException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				String str1 = null;
				try {
					str1 = br.readLine();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				String result = null;
				try {
					result = new String(str1.getBytes("gbk"), "utf-8");
				} catch (UnsupportedEncodingException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				JSONObject Topojson = new JSONObject(result);
				JSONArray Flows = Topojson.getJSONArray("flows");
				System.out.println("flows number:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + Flows.length());
			//	System.out.println("jinxunhuan:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~```" + i);
				// System.out.println("devices:" +
				// Topojson.get("devices").toString());
				// devices[i] = Topojson.get("devices").toString();
				//
				File file1 = new File("C:\\a");
				if (file1.exists()) {
				} else {
					file1.mkdir();
				}
				File file2 = new File("C:\\a\\test.txt");

				if (file2.exists()) {
				} else {
					try {
						file2.createNewFile(); // 文件的创建，注意与文件夹创建的区别
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					FileWriter fileWriter = new FileWriter(file2, true);
					String s = new String("jinxunhuancishu! \n" + i + "\r\n");
					fileWriter.write(s);
					fileWriter.close(); // 关闭数据流
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("failed!");
			}

			// if (i >= 2) {
			//
			// if (devices[i].equals(devices[i - 1]) &&
			// devices[i].equals(devices[i - 2])) {
			// jishu = jishu + 1;
			// switch (jishu) {
			// case 1:
			// try {
			// System.out.println("first check，device number: " +
			// devices[i]);
			// // fileWriter.write("first check，device number:
			// // "+devices[i]+ "\r\n");
			// Thread.sleep(10000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;
			// case 2:
			// try {
			// System.out.println("second check，device number: " +
			// devices[i]);
			// // fileWriter.write("second check，device number:
			// // "+devices[i]+ "\r\n");
			// Thread.sleep(20000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;
			// case 3:
			//
			// try {
			// System.out.println("third check，device number: " +
			// devices[i]);
			//
			// // fileWriter.write("third check，device number:
			// // "+devices[i]+ "\r\n");
			// Thread.sleep(40000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// break;
			// // fileWriter.write(devices[i]);
			// case 4:
			// System.out.println("its over");
			// // fileWriter.write(devices[i]+ "\r\n");
			// Main.N_progressBar.setValue(Main.N_progressBar.getMaximum());
			// Main.N_progressBarTotal.setValue(1);
			// N_Result.ADD_RESULT(devices[i] + "", 0, 5);
			// Initializer.INITIAL_CHANNEL_POOL();
			// b=i;
			// break n;
			// }
			// } else {
			// try {
			//
			// try {
			// Thread.sleep(10);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Thread.sleep(1000);
			// System.out.println("不相等后的一秒 ");
			// jishu = 0;
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			//
			// } else {
			// try {
			// Thread.sleep(1000);
			// System.out.println("最之前的一秒 ");
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			//
			// System.out.println("！！！！！！！！！！！！！！！！！ ");
			// }
			// System.out.println(b);
			// System.out.println("task over");
			//
			// }
		}
	}
}
