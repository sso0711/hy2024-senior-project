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

import com.Main;
import com.mir.ui.GUI;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mir.ui.N_Result;
import com.mirlab.metric.northbound.AsyncForwdTabCapacity1;

//20170308
//修改成多线程，之后不显示时间
public class AsyncClientProactiveProRateTimeThread extends Thread {

	public AsyncClientProactiveProRateTimeThread() {

	}

	public void run() {
		int a[] = new int[5];
		// long iniTime = System.nanoTime();
		for (int i = 0; i < 5; i++) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			a[i] = AsyncClientProactiveProRate.jishu;
		}
		int b[] = new int[3];
		b[0] = a[2] - a[1];
		b[1] = a[3] - a[2];
		b[2] = a[4] - a[3];
		int rate = ((b[0] + b[1] + b[2]) / 3);
		Main.gui.N_progressBar.setValue(Main.gui.N_progressBar.getMaximum());
		Main.gui.N_progressBarTotal.setValue(1);
		System.out.println("");
		System.out.println("");
		System.out.println("------------------------------------------------------------------ㄱ");
		System.out.println("###### Number of flows added in 1 seconds  :  " + a[1]);
		System.out.println("###### Number of flows added from 0 to 1 second  :  " + b[0]);
		System.out.println("###### Number of flows added in 2 seconds  :  " + a[2]);
		System.out.println("###### Number of flows added from 1 to 2 second  :  " + b[1]);
		System.out.println("###### Number of flows added in 1 seconds  :  " + a[3]);
		System.out.println("###### Number of flows added from 2 to 3 second b[2]  :  " + b[2]);
		System.out.println("<<Mirlab - Benchmark>> : Experiment Result   :: " );
		System.out.println("<<MirLab Benchmark>> Proactive Path Provisioning rate  :  "+ rate);
		System.out.println("##### This result is a rate result for 3 seconds.");
		System.out.println("");
		System.out.println("");
		System.out.println("-------------------------------------------------------------------");
		N_Result.ADD_RESULT(rate + "", 8, 2);

		Initializer.INITIAL_CHANNEL_POOL();
	}
}
