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
import com.mirlab.metric.northbound.AsyncForwdTabCapacity1;

//20170308
//修改成多线程，之后不显示时间
public class FTCTimeThread extends Thread {

	public FTCTimeThread() {

	}

	public void run() {

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			 System.out.println((i+1)+" minutes "+"successful response！！！！！！！！！！！！！！！！！！！！！！！" +AsyncForwdTabCapacity2.jishu);
	

			File file1 = new File("C:\\a"); //* need to modify 
			if (file1.exists()) {
			} else {
				file1.mkdir();
			}
			File file2 = new File("C:\\a\\test.txt");

			if (file2.exists()) {
			} else {
				try {
					file2.createNewFile(); // v
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(file2, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = new String((i+1)+" minutes "+" Successful Response: \n" + AsyncForwdTabCapacity1.jishu + "\r\n");
			try {
				fileWriter.write(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 关闭数据流
			System.out.println("count！！！！！！！！！！！！！！！！！！！！！！！" + AsyncForwdTabCapacity1.jishu);
		}
	}
}
