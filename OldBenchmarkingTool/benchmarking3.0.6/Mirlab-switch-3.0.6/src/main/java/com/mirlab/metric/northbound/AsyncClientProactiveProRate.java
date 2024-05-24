package com.mirlab.metric.northbound;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.global.Global;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * This example demonstrates a basic asynchronous HTTP request / response
 * exchange. Response content is buffered in memory for simplicity.
 */

public class AsyncClientProactiveProRate {
	public static int jishu = 0;
	public static int jishu2 = 0;
	public static String onosIP = null;
	private static int flowNum = 0;

	public static void post() throws Exception {
	
		
		AsyncClientProactiveProRateTimeThread tm = new AsyncClientProactiveProRateTimeThread();
		tm.start();
		
		
		 ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
	        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
	        cm.setMaxTotal(500);
		 cm.setDefaultMaxPerRoute(500);
		
		
		
	        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
	        httpAsyncClient .start();

		final CountDownLatch latch = new CountDownLatch(1);
		
		onosIP = Global.SDN_CONTROLLER_IP[0];
		String URL = "http://"+onosIP+":8181/onos/v1/flows/of%3A0000000000000002?appId=org.onosproject.testfwd";
		final HttpPost request = new HttpPost(URL);
		flowNum = Global.NUMBER_OF_TEST_FLOW;

		
		request.setHeader("Connection", "keep-alive");
		request.addHeader("Content-Type", "application/json");
		String encoding = null;
		try {
			encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		request.setHeader("Authorization", "Basic " + encoding);
	//	System.out.println(" caller thread id is : " + Thread.currentThread().getId());
		for (int i = 0; i < flowNum; i++) {
			
			
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
			StringEntity s = new StringEntity(jsobjpop.toString(), "utf-8");
			
//			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			
			request.setEntity(s);
			httpAsyncClient .execute(request, new FutureCallback<HttpResponse>() {

				public void completed(final HttpResponse response) {

					latch.countDown();
				//	System.out.println(" callback thread id is : " + Thread.currentThread().getId());
				//	System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
					if (response.getStatusLine().getStatusCode() == 201) {
					jishu = jishu + 1;
					System.out.println("Proactive rate created :  " + jishu);
				}
					try {
						String content = EntityUtils.toString(response.getEntity(), "UTF-8");
						//System.out.println(" response content is : " + content);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				public void failed(final Exception ex) {
					latch.countDown();
				//	System.out.println(request.getRequestLine() + "->" + ex);
				//	System.out.println(" callback thread id is : " + Thread.currentThread().getId());
				}

				public void cancelled() {
					latch.countDown();
					//System.out.println(request.getRequestLine() + " cancelled");
				//	System.out.println(" callback thread id is : " + Thread.currentThread().getId());
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
