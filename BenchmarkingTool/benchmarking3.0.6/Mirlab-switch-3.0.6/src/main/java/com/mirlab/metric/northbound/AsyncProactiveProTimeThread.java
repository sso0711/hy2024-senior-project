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
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Main;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mir.ui.N_Result;

//20170308
//修改成多线程，之后不显示时间
public class AsyncProactiveProTimeThread extends Thread {

	public int a = 0;
	public static long stopTime = 0;
	public static long totalTime = 0; ////

	public AsyncProactiveProTimeThread() {

	}

	public void run() {

		long stopnum = 0;
		ConnectingIOReactor ioReactor = null;
		try {
			ioReactor = new DefaultConnectingIOReactor();
		} catch (IOReactorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setMaxTotal(5000);
		cm.setDefaultMaxPerRoute(5000);

		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
		httpAsyncClient.start();
		final CountDownLatch latch = new CountDownLatch(1);
		final HttpGet request = new HttpGet("http://192.168.1.114:8181/onos/v1/flows");
		request.setHeader("Connection", "keep-alive");
		String encoding = null;
		try {
			encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		request.setHeader("Authorization", "Basic " + encoding);
		System.out.println(" caller thread id is : " + Thread.currentThread().getId());

		// final ArrayList<Integer> shuliang = new ArrayList<Integer>();
		n: for (int i = 0; i < 200; i++) {

			httpAsyncClient.execute(request, new FutureCallback<HttpResponse>() {

				public void completed(final HttpResponse response) {

					latch.countDown();
					// System.out.println(" callback thread id is : " +
					// Thread.currentThread().getId());
					// System.out.println(request.getRequestLine() + "->" +
					// response.getStatusLine());

					try {
						String content = EntityUtils.toString(response.getEntity(), "UTF-8");
						// System.out.println(" response content is : " +
						// content);
						JSONObject Topojson = new JSONObject(content);

						JSONArray Flows = Topojson.getJSONArray("flows  " + response.getStatusLine().getStatusCode());
						// System.out.println("flow number : " + Flows.length());

						if (Flows.length() == 36) {
							long stopTime = System.nanoTime();
							long ppt = stopTime - AsyncProactiveProTime.starttime;
							// System.out.println("ProactiveProvisioningTime: " +ppt);
							Main.gui.N_progressBar.setValue(Main.gui.N_progressBar.getMaximum());
							Main.gui.N_progressBarTotal.setValue(1);

							// a is what??>>>!>
							if (a == 0) {
								double time = ppt / 1000000;

								System.out.println("###### proactive provisionig test result :  " + time);

								totalTime += time;

								System.out.println("#############   real tiem avg check   :   "
										+ time / AsyncProactiveProTime.count);

								////
								for (int i = 0; i < 10; i++) {
									try {
										AsyncProactiveProTime.post();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								////
								// N_Result.ADD_RESULT(time + "", 7, 2);
								a = a + 1; // what is mean ?

							}
							Initializer.INITIAL_CHANNEL_POOL();
							// break n;

						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				public void failed(final Exception ex) {
					latch.countDown();
					System.out.println(request.getRequestLine() + "->" + ex);
					System.out.println(" callback thread id is : " + Thread.currentThread().getId());
				}

				public void cancelled() {
					latch.countDown();
					System.out.println(request.getRequestLine() + " cancelled");
					System.out.println(" callback thread id is : " + Thread.currentThread().getId());
				}

			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
